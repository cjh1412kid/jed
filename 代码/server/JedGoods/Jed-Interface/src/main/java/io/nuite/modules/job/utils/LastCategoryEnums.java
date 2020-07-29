package io.nuite.modules.job.utils;

import lombok.Getter;

/**
 * @description: 鞋子底层类枚举类
 * @author: jxj
 * @create: 2019-04-08 16:55
 */
@Getter
public enum LastCategoryEnums {
    K(1001,"欧版鞋、绅士鞋","K"),
    H(1010,"小韩版","H"),
    B(1011,"乐福鞋","B"),
    BS(1012,"绅士鞋","S"),
    NY(1013,"新大众运动休","NY"),
    FY(1014,"时尚运动休","FY"),
    NB(1015,"新大众板休","NB"),
    FB(1016,"时尚板休","FB"),
    DD(1017,"豆豆鞋","DD"),
    YL(1018,"英伦休","YL"),
    FJ(1019,"福建风","FJ"),
    CT(1020,"传统休","CT"),
    KX(1021,"功能鞋休闲鞋","K休"),
    OLD(1022,"老人鞋","老人鞋"),
    FG(1023,"高跟鞋","FG"),
    FZ(1024,"中跟鞋","FZ"),
    FD(1025,"低跟鞋","FD"),
    CG(1026,"高跟鞋","CG"),
    CZ(1027,"中跟鞋","CZ"),
    CD(1028,"低跟鞋","CD"),
    W(1029,"偏传统","W"),
    PD(1030,"粗低跟","PD"),
    PP(1031,"平底","PP"),
    DP(1032,"坡跟/成型底","DP"),
    GS(1033,"淑女鞋","S"),
    U(1034,"休闲鞋","U");

    private Integer value;

    private String message;

    private String key;

    private LastCategoryEnums(Integer value, String message, String key) {
        this.value = value;
        this.message = message;
        this.key = key;
    }

    public static String getMessage(Integer value) {
        for(LastCategoryEnums e : LastCategoryEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getMessage(String key) {
        for(LastCategoryEnums e : LastCategoryEnums.values()) {
            if(e.getKey().equals(key)) {
                return e.getMessage();
            }
        }
        return "";
    }

    public static String getKey(Integer value) {
        for(LastCategoryEnums e : LastCategoryEnums.values()) {
            if(e.getValue().equals(value)) {
                return e.getKey();
            }
        }
        return "";
    }
}
