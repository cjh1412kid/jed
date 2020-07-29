package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.Query;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.utils.RongCloudUtils;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.sr_base.service.SysUserMenuService;
import io.nuite.modules.system.service.SysFactorySubService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysFactorySubServiceImpl implements SysFactorySubService {
    @Autowired
    private BaseUserService baseUserService;
    
    @Autowired
    private SysUserMenuService sysUserMenuService;
    
    @Autowired
    private BaseUserRoleDao baseUserRoleDao;
    
    @Autowired
    private RongCloudUtils rongCloudUtils;
    
    @Override
    public PageUtils subAccountList(BaseUserEntity baseUserEntity, Map<String, Object> params) {
        // 查询工厂子账号
        String key = (String) params.get("key");
        
        Page<BaseUserEntity> page = new Query<BaseUserEntity>(params).getPage();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("UserName", key);
        map.put("CompanySeq", baseUserEntity.getCompanySeq());
        
        page.setOrderByField("ybu.InputTime").setAsc(false);
        List<BaseUserEntity> list = baseUserService.getCompanyUsers(page, map);
        // 去除账号中的密码
        for (BaseUserEntity user : list) {
            user.setPassword("");
            user.setUserRole(baseUserRoleDao.selectRoleByUserSeq(user.getSeq()));
        }
        return new PageUtils(list, page.getTotal(), page.getSize(), page.getCurrent());
    }
    
    @Override
    public R querySubFactory(Long seq) {
        R result = R.ok();
        BaseUserEntity baseUserEntity = baseUserService.selectById(seq.intValue());
        baseUserEntity.setUserRole(baseUserRoleDao.selectRoleByUserSeq(baseUserEntity.getSeq()));
        baseUserEntity.setPassword("");
        result.put("account", baseUserEntity);
        List<Long> allMenuId = baseUserService.queryAllMenuId(seq);
        result.put("menu", allMenuId);
        return result;
    }
    
    @Override
    @Transactional
    public void newSubAccount(BaseUserEntity baseUserEntity, List<Long> menus, BaseUserEntity createUser) {
        if (baseUserEntity.getUserRole() == null || baseUserEntity.getUserRole().getSeq() == null) {
            throw new RRException("角色信息未填写");
        }
        Integer roleSeq = baseUserEntity.getUserRole().getSeq();
        // 判断账号是否存在，并保存当前用户信息
        BaseUserEntity oldUserEntity = baseUserService.selectOne(new EntityWrapper<BaseUserEntity>()
            .eq("AccountName", baseUserEntity.getAccountName().trim())
            .eq("Del", 0));
        if (oldUserEntity != null) {
            throw new RRException("账号名已存在，请输入其他账号名称！");
        }
        BaseUserEntity newUserEntity = new BaseUserEntity();
        newUserEntity.setAccountName(baseUserEntity.getAccountName().trim());
        String newPassword = DigestUtils.sha256Hex(baseUserEntity.getPassword());
        newUserEntity.setPassword(newPassword);
        newUserEntity.setUserName(baseUserEntity.getUserName().trim());
        newUserEntity.setTelephone(baseUserEntity.getTelephone().trim());
        newUserEntity.setCompanySeq(createUser.getCompanySeq());
        newUserEntity.setBrandSeq(createUser.getBrandSeq());
        newUserEntity.setShopSeq(baseUserEntity.getShopSeq());
        newUserEntity.setInputTime(new Date());
        newUserEntity.setDel(0);
        baseUserService.insert(newUserEntity);
        
        // 角色信息插入
        BaseUserRoleEntity userRoleEntity = new BaseUserRoleEntity();
        userRoleEntity.setRoleSeq(roleSeq);
        userRoleEntity.setUserSeq(newUserEntity.getSeq());
        baseUserRoleDao.insert(userRoleEntity);
        
        Integer initedUserSeq = newUserEntity.getSeq();
        // 获取融云token
        baseUserEntity = baseUserService.selectById(initedUserSeq);
        String userName = baseUserEntity.getUserName();
        String rongCloudToken = baseUserEntity.getRongCloudToken();
        if (userName == null) userName = "default";
        String newRongCloudToken;
        if (org.apache.commons.lang.StringUtils.isBlank(rongCloudToken)) {
            newRongCloudToken = rongCloudUtils.registerUser(initedUserSeq, userName, "123");
            baseUserService.updateRongCloud(initedUserSeq, newRongCloudToken);
        } else {
            rongCloudUtils.refreshUserInfo(initedUserSeq, userName, "123");
        }
        
        // 插入用户的菜单
        sysUserMenuService.updateUserMenu(initedUserSeq.longValue(), menus);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSubAccount(BaseUserEntity baseUserEntity, List<Long> menus, BaseUserEntity createUser) {
        
        if (StringUtils.isNotBlank(baseUserEntity.getPassword())) {
            String newPassword = DigestUtils.sha256Hex(baseUserEntity.getPassword());
            baseUserEntity.setPassword(newPassword);
        }
        
        baseUserService.updateById(baseUserEntity);
        
        rongCloudUtils.refreshUserInfo(baseUserEntity.getSeq(), baseUserEntity.getUserName().trim(), "123");
        
        // 插入用户的菜单
        sysUserMenuService.updateUserMenu(baseUserEntity.getSeq().longValue(), menus);
    }
    
    @Override
    @Transactional
    public void delSubAccount(Long seq, BaseUserEntity createUser) {
        BaseUserEntity oldUserEntity = baseUserService.selectOne(new EntityWrapper<BaseUserEntity>()
            .eq("Seq", seq)
            .eq("Del", 0));
        if (oldUserEntity == null) {
            throw new RRException("账号不存在！");
        }
        
        sysUserMenuService.updateUserMenu(seq, null);
        
        baseUserService.deleteById(seq.intValue());
    }
}
