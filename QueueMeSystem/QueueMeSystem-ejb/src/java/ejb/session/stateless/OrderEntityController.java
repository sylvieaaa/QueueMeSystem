/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.OrderEntity;
import entity.SaleTransactionLineItemEntity;
import entity.VendorEntity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OrderNotFoundException;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author User
 */
@Stateless
public class OrderEntityController implements OrderEntityControllerLocal {

    @PersistenceContext(unitName = "QueueMeSystem-ejbPU")
    private EntityManager em;

    public final static String AUTH_KEY_FCM = "AAAAqzmNsWQ:APA91bFdRabP13nS2puJwbreT5V6YXsTroB_LuA7ML_poVWXLi633tB8L_D9WN5pdvVCVj8kMSkYimSFNYjAoSwFKQLmPz7BnGChSNr42KbHAV6qAqiTsuOtq_b0R_C5JxIlS6CnCsYm";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    @Override
    public OrderEntity createOrder(OrderEntity orderEntity) {
        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = orderEntity.getSaleTransactionLineItemEntities();
        System.err.println(saleTransactionLineItemEntities);
        for (SaleTransactionLineItemEntity stlie : saleTransactionLineItemEntities) {
            System.err.println(stlie.getSerialNumber() + " in order");
        }
        em.persist(orderEntity);
        for (SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionLineItemEntities) {
            saleTransactionLineItemEntity.setOrderEntity(orderEntity);
        }
        em.flush();
        em.refresh(orderEntity);

        return orderEntity;
    }

    @Override
    public void updateOrder(OrderEntity orderEntity) {

        System.err.println("This is orderEntity id: " + orderEntity.getOrderId());
        OrderEntity updateOrder = em.find(OrderEntity.class, orderEntity.getOrderId());
        updateOrder.setFulfilled(Boolean.TRUE);
        if (orderEntity.getCustomerEntity() != null) {
            try {
                pushFCMNotification(orderEntity.getCustomerEntity().getPushToken(), orderEntity);
            } catch (Exception ex) {
            }
        }

    }

    public void pushFCMNotification(String userDeviceIdKey, OrderEntity orderEntity) throws Exception {

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

//        StringBuilder stringBuilder = new StringBuilder();
//        json.put("to", "cGMmUCfj7_k:APA91bE-KNQDPhexE3vOvMrI_50sIy_31DtHwfYNmBas3pRK0WP9H4uWoaqRgmbrUwHVARCepzBcmlyCIQbpA0VIVCp01mNgW3grteEkL2Sv_cosjk5cDzYN1fXbBZ93j6T3LgE2wIBN");
//        stringBuilder.append("to : cGMmUCfj7_k:APA91bE-KNQDPhexE3vOvMrI_50sIy_31DtHwfYNmBas3pRK0WP9H4uWoaqRgmbrUwHVARCepzBcmlyCIQbpA0VIVCp01mNgW3grteEkL2Sv_cosjk5cDzYN1fXbBZ93j6T3LgE2wIBN");
        JsonObject info = Json.createObjectBuilder()
                .add("title", "Your food is ready")
                .add("body", "Please collect it at " + orderEntity.getVendorEntity().getVendorName())
                .add("click_action", "FCM_PLUGIN_ACTIVITY")
                .add("sound","default")
                .build();
        JsonObject data = Json.createObjectBuilder()
                .add("data", "Collect your food at " + orderEntity.getVendorEntity().getVendorName())
//                .add("vibrationPattern", "[2000, 1000, 500, 500]")
                .build();
        JsonObject json = Json.createObjectBuilder()
                .add("to", userDeviceIdKey)
                .add("notification", info)
                .add("data", data)
                .build();

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        System.err.println(json.toString());
        wr.write(json.toString());
        wr.flush();
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        System.err.println(out.toString());
    }

    @Override
    public List<OrderEntity> retrieveAllOrders(VendorEntity vendorEntity) {
        Long vendorId = vendorEntity.getBusinessId();
        Query query = em.createQuery("SELECT p FROM OrderEntity p ORDER BY p.orderId ASC AND p.vendorEntity.businessId = :inVendorId");
        query.setParameter("inVendorId", vendorId);
        return query.getResultList();
    }

    @Override
    public List<OrderEntity> retrieveAllPendingOrders(VendorEntity vendorEntity) {
        Long vendorId = vendorEntity.getBusinessId();
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.fulfilled = 0 AND p.vendorEntity.businessId = :inVendorId");
        query.setParameter("inVendorId", vendorId);
        return query.getResultList();
    }

    @Override
    public List<OrderEntity> retrieveAllCompletedOrders(VendorEntity vendorEntity) {
        Long vendorId = vendorEntity.getBusinessId();
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.fulfilled = 1 AND p.vendorEntity.businessId = :inVendorId");
        query.setParameter("inVendorId", vendorId);
        return query.getResultList();
    }

    @Override
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = em.find(OrderEntity.class, orderId);

        if (orderEntity != null) {
            return orderEntity;
        } else {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
        }
    }

    @Override
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemEntities(Long orderId) {

        Query query = em.createQuery("SELECT l FROM LineItemEntity l WHERE l.orderId =:inOrderId");
        return query.getResultList();
    }

    @Override
    public BigDecimal getEarnings(Long orderId) {

        Query query = em.createQuery("SELECT l FROM LineItemEntity l WHERE l.orderId =:inOrderId");
        BigDecimal earnings = BigDecimal.ZERO;
        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = query.getResultList();
        for (SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionLineItemEntities) {
            earnings.add(saleTransactionLineItemEntity.getSubTotal());
        }

        return earnings;
    }

    @Override
    public List<OrderEntity> retrieveCustomerOrders(Long customerId) {
        Query query = em.createQuery("SELECT p FROM OrderEntity p WHERE p.customerEntity.businessId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }
}
