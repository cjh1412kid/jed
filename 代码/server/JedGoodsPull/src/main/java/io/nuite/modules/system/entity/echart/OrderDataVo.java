package io.nuite.modules.system.entity.echart;

/**
 * @Author: yangchuang
 * @Date: 2018/8/16 16:48
 * @Version: 1.0
 * @Description: 工具类，存储订单查询数据
 */
public class OrderDataVo {

    private String datestr;

    private int orderCount;

    private int cartCount;

    private String year;

    private String month;

    private String day;

    public String getDatestr() {
        return datestr;
    }

    public void setDatestr(String datestr) {
        this.datestr = datestr;

        if (datestr != null) {
            String[] strs = datestr.split("-");
            this.year = strs[0];
            this.month = strs[1];
            if (strs.length > 2) {
                this.day = strs[2];
            }
        }
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderDataVo{");
        sb.append("datestr='").append(datestr).append('\'');
        sb.append(", orderCount=").append(orderCount);
        sb.append(", cartCount=").append(cartCount);
        sb.append(", year='").append(year).append('\'');
        sb.append(", month='").append(month).append('\'');
        sb.append(", day='").append(day).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
