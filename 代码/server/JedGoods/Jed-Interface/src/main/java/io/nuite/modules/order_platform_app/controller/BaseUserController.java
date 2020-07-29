package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.service.CommunityService;
import io.nuite.modules.order_platform_app.service.PushRecordService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.order_platform_app.utils.RongCloudUtils;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseBrandService;
import io.nuite.modules.sr_base.service.BaseCompanyService;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户基本信息接口
 *
 * @author yy
 * @date 2018-04-27 13:47
 */
@RestController
@RequestMapping("/order/app/baseUser")
@Api(tags = "订货平台 - 用户基本信息", description = "修改用户基本信息 + 直播相关")
public class BaseUserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private RongCloudUtils rongCloudUtils;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private JPushUtils jPushUtils;

    @Autowired
    private PushRecordService pushRecordService;

    @Autowired
    private BaseCompanyService baseCompanyService;

    @Autowired
    private BaseBrandService baseBrandService;


    /**
     * 修改用户基本信息
     */
    @Login
    @PostMapping("updateBaseUser")
    @ApiOperation("修改用户基本信息")
    public R updateBaseUser(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
                            @ApiParam("姓名") @RequestParam(value = "userName", required = false) String userName,
                            @ApiParam("电话") @RequestParam(value = "telephone", required = false) String telephone,
                            HttpServletRequest request) {
        try {
            if (userName != null || telephone != null) {
                BaseUserEntity baseUserEntity = new BaseUserEntity();
                baseUserEntity.setSeq(userSeq);
                if (userName != null && !userName.equals("")) {
                    baseUserEntity.setUserName(userName);
                    //修改姓名后需要刷新融云平台用户信息
//					rongCloudUtils.refreshUserInfo(userSeq, userName, getBaseUserPictureUrl(userSeq.toString()) + baseUserEntity.getHeadImg());
                    rongCloudUtils.refreshUserInfo(userSeq, userName, "1.jpg");
                }
                if (telephone != null && !telephone.equals("")) {
                    baseUserEntity.setTelephone(telephone);
                }
                baseUserService.updateBaseUser(baseUserEntity);
            }
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("修改失败");
        }
    }


    /**
     * 修改用户密码
     */
    @Login
    @PostMapping("updateUserPassword")
    @ApiOperation("修改用户密码")
    public R updateUserPassword(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                @ApiParam("原密码") @RequestParam(value = "oldPassword", required = false) String oldPassword,
                                @ApiParam("新密码") @RequestParam(value = "newPassword", required = false) String newPassword,
                                HttpServletRequest request) {
        try {
            //判断密码是否正确
            if (!DigestUtils.sha256Hex(oldPassword).equals(loginUser.getPassword())) {
                return R.error(HttpStatus.SC_FORBIDDEN, "密码错误");
            }
            BaseUserEntity baseUserEntity = new BaseUserEntity();
            baseUserEntity.setSeq(loginUser.getSeq());
            baseUserEntity.setPassword(DigestUtils.sha256Hex(newPassword));
            baseUserService.updateBaseUser(baseUserEntity);
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("修改失败");
        }
    }


    /**
     * 更换用户头像
     */
    @Login
    @PostMapping("changeHeadImg")
    @ApiOperation("更换用户头像")
    public R changeHeadImg(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
                           @ApiParam("用户头像") @RequestParam("headImg") MultipartFile headImg,
                           HttpServletRequest request) {
        try {
            BaseUserEntity baseUserEntity = new BaseUserEntity();
            baseUserEntity.setSeq(userSeq);
            baseUserEntity.setHeadImg(upLoadFile(userSeq, getBaseUserUploadUrl(request, userSeq.toString()), headImg));
            baseUserService.updateBaseUser(baseUserEntity);

            Map<String, Object> map = new HashMap<>();
            map.put("headImg", getBaseUserPictureUrl(userSeq.toString()) + baseUserEntity.getHeadImg());
            return R.ok(map).put("msg", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("修改失败");
        }
    }

    /**
     * 关于我们（获取工厂公司、品牌信息）
     */
    @Login
    @GetMapping("getCompanyBrandInfo")
    @ApiOperation("关于我们（公司、品牌信息）")
    public R getCompanyBrandInfo(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        try {
            Map<String, Object> companyMap = baseCompanyService.getCompanyMapBySeq(loginUser.getCompanySeq());
            Map<String, Object> brandMap = baseBrandService.getBrandMapBySeq(loginUser.getBrandSeq());
            //处理品牌图片路径
            brandMap.put("brandImage", getBaseBrandPictureUrl(loginUser.getBrandSeq().toString()) + brandMap.get("brandImage"));

            companyMap.putAll(brandMap);
            return R.ok(companyMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获取失败");
        }

    }
}
