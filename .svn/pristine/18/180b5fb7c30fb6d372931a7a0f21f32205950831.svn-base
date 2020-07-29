package io.nuite.modules.system.service.impl;

import io.nuite.common.utils.Constant;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.dao.SysMenuDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.SysMenuEntity;
import io.nuite.modules.sr_base.service.BaseCompanyService;
import io.nuite.modules.system.dao.SystemUserTokenDao;
import io.nuite.modules.system.entity.SystemUserTokenEntity;
import io.nuite.modules.system.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("sysShiroServiceImpl")
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private BaseUserDao baseUserDao;
    @Autowired
    private BaseUserRoleDao baseUserRoleDao;
    @Autowired
    private SystemUserTokenDao systemUserTokenDao;
    @Autowired
    private BaseCompanyService baseCompanyService;
    
    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;
        
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = baseUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
    
    @Override
    public SystemUserTokenEntity queryByToken(String token) {
        return systemUserTokenDao.queryByToken(token);
    }
    
    @Override
    public BaseUserEntity queryUser(Long userId) {
        BaseUserEntity userEntity = baseUserDao.selectById(userId);
        userEntity.setUserRole(baseUserRoleDao.selectRoleByUserSeq(userEntity.getSeq()));
        return userEntity;
    }
}
