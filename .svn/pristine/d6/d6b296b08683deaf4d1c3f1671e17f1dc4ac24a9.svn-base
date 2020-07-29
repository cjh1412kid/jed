package io.nuite.modules.order_platform_app.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.jiguang.common.utils.StringUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.entity.ReceiverInfoEntity;
import io.nuite.modules.order_platform_app.service.ReceiverInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 收货人信息接口
 * 
 * @author yy
 * @date 2018-06-25 13:47
 */
@RestController
@RequestMapping("/order/app/receiverInfo")
@Api(tags = "订货平台 - 收货人信息接口", description = "收货人管理")
public class ReceiverInfoController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReceiverInfoService receiverInfoService;

	
	
	/**
	 * 收货人列表
	 */
	@Login
	@GetMapping("receiverInfoList")
	@ApiOperation("收货人列表")
	public R receiverInfoList(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq) {
		try {
			List<ReceiverInfoEntity> receiverInfoList= receiverInfoService.getReceiverInfoByUserSeq(userSeq);
			return R.ok(receiverInfoList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("获取失败");
		}
	}
	
	
	
	/**
	 * 新增收货人
	 */
	@Login
	@PostMapping("addReceiverInfo")
	@ApiOperation("新增收货人")
	public R addReceiverInfo(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
			@ApiParam("收货人姓名") @RequestParam("receiverName") String receiverName,
			@ApiParam("电话") @RequestParam("telephone") String telephone,
			@ApiParam("省") @RequestParam("province") String province,
			@ApiParam("省编号") @RequestParam("provinceCode") Integer provinceCode,
			@ApiParam("市") @RequestParam("city") String city,
			@ApiParam("市编号") @RequestParam("cityCode") Integer cityCode,
			@ApiParam("区") @RequestParam("district") String district,
			@ApiParam("区编号") @RequestParam("districtCode") Integer districtCode,
			@ApiParam("详细地址") @RequestParam("detailAddress") String detailAddress,
			@ApiParam("是否默认地址（0：否 1：是）") @RequestParam("isDefault") Integer isDefault) {
		try {
			ReceiverInfoEntity receiverInfoEntity = new ReceiverInfoEntity();
			receiverInfoEntity.setUserSeq(userSeq);
			receiverInfoEntity.setReceiverName(receiverName);
			receiverInfoEntity.setTelephone(telephone);
			receiverInfoEntity.setProvince(province);
			receiverInfoEntity.setProvinceCode(provinceCode);
			receiverInfoEntity.setCity(city);
			receiverInfoEntity.setCityCode(cityCode);
			receiverInfoEntity.setDistrict(district);
			receiverInfoEntity.setDistrictCode(districtCode);
			receiverInfoEntity.setDetailAddress(detailAddress);
			receiverInfoEntity.setIsDefault(isDefault);
			receiverInfoEntity.setInputTime(new Date());
			receiverInfoEntity.setDel(0);
			receiverInfoService.addReceiverInfo(receiverInfoEntity);
			return R.ok("新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("新增失败");
		}
	}
	
	
	
	/**
	 * 修改收货人
	 */
	@Login
	@PostMapping("updateReceiverInfo")
	@ApiOperation("修改收货人")
	public R updateReceiverInfo(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
			@ApiParam("收货人seq") @RequestParam("seq") Integer seq,
			@ApiParam("收货人姓名") @RequestParam(value = "receiverName", required = false) String receiverName,
			@ApiParam("电话") @RequestParam(value = "telephone", required = false) String telephone,
			@ApiParam("省") @RequestParam(value = "province", required = false) String province,
			@ApiParam("省编号") @RequestParam(value = "provinceCode", required = false) Integer provinceCode,
			@ApiParam("市") @RequestParam(value = "city", required = false) String city,
			@ApiParam("市编号") @RequestParam(value = "cityCode", required = false) Integer cityCode,
			@ApiParam("区") @RequestParam(value = "district", required = false) String district,
			@ApiParam("区编号") @RequestParam(value = "districtCode", required = false) Integer districtCode,
			@ApiParam("详细地址") @RequestParam(value = "detailAddress", required = false) String detailAddress,
			@ApiParam("是否默认地址（0：否 1：是）") @RequestParam(value = "isDefault", required = false) Integer isDefault) {
		try {
			ReceiverInfoEntity receiverInfoEntity = new ReceiverInfoEntity();
			receiverInfoEntity.setSeq(seq);
			if(StringUtils.isNotEmpty(receiverName)) {
				receiverInfoEntity.setReceiverName(receiverName);
			}
			if(StringUtils.isNotEmpty(telephone)) {
				receiverInfoEntity.setTelephone(telephone);
			}
			if(StringUtils.isNotEmpty(province) && provinceCode != null) {
				receiverInfoEntity.setProvince(province);
				receiverInfoEntity.setProvinceCode(provinceCode);
			}
			if(StringUtils.isNotEmpty(city) && cityCode != null) {
				receiverInfoEntity.setCity(city);
				receiverInfoEntity.setCityCode(cityCode);
			}
			if(StringUtils.isNotEmpty(district) && districtCode != null) {
				receiverInfoEntity.setDistrict(district);
				receiverInfoEntity.setDistrictCode(districtCode);
			}
			if(StringUtils.isNotEmpty(detailAddress)) {
				receiverInfoEntity.setDetailAddress(detailAddress);
			}
			if(isDefault != null) {
				receiverInfoEntity.setIsDefault(isDefault);
			}
			receiverInfoService.updateReceiverInfo(receiverInfoEntity);
			return R.ok("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("修改失败");
		}
	}
	
	
	
	/**
	 * 设置为默认收货人
	 */
	@Login
	@PostMapping("setReceiverDefault")
	@ApiOperation("设置为默认收货人")
	public R setReceiverDefault(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
			@ApiParam("收货人seq") @RequestParam("seq") Integer seq) {
		try {
			receiverInfoService.setReceiverDefault(userSeq, seq);
			return R.ok("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("设置失败");
		}
	}
	
	
	
	/**
	 * 删除收货人
	 */
	@Login
	@PostMapping("deleteReceiverInfo")
	@ApiOperation("删除收货人")
	public R deleteReceiverInfo(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
			@ApiParam("收货人seq") @RequestParam("seq") Integer seq) {
		try {
			receiverInfoService.deleteReceiverInfo(seq);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("删除失败");
		}
	}
	

}
