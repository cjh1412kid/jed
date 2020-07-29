package io.nuite.modules.order_platform_app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.entity.NoticeEntity;
import io.nuite.modules.order_platform_app.service.HomepageService;
import io.nuite.modules.order_platform_app.service.NoticeService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.entity.HomeCarouselEntity;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.sr_base.service.GoodsPeriodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 首页接口
 *
 * @author yy
 * @date 2018-04-09 13:47
 */
@RestController
@RequestMapping("/order/app/homepage")
@Slf4j
@Api(tags = "jrd - 首页", description = "轮播图 + 爆款 + 新品 + 公告")
public class HomepageController extends BaseController {
    
    @Autowired
    private HomepageService homepageService;
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    @Autowired
    private GoodsPeriodService goodsPeriodService;
    
    @Autowired
    private NoticeService noticeService;
    
    /**
     * 轮播图列表
     */
    @Login
    @GetMapping("carousel")
    @ApiOperation("轮播图列表+公告")
    public R getImgMainUrl(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        List result = new ArrayList();

        
        //获取所有启用的公告
        List<NoticeEntity> noticeList = noticeService.getEnableNoticeList(loginUser.getCompanySeq());
        
        for(NoticeEntity noticeEntity : noticeList) {
            Map<String, Object> noticeMap = new HashMap<>();
	        noticeMap.put("type", -1);
	        noticeMap.put("seq", noticeEntity.getSeq());
	        
	        String imgBasePath = configUtils.getPictureBaseUrl() + "/notice/" + loginUser.getCompanySeq() + "/";
	        noticeEntity.setImgSrc(imgBasePath + noticeEntity.getImageName());
	        noticeMap.put("image", noticeEntity.getImgSrc());
	        
	        noticeMap.put("title", noticeEntity.getName());
	        noticeMap.put("companySeq", noticeEntity.getCompanySeq());
	        noticeMap.put("detailedPage", noticeEntity.getDetailedPage());
	            
	        result.add(noticeMap);
        }
        
        List<HomeCarouselEntity> carouselList = homepageService.getHomeCarouselList(loginUser.getBrandSeq());
        for (HomeCarouselEntity carousel : carouselList) {
            //图片路径
            carousel.setImage(getHomeCarouselPictureUrl(carousel.getBrandSeq().toString()) + carousel.getImage());
            int type = carousel.getType();
            //关联分类或波次，添加标题
            if (type == 2 || type == 3) {
                StringBuilder title = new StringBuilder();
                List<String> linkSeq = Arrays.asList(carousel.getLinkSeq().split(","));
                if (type == 2) {
                    //拼接分类名称
                    List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryService.selectBatchIds(linkSeq);
                    for (GoodsCategoryEntity goodsCategory : goodsCategoryList) {
                        title.append(goodsCategory.getName()).append(",");
                    }
                } else if (type == 3) {
                    //拼接波次名称
                    List<GoodsPeriodEntity> goodsPeriodList = goodsPeriodService.selectBatchIds(linkSeq);
                    for (GoodsPeriodEntity goodsPeriod : goodsPeriodList) {
                        title.append(goodsPeriod.getName()).append(",");
                    }
                }
                if (title.length() > 1) {
                    title.deleteCharAt(title.length() - 1);
                }
                carousel.setTitle(title.toString());
            }
        }
        
        result.addAll(carouselList);
        
        return R.ok().result(result);
    }
    
    @Login
    @GetMapping("notices")
    @ApiOperation("根据用户公司Seq获取所有历史公告")
    public R listAllNotice(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        
        Integer companySeq = loginUser.getCompanySeq();
        
        List<NoticeEntity> noticeEntities = noticeService.selectList(new EntityWrapper<NoticeEntity>()
            .eq("CompanySeq", companySeq).orderBy("InputTime", false));
        
        for (NoticeEntity noticeEntity : noticeEntities) {
            String imgBasePath = configUtils.getPictureBaseUrl() + "/notice/" + companySeq + "/";
            noticeEntity.setImgSrc(imgBasePath + noticeEntity.getImageName());
            
        }
        
        return R.ok().result(noticeEntities);
    }
    
