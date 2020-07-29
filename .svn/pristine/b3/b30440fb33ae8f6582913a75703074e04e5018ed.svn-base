package io.nuite.modules.order_platform_app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.service.AllocateTransferTransferInApplicationService;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


/**
 * App端调拨管理Tab模块接口
 *
 * @author yy
 * @date 2019-12-24 15:46:51
 */
@RestController
@RequestMapping("/order/app/allocateTransferInApplication")
@Api(tags = "调拨管理接口")
public class AllocateTransferInApplicationController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    @Autowired
    private AllocateTransferTransferInApplicationService allocateTransferTransferInApplicationService;
    
    
    
    /**
     * 调入、调出列表
     */
    @Login
    @GetMapping("transferInOutList")
    @ApiOperation("调入、调出列表")
    public R transferInOutList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("类型 1:调入 2:调出") @RequestParam(value = "type") Integer type,
    		@ApiParam("状态 -1:全部 0:待处理 1:已同意 2:已作废") @RequestParam(value = "state", required = false, defaultValue = "-1") Integer state,
    		@ApiParam("开始日期") @RequestParam(value = "startDate", required = false) Date startDate,
    		@ApiParam("结束日期") @RequestParam(value = "endDate", required = false) Date endDate,
    		@ApiParam("起始条数（按天统计一天一条）") @RequestParam(value = "start", required = false, defaultValue = "1") Integer start,
    		@ApiParam("总条数（按天统计一天一条）") @RequestParam(value = "num", required = false) Integer num) {
        try {
        	Date nowDate = new Date();
        	//默认的起止日期
        	if(endDate == null) {
        		endDate = nowDate;
        	}
        	if(startDate == null) {
        		startDate = DateUtils.addDateDays(endDate, -30);
        	}
        	if(endDate.before(startDate)) {
        		return R.error("日期范围不正确");
        	}
        	if(num == null) {
        		num = DateUtils.logicDaysBetweenDate(startDate, endDate) + 1; //起止日期间间隔的逻辑天数
        	}
        	if(num > 31) {
        		return R.error("日期范围不能超过31天");
        	}
        	
        	
        	//用户所属门店序号（总部账号填0）
        	Integer userShopSeq = loginUser.getShopSeq();
        	if(loginUser.getShopSeq() == null) {
        		userShopSeq = 0;
        	}
        	
        	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        	
        	//从结束日期开始倒序
        	for(int i = start - 1; i < start + num - 1; i++) {
        		
        		Date date = DateUtils.addDateDays(endDate, -i);
        		String dateStr = DateUtils.format2(date);
        		
        		Map<String, Object> resultMap = new HashMap<String, Object>();
        		resultMap.put("date", dateStr);
        		
        		List<Map<String, Object>> shoeIdSizeNumList = allocateTransferTransferInApplicationService.getDayTransferInOutList(userShopSeq, type, state, dateStr);
        		//循环添加鞋子图片
        		for(Map<String, Object> map : shoeIdSizeNumList) {
        			Integer shoesSeq = (Integer) map.get("shoesSeq");
        			GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesSeq);
        			map.put("goodId", goodsShoesEntity.getGoodID());
        			//查询鞋子图片
                    map.put("img", getGoodsShoesPictureUrl(goodsShoesEntity.getGoodID()) + goodsShoesEntity.getImg1());
        		}
        		resultMap.put("shoeIdSizeNumList", shoeIdSizeNumList);
        		
        		resultList.add(resultMap);
        	}

            return R.ok(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }
    
    
}
