package io.nuite.modules.system.service.impl;

import io.nuite.common.utils.PageUtils;
import io.nuite.modules.order_platform_app.utils.RongCloudUtils;
import io.nuite.modules.sr_base.entity.BaseBrandEntity;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import io.nuite.modules.sr_base.service.*;
import io.nuite.modules.system.dao.SystemFactoryDao;
import io.nuite.modules.system.entity.SysFactoryEntity;
import io.nuite.modules.system.service.SystemFactoryService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SystemFactoryServiceImpl implements SystemFactoryService {
    
    @Resource
    private SystemFactoryDao systemFactoryDao;
    
    @Autowired
    private SysUserMenuService sysUserMenuService;
    
    @Autowired
    private BaseUserService baseUserService;
    
    @Autowired
    private BaseUserRoleService baseUserRoleService;
    
    @Autowired
    private BaseCompanyService baseCompanyService;
    
    @Autowired
    private BaseBrandService baseBrandService;
    
    @Autowired
    private RongCloudUtils rongCloudUtils;
    
    @Override
    public PageUtils queryFactoryPage(Map<String, Object> params) {
        int limit = Integer.valueOf(params.get("limit").toString());
        int page = Integer.valueOf(params.get("page").toString());
        Object keywords = params.get("keywords");
        List<SysFactoryEntity> list = systemFactoryDao.queryFactoryPage(limit, page, keywords);
        int totalCount = systemFactoryDao.queryFactoryTotal(keywords);
        return new PageUtils(list, totalCount, limit, page);
    }
    
    @Override
    public SysFactoryEntity queryFactory(Long seq) {
        SysFactoryEntity sysFactoryEntity = systemFactoryDao.queryFactoryOne(seq);
        List<Long> allMenuId = baseUserService.queryAllMenuId(sysFactoryEntity.getSeq());
        sysFactoryEntity.setMenuIdList(allMenuId);
        return sysFactoryEntity;
    }
    
    @Override
    @Transactional
    public void saveFactory(SysFactoryEntity factoryEntity) {
        // 保存公司信息
        BaseCompanyEntity baseCompanyEntity = new BaseCompanyEntity();
        baseCompanyEntity.setName(factoryEntity.getCompanyName());
        baseCompanyEntity.setCompanyTypeSeq(1);
        baseCompanyEntity.setParentSeq(0);
        Long companySeq = factoryEntity.getCompanySeq();
        if (companySeq != null) {
            baseCompanyEntity.setSeq(companySeq.intValue());
        }
        baseCompanyService.insertOrUpdate(baseCompanyEntity);
        
        Integer initedCompanySeq = baseCompanyEntity.getSeq();
        
        // 保存品牌信息
        BaseBrandEntity baseBrandEntity = new BaseBrandEntity();
        baseBrandEntity.setName(factoryEntity.getBrandName());
        baseBrandEntity.setCompanySeq(initedCompanySeq);
        Long brandSeq = factoryEntity.getBrandSeq();
        if (brandSeq != null) {
            baseBrandEntity.setSeq(brandSeq.intValue());
        }
        
        baseBrandService.insertOrUpdate(baseBrandEntity);
        
        Integer initedBrandSeq = baseBrandEntity.getSeq();
        
        // 保存用户信息
        BaseUserEntity baseUserEntity = new BaseUserEntity();
        baseUserEntity.setAccountName(factoryEntity.getAccountName());
        baseUserEntity.setUserName(factoryEntity.getUserName());
        baseUserEntity.setCompanySeq(initedCompanySeq);
        baseUserEntity.setBrandSeq(initedBrandSeq);
        if (factoryEntity.getSeq() != null) {
            baseUserEntity.setSeq(factoryEntity.getSeq().intValue());
    
            baseUserService.updateById(baseUserEntity);
        } else {
            String newPassword = DigestUtils.sha256Hex("123");
            baseUserEntity.setPassword(newPassword);
            baseBrandEntity.setName("default");
            baseUserService.insert(baseUserEntity);
    
            //新用户，保存用户角色关系
            BaseUserRoleEntity baseUserRoleEntity = new BaseUserRoleEntity();
            baseUserRoleEntity.setUserSeq(baseUserEntity.getSeq());
            baseUserRoleEntity.setRoleSeq(1);
            baseUserRoleService.insert(baseUserRoleEntity);
        }
        
        Integer initedUserSeq = baseUserEntity.getSeq();
        
        // 获取融云token
        baseUserEntity = baseUserService.selectById(initedUserSeq);
        String userName = baseUserEntity.getUserName();
        String rongCloudToken = baseUserEntity.getRongCloudToken();
        if (userName == null) userName = "default";
        String newRongCloudToken;
        if (StringUtils.isBlank(rongCloudToken)) {
            newRongCloudToken = rongCloudUtils.registerUser(initedUserSeq, userName, "123");
            BaseUserEntity rongCloudUpdate = new BaseUserEntity();
            rongCloudUpdate.setSeq(initedUserSeq);
            rongCloudUpdate.setRongCloudToken(newRongCloudToken);
            baseUserService.updateById(rongCloudUpdate);
        } else {
            rongCloudUtils.refreshUserInfo(initedUserSeq, userName, "123");
        }
        
        // 更新菜单权限
        List<Long> menuIdList = factoryEntity.getMenuIdList();
        sysUserMenuService.updateUserMenu(initedUserSeq.longValue(), menuIdList);
    }
    
    @Override
    public void deleteFactory(Long seq) {
        //todo 删除工厂的所有子账号和对应的菜单
        //todo 删除用户对应的菜单
    }
}
