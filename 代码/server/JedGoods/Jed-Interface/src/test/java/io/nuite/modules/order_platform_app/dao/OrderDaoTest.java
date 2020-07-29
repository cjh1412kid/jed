package io.nuite.modules.order_platform_app.dao;

import io.nuite.common.utils.DateUtils;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.system.entity.echart.OrderDataVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/16 16:56
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderDaoTest {

    @Autowired
    OrderDao orderDao;


    @Test
    public void getOrderDataByDate() {
        List<OrderDataVo> orderDataByDate = orderDao.getOrdersCountByDay(null,null,38);

        for (OrderDataVo orderDataVo : orderDataByDate) {
            System.out.println("orderDataVo = " + orderDataVo);
        }
    }

    @Test
    public void getOrdersByDateAndCompanySeq() {

        List<OrderEntity> orders = orderDao.getOrdersByDateAndCompanySeq(DateUtils.addDateWeeks(new Date(), -4), new Date(), 3);
        orders.stream().forEach(System.out::println);

    }

    @Test
    public void test(){
        List<OrderDataVo> orderDataVoList=new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String dateStr = DateUtils.format(DateUtils.addDateDays(new Date(), -i));
            OrderDataVo orderDataVo = new OrderDataVo();
            orderDataVo.setDatestr(dateStr);
            orderDataVoList.add(orderDataVo);
        }

        System.out.println("orderDataVoList = " + orderDataVoList);
    }
}