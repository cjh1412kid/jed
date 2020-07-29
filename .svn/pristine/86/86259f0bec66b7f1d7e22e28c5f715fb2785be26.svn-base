package io.nuite.modules.sr_base.service.impl;

import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: yangchuang
 * @Date: 2018/8/8 8:57
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseUserServiceImplTest {

    @Autowired
    BaseUserService baseUserService;

    @Test
    public void queryUserByCompanyAndAccountName() {

        BaseUserEntity yc = baseUserService.queryUserByCompanyAndAccountName("yc", 38);

        System.out.println("yc = " + yc);

    }
}