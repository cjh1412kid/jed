package io.nuite.modules.system.model.excel.qiangren;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class SampleSize implements Serializable {
    @Excel(name = "样品")
    private String goodsId;

    @Excel(name = "尺码代码")
    private String sizeCode;

    public SampleSize() {
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }
}
