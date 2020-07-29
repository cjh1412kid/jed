package io.nuite.modules.order_platform_app.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.nuite.modules.order_platform_app.entity.OnlineMessageEntity;
import io.nuite.modules.order_platform_app.service.OnlineMessageService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 在线沟通接口
 * @author yy
 * @date 2018-04-14 16:47
 */
@RestController
@RequestMapping("/order/app/onlineMessage")
@Api(tags = "订货平台 - 在线沟通", description = "在线沟通接口")
public class OnlineMessageController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private OnlineMessageService onlineMessageService;
    
  
	/**
	 * 根据指定用户seq（支持多个seq）获取用户（或多个用户）的详细信息
	 */
	@Login
	@GetMapping("getBaseUserInfoBySeqs")
	@ApiOperation("根据用户seq（支持多个）获取用户详细信息")
	public R getBaseUserInfoBySeqs(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
			@ApiParam("用户seq（支持多个）") @RequestParam("userSeqs") String userSeqs,
			HttpServletRequest request) {
		try {
			List<Map<String,Object>> list =  onlineMessageService.getBaseUserInfoBySeqs(userSeqs);
			for(Map<String, Object> map : list) {
				if (map.get("headImg") != null && !map.get("headImg").toString().equals("")) {
					map.put("headImg", getBaseUserPictureUrl(map.get("seq").toString()) + map.get("headImg"));
				}
			}
			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
    
    
	/**
	 * 保存在线沟消息记
	 */
	@Login
	@PostMapping("addOnlineMessage")
	@ApiOperation("保存在线沟消息记")
	public R addOnlineMessage(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
			@ApiParam("接收消息对象的Seq(用户或群组)") @RequestParam("receiveObjectSeq") Integer receiveObjectSeq,
			@ApiParam("接收对象类型 PRIVATE用户,GROUP群组") @RequestParam("receiveObjectType") String receiveObjectType,
			@ApiParam("消息内容") @RequestParam(value = "content", required = false) String content,
			@ApiParam("图片") @RequestParam(value = "img", required = false) MultipartFile img,
			HttpServletRequest request) {
		try {
			OnlineMessageEntity onlineMessageEntity = new OnlineMessageEntity();
			onlineMessageEntity.setSenderUserSeq(userSeq);
			onlineMessageEntity.setReceiveObjectSeq(receiveObjectSeq);
			if("PRIVATE".equals(receiveObjectType)) {
				onlineMessageEntity.setReceiveObjectType(1);
			} else if ("GROUP".equals(receiveObjectType)) {
				onlineMessageEntity.setReceiveObjectType(2);
			}
			onlineMessageEntity.setContent(content);
			if (img != null) {
				onlineMessageEntity.setImgPath(upLoadFile(userSeq, getOnlineMessageUploadUrl(request, userSeq.toString()), img));
			}
			onlineMessageEntity.setInputTime(new Date());
			onlineMessageEntity.setDel(0);

			onlineMessageService.addOnlineMessage(onlineMessageEntity);
			return R.ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
	
	
	/**
	 * 客服列表（工厂和工厂子账号）
	 */
	@Login
	@GetMapping("getCustomServiceList")
	@ApiOperation("客服列表（工厂和工厂子账号）")
	public R getCustomServiceList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
		try {
			List<Map<String, Object>> list =  onlineMessageService.getCustomServiceList(loginUser);
			for(Map<String, Object> map : list) {
				if (map.get("headImg") != null && !map.get("headImg").toString().equals("")) {
					map.put("headImg", getBaseUserPictureUrl(map.get("seq").toString()) + map.get("headImg"));
				}
			}
			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}

}
