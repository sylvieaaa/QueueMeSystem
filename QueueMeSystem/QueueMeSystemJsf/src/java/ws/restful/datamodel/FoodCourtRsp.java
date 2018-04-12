/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.datamodel;

import entity.FoodCourtEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author SYLVIA
 */

@XmlType(name = "foodCourtRsp", propOrder = {
    "foodCourts"
})
public class FoodCourtRsp {
    private List<FoodCourtEntity> foodCourts;

    public FoodCourtRsp() {
    }

    public FoodCourtRsp(List<FoodCourtEntity> foodCourts) {
        this.foodCourts = foodCourts;
    }

    public List<FoodCourtEntity> getFoodCourts() {
        return foodCourts;
    }

    public void setFoodCourts(List<FoodCourtEntity> foodCourts) {
        this.foodCourts = foodCourts;
    }
    
    
}
