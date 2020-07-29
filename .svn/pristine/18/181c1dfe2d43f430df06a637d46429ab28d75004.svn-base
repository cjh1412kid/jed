package io.nuite.modules.order_platform_app.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.entity.PushRecordEntity;
import io.nuite.modules.order_platform_app.service.PushRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 推送记录接口
 * @author yy
 * @date 2018-05-15 13:47
 */
@RestController
@RequestMapping("/order/app/pushRecord")
@Api(tags = "订货平台 - 推送记录", description = "未读消息数量+消息列表+消息状态变更")
public class PushRecordController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private PushRecordService pushRecordService;

    
    /**
     * 未读推送消息数量
     */
    @Login
    @GetMapping("unreadRecordNum")
    @ApiOperation("未读推送消息数量")
    public R unreadRecordNum(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq) {
    	try {
    		//未读消息
    		List<Map<String,Object>> list = pushRecordService.getUnreadRecordNum(userSeq);
			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    /**
     * 所有推送消息列表
     */
    @Login
    @GetMapping("pushRecordList")
    @ApiOperation("所有推送消息列表")
    public R pushRecordList(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num) {
    	try {
    		//所有消息列表
    		List<PushRecordEntity> list = pushRecordService.getPushRecordList(userSeq, start, num);
    		return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
	/**
	 * 标记推送消息为已读
	 */
	@Login
	@PostMapping("readPushRecord")
	@ApiOperation("标记消息为已读")
	public R readPushRecord(@ApiParam("消息的Seq") @RequestParam("seq") Integer seq) {
		try {
			try {
				pushRecordService.readPushRecord(seq);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return R.error("设置失败");
			}
			return R.ok("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
    
    
    

}
