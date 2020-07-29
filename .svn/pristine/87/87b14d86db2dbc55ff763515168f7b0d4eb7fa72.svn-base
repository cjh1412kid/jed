package io.nuite.modules.order_platform_app.dao;

import io.nuite.common.utils.DateUtils;
import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/17 11:50
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShoppingCartDaoTest {

    @Autowired
    ShoesDataDao shoesDataDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Test
    public void getShoppingCartListByDateAndShoesDataSeq() {
        List<Integer> shoesDataSeqs = shoesDataDao.selectByShoeSeq(3);
//        shoesDataSeqs=null;
        List<ShoppingCartEntity> shoppingCartList = shoppingCartDao.getShoppingCartListByDateAndShoesDataSeq(DateUtils.addDateWeeks(new Date(), -4), new Date(), shoesDataSeqs);

        shoppingCartList.forEach(System.out::println);
    }
}