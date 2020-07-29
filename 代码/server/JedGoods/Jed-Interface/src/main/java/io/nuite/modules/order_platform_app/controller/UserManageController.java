package io.nuite.modules.order_platform_app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.sr_base.service.BaseRoleService;
import io.nuite.modules.sr_base.entity.BaseRoleEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 用户管理Controller类
 * @author: jxj
 * @create: 2019-03-29 09:20
 */
@RestController
@RequestMapping("/order/app/userManage")
@Api(tags = "jed用户管理")
public class UserManageController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseRoleService baseRoleService;

    @Login
    @ApiOperation("新增/修改用户")
    @PostMapping("/insertUser")
    public R insertUser(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                        @ApiParam(name = "user", value = "用户实体类", required = true) @RequestParam String user) {
        try {

            if (baseUserService.insertOrUpdateUser(loginUser,user)) {
                return R.ok("操作成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error("操作失败");
    }

    @Login
    @ApiOperation("删除用户")
    @PostMapping("/deleteUser")
    @ApiImplicitParams({@ApiImplicitParam(name = "seq",value = "用户序号",required = true,paramType = "query")})
    public R deleteUser(@RequestParam("seq") Integer seq) {
        try {
            if(baseUserService.deleteById(seq)) {
                return R.ok("删除成功");
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("删除失败");
    }

    @Login
    @ApiOperation("获取品牌下用户列表")
    @GetMapping("/selectBrandUser")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageIndex",value = "页数",required = true,paramType = "query"),
                        @ApiImplicitParam(name = "pageSize",value = "查询条数",required = true,paramType = "query")})
    public R selectBrandUser(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                             @RequestParam("pageIndex") Integer pageIndex,
                             @RequestParam("pageSize") Integer pageSize) {
        try {
            Page<BaseUserEntity> page = new Page<>(pageIndex - 1,pageSize);
            Map<String,Object> result = new HashMap<>(10);
            List<BaseUserEntity> baseUserEntities = baseUserService.selectShopUser(loginUser,page);
            for(int i = 0;i < baseUserEntities.size();i++) {
                baseUserEntities.get(i).setHeadImg(getBaseUserPictureUrl(baseUserEntities.get(i).getSeq().toString()) +  baseUserEntities.get(i).getHeadImg());
            }
            result.put("result",baseUserEntities);
            result.put("total",page.getTotal());
            return R.ok().result(result);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取单条用户信息")
    @GetMapping("/selectUserBySeq")
    @ApiImplicitParams({@ApiImplicitParam(name = "seq",value = "用户序号",required = true,paramType = "query")})
    public R selectUserBySeq(@RequestParam("seq") Integer seq) {
        try {
            Map<String,Object> result = new HashMap<>(10);
            result.put("result",baseUserService.selectById(seq));
            return R.ok().result(result);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取角色列表")
    @GetMapping("/selectRole")
    public R selectRole() {
        try {
            Wrapper<BaseRoleEntity> baseRoleEntityWrapper = new EntityWrapper<>();
            baseRoleEntityWrapper.ne("Seq",1);
            Map<String,Object> result = new HashMap<>(10);
            result.put("result",baseRoleService.selectList(baseRoleEntityWrapper));
            return R.ok().result(result);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.error();
    }
}
