package io.nuite.modules.order_platform_app.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态
 */
public enum OrderStatusEnum {

    // 工厂方：待确认、待审核、待入库、未发货、部分发货、已发货、已到货、已取消
    // 订货方：待确认、待支付、待入库、待发货、部分发货、已发货、已收货、已取消
    ORDSTATUS_ZERO(0, "待确认", "待确认"),
    ORDSTATUS_ONE(1, "待审核", "待支付"),
    ORDSTATUS_TWO(2, "待入库", "待入库"),
    ORDSTATUS_THREE(3, "未发货", "待发货"),
    ORDSTATUS_FOUR(4, "部分发货", "部分发货"),
    ORDSTATUS_FIVE(5, "已发货", "已发货"),
    ORDSTATUS_SIX(6, "已到货", "已收货"),
    ORDSTATUS_SEVEN(7, "已取消", "已取消");

    private int value;
    private String factoryName;
    private String brandName;

    private OrderStatusEnum(int value, String factoryName, String brandName) {
        this.value = value;
        this.factoryName = factoryName;
        this.brandName = brandName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    private static final Map<Integer, OrderStatusEnum> lookup = new HashMap<Integer, OrderStatusEnum>();

    static {
        for (OrderStatusEnum s : EnumSet.allOf(OrderStatusEnum.class)) {
            lookup.put(s.getValue(), s);
        }
    }

    //根据数字获取枚举对象
    public static OrderStatusEnum get(Integer code) {
        return lookup.get(code);
    }

}
