package io.nuite.modules.order_platform_app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.sr_base.dao.GoodsSXDao;
import io.nuite.modules.sr_base.dao.GoodsSXOptionDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 数据同步（临时使用）
 * @author yy
 * @date 2018-08-08 13:47
 */
@RestController
@RequestMapping("/order/app/SyncData")
public class SyncDataController extends BaseController {
    @Autowired
    private GoodsShoesService goodsShoesService;
    
    @Autowired
    private GoodsSXDao goodsSXDao;
    
    @Autowired
    private GoodsSXOptionDao goodsSXOptionDao;
    
    @Login
    @GetMapping("GoodsSX")
    @ApiOperation("同步goods表8个属性接口")
    public R GoodsSX(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("处理时间流水号，同一时间不再处理") @RequestParam("serialDate") String serialDateStr) {
    	try {
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date serialDate = df.parse(serialDateStr);
    		
    		Wrapper<GoodsSXEntity> SXwrapper = new EntityWrapper<GoodsSXEntity>();
    		SXwrapper.where("InputTime = {0}", serialDate);
    		List<GoodsSXEntity> list = goodsSXDao.selectList(SXwrapper);
    		if(list != null && list.size() > 0) {
    			return R.error("已存在数据，同一时间不再处理");
    		}
    		
    		//查询所有鞋子的所有公司序号
    		Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<GoodsShoesEntity>();
    		wrapper.setSqlSelect("DISTINCT Company_Seq AS companySeq");
    		List<Object> companySeqList = goodsShoesService.selectObjs(wrapper);
    		
    		
//    	        鞋面材质
//    	    @TableField(value = "SurfaceMaterial")
//    	    private String surfaceMaterial;
//    	        鞋里材质
//    	    @TableField(value = "InnerMaterial")
//    	    private String innerMaterial;
//    	        流行元素
//    	    @TableField(value = "PopularElement")
//    	    private String popularElement;
//    	        鞋底材质
//    	    @TableField(value = "SoleMaterial")
//    	    private String soleMaterial;
//    	        闭合方式
//    	    @TableField(value = "CloseForm")
//    	    private String closeForm;
//    	        制造工艺
//    	    @TableField(value = "HeelShape")
//    	    private String heelShape;
//    	        鞋头风格
//    	    @TableField(value = "ToeStyle")
//    	    private String toeStyle;
//    	        鞋跟高度
//    	    @TableField(value = "HeelHeight")
//    	    private String heelHeight;
    		
    		String[] defaultSXNameArr = {"鞋面材质", "鞋里材质", "流行元素", "鞋底材质", "闭合方式", "制造工艺", "鞋头风格", "鞋跟高度"};
    		String[] defaultSXcolumnArr = {"SurfaceMaterial", "InnerMaterial", "PopularElement", "SoleMaterial", "CloseForm", "HeelShape", "ToeStyle", "HeelHeight"};
    		for(Object companySeq : companySeqList) {
        		GoodsSXEntity goodsSXEntity = new GoodsSXEntity();
        		goodsSXEntity.setCompanySeq((int)companySeq);
        		for(int i = 1; i <= defaultSXNameArr.length; i++) {
        			//SX表添加8个属性的中文含义
        			String defaultSXName = defaultSXNameArr[i-1];
        			goodsSXEntity.setSXId("SX" + i);
        			goodsSXEntity.setSXName(defaultSXName);
        			goodsSXEntity.setInputTime(serialDate);
        			goodsSXDao.insert(goodsSXEntity);
        			
        			//查询每个属性的可选值
        			wrapper = new EntityWrapper<GoodsShoesEntity>();
        			wrapper.setSqlSelect("DISTINCT " + defaultSXcolumnArr[i-1]).where("Company_Seq = {0} ", (int)companySeq);
            		List<Object> optionsList = goodsShoesService.selectObjs(wrapper);

            		GoodsSXOptionEntity goodsSXOptionEntity = new GoodsSXOptionEntity();
        			goodsSXOptionEntity.setSXSeq(goodsSXEntity.getSeq());
            		for(int j = 1; j <= optionsList.size(); j++) {
            			//移除空的option
            			Object option = optionsList.get(j-1);
              			if(option == null || StringUtils.isBlank(option.toString())) {
            				optionsList.remove(option);
            				j--;
            				continue;
            			}
              			
            			//SX_Option表添加每个属性的可选值
            			String code = "00" + j;
            			String optionCode = code.substring(code.length() - 3, code.length());
            			String optionValue = (String)optionsList.get(j-1);
	            		goodsSXOptionEntity.setCode(optionCode);
	            		goodsSXOptionEntity.setValue(optionValue);
	            		goodsSXOptionEntity.setInputTime(serialDate);
	            		goodsSXOptionDao.insert(goodsSXOptionEntity);
	            		
	            		
	            		//查询goods表，根据填入的中文，为SX1~SX8字段添加code值
	            		Wrapper<GoodsShoesEntity> goodsWrapper = new EntityWrapper<GoodsShoesEntity>();
	            		goodsWrapper.where("Company_Seq = {0} AND " + defaultSXcolumnArr[i-1] + " = {1}" , (int)companySeq, optionValue);
	            		GoodsShoesEntity goodsShoesEntity = new GoodsShoesEntity();
	            		if(i == 1) {
		            		goodsShoesEntity.setSX1(optionCode);
	            		} else if(i == 2) {
		            		goodsShoesEntity.setSX2(optionCode);
	            		} else if(i == 3) {
		            		goodsShoesEntity.setSX3(optionCode);
	            		} else if(i == 4) {
		            		goodsShoesEntity.setSX4(optionCode);
	            		} else if(i == 5) {
		            		goodsShoesEntity.setSX5(optionCode);
	            		} else if(i == 6) {
		            		goodsShoesEntity.setSX6(optionCode);
	            		} else if(i == 7) {
		            		goodsShoesEntity.setSX7(optionCode);
	            		} else if(i == 8) {
		            		goodsShoesEntity.setSX8(optionCode);
	            		}
	            		goodsShoesService.update(goodsShoesEntity, goodsWrapper);
            		}
        		}
    		}
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("服务器异常");
		}
    }
    
}
