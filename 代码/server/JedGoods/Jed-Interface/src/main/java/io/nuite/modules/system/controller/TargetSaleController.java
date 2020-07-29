package io.nuite.modules.system.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import io.nuite.modules.order_platform_app.service.TargetSalesService;
import io.nuite.modules.order_platform_app.service.TargetShopService;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.system.entity.OrderManageEntity;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/system/target")
public class TargetSaleController extends AbstractController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	  @Autowired
	  private TargetShopService targetShopService;
	  @Autowired
	  private TargetSalesService targetSalesService;
	  
	  @Autowired
	  private BaseShopService baseShopService;
	  
	  @RequestMapping("list")
	  public R getAllList(@RequestParam Integer page,
		        @RequestParam Integer limit,
		        @RequestParam Map<String, Object> params) {
		  try {
			PageUtils targets = targetShopService.getTargetList(new Page(page, limit), getUser().getBrandSeq(), params);
			return  R.ok().put("page", targets);
		  } catch (Exception e) {
			e.printStackTrace();
			return R.error();
		}
		  
	  }
	  
	  @RequestMapping("allShopList")
	  public R allShopList() {
		  Integer companySeq=getUser().getCompanySeq();
		  return R.ok(baseShopService.getShopsByCompanySeq(companySeq));
	  }
	  
	  @RequestMapping("/saveOrUpdate")
	    public R save(TargetShopEntity targetShopEntity) {
		Integer standardSeq=targetShopEntity.getStandardSeq();
		BigDecimal standardMoney=targetShopEntity.getStandardMoney();
		Integer middleSeq=targetShopEntity.getMiddleSeq();
		BigDecimal middleMoney=targetShopEntity.getMiddleMoney();
		Integer advanceSeq=targetShopEntity.getAdvanceSeq();
		BigDecimal advanceMoney=targetShopEntity.getAdvanceMoney();
		if(standardSeq!=null) {
			TargetShopEntity standardShop=new TargetShopEntity();
			standardShop.setSeq(standardSeq);
			standardShop.setMoney(standardMoney);
			  targetShopService.insertOrUpdate(standardShop);
		}else {
			TargetShopEntity standardShop=new TargetShopEntity();
			standardShop.setMoney(standardMoney);
			standardShop.setTargetYear(targetShopEntity.getTargetYear());
			standardShop.setTargetMonth(targetShopEntity.getTargetMonth());
			standardShop.setShopSeq(targetShopEntity.getShopSeq());
			standardShop.setCreator(getUser().getSeq());
			standardShop.setInputTime(new Date());
			standardShop.setDel(0);
			standardShop.setTag(1);
			targetShopService.insertOrUpdate(standardShop);
		}
		if(middleSeq!=null) {
			TargetShopEntity middleShop=new TargetShopEntity();
			middleShop.setSeq(middleSeq);
			middleShop.setMoney(middleMoney);
			  targetShopService.insertOrUpdate(middleShop);
		}else {
			TargetShopEntity middleShop=new TargetShopEntity();
			middleShop.setMoney(middleMoney);
			middleShop.setTargetYear(targetShopEntity.getTargetYear());
			middleShop.setTargetMonth(targetShopEntity.getTargetMonth());
			middleShop.setShopSeq(targetShopEntity.getShopSeq());
			middleShop.setCreator(getUser().getSeq());
			middleShop.setInputTime(new Date());
			middleShop.setDel(0);
			middleShop.setTag(2);
			 targetShopService.insertOrUpdate(middleShop);
		}
		if(advanceSeq!=null) {
			TargetShopEntity advanceShop=new TargetShopEntity();
			advanceShop.setSeq(advanceSeq);
			advanceShop.setMoney(advanceMoney);
			  targetShopService.insertOrUpdate(advanceShop);
		}else {
			TargetShopEntity advanceShop=new TargetShopEntity();
			advanceShop.setMoney(advanceMoney);
			advanceShop.setTargetYear(targetShopEntity.getTargetYear());
			advanceShop.setTargetMonth(targetShopEntity.getTargetMonth());
			advanceShop.setShopSeq(targetShopEntity.getShopSeq());
			advanceShop.setCreator(getUser().getSeq());
			advanceShop.setInputTime(new Date());
			advanceShop.setDel(0);
			advanceShop.setTag(3);
			targetShopService.insertOrUpdate(advanceShop);
		}
	        return R.ok();
	    }
	  
	    @PostMapping("uploadGoodsExcel")
	    public R uploadExcelFile(MultipartFile goodsExcelFile ,@RequestParam("year") Integer year) {
	        logger.info(year.toString());
	        if (goodsExcelFile.isEmpty()) {
	            return R.error("文件不存在！");
	        }
	        try {
	            
	        	targetShopService.parseJRDExcelToSave(goodsExcelFile, getUser(),year);
	/*            if (getUser().getCompanyName() != null && getUser().getCompanyName().contains("吉尔达")) {
	                //吉尔达
	                orderPlanManageService.parseJRDExcelToSave(goodsExcelFile, getUser());
	            } else {
	                orderPlanManageService.parseExcelToSave(goodsExcelFile, getUser());
	            }*/
	            
	        } catch (MybatisPlusException e) {
	            return R.error("数据保存出错！");
	        } catch (RRException e) {
	            return R.error(e.getMsg());
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	            return R.error("文件解析出错！" + e.getMessage());
	        }
	        return R.ok();
	    }
	    
	    @RequestMapping("del")
	    public R del(@RequestParam("seq") Integer seq) {
	    	targetShopService.deleteById(seq);
			return R.ok();
	    }
}
