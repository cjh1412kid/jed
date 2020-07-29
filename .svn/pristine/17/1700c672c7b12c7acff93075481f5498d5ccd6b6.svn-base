package io.nuite.modules.job.utils;

import lombok.Getter;

/**
 * @description: 鞋子中类枚举类
 * @author: jxj
 * @create: 2019-04-08 16:53
 */
@Getter
public enum LargeCategoryEnums {
    B(1001,"男鞋","B"),
    G(1002,"女鞋","G");

    private Integer value;

    private String message;

    private String key;

    private LargeCategoryEnums(Integer value, String message, String key) {
        this.value = value;
        this.message = message;
        this.key = key;
    }

    public static String getMessage(Integer value) {
        for(LargeCategoryEnums e : LargeCategoryEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getMessage(String key) {
        for(LargeCategoryEnums e : LargeCategoryEnums.values()) {
            if(e.getKey().equals(key)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getKey(Integer value) {
        for(LargeCategoryEnums e : LargeCategoryEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getKey();
            }
        }
        return "";
    }
}
