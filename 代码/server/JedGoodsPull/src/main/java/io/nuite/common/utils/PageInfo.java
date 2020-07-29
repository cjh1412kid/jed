package io.nuite.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static int PAGESIZE = 10; //默认显示的记录数

    private int total; // 总记录
    private List rows; //显示的记录

    @JsonIgnore
    private int from;
    @JsonIgnore
    private int size;
    @JsonIgnore
    private int nowpage; // 当前页
    @JsonIgnore
    private int pagesize; // 每页显示的记录数
    @JsonIgnore
    private Map<String, Object> condition; //查询条件

    @JsonIgnore
    private String sort = "Seq";// 排序字段
    @JsonIgnore
    private String order = "asc";// asc，desc mybatis Order 关键字

    //构造方法
    public PageInfo(Map<String, Object> params) {
        String limit = (String) params.get("limit");
        String curPage = (String) params.get("page");
        String order = (String) params.get("order");
        String sort = (String) params.get("sidx");
        //计算当前页
        if (curPage == null || Integer.valueOf(curPage) < 0) {
            this.nowpage = 1;
        } else {
            //当前页
            this.nowpage = Integer.valueOf(curPage);
        }
        //记录每页显示的记录数
        if (limit == null || Integer.valueOf(limit) < 0) {
            this.pagesize = PAGESIZE;
        } else {
            this.pagesize = Integer.valueOf(limit);
        }
        //计算开始的记录和结束的记录
        this.from = (this.nowpage - 1) * this.pagesize;
        this.size = this.pagesize;

        if (sort != null) {
            this.sort = sort;
        }
        if (order != null) {
            this.order = order;
        }
        params.remove("limit");
        params.remove("page");
        params.remove("order");
        params.remove("sidx");
        this.condition = params;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNowpage() {
        return nowpage;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
