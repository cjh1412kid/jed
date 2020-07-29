package io.nuite.modules.system.model.excel.qiangren;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class SampleColor implements Serializable {
    @Excel(name = "样品")
    private String goodsId;

    @Excel(name = "颜色代码")
    private String colorCode;

    public SampleColor() {
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
