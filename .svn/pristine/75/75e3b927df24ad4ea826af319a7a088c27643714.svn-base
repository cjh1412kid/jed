package io.nuite.modules.sr_base.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: yangchuang
 * @Date: 2018/8/9 16:14
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsSizeDaoTest {

    @Autowired
    GoodsSizeDao goodsSizeDao;

    @Test
    public void selectSizeSeqByCodeAndCompanySeq(){

        Integer a = goodsSizeDao.selectSizeSeqByCodeAndCompanySeq(38, "12222");
        System.out.println("a = " + a);
    }

}