package io.nuite.modules.order_platform_app.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * jrd订货会订单状态
 */
public enum MeetingOrderStatusEnum {

    // 工厂方：(0：未发货、1：部分发货、 2：已发货、3：已取消)
    // 订货方：(0：未发货、1：部分发货、 2：已发货、3：已取消)
    ORDSTATUS_ZERO(0, "未发货", "未发货"),
    ORDSTATUS_ONE(1, "部分发货", "部分发货"),
    ORDSTATUS_TWO(2, "已发货", "已发货"),
    ORDSTATUS_THREE(3, "已取消", "已取消");

    private int value;
    private String factoryName;
    private String brandName;

    private MeetingOrderStatusEnum(int value, String factoryName, String brandName) {
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


    private static final Map<Integer, MeetingOrderStatusEnum> lookup = new HashMap<Integer, MeetingOrderStatusEnum>();

    static {
        for (MeetingOrderStatusEnum s : EnumSet.allOf(MeetingOrderStatusEnum.class)) {
            lookup.put(s.getValue(), s);
        }
    }

    //根据数字获取枚举对象
    public static MeetingOrderStatusEnum get(Integer code) {
        return lookup.get(code);
    }

}
