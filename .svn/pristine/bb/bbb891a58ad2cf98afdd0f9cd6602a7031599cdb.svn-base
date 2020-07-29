package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.common.utils.Constant;
import io.nuite.modules.sr_base.dao.BaseRoleDao;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.entity.BaseRoleEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import io.nuite.modules.sr_base.service.BaseUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2019/3/11 10:32
 * @Version: 1.0
 */

@Service
public class BaseUserRoleServiceImpl extends ServiceImpl<BaseUserRoleDao, BaseUserRoleEntity> implements BaseUserRoleService {

    @Autowired
    private BaseUserRoleDao baseUserRoleDao;
    
    @Autowired
    private BaseRoleDao baseRoleDao;

    
    @Override
    public BaseUserRoleEntity findAdminByUserSeq(Integer userSeq) {
        List<BaseUserRoleEntity> adminRoles = baseUserRoleDao.selectByUserSeqAndRoleCode(userSeq, Constant.Role.FACTORY_ADMIN.getCode());
        if (adminRoles != null && adminRoles.size() >= 1) {
            return adminRoles.get(0);
        }
        return null;
    }

    @Override
    public boolean isAdminUser(Integer userSeq) {
        BaseUserRoleEntity adminRole = findAdminByUserSeq(userSeq);
        return adminRole == null ? false : true;
    }

    
    /**
     * 根据用户序号查询用户角色code
     */
	@Override
	public String getRoleCodeByUserSeq(Integer userSeq) {
		BaseUserRoleEntity baseUserRoleEntity = new BaseUserRoleEntity();
		baseUserRoleEntity.setUserSeq(userSeq);
		baseUserRoleEntity = baseUserRoleDao.selectOne(baseUserRoleEntity);
		Integer roleSeq = baseUserRoleEntity.getRoleSeq();
		BaseRoleEntity baseRoleEntity = baseRoleDao.selectById(roleSeq);
		return baseRoleEntity.getCode();
	}
}
