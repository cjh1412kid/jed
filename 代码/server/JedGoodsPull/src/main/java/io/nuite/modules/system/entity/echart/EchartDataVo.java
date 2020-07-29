package io.nuite.modules.system.entity.echart;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/24 14:10
 * @Version: 1.0
 * @Description: 工具类，echart订单数据
 */
public class EchartDataVo {

    private String title;

    private String subTitle;

    private String[] legendData;

    private String seriesName;

    private List list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String[] getLegendData() {
        return legendData;
    }

    public void setLegendData(String[] legendData) {
        this.legendData = legendData;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

}
