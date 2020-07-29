package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.DateUtils;
import io.nuite.modules.order_platform_app.dao.OrderDao;
import io.nuite.modules.order_platform_app.dao.OrderProductsDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.dao.ShoppingCartDao;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.order_platform_app.entity.OrderProductsEntity;
import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import io.nuite.modules.system.entity.echart.EchartDataVo;
import io.nuite.modules.system.entity.echart.OrderDataVo;
import io.nuite.modules.system.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/16 18:51
 * @Version: 1.0
 * @Description:
 */

@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ShoesDataDao shoesDataDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Autowired
    OrderProductsDao orderProductsDao;

    /**
     * 获取订单最近7天的每日数量
     *
     * @return
     */
    @Override
    public List<OrderDataVo> getOrderDataByDate() {
        return orderDao.getOrderDataByDate();
    }

    /**
     * 查询当前一周的某款鞋子的订单数据
     *
     * @param shoeSeq
     * @return
     */
    @Override
    public List<OrderDataVo> getSalesWithWeek(Integer shoeSeq, Integer companySeq) {
        List<Integer> shoesDataSeqs = shoesDataDao.selectByShoeSeq(shoeSeq);

        if (shoesDataSeqs == null) {
            return null;
        }

        List<OrderDataVo> orderDataVoList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            String dateStr = DateUtils.format(DateUtils.addDateDays(new Date(), -i));
            OrderDataVo orderDataVo = new OrderDataVo();
            orderDataVo.setDatestr(dateStr);
            orderDataVoList.add(orderDataVo);
        }

        //获取最近一周的订单
        List<OrderEntity> orders = orderDao.getOrdersByDateAndCompanySeq(DateUtils.addDateDays(new Date(), -6), new Date(), companySeq);

        //遍历订单，统计该款鞋子的下单数量
        for (OrderEntity order : orders) {
            int orderProductsCount = 0; //订单下的指定商品数量
            List<OrderProductsEntity> products = orderProductsDao.selectList(new EntityWrapper<OrderProductsEntity>().eq("Order_Seq", order.getSeq()));
            for (OrderProductsEntity product : products) {
                for (Integer shoesDataSeq : shoesDataSeqs) {
                    //判断订单下的商品是否是查询的商品
                    if (shoesDataSeq == product.getShoesDataSeq()) {
                        //统计数量
                        orderProductsCount += product.getBuyCount();
                        break;
                    }
                }
            }

            //判断订单日期，保存数量
            for (OrderDataVo orderDataVo : orderDataVoList) {
                if (orderDataVo.getDatestr().equals(order.getDateStr())) {
                    orderDataVo.setOrderCount(orderDataVo.getOrderCount() + orderProductsCount);
                    break;
                }
            }
        }

        //获取最近一周的购物车记录(指定的某一款鞋子)
        List<ShoppingCartEntity> shoppingCartList = shoppingCartDao.getShoppingCartListByDateAndShoesDataSeq(DateUtils.addDateDays(new Date(), -6), new Date(), shoesDataSeqs);

        //遍历购物车，统计该款鞋子的数量
        for (ShoppingCartEntity cartEntity : shoppingCartList) {
            for (OrderDataVo orderDataVo : orderDataVoList) {
                if (orderDataVo.getDatestr().equals(cartEntity.getDateStr())) {
                    //按日期累加商品购物车数量
                    orderDataVo.setCartCount(orderDataVo.getCartCount() + cartEntity.getBuyCount());
                    break;
                }
            }
        }

        return orderDataVoList;
    }

    /**
     * @param dateType  日期类型  1 年，2 月，3 周，0 自定义
     * @param changeVal 上下移动（周、月、年）
     * @return
     */
    @Override
    public EchartDataVo getOrdersCountByDateAndCompanySeq(Integer dateType, Integer changeVal, Integer companySeq) {
        EchartDataVo echartDataVo = new EchartDataVo();
        boolean continueFlag = true;
        List<String> days = null; //图表的时间区间
        //时间处理
        Date startDate = null;
        Date endDate = null;
        if (dateType == 2) {
            Date[] monthStartAndEnd = DateUtils.getMonthStartAndEnd(changeVal);
            startDate = monthStartAndEnd[0];
            endDate = monthStartAndEnd[1];
            String monthOfYear = DateUtils.format(startDate, "yyyy年MM月 ");
            days = DateUtils.getDaysOfMonth(changeVal);

            echartDataVo.setTitle(monthOfYear + "订单数量图");
            echartDataVo.setSubTitle("单位（日）");

        } else if (dateType == 3) {
            Date[] weekStartAndEnd = DateUtils.getWeekStartAndEnd(changeVal);
            startDate = weekStartAndEnd[0];
            endDate = weekStartAndEnd[1];
            days = DateUtils.getDaysOfWeek(changeVal);

            echartDataVo.setTitle("周订单数量图 (" + DateUtils.format(startDate, DateUtils.DATE_PATTERN) + " ~ " + DateUtils.format(endDate, DateUtils.DATE_PATTERN) + ")");
            echartDataVo.setSubTitle("单位（日）");
        } else if (dateType == 1) {
            startDate = DateUtils.addDateYears(new Date(), changeVal);
            days = DateUtils.getMonthsOfYear(changeVal);

            echartDataVo.setTitle(DateUtils.format(startDate, "yyyy年 ") + "订单数量图");
            echartDataVo.setSubTitle("单位（月）");
        }

        //时间区间为空，不执行查询
        if (startDate == null) {
            throw new RRException("查询条件时间为null");
        }

        //如果查询的开始时间大于当前时间，不执行查询，无意义
        if (startDate.getTime() > new Date().getTime()) {
            continueFlag = false;
        }

        List<OrderDataVo> list = new ArrayList<>();
        if (continueFlag) {
            if (dateType == 1) {  //年
                list = orderDao.getOrdersCountByMonthOfYear(startDate, companySeq);
            } else {
                list = orderDao.getOrdersCountByDay(startDate, endDate, companySeq);
            }
        }

        //补全日期 （x轴用）
        for (String day : days) {
            boolean addFlag = true;
            for (OrderDataVo vo : list) {
                if (day.equals(vo.getDatestr())) {
                    addFlag = false;
                    break;
                }
            }

            if (addFlag) {
                OrderDataVo orderDataVo = new OrderDataVo();
                orderDataVo.setDatestr(day);
                list.add(orderDataVo);
            }
        }

        //list日期排序
        list.sort(new Comparator<OrderDataVo>() {
            @Override
            public int compare(OrderDataVo o1, OrderDataVo o2) {
                return o1.getDatestr().compareTo(o2.getDatestr());
            }
        });

        //设置echart显示信息
        echartDataVo.setLegendData(new String[]{"订单量"});
        echartDataVo.setSeriesName("订单量");
        echartDataVo.setList(list);
        return echartDataVo;
    }

    /**
     * 自定义查询时间
     *
     * @param companySeq
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public EchartDataVo getOrdersCountByDateAndCompanySeq(Integer companySeq, String startTime, String endTime) {
        EchartDataVo echartDataVo = new EchartDataVo();
        //图表的时间区间
        Date startDate=DateUtils.parse(startTime, DateUtils.DATE_PATTERN);
        Date endDate=DateUtils.parse(endTime, DateUtils.DATE_PATTERN);
        List<String> days=DateUtils.getDaysOfCustom(startDate,endDate);
        //获得时间区间的数据
        List<OrderDataVo> dataVoList = orderDao.getOrdersCountByDay(startDate, endDate, companySeq);

        //补全日期 （x轴用）
        for (String day : days) {
            boolean addFlag = true;
            for (OrderDataVo vo : dataVoList) {
                if (day.equals(vo.getDatestr())) {
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                OrderDataVo orderDataVo = new OrderDataVo();
                orderDataVo.setDatestr(day);
                dataVoList.add(orderDataVo);
            }
        }

        //list日期排序
        dataVoList.sort(new Comparator<OrderDataVo>() {
            @Override
            public int compare(OrderDataVo o1, OrderDataVo o2) {
                return o1.getDatestr().compareTo(o2.getDatestr());
            }
        });

        //设置echart显示信息
        echartDataVo.setTitle("订单数量图");
        echartDataVo.setSubTitle("单位（日）");
        echartDataVo.setLegendData(new String[]{"订单量"});
        echartDataVo.setSeriesName("订单量");
        echartDataVo.setList(dataVoList);
        return echartDataVo;
    }

}
