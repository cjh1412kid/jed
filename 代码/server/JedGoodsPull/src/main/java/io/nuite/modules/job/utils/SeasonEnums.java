package io.nuite.modules.job.utils;

import lombok.Getter;

/**
 * @description: ERP相关业务枚举类
 * @author: jxj
 * @create: 2019-04-08 09:20
 */
@Getter
public enum SeasonEnums {
    SPRING(1,"春季"),
    SUMMER(2,"夏季"),
    AUTUMN(3,"秋季"),
    WINTER(5,"冬季");

    private Integer value;

    private String message;

    private String key;

    private SeasonEnums(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    private SeasonEnums(Integer value, String message, String key) {
        this.value = value;
        this.message = message;
        this.key = key;
    }

    public static String getMessage(Integer value) {
        for(SeasonEnums e : SeasonEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getKey(Integer value) {
        for(SeasonEnums e : SeasonEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getKey();
            }
        }
        return "";
    }
}
