package io.nuite.modules.system.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class GoodsShoesStockForm {
    private Integer seq;
    private String code;
    private String name;
    private Integer sizeSeq;
    private String sizeCode;
    private String sizeName;
    private String remark;
    private Integer stock;
    private Integer setStock;

    public GoodsShoesStockForm() {

    }

    public GoodsShoesStockForm(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        this.seq = jsonObject.getInteger("seq");
        this.code = jsonObject.getString("code");
        this.name = jsonObject.getString("name");
        this.sizeSeq = jsonObject.getInteger("sizeSeq");
        this.sizeCode = jsonObject.getString("sizeCode");
        this.sizeName = jsonObject.getString("sizeName");
        this.remark = jsonObject.getString("remark");
        this.stock = jsonObject.getInteger("stock");
        this.setStock = jsonObject.getInteger("setStock");
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSizeSeq() {
		return sizeSeq;
	}

	public void setSizeSeq(Integer sizeSeq) {
		this.sizeSeq = sizeSeq;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSetStock() {
        return setStock;
    }

    public void setSetStock(Integer setStock) {
        this.setStock = setStock;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }
}
