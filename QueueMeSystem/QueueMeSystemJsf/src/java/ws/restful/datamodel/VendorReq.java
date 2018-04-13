/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.FoodCourtEntity;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "vendorReq", propOrder = {
    "foodCourtEntity"
})
public class VendorReq {
    private FoodCourtEntity foodCourtEntity;

    public VendorReq() {
    }

    public VendorReq(FoodCourtEntity foodCourtEntity) {
        this.foodCourtEntity = foodCourtEntity;
    }

    public FoodCourtEntity getFoodCourtEntity() {
        return foodCourtEntity;
    }

    public void setFoodCourtEntity(FoodCourtEntity foodCourtEntity) {
        this.foodCourtEntity = foodCourtEntity;
    }
    
    
}
