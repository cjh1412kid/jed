package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.common.utils.Constant;
import io.nuite.modules.order_platform_app.dao.PublicityPicDao;
import io.nuite.modules.order_platform_app.service.BaseService;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import io.nuite.modules.system.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private PublicityPicDao publicityPicDao;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private BaseUserRoleDao userRoleDao;

    /**
     * 根据 品牌编号 查询波次编号列表
     */
    @Override
    public List<Integer> getPeriodListByBrandSeq(Integer brandSeq) {
        return publicityPicDao.getPeriodListByBrandSeq(brandSeq);
    }

    /**
     * 查询登录用户所属的工厂管理员
     */
    @Override
    public BaseUserEntity getAdminUserByLoginUser(BaseUserEntity loginUser) {

        List<BaseUserEntity> users = baseUserDao.selectList(new EntityWrapper<BaseUserEntity>()
                .eq("Company_Seq", loginUser.getCompanySeq())
                .eq("Brand_Seq", loginUser.getBrandSeq()));

        //返回第一个管理员用户
        for (BaseUserEntity user : users) {

            List<BaseUserRoleEntity> userRoleEntities = userRoleDao.selectByUserSeqAndRoleCode(user.getSeq(), Constant.Role.FACTORY_ADMIN.getCode());

            if (userRoleEntities != null && userRoleEntities.size() > 0) {
                return user;
            }
        }

        return null;
    }

    /**
     * 查询登录用户所属工厂拥有特定权限的工厂用户列表
     */
    @Override
    public List<BaseUserEntity> getUserByPermission(BaseUserEntity loginUser, String[] perms) {
        Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
        wrapper.where("Company_Seq = {0} AND Brand_Seq = {1} AND SaleType = {2} ", loginUser.getCompanySeq(), loginUser.getBrandSeq(), null);

        List<BaseUserEntity> userList = baseUserDao.selectList(wrapper);
        if (perms != null && perms.length > 0) {
            for (int i = 0; i < userList.size(); i++) {
                BaseUserEntity user = userList.get(i);
                Set<String> permissions = shiroService.getUserPermissions(user.getSeq());
                for (String s : perms) {
                    if (!permissions.contains(s)) {
                        userList.remove(user);
                        i--;
                        break;
                    }
                }
            }
            return userList;
        } else {
            return userList;
        }
    }

}
