package io.nuite.modules.system.from;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ShippedProductForm {
    private int buyCount;
    private String color;
    private String shoesSize;
    private String goodId;
    private Integer seq;
    private int shipNum;

    public ShippedProductForm(String message) {
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            this.buyCount = jsonObject.getInteger("buyCount");
            this.color = jsonObject.getString("color");
            this.shoesSize = jsonObject.getString("shoesSize");
            this.goodId = jsonObject.getString("goodId");
            this.seq = jsonObject.getInteger("seq");
            this.shipNum = jsonObject.getIntValue("shipNum");
        } catch (Exception e) {

        }
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShoesSize() {
        return shoesSize;
    }

    public void setShoesSize(String shoesSize) {
        this.shoesSize = shoesSize;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public int getShipNum() {
        return shipNum;
    }

    public void setShipNum(int shipNum) {
        this.shipNum = shipNum;
    }
}
