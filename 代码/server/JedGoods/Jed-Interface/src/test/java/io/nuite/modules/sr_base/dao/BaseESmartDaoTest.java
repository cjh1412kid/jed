package io.nuite.modules.sr_base.dao;

import io.nuite.modules.sr_base.entity.BaseESmartEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 15:37
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseESmartDaoTest {

    @Autowired
    BaseESmartDao baseESmartDao;

    @Test
    public void selectByUserSeq() {

        BaseESmartEntity baseESmartEntity = baseESmartDao.selectByUserSeq(152);

        System.out.println("baseESmartEntity = " + baseESmartEntity);

    }


}