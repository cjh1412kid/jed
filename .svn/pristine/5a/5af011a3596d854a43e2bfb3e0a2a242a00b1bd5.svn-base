package io.nuite.modules.order_platform_app.service.impl;

import io.nuite.modules.order_platform_app.service.LoginService;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    
    @Autowired
    private BaseUserDao baseUserDao;
    
    @Autowired
    private BaseUserRoleDao baseUserRoleDao;
    
    /**
     * 登录验证
     */
   /* @Override
    public BaseUserEntity login(OnlineLoginForm form) {
        //基础信息验证
        BaseUserEntity baseUser = getBaseUserByAccountName(form.getAccountName());
        Assert.isNull(baseUser, "账号或密码错误");
        
        //密码错误
        if (!baseUser.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))) {
            throw new RRException("账号或密码错误");
        }
        
        //查询用户权限
        List<BaseUserRoleEntity> userRoleEntities = baseUserRoleDao.selectByUserSeq(baseUser.getSeq());
        
        for (BaseUserRoleEntity roleEntity : userRoleEntities) {
            if (Constant.Role.FACTORY_USER.getCode().equalsIgnoreCase(roleEntity.getRoleCode())
                || Constant.Role.FACTORY_ADMIN.getCode().equalsIgnoreCase(roleEntity.getRoleCode())
            ) {
                baseUser.setFactoryUserFlag(true);
                break;
            }
        }
        
        for (BaseUserRoleEntity roleEntity : userRoleEntities) {
            if (Constant.Role.SHOP_ADMIN.getCode().equalsIgnoreCase(roleEntity.getRoleCode())) {
                baseUser.setShopAdminFlag(true);
                break;
            }
        }
        
        baseUser.setPassword(null);
        return baseUser;
    }
    
    //根据accountName查询用户
    private BaseUserEntity getBaseUserByAccountName(String accountName) {
        BaseUserEntity baseUserEntity = new BaseUserEntity();
        baseUserEntity.setAccountName(accountName);
        return baseUserDao.selectOne(baseUserEntity);
    }*/
    
}
