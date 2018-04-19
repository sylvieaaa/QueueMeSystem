/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.MenuItemEntity;
import entity.OrderEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author User
 */
@Stateless
public class SaleTransactionEntityController implements SaleTransactionEntityControllerLocal {

    @EJB
    private EmailControllerLocal emailControllerLocal;

    @Resource(mappedName = "jdbc/queuemesystem")
    private DataSource queueMeSystemDataSource;

    @EJB
    private OrderEntityControllerLocal orderEntityControllerLocal;

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")

    private EntityManager em;

    @Resource
    private EJBContext eJBContext;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public SaleTransactionEntity createSaleTransaction(SaleTransactionEntity saleTransactionEntity) {
        for (SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionEntity.getSaleTransactionLineItemEntities()) {
            saleTransactionLineItemEntity.setSaleTransactionEntity(saleTransactionEntity);
        }
        em.persist(saleTransactionEntity);
        em.flush();
        em.refresh(saleTransactionEntity);

        processSaleTransaction(saleTransactionEntity);
        if (saleTransactionEntity.getCustomerEntity() != null) {
            sendReceipt(saleTransactionEntity);
        }

        return saleTransactionEntity;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void processSaleTransaction(SaleTransactionEntity saleTransactionEntity) {
        System.err.println(saleTransactionEntity.getSaleTransactionLineItemEntities());
        CustomerEntity customerEntity = saleTransactionEntity.getCustomerEntity();
        HashMap<VendorEntity, List<SaleTransactionLineItemEntity>> vendorToSales = new HashMap<>();
        for (SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionEntity.getSaleTransactionLineItemEntities()) {
            // for link back 
            saleTransactionLineItemEntity.getMenuItemEntity().getSaleTransactionLineItemEntities().add(saleTransactionLineItemEntity);
            //System.err.println(saleTransactionLineItemEntity.getMenuItemEntity().getSaleTransactionLineItemEntities().size() + " m");
            MenuItemEntity menuItemEntity = saleTransactionLineItemEntity.getMenuItemEntity();
            VendorEntity vendorEntity = menuItemEntity.getVendorEntity();
            if (vendorToSales.containsKey(vendorEntity)) {
                System.err.println("contain");
                vendorToSales.get(vendorEntity).add(saleTransactionLineItemEntity);
            } else {
                System.err.println("not contain");
                List<SaleTransactionLineItemEntity> newSaleTransactionLineItemEntities = new ArrayList<>();
                newSaleTransactionLineItemEntities.add(saleTransactionLineItemEntity);
                vendorToSales.put(vendorEntity, newSaleTransactionLineItemEntities);
            }
        }

        BigDecimal totalEarnings = BigDecimal.ZERO;
        for (Map.Entry<VendorEntity, List<SaleTransactionLineItemEntity>> entry : vendorToSales.entrySet()) {
            System.err.println("loopp");
            VendorEntity vendorEntity = entry.getKey();
            List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = entry.getValue();
            for (SaleTransactionLineItemEntity stlie : saleTransactionLineItemEntities) {
                totalEarnings = totalEarnings.add(stlie.getSubTotal());
            }
            OrderEntity oe = orderEntityControllerLocal.createOrder(new OrderEntity(Calendar.getInstance().getTime(), totalEarnings, Boolean.FALSE, customerEntity, vendorEntity, saleTransactionLineItemEntities));
            System.err.println(oe);
        }
    }

    public void sendReceipt(SaleTransactionEntity saleTransactionEntity) {
        String path = System.getProperty("user.dir").replaceAll("config", "docroot").replaceFirst("docroot", "config")
                + System.getProperty("file.separator") + "queueme-uploads" + System.getProperty("file.separator");
        String jasperPath = path + "Queue_Me_Receipt.jasper";
        String pdfPath = path + "pdf";

        try {
            File newPdfFile = new File(pdfPath);
            newPdfFile = File.createTempFile("PDF", ".pdf", newPdfFile);
            File jasperFile = new File(jasperPath);
            FileInputStream fis = new FileInputStream(jasperFile);
            Connection conn = queueMeSystemDataSource.getConnection();
            HashMap map = new HashMap();
            map.put("SaleTransactionId", saleTransactionEntity.getSaleTransactionId());
            JasperPrint print = JasperFillManager.fillReport(fis, map, conn);
            System.err.println(newPdfFile.getAbsolutePath());
            String newFilePath = newPdfFile.getAbsolutePath();
            newPdfFile.delete();
            JasperExportManager.exportReportToPdfFile(print, newFilePath);

            emailControllerLocal.sendReceipt(new File(newFilePath), saleTransactionEntity.getCustomerEntity());
        } catch (FileNotFoundException ex) {
        } catch (SQLException ex) {
        } catch (JRException ex) {
        } catch (IOException ex) {
        } catch (CustomerNotFoundException ex) {
        }
    }

    @Override
    public List<SaleTransactionEntity> retrieveSaleTransaction(Long customerId) {

        Query query = em.createQuery("SELECT p FROM SaleTransactionEntity p WHERE p.customerEntity.businessId = :inCustomerId ORDER BY p.saleTransactionId DESC");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }

    @Override
    public List<SaleTransactionEntity> retrieveAllSaleTransactions() {
        Query query = em.createQuery("SELECT st FROM SaleTransactionEntity st");

        List<SaleTransactionEntity> saleTransactionEntities = query.getResultList();

        for (SaleTransactionEntity saleTransactionEntity : saleTransactionEntities) {
            saleTransactionEntity.getSaleTransactionLineItemEntities().size();
        }

        return saleTransactionEntities;
    }

}
