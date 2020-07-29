package io.nuite.modules.system.model.excel.qiangren;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class Sample implements Serializable {

    @Excel(name = "样品代码")
    private String goodsId;

    @Excel(name = "样品名称")
    private String introduce;

    @Excel(name = "助记符")
    private String mnemonic;

    @Excel(name = "商品代码")
    private String goodsName;

    @Excel(name = "成本价")
    private String salePrice;

    @Excel(name = "吊牌价")
    private String purchasePrice;

    @Excel(name = "品牌代码")
    private String brandCode;

    @Excel(name = "大类代码")
    private String categoryCode;

    @Excel(name = "季节代码")
    private String sessionCode;

    @Excel(name = "年份代码")
    private String yearCode;

    @Excel(name = "类别代码")
    private String sx1Code;

    @Excel(name = "品类代码")
    private String sx2Code;

    @Excel(name = "风格代码")
    private String sx3Code;

    @Excel(name = "款式代码")
    private String sx4Code;

    @Excel(name = "面料代码")
    private String sx5Code;

    @Excel(name = "跟型代码")
    private String sx6Code;

    @Excel(name = "鞋底代码")
    private String sx7Code;

    @Excel(name = "垫脚代码")
    private String sx8Code;

    @Excel(name = "供应商代码")
    private String sx9Code;

    @Excel(name = "线上线下代码")
    private String sx10Code;

    @Excel(name = "里料代码")
    private String sx11Code;

    @Excel(name = "功能代码")
    private String sx12Code;

    @Excel(name = "跟高代码")
    private String sx13Code;

    @Excel(name = "系列代码")
    private String sx14Code;

    @Excel(name = "首订/后期版代码")
    private String sx15Code;

    @Excel(name = "靴尺寸代码")
    private String sx16Code;

    public Sample() {
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public String getYearCode() {
        return yearCode;
    }

    public void setYearCode(String yearCode) {
        this.yearCode = yearCode;
    }

    public String getSx1Code() {
        return sx1Code;
    }

    public void setSx1Code(String sx1Code) {
        this.sx1Code = sx1Code;
    }

    public String getSx2Code() {
        return sx2Code;
    }

    public void setSx2Code(String sx2Code) {
        this.sx2Code = sx2Code;
    }

    public String getSx3Code() {
        return sx3Code;
    }

    public void setSx3Code(String sx3Code) {
        this.sx3Code = sx3Code;
    }

    public String getSx4Code() {
        return sx4Code;
    }

    public void setSx4Code(String sx4Code) {
        this.sx4Code = sx4Code;
    }

    public String getSx5Code() {
        return sx5Code;
    }

    public void setSx5Code(String sx5Code) {
        this.sx5Code = sx5Code;
    }

    public String getSx6Code() {
        return sx6Code;
    }

    public void setSx6Code(String sx6Code) {
        this.sx6Code = sx6Code;
    }

    public String getSx7Code() {
        return sx7Code;
    }

    public void setSx7Code(String sx7Code) {
        this.sx7Code = sx7Code;
    }

    public String getSx8Code() {
        return sx8Code;
    }

    public void setSx8Code(String sx8Code) {
        this.sx8Code = sx8Code;
    }

    public String getSx9Code() {
        return sx9Code;
    }

    public void setSx9Code(String sx9Code) {
        this.sx9Code = sx9Code;
    }

    public String getSx10Code() {
        return sx10Code;
    }

    public void setSx10Code(String sx10Code) {
        this.sx10Code = sx10Code;
    }

    public String getSx11Code() {
        return sx11Code;
    }

    public void setSx11Code(String sx11Code) {
        this.sx11Code = sx11Code;
    }

    public String getSx12Code() {
        return sx12Code;
    }

    public void setSx12Code(String sx12Code) {
        this.sx12Code = sx12Code;
    }

    public String getSx13Code() {
        return sx13Code;
    }

    public void setSx13Code(String sx13Code) {
        this.sx13Code = sx13Code;
    }

    public String getSx14Code() {
        return sx14Code;
    }

    public void setSx14Code(String sx14Code) {
        this.sx14Code = sx14Code;
    }

    public String getSx15Code() {
        return sx15Code;
    }

    public void setSx15Code(String sx15Code) {
        this.sx15Code = sx15Code;
    }

    public String getSx16Code() {
        return sx16Code;
    }

    public void setSx16Code(String sx16Code) {
        this.sx16Code = sx16Code;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
}
