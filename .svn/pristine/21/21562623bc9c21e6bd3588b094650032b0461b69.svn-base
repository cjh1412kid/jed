package io.nuite.modules.erp.utils;

/**
 * @description: 数据预处理公用类
 * @author: jxj
 * @create: 2019-07-09 14:06
 */

public class DataHandleUtil {
    public static String[][] removeBlank(String[][] data) {
        for(String[] row : data) {
            for(int i = 0;i < row.length;i++) {
                if(row[i] != null) {
                    row[i] = row[i].replaceAll("\\s", "");
                }
            }
        }
        return data;
    }
}
