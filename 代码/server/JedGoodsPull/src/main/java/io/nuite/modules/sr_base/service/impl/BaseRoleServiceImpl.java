package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.BaseRoleDao;
import io.nuite.modules.sr_base.entity.BaseRoleEntity;
import io.nuite.modules.sr_base.service.BaseRoleService;
import org.springframework.stereotype.Service;

/**
 * @description: 角色Service实现类
 * @author: jxj
 * @create: 2019-04-23 14:14
 */
@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleDao, BaseRoleEntity> implements BaseRoleService {
}