    /**
     * 订货爆款图片
     */
    @Login
    @GetMapping("hotsaleImg")
    @ApiOperation("订货爆款图片")
    public R getHotsaleImg(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        //1.根据 品牌编号 查询 波次编号列表
        List<Integer> periodSeqList = baseService.getPeriodListByBrandSeq(loginUser.getBrandSeq());
        
        //2.根据 波次编号列表、用户销售类型 获取订货爆款
        List<Map<String, Object>> shoesSeqImglist = homepageService.getHotsaleShoes(periodSeqList, null);
        for (Map<String, Object> map : shoesSeqImglist) {
            map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
        }
        return R.ok(shoesSeqImglist);
    }
    
    /**
     * 新品推荐图片
     */
    @Login
    @GetMapping("newestImg")
    @ApiOperation("新品推荐图片")
    public R getNewestImg(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        //1.根据 品牌编号 查询 波次编号列表
        List<Integer> periodSeqList = baseService.getPeriodListByBrandSeq(loginUser.getBrandSeq());
        
        //2.根据 波次编号列表 获取新品推荐
        List<Map<String, Object>> shoesSeqImglist = homepageService.getNewestShoes(periodSeqList, null);
        for (Map<String, Object> map : shoesSeqImglist) {
            map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
        }
        
        return R.ok(shoesSeqImglist);
    }
    
    /**
     * 订货爆款列表
     */
    @Login
    @GetMapping("hotsaleList")
    @ApiOperation("订货爆款列表")
    public R getHotsaleList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                            @ApiParam("起始条数") @RequestParam("start") Integer start,
                            @ApiParam("总条数") @RequestParam("num") Integer num) {
//1.根据 品牌编号 查询 波次编号列表
        List<Integer> periodSeqList = baseService.getPeriodListByBrandSeq(loginUser.getBrandSeq());
        
        //2.根据 波次编号列表 获取订货爆款列表
        List<Map<String, Object>> hotsaleShoeslist = homepageService.getHotsaleShoesList(periodSeqList, null, start, num);
        for (Map<String, Object> map : hotsaleShoeslist) {
            map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));

				/*//判断鞋子展示哪种价格
				if(loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
					map.put("purchasePrice", map.get("oemPrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
					map.put("purchasePrice", map.get("wholesalerPrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
					map.put("purchasePrice", map.get("storePrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.FACTORY.getValue()){
					map.put("purchasePrice", map.get("salePrice"));
				}*/
            map.remove("oemPrice");
            map.remove("wholesalerPrice");
            map.remove("storePrice");
            map.remove("salePrice");
        }
        return R.ok(hotsaleShoeslist);
    }
    
    /**
     * 新品推荐列表
     */
    @Login
    @GetMapping("newestList")
    @ApiOperation("新品推荐列表")
    public R getNewestList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                           @ApiParam("起始条数") @RequestParam("start") Integer start,
                           @ApiParam("总条数") @RequestParam("num") Integer num) {
        //1.根据 品牌编号 查询 波次编号列表
        List<Integer> periodSeqList = baseService.getPeriodListByBrandSeq(loginUser.getBrandSeq());
        
        //2.根据 波次编号列表 获取新品推荐列表
        List<Map<String, Object>> newestShoesList = homepageService.getNewestShoesList(periodSeqList, null, start, num);
        for (Map<String, Object> map : newestShoesList) {
            map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
            
            //判断鞋子展示哪种价格
				/*if(loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
					map.put("purchasePrice", map.get("oemPrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
					map.put("purchasePrice", map.get("wholesalerPrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
					map.put("purchasePrice", map.get("storePrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.FACTORY.getValue()){
					map.put("purchasePrice", map.get("salePrice"));
				}*/
            map.remove("oemPrice");
            map.remove("wholesalerPrice");
            map.remove("storePrice");
            map.remove("salePrice");
        }
        
        return R.ok(newestShoesList);
    }
    
}
