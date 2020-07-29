package io.nuite.modules.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.utils.R;
import io.nuite.common.validator.Assert;
import io.nuite.modules.sr_base.entity.BaseRoleEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.from.PasswordForm;
import io.nuite.modules.system.service.SystemRoleMenuService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/system/user")
public class SystenUserController extends AbstractController {
    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private SystemRoleMenuService systemRoleService;

    @GetMapping("/roles")
    public List<BaseRoleEntity> roles() {
        return systemRoleService.selectRoles();
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public R info() {
        return R.ok().put("user", getUser()).put("rongCloudKey", configUtils.getOrderPlatformApp().getRongCloudAppKey());
    }

    /**
     * 所有用户列表
     *//*
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getUserSeq() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserSeq());
		}
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}*/

    /**
     * 修改登录用户密码
     */
    @PostMapping("/password")
    public R password(@RequestBody PasswordForm form) {
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");

        //sha256加密
        String password = DigestUtils.sha256Hex(form.getPassword());
        //sha256加密
        String newPassword = DigestUtils.sha256Hex(form.getNewPassword());

        //更新密码
        boolean flag = baseUserService.updatePassword(getUserSeq(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     *//*
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		BaseUserEntity user = sysUserService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	*//**
     * 保存用户
     *//*
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody BaseUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserSeq());
		sysUserService.save(user);
		
		return R.ok();
	}
	
	*//**
     * 修改用户
     *//*
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody BaseUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserSeq());
		sysUserService.update(user);
		
		return R.ok();
	}
	
	*/

    /**
     * 删除用户
     *//*
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserSeq())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}*/
    @GetMapping("/chatInit")
    public JSONObject chatInit() {
        JSONObject data = new JSONObject();
        String msg = "";
        int code = 0;
        try {
            String userAvatarUrl = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getBaseUser() + "/";
            BaseUserEntity baseUserEntity = getUser();
            JSONObject mine = new JSONObject();
            mine.accumulate("username", baseUserEntity.getUserName());
            mine.accumulate("id", baseUserEntity.getSeq());
            mine.accumulate("status", "online");
            mine.accumulate("sign", "");
            mine.accumulate("avatar", userAvatarUrl + getUserHeader(baseUserEntity.getHeadImg(), baseUserEntity.getSeq()));

            JSONArray groups = new JSONArray();
            // 工厂子账号-》客服列表
            List<BaseUserEntity> subAccountUser = baseUserService.selectList(new EntityWrapper<BaseUserEntity>()
                    .eq("Company_Seq", baseUserEntity.getCompanySeq())
                    .ne("Seq", baseUserEntity.getSeq())
                    .orderBy("InputTime")
                    .eq("Del", 0)
            );
            for (BaseUserEntity subAccount : subAccountUser) {
                JSONObject sub = new JSONObject();
                sub.accumulate("groupname", subAccount.getUserName());
                sub.accumulate("id", subAccount.getSeq());
                sub.accumulate("avatar", userAvatarUrl + getUserHeader(subAccount.getHeadImg(), subAccount.getSeq()));
                groups.add(sub);
            }


            JSONArray friends = new JSONArray();
            List<Integer> saleTypes = new ArrayList<>();
            // 工厂订货方-》好友列表
            List<BaseUserEntity> orderAccountUser = baseUserService.selectList(new EntityWrapper<BaseUserEntity>()
                    .eq("Company_Seq", baseUserEntity.getCompanySeq())
                    .in("SaleType", saleTypes)
                    .orderBy("InputTime")
                    .eq("Del", 0)
            );
            for (BaseUserEntity orderAccount : orderAccountUser) {
                JSONObject order = new JSONObject();
                order.accumulate("username", orderAccount.getUserName());
                order.accumulate("id", orderAccount.getSeq());
                order.accumulate("avatar", userAvatarUrl + getUserHeader(orderAccount.getHeadImg(), orderAccount.getSeq()));
                order.accumulate("sign", "");
                order.accumulate("status", "online");
                friends.add(order);
            }
            JSONArray friendGroup = new JSONArray();
            JSONObject friendOneGroup = new JSONObject();
            friendOneGroup.accumulate("groupname", "我的客户");
            friendOneGroup.accumulate("id", 0);
            friendOneGroup.accumulate("list", friends);
            friendGroup.add(friendOneGroup);

            data.accumulate("mine", mine);
            data.accumulate("friend", friendGroup);
            data.accumulate("group", groups);
        } catch (Exception e) {
            logger.error("chat init error", e);
            code = 1;
            msg = "初始化信息失败";
        }


        JSONObject result = new JSONObject();
        result.accumulate("code", code);
        result.accumulate("msg", msg);
        result.accumulate("data", data);
        return result;
    }

    private String getUserHeader(String header, Integer seq) {
        String resultHeader;
        if (StringUtils.isBlank(header)) {
            resultHeader = "default.png";
        } else {
            resultHeader = seq + "/" + header;
        }
        return resultHeader;
    }
}
