package io.nuite.modules.job.utils;

import lombok.Getter;

/**
 * @description: 鞋子中类枚举类
 * @author: jxj
 * @create: 2019-04-08 16:50
 */
@Getter
public enum MiddleCategoryEnums {
    D(1003,"单鞋","D"),
    U(1004,"休闲鞋","U"),
    S(1005,"注塑","S"),
    L(1006,"凉鞋","L"),
    M(1007,"棉鞋","M"),
    Z(1008,"中空鞋","Z");

    private Integer value;

    private String message;

    private String key;

    private MiddleCategoryEnums(Integer value, String message, String key) {
        this.value = value;
        this.message = message;
        this.key = key;
    }

    public static String getMessage(Integer value) {
        for(MiddleCategoryEnums e : MiddleCategoryEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getMessage(String key) {
        for(MiddleCategoryEnums e : MiddleCategoryEnums.values()) {
            if(e.getKey().equals(key)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getKey(Integer value) {
        for(MiddleCategoryEnums e : MiddleCategoryEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getKey();
            }
        }
        return "";
    }
}
