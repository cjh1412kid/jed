package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.utils.RongCloudUtils;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserDao, BaseUserEntity> implements BaseUserService {

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private BaseUserRoleDao baseUserRoleDao;
    
	@Autowired
	private RongCloudUtils rongCloudUtils;

    /**
     * 修改用户信息
     */
    @Override
    public void updateBaseUser(BaseUserEntity baseUserEntity) {
        baseUserDao.updateById(baseUserEntity);
    }

    /**
     * 根据seq获取用户信息
     */
    @Override
    public BaseUserEntity getBaseUserBySeq(Integer userSeq) {
        return baseUserDao.selectById(userSeq);
    }

    /*	@Autowired
        private SysUserRoleService sysUserRoleService;
        @Autowired
        private SysRoleService sysRoleService;
    */
    @Override
    public BaseUserEntity queryByUserName(String username) {
        BaseUserEntity userEntity = baseMapper.queryByUserName(username);
        if(userEntity != null) {
        	userEntity.setUserRole(baseUserRoleDao.selectRoleByUserSeq(userEntity.getSeq()));
        }
        return userEntity;
    }

    @Override
    public List<Long> queryAllMenuId(Long userSeq) {
        return baseMapper.queryAllMenuId(userSeq);
    }

	/*
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");
		Long createUserId = (Long)params.get("createUserId");

		Page<BaseUserEntity> page = this.selectPage(
			new Query<SysUserEntity>(params).getPage(),
			new EntityWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.eq(createUserId != null,"create_user_id", createUserId)
		);

		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.insert(user);

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserSeq(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserSeq(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.deleteBatchIds(Arrays.asList(userId));
	}*/

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        BaseUserEntity userEntity = new BaseUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new EntityWrapper<BaseUserEntity>().eq("Seq", userId).eq("Password", password));
    }

    /**
     * 检查角色是否越权
     *//*
	private void checkRole(SysUserEntity user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}

		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}*/
    @Override
    public void updateRongCloud(Integer userSeq, String token) {
        BaseUserEntity rongCloudUpdate = new BaseUserEntity();
        rongCloudUpdate.setSeq(userSeq);
        rongCloudUpdate.setRongCloudToken(token);
        this.updateById(rongCloudUpdate);
    }

    @Override
    public BaseUserEntity queryUserByCompanyAndAccountName(String accountName, Integer companySeq) {
        return baseUserDao.selectUserByCompanyAndAccountName(accountName, companySeq);
    }

    /**
     * 根据seq更新，忽略Del
     *
     * @param baseUserEntity
     * @return
     */
    @Override
    public boolean updateUserBySeq(BaseUserEntity baseUserEntity) {
        try {
            baseUserDao.updateUserBySeq(baseUserEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    /**
     * 客服列表:获取工厂账号和工厂子账号列表
     */
    @Override
    public List<BaseUserEntity> getCompanyUsers(Page<BaseUserEntity> page, Map<String, Object> map) {
        return baseUserDao.selectUserPage(page, map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateUser(BaseUserEntity loginUser,String user) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(user);
        BaseUserEntity baseUserEntity = (BaseUserEntity)JSONObject.toBean(jsonObject,BaseUserEntity.class);
        baseUserEntity.setInputTime(new Date());
        baseUserEntity.setCompanySeq(loginUser.getCompanySeq());
        baseUserEntity.setBrandSeq(loginUser.getBrandSeq());
        baseUserEntity.setPassword(DigestUtils.sha256Hex(baseUserEntity.getPassword()));
        boolean isSuccess = false;
        if(baseUserEntity.getSeq() == null) {
            isSuccess = retBool(baseMapper.insert(baseUserEntity));
            BaseUserRoleEntity baseUserRoleEntity = new BaseUserRoleEntity();
            baseUserRoleEntity.setUserSeq(baseUserEntity.getSeq());
            baseUserRoleEntity.setRoleSeq(3);
            baseUserRoleDao.insert(baseUserRoleEntity);


            //注册融云生成token
            String rongCloudToken = rongCloudUtils.registerUser(baseUserEntity.getSeq(), baseUserEntity.getUserName(), "1.jpg");
            if(rongCloudToken == null) {
                throw new RuntimeException("获取融云Token失败");
            }
            baseUserEntity.setRongCloudToken(rongCloudToken);
            baseUserDao.updateById(baseUserEntity);
        }else {
            isSuccess = retBool(baseMapper.updateById(baseUserEntity));
            BaseUserRoleEntity baseUserRoleEntity = new BaseUserRoleEntity();
            baseUserRoleEntity.setUserSeq(baseUserEntity.getSeq());
            baseUserRoleEntity.setRoleSeq(3);
            Wrapper<BaseUserRoleEntity> baseUserRoleEntityWrapper = new EntityWrapper<>();
            baseUserRoleEntityWrapper.eq("User_Seq",baseUserEntity.getSeq());
            baseUserRoleDao.update(baseUserRoleEntity,baseUserRoleEntityWrapper);
        }

		
		
        return isSuccess;
    }

    @Override
    public List<BaseUserEntity> selectShopUser(BaseUserEntity loginUser,Page<BaseUserEntity> page) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("brandSeq",loginUser.getBrandSeq());
        map.put("companySeq",loginUser.getCompanySeq());
        return baseUserDao.selectShopUser(page,map);
    }
}
