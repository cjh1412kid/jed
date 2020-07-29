package io.nuite.modules.order_platform_app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import io.nuite.modules.order_platform_app.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.entity.ShopMainpushEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.sr_base.service.GoodsSizeService;
import io.nuite.modules.system.service.ShoesReplenishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * jrd鞋子列表查询
 *
 * @author yy
 * @date 2018-04-18 13:47
 */
@RestController
@RequestMapping("/order/app/shoesInfo")
@Api(tags = "jrd鞋子列表查询")
public class ShoesInfoController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsShoesService goodsShoesService;

    @Autowired
    private ShoesInfoService shoesInfoService;

    @Autowired
    private ShoesDataService shoesDataService;

    @Autowired
    private SaleShoesDetailService saleShoesDetailService;

    @Autowired
    private BaseShopService shopService;

    @Autowired
    private GoodsSizeService goodsSizeService;

    @Autowired
    private ShoesReplenishService shoesReplenishService;

    @Autowired
    private ShopMainpushService shopMainpushService;

    @Autowired
    private ShoesDataDailyDetailService shoesDataDailyDetailService;

    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    @Autowired
    private AllocateTransferFictitiousMallService allocateTransferFictitiousMallService;

    /**
     * 全部鞋款列表
     */
    @Login
    @GetMapping("shoesList")
    @ApiOperation("全部鞋款列表(支持按多个分类查询)")
    public R getShoesList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
	    @ApiParam("范围（0:全部（可筛选多个门店） 1:本店（门店调用，查本店货品）2:总仓（总部+门店调用，查总仓货品））") @RequestParam("rangeType") Integer rangeType,
	    @ApiParam("类型（0:全部货品 1:新品推荐）") @RequestParam("dataType") Integer dataType,
	    @ApiParam("补单  (0:无补单 1:有补单)") @RequestParam(value = "supplement", required = false) Integer supplement,
	    @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
	    @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
	    @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
	    @ApiParam("颜色") @RequestParam(value = "colorSeqList", required = false) List<Integer> colorSeqList,
	    @ApiParam(value = "自定义属性", example = "{SX1:'001',SX2:'002'}") @RequestParam(value = "sXMap", required = false, defaultValue = "{}") String sXMapStr,
	    @ApiParam("模糊查询条件（多个用空格隔开）") @RequestParam(value = "fuzzySearchParam", required = false) String fuzzySearchParam,
	    @ApiParam("门店序号列表") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
	    @ApiParam("尺码类型： 0:断码 1:缺码 2:自定义 3:齐码") @RequestParam(value = "sizeType", required = false) Integer sizeType,
	    @ApiParam("尺码：起始") @RequestParam(value = "sizeSeqStart", required = false) Integer sizeSeqStart,
	    @ApiParam("尺码：结束") @RequestParam(value = "sizeSeqEnd", required = false) Integer sizeSeqEnd,
	    @ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:日平均销量 5:总库存 6:总仓库存 7:门店总库存)") @RequestParam("orderBy") Integer orderBy,
	    @ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
	    @ApiParam("起始条数") @RequestParam("start") Integer start,
	    @ApiParam("总条数") @RequestParam("num") Integer num,
	    @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = true) Date saleTimeStart,
	    @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = true) Date saleTimeEnd,
	    @ApiParam("库存最小数量") @RequestParam(value = "stockMinNum", required = false) Integer stockMinNum,
	    @ApiParam("库存最大数量") @RequestParam(value = "stockMaxNum", required = false) Integer stockMaxNum,
	    @ApiParam("总仓入仓时间最小时间（后台调拨页面用）") @RequestParam(value = "depositTime", required = false) Date depositTime,
	    @ApiParam("请求来源是否为后台调拨页面(0:不是 1:是)") @RequestParam(value = "type", required = false, defaultValue = "0") Integer requestFromType
	    ) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> sXMap = JSONObject.parseObject(sXMapStr, Map.class);
            /**1.根据属性条件，查询满足属性条件的所有鞋子序号**/
            List<Integer> shoesSeqList = new ArrayList<>(10);
            if (rangeType == 0 && shopSeqList != null && shopSeqList.size() > 0) {
                shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, colorSeqList, sXMap, fuzzySearchParam, null, null, shopSeqList, depositTime);
            } else {
                shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, colorSeqList, sXMap, fuzzySearchParam, null, null, null, depositTime);
            }
            
            /**筛选是否总部已补单 **/
            if (supplement != null) {
            	//总部已补单鞋子Seq List
                List<Object> replenishShoesSeqList =  shoesReplenishService.getSupplementShoesSeqList(loginUser.getCompanySeq());
                if (supplement == 0) { //未补单
                	shoesSeqList.removeAll(replenishShoesSeqList); //去除已补单
                } else if (supplement == 1) {  // 已补单
                	shoesSeqList.retainAll(replenishShoesSeqList); //求交集
                }
            }

            /** 2.根据范围、类型、尺码条件，筛选出满足条件的所有鞋子序号  **/
            List<Integer> satisfyShoesSeqs = null;  //所有满足条件的鞋子序号
            if (rangeType == 0) {  // 全部（总部加门店）
                /*2019-05-27 同步伯俊ERP货品发现了没有任何库存和销售记录的货品，为了展示出来，此处判断库存的部分删除（门店货品不做改动）
    			// 总部加门店订过的（有过库存的）鞋子序号
    			List<Object> hqAndShopOrderedShoeSeqs = shoesDataService.getHqAndShopPickShoesSeqs(loginUser.getCompanySeq());
    			shoesSeqList.retainAll(hqAndShopOrderedShoeSeqs); //求交集
    			*/
                // 类型
                if (dataType == 0) { // 全部货品（即 全部货品）

                } else if (dataType == 1) { // 新品推荐（即 新品推荐）
                    //所有新品鞋子序号
                    List<Object> newestShoeSeqs = shoesInfoService.getHqNewestShoesSeqs(loginUser.getCompanySeq());
                    shoesSeqList.retainAll(newestShoeSeqs); //求交集
                }
                
                
                //查询满足尺码条件的所有鞋子序号
                if (sizeType == null) { //无
                    satisfyShoesSeqs = shoesSeqList;
                } else if (sizeType == 0) { //断码
                    //获取全部范围（总部加门店） 出现断码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> breakSizeShoesSeqs = shoesDataService.getBreakShoesSeqList(loginUser.getCompanySeq(),shoesSeqList, shopSeqList);
                    satisfyShoesSeqs = breakSizeShoesSeqs;
                } else if (sizeType == 1) { //缺码
                    //获取全部范围（总部加门店） 出现缺码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> missSizeShoesSeqs = shoesDataService.getAbsenceShoesSeqList(loginUser.getCompanySeq(),shoesSeqList, shopSeqList);
                    satisfyShoesSeqs = missSizeShoesSeqs;
                } else if (sizeType == 2) { //自定义尺码（一个范围内全部有库存的鞋子，其实是单选情况居多）
                    //获取全部范围（总部加门店） 出现在自定义尺码范围内全码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> fullSizeInSomeRangeShoesSeqs = shoesDataService.getHqAndShopFullSizeInSomeRangeShoesSeqs(shoesSeqList, sizeSeqStart, sizeSeqEnd, shopSeqList);
                    satisfyShoesSeqs = fullSizeInSomeRangeShoesSeqs;
                } else if (sizeType == 3) { //齐码
                    //获取全部范围（总部加门店） 出现齐码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> fullSizeShoesSeqs = shoesDataService.getHqAndShopFullSizeShoesSeqs(shoesSeqList, shopSeqList);
                    satisfyShoesSeqs = fullSizeShoesSeqs;
                }
                
                
            } else if (rangeType == 1) { // 本店
                // 门店用户点击时，该门店订过的（有过库存的）鞋子序号
                List<Object> shopOrderedShoeSeqs = shoesDataService.getShopPickShoeSeqs(loginUser.getCompanySeq(), Arrays.asList(loginUser.getShopSeq()));
                shoesSeqList.retainAll(shopOrderedShoeSeqs); //求交集

                
                // 类型
                if (dataType == 0) { // 全部货品（即 本店货品）

                } else if (dataType == 1) { // 新品推荐 （门店没有）
                    return R.error("参数有误");
                }

                List<Integer> shopSeqs = new ArrayList<>(10);
                shopSeqs.add(loginUser.getShopSeq());
                //查询满足尺码条件的所有鞋子序号
                if (sizeType == null) {
                    satisfyShoesSeqs = shoesSeqList;
                } else if (sizeType == 0) { //断码
                    //获取门店出现断码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> breakSizeShoesSeqs = shoesDataService.getBreakShoesSeqList(loginUser.getCompanySeq(), shoesSeqList,shopSeqs);
                    satisfyShoesSeqs = breakSizeShoesSeqs;
                } else if (sizeType == 1) { //缺码
                    //获取门店 出现缺码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> missSizeShoesSeqs = shoesDataService.getAbsenceShoesSeqList(loginUser.getCompanySeq(), shoesSeqList,shopSeqs);
                    satisfyShoesSeqs = missSizeShoesSeqs;
                } else if (sizeType == 2) { //自定义尺码（一个范围内全部有库存的鞋子，其实是单选情况居多）
                    //获取门店出现在自定义尺码范围内全码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> fullSizeInSomeRangeShoesSeqs = shoesDataService.getShopFullSizeInSomeRangeShoesSeqs(loginUser.getShopSeq(), shoesSeqList, sizeSeqStart, sizeSeqEnd);
                    satisfyShoesSeqs = fullSizeInSomeRangeShoesSeqs;
                } else if (sizeType == 3) { //齐码
                    //获取门店出现齐码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> fullSizeShoesSeqs = shoesDataService.getShopFullSizeShoesSeqs(loginUser.getShopSeq(), shoesSeqList);
                    satisfyShoesSeqs = fullSizeShoesSeqs;
                }

            } else if (rangeType == 2) { // 总仓
                //查询总仓货品（总仓库存的门店序号为0和-1）
                List<Object> hqOrderedShoeSeqs = shoesDataService.getShopPickShoeSeqs(loginUser.getCompanySeq(), Arrays.asList(-1, 0));
                shoesSeqList.retainAll(hqOrderedShoeSeqs); //求交集
                
                
                // 类型
                if (dataType == 0) { // 全部货品（即 总仓货品）

                } else if (dataType == 1) { // 新品推荐 （总仓没有）
                    return R.error("参数有误");
                }

                //查询满足尺码条件的所有鞋子序号
                if (sizeType == null) {
                    satisfyShoesSeqs = shoesSeqList;
                } else if (sizeType == 0) { //断码
                    //获取总仓 出现断码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> breakSizeShoesSeqs = shoesDataService.getBreakShoesSeqList(loginUser.getCompanySeq(), shoesSeqList, Arrays.asList(-1, 0));
                    satisfyShoesSeqs = breakSizeShoesSeqs;
                } else if (sizeType == 1) { //缺码
                    //获取总仓 出现缺码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> missSizeShoesSeqs = shoesDataService.getAbsenceShoesSeqList(loginUser.getCompanySeq(), shoesSeqList, Arrays.asList(-1, 0));
                    satisfyShoesSeqs = missSizeShoesSeqs;
                } else if (sizeType == 2) { //自定义尺码（一个范围内全部有库存的鞋子，其实是单选情况居多）
                    //获取总仓 出现在自定义尺码范围内全码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> fullSizeInSomeRangeShoesSeqs = shoesDataService.getShopFullSizeInSomeRangeShoesSeqs(loginUser.getShopSeq(), shoesSeqList, sizeSeqStart, sizeSeqEnd);
                    satisfyShoesSeqs = fullSizeInSomeRangeShoesSeqs;
                } else if (sizeType == 3) { //齐码
                    //获取总仓 出现齐码的鞋子序号（在一定鞋子序号范围内）
                    List<Integer> fullSizeShoesSeqs = shoesDataService.getShopFullSizeShoesSeqs(loginUser.getShopSeq(), shoesSeqList);
                    satisfyShoesSeqs = fullSizeShoesSeqs;
                }
                
            }
            
            
            if (satisfyShoesSeqs == null || satisfyShoesSeqs.size() == 0) {
                return R.ok("没有满足条件的商品");
            }


            /** 3.根据筛出来的鞋子序号和排序、分页条件，查询鞋子列表  **/

            List<Map<String, Object>> shoesList = null;

            Integer totalCount = 0;

            if (rangeType == 0) {  // 全部（总部加门店）
            	Page<Map<String, Object>> page = shoesInfoService.getHqAndShopShoesListOnSaleTime(requestFromType, satisfyShoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir, start, num, shopSeqList);
            	shoesList = page.getRecords();
            	
            	if(requestFromType==1) {
                	totalCount = page.getTotal();
                }
            } else if (rangeType == 1) {  //本店
	            // 门店用户点击时，查询该门店的鞋子List
            	Page<Map<String, Object>> page = shoesInfoService.getShopShoesListOnSaleTime(requestFromType, null, loginUser.getShopSeq(), satisfyShoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir, start, num);
            	shoesList = page.getRecords();
            	
            	if(requestFromType==1) {
	            	totalCount = page.getTotal();
	            }
            } else if (rangeType == 2) {  //总仓
	            // 查询总仓鞋子List
	            List<Object> shopSeqsList = Arrays.asList(-1, 0); // 总仓门店序号为0和-1
	            Page<Map<String, Object>> page = shoesInfoService.getShopShoesListOnSaleTime(requestFromType, shopSeqsList, loginUser.getShopSeq(), satisfyShoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir, start, num);
	            shoesList = page.getRecords();
	            
	            if(requestFromType==1) {
	            	totalCount = page.getTotal();
	            }
            }
            

            
            //所有鞋子总库存
            Integer totalNum = shoesDataService.totalNum(satisfyShoesSeqs, shopSeqList);
            //所有鞋子总仓库存
            Integer totalStockNum = shoesDataService.totalStockNum(satisfyShoesSeqs);
            //所有鞋子门店总库存
            Integer totalShopNum = shoesDataService.totalShopNum(satisfyShoesSeqs, shopSeqList);
            
            
            /**处理图片、添加最近7日销售额，最近七日销售最好的门店**/
            for (Map<String, Object> map : shoesList) {
                if (loginUser.getShopSeq() != null) {  //门店用户点击时
                    //是否本店货品 （门店用户范围选择全部时，页面上标记此货品是否本门店货品）
                    Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
                    shoesDataEntityWrapper.setSqlSelect("TOP 1 *");
                    shoesDataEntityWrapper.eq("Shoes_Seq", map.get("seq"));
                    shoesDataEntityWrapper.eq("Shop_Seq", loginUser.getShopSeq());
                    List<ShoesDataEntity> shoesDataEntityList = shoesDataService.selectList(shoesDataEntityWrapper);
                    if (shoesDataEntityList.size() > 0) {
                        map.put("shopGood", 1);
                    } else {
                        map.put("shopGood", 0);
                    }
                    //是否本店主推（门店用户选择全部货品，页面上标记此货品是否本门店主推）
                    Wrapper<ShopMainpushEntity> shopMainpushEntityWrapper = new EntityWrapper<>();
                    shopMainpushEntityWrapper.setSqlSelect("TOP 1 *");
                    shopMainpushEntityWrapper.eq("Shoes_Seq", map.get("seq"));
                    shopMainpushEntityWrapper.eq("Shop_Seq", loginUser.getShopSeq());
                    List<ShopMainpushEntity> shopMainpushEntities = shopMainpushService.selectList(shopMainpushEntityWrapper);
                    if (shopMainpushEntities.size() > 0) {
                        map.put("shopPush", 1);
                    } else {
                        map.put("shopPush", 0);
                    }
                } else {  //总部点击时
                    //是否本店货品（无意义，给了个0）
                    map.put("shopGood", 0);
                    //是否有（任何一个）门店主推过
                    Wrapper<ShopMainpushEntity> shopMainpushEntityWrapper = new EntityWrapper<>();
                    shopMainpushEntityWrapper.setSqlSelect("TOP 1 *");
                    shopMainpushEntityWrapper.eq("Shoes_Seq", map.get("seq"));
                    List<ShopMainpushEntity> shopMainpushEntities = shopMainpushService.selectList(shopMainpushEntityWrapper);
                    if (shopMainpushEntities.size() > 0) {
                        map.put("shopPush", 1);
                    } else {
                        map.put("shopPush", 0);
                    }
                }
                //图片
                map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
                //售罄率
                //如果售罄率存在
                if (map.get("stock") == null) {
                    map.put("stock", 0);
                }

                if (map.get("selloutRate") != null) {
                    map.put("selloutRate", map.get("selloutRate") + "%");
                    //如果有销量
//                } else if (map.get("saleCount") != null) {
//                    BigDecimal saleCount = new BigDecimal(map.get("saleCount").toString());
//                    //查询进货记录
//                    Wrapper<ShoesDataDailyDetailEntity> inWrapper = new EntityWrapper<>();
//                    inWrapper.where("Shoes_Seq = {0} AND (Type = 0 OR Type = 1)", map.get("seq")).setSqlSelect("Shoes_Seq, SUM (Count) AS num").groupBy("Shoes_Seq");
//                    Integer inNum = 0;
//                    if (shoesDataDailyDetailService.selectList(inWrapper).size() > 0) {
//                        inNum = shoesDataDailyDetailService.selectList(inWrapper).get(0).getNum();
//                    }
//                    //查询退货记录
//                    Wrapper<ShoesDataDailyDetailEntity> outWrapper = new EntityWrapper<>();
//                    outWrapper.where("Shoes_Seq = {0} AND (Type = 2 OR Type = 4)", map.get("seq")).setSqlSelect("Shoes_Seq, SUM (Count) AS num").groupBy("Shoes_Seq");
//                    Integer outNum = 0;
//                    if (shoesDataDailyDetailService.selectList(outWrapper).size() > 0) {
//                        outNum = shoesDataDailyDetailService.selectList(outWrapper).get(0).getNum();
//                    }
//                    //计算总进货量
//                    BigDecimal totalInNum = new BigDecimal(inNum).subtract(new BigDecimal(outNum));
//                    if (totalInNum.compareTo(BigDecimal.ZERO) == 0) {
//                        map.put("selloutRate", "0%");
//                    } else {
//                        BigDecimal selloutRate = saleCount.divide(totalInNum, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
//                        map.put("selloutRate", selloutRate.toString() + "%");
//                    }
                } else {
                    map.put("selloutRate", "0%");
                }

                BigDecimal salesNum = new BigDecimal(0);
                if (map.get("salesNum") != null) {
                    salesNum = new BigDecimal(map.get("salesNum").toString());
                } else {
                    map.put("salesNum", 0);
                }
                //查询销售时间
                Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
                saleShoesDetailEntityWrapper.eq("ShoeSeq", map.get("seq")).orderBy("SaleDate", true);
                List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(saleShoesDetailEntityWrapper);
                //如果有销售记录
                if (saleShoesDetailEntities.size() > 0) {
                    Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
                    Integer days = 0;
                    //如果销售时间比开始时间早
                    if (saleTimeStart.getTime() - saleDate.getTime() > 0L) {
                        days = (int) ((saleTimeEnd.getTime() - saleTimeStart.getTime()) / (1000 * 60 * 60 * 24)) + 1;
                    } else {
                        days = (int) ((saleTimeEnd.getTime() - saleDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
                    }
                    if (salesNum.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal salesDayAvg = salesNum.divide(new BigDecimal(days), 2, BigDecimal.ROUND_HALF_UP);
                        map.put("salesDayAvg", salesDayAvg);
                    } else {
                        map.put("salesDayAvg", 0);
                    }
                    //如果没有销售记录
                } else {
                    map.put("salesDayAvg", 0);
                }

                //实际成交价
                List<Object> shopSeqs = new ArrayList<>(10);
                Integer shoesSeq = (Integer) map.get("seq");
                //获取门店序号
                if (rangeType == 0) {
                    Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
                    baseShopEntityWrapper.eq("Company_Seq", loginUser.getCompanySeq()).setSqlSelect("Seq");
                    shopSeqs = shopService.selectObjs(baseShopEntityWrapper);
                } else if (rangeType == 1) {
                    shopSeqs.add(loginUser.getShopSeq());
                } else if (rangeType == 2) {
                    Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
                    baseShopEntityWrapper.eq("Company_Seq", loginUser.getCompanySeq()).setSqlSelect("Seq");
                    shopSeqs = shopService.selectObjs(baseShopEntityWrapper);
                }
                map.put("dealPrice", saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq, saleTimeStart, saleTimeEnd).setScale(2, BigDecimal.ROUND_HALF_UP));


                //获取7天前的时间
                Date sevenDayBefore = DateUtils.addDateDays(new Date(), 7);
                // 7天总销量(需区分是全部总销量还是门店总销量)
                Integer sevenDaySaleNum = 0;
                if (rangeType == 0) {
                    sevenDaySaleNum = saleShoesDetailService.getAllShopsSaleNum(sevenDayBefore, (Integer) map.get("seq"));
                } else if (rangeType == 1) {
                    sevenDaySaleNum = saleShoesDetailService.getShopSaleNum(loginUser.getShopSeq(), sevenDayBefore, (Integer) map.get("seq"));
                } else if (rangeType == 2) {
                	sevenDaySaleNum = saleShoesDetailService.getAllShopsSaleNum(sevenDayBefore, (Integer) map.get("seq"));
                }
                map.put("sevenDaySaleNum", sevenDaySaleNum);


                // 7天销售最好的店铺名称和销量
                Map<String, Object> sevenDaySaleMostShop = saleShoesDetailService.getMostSaleShopSeq(sevenDayBefore, (Integer) map.get("seq"));
                if (sevenDaySaleMostShop != null) {
                    BaseShopEntity baseShopEntity = shopService.selectById((Integer) sevenDaySaleMostShop.get("shopSeq"));
                    map.put("sevenDayShopName", baseShopEntity.getName());
                    map.put("sevenDayShopSaleNum", sevenDaySaleMostShop.get("saleNum"));
                }
                
                
                //此处三个所有鞋子总计总库存放置位置有问题，不需要放在每个鞋子数据中 TODO
                //总库存
                map.put("totalNum", totalNum);
                //总仓库存
                map.put("totalStockNum", totalStockNum);
                //门店总库存
                map.put("totalShopNum", totalShopNum);

                //如果是门店用户，展示当前货品是否已添加虚拟商城的标识
                map.put("isTransferOut", 0);
                if(loginUser.getShopSeq() != null) {
	                if(allocateTransferFictitiousMallService.isTransferOutToFictitiousMall(loginUser.getShopSeq(), (Integer)map.get("seq"))) {
	                	map.put("isTransferOut", 1);
	                }
                }
                
            }

            return R.ok(shoesList).put("totalCount", totalCount);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    @Login
    @ApiOperation("获取尺码列表")
    @GetMapping("/selectGoodsSize")
    public R selectGoodsSize(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        try {
            return R.ok().result(goodsSizeService.selectGoodsSize(loginUser.getCompanySeq()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取鞋子在各门店的库存")
    @GetMapping("/selectShopStockByShoesSeq")
    @ApiImplicitParams({@ApiImplicitParam(name = "goodId", value = "鞋子货号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "sizeSeq", value = "尺码序号", required = true, paramType = "query")})
    public R selectShopStockByShoesSeq(@RequestParam String goodId, Integer sizeSeq) {
        try {
            return R.ok().result(shoesDataService.selectShopStockByShoesSeq(goodId, sizeSeq));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取主推门店")
    @GetMapping("/selectMainPushShop")
    @ApiImplicitParams({@ApiImplicitParam(name = "shoesSeq", value = "鞋子序号", required = true, paramType = "query")})
    public R selectMainPushShop(@RequestParam Integer shoesSeq) {
        try {
            Map<String, Object> map = new HashMap<>(10);
            map.put("result", shopMainpushService.selectMainPushShop(shoesSeq));
            return R.ok().result(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取主推鞋子")
    @GetMapping("/selectMainPushShoes")
    public R selectMainPushShoes(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                 @ApiParam("门店序号列表") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
                                 @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
                                 @ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:日平均销量)") @RequestParam(value = "orderBy") Integer orderBy,
                                 @ApiParam("排序方式(0:降序 1:升序)") @RequestParam(value = "orderDir") Integer orderDir,
                                 @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                                 @ApiParam("总条数") @RequestParam(value = "num") Integer num,
                                 @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
                                 @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            return R.ok(shoesInfoService.selectMainPushShoes(userEntity, shopSeqList, categorySeqList, orderBy, orderDir, saleTimeStart, saleTimeEnd, page)).put("page", page);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取畅销鞋子")
    @GetMapping("/selectSaleableShoes")
    public R selectSaleableShoes(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                 @ApiParam("门店序号列表") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
                                 @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                                 @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                                 @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
                                 @ApiParam("TOP类型: 1:50/10 2:80/20 3:100/30") @RequestParam(value = "ranking") Integer ranking,
                                 @ApiParam("排序字段(1:销量 2:库存 3:在仓天数") @RequestParam(value = "orderBy") Integer orderBy,
                                 @ApiParam("排序方式(0:降序 1:升序)") @RequestParam(value = "orderDir") Integer orderDir,
                                 @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                                 @ApiParam("总条数") @RequestParam(value = "num") Integer num,
                                 @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
                                 @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            if (start > 1) {
                return R.ok(new ArrayList<>(10));
            } else {
                return R.ok(shoesInfoService.selectSalableShoes(userEntity, shopSeqList, yearList, seasonSeqList, categorySeqList, ranking, orderBy, orderDir, saleTimeStart, saleTimeEnd, page));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取滞销鞋子")
    @GetMapping("/selectUnsaleableShoes")
    public R selectUnsaleableShoes(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                 @ApiParam("门店序号列表") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
                                 @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                                 @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                                 @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
                                 @ApiParam("TOP类型: 1:50/10 2:80/20 3:100/30") @RequestParam(value = "ranking") Integer ranking,
                                 @ApiParam("排序字段(1:销量 2:库存 3:在仓天数") @RequestParam(value = "orderBy") Integer orderBy,
                                 @ApiParam("排序方式(0:降序 1:升序)") @RequestParam(value = "orderDir") Integer orderDir,
                                 @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                                 @ApiParam("总条数") @RequestParam(value = "num") Integer num,
                                 @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
                                 @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            if (start > 1) {
                return R.ok(new ArrayList<>(10));
            } else {
                return R.ok(shoesInfoService.selectUnsalableShoes(userEntity, shopSeqList, yearList, seasonSeqList, categorySeqList, ranking, orderBy, orderDir, saleTimeStart, saleTimeEnd, page));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }

    @Login
    @ApiOperation("库存结构按年份")
    @GetMapping("/selectYearStockStructure")
    public R selectYearStockStructure(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                      @ApiParam("门店序号列表") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
                                      @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList) {
        try {
            return R.ok(shoesInfoService.selectYearStockStructure(userEntity, shopSeq, yearList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("库存结构按季节")
    @GetMapping("/selectSeasonStockStructure")
    public R selectSeasonStockStructure(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                        @ApiParam("门店序号列表") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
                                        @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                                        @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList) {
        try {
            return R.ok(shoesInfoService.selectSeasonStockStructure(userEntity, shopSeq, yearList, seasonSeqList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("库存结构按类别")
    @GetMapping("/selectCategoryStockStructure")
    public R selectCategoryStockStructure(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                          @ApiParam("门店序号列表") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
                                          @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                                          @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                                          @ApiParam("类别") @RequestParam(value = "type") Integer type,
                                          @ApiParam("品类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList) {
        try {
            return R.ok(shoesInfoService.selectCategoryStockStructure(userEntity, shopSeq, yearList, seasonSeqList, type, categorySeqList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("鞋子库存列表")
    @GetMapping("/selectShoesStock")
    public R selectShoesStock(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                              @ApiParam("门店序号列表") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
                              @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                              @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                              @ApiParam("类别") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
                              @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                              @ApiParam("总条数") @RequestParam(value = "num") Integer num) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            return R.ok(shoesInfoService.selectShoesStock(userEntity, shopSeq, yearList, seasonSeqList, categorySeqList, page)).put("page", page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取年份列表")
    @GetMapping("/selectYearList")
    public R selectYearList(@ApiIgnore @LoginUser BaseUserEntity userEntity) {
        try {
            Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Company_Seq", userEntity.getCompanySeq()).setSqlSelect("[Year]").groupBy("[Year]").orderBy("[Year]", false);
            List<Integer> yearList = new ArrayList<>(10);
            List<GoodsShoesEntity> goodsShoesEntities = goodsShoesService.selectList(wrapper);
            for (GoodsShoesEntity goodsShoesEntity : goodsShoesEntities) {
                yearList.add(goodsShoesEntity.getYear());
            }
            return R.ok(yearList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取老款鞋子")
    @GetMapping("/selectOldShoes")
    public R selectOldShoes(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                            @ApiParam("门店序号列表") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
                            @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                            @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
                            @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                            @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                            @ApiParam("总条数") @RequestParam(value = "num") Integer num,
                            @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
                            @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            return R.ok(shoesInfoService.selectOldShoes(userEntity, shopSeqList, yearList, categorySeqList, seasonSeqList, saleTimeStart, saleTimeEnd, page)).put("page", page);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }


    /**
		筛选条件：
		
		年份、季节、品类、销售起止时间、门店序号List
		
		展示数据：
		
		门店名1：销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
		门店名2：销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
		门店名3：销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
		门店名4：销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
     */
    @Login
    @ApiOperation("存销比(品类维度)")
    @GetMapping("/stockSaleRatioByCategory")
    public R stockSaleRatioByCategory(@ApiIgnore @LoginUser BaseUserEntity loginUser,
         @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
         @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
         @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
         @ApiParam("门店序号") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
         @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
         @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd) {
        try {
            if (shopSeqList == null || shopSeqList.size() == 0) {
                shopSeqList = shopService.getAllShopSeqs(loginUser.getBrandSeq());
            }

            //获取 年份、季节、品类 所有鞋子Seq
            List<Integer> shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, null, null, null, null, null, null, null);
            Set<Integer> shoesSeqs = new TreeSet<>();
            shoesSeqs.addAll(shoesSeqList);
            if (shoesSeqList != null && shoesSeqList.size() > 0) {

                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

                for (Integer shopSeq : shopSeqList) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    BaseShopEntity baseShopEntity = shopService.selectById(shopSeq);
                    map.put("shopSeq", shopSeq);
                    map.put("shopName", baseShopEntity.getName());

                    Map<String, Object> saleNumAndSKUsMap = saleShoesDetailService.getShopSaleNumAndSKUsMap(loginUser.getCompanySeq(), Arrays.asList(shopSeq), saleTimeStart, saleTimeEnd, shoesSeqList);
                    map.putAll(saleNumAndSKUsMap);


                    // 门店 指定货品 总库存
                    Integer stock = shoesDataService.totalShopNum(shoesSeqList, Arrays.asList(shopSeq));
                    map.put("stock", stock);
                    //计算 存销比率（当前库存/销售数量）
                    Integer saleNum = (Integer) saleNumAndSKUsMap.get("saleNum");
                    BigDecimal stockSaleRatio = BigDecimal.ZERO;
                    if (stock != null && saleNum != null && saleNum != 0) {
                        stockSaleRatio = BigDecimal.valueOf(stock).divide(BigDecimal.valueOf(saleNum), 2, RoundingMode.HALF_UP);
                    }
                    map.put("stockSaleRatio", stockSaleRatio);

                    resultList.add(map);
                }

                return R.ok(resultList);

            } else {
                return R.error("没有符合条件的货品");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return R.error("服务器异常");
        }
    }


	/**
	 * 
	门店维度：
		
		筛选条件：
		
		年份、季节、销售起止时间、门店序号List
		
		展示数据：	
		
		二级品类1： 销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
		二级品类2： 销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
		二级品类3： 销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
		二级品类4： 销售数量、当前库存、存销比率（当前库存/销售数量）、SKU数
	 */
    @Login
    @ApiOperation("存销比(门店维度)")
    @GetMapping("/stockSaleRatioByShop")
    public R stockSaleRatioByShop(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                  @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                                  @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                                  @ApiParam("门店序号") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
                                  @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
                                  @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd) {
        try {

            //查询所有二级分类
            List<GoodsCategoryEntity> secondCategoryList = goodsCategoryService.getSecondCategoryList(loginUser.getCompanySeq());

            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            for (GoodsCategoryEntity goodsCategoryEntity : secondCategoryList) {
                Map<String, Object> map = new HashMap<String, Object>();

                Integer categorySeq = goodsCategoryEntity.getSeq();
                map.put("categorySeq", categorySeq);
                map.put("categoryName", goodsCategoryEntity.getName());

                //获取 年份、季节、品类 所有鞋子Seq
                List<Integer> shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, Arrays.asList(categorySeq), null, null, null, null, null, null, null);

                Set<Integer> shoesSeqs = new TreeSet<>();
                shoesSeqs.addAll(shoesSeqList);
                if (shoesSeqList != null && shoesSeqList.size() > 0) {
                    // 多个门店 起止时间内 指定货品  总销售数量+SKU数
                    Map<String, Object> saleNumAndSKUsMap = saleShoesDetailService.getShopSaleNumAndSKUsMap(loginUser.getCompanySeq(), shopSeqList, saleTimeStart, saleTimeEnd, shoesSeqList);
                    map.putAll(saleNumAndSKUsMap);

                    // 多个门店 指定货品 总库存
                    Integer stock = shoesDataService.totalShopNum(shoesSeqList, shopSeqList);
                    map.put("stock", stock);

                    //计算 存销比率（当前库存/销售数量）
                    Integer saleNum = (Integer) saleNumAndSKUsMap.get("saleNum");
                    BigDecimal stockSaleRatio = BigDecimal.ZERO;
                    if (stock != null && saleNum != null && saleNum != 0) {
                        stockSaleRatio = BigDecimal.valueOf(stock).divide(BigDecimal.valueOf(saleNum), 2, RoundingMode.HALF_UP);
                    }
                    map.put("stockSaleRatio", stockSaleRatio);
                } else {
                    map.put("saleNum", "");
                    map.put("SKUs", "");
                    map.put("stock", "");
                    map.put("stockSaleRatio", "");
                }
                resultList.add(map);
            }
            return R.ok(resultList);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return R.error("服务器异常");
        }
    }


    @Login
    @ApiOperation("获取断缺码鞋子")
    @GetMapping("/selectBreakOrAbsenceShoes")
    public R selectBreakOrAbsenceShoes(@ApiIgnore @LoginUser BaseUserEntity userEntity,
                                       @ApiParam("门店序号列表") @RequestParam(value = "shopSeqList", required = false) List<Integer> shopSeqList,
                                       @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
                                       @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
                                       @ApiParam("类别(0:断码,1:缺码)") @RequestParam(value = "type") Integer type,
                                       @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                                       @ApiParam("总条数") @RequestParam(value = "num") Integer num) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            return R.ok(shoesInfoService.selectBreakOrAbsenceShoes(userEntity, shopSeqList, yearList, seasonSeqList, type, page)).put("page", page);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return R.error();
    }


    @Login
    @ApiOperation("获取调出鞋子")
    @GetMapping("/selectExportOutShoes")
    public R selectExportOutShoes(@ApiIgnore @LoginUser BaseUserEntity userEntity,
	    @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
	    @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
	    @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
	    @ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:日平均销量)") @RequestParam(value = "orderBy") Integer orderBy,
	    @ApiParam("排序方式(0:降序 1:升序)") @RequestParam(value = "orderDir") Integer orderDir,
	    @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
	    @ApiParam("总条数") @RequestParam(value = "num") Integer num,
	    @ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart") Date saleTimeStart,
	    @ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd") Date saleTimeEnd,
	    @ApiParam("是否进行调配") @RequestParam(value = "isTransferOut", required = false) Integer isTransferOut) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num, num);
            return R.ok(shoesInfoService.selectExportOutShoes(userEntity, yearList, categorySeqList, seasonSeqList, orderBy, orderDir, saleTimeStart, saleTimeEnd, isTransferOut, page)).put("page", page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取最近两个季节")
    @GetMapping("/selectLastSeason")
    public R selectLastSeason(@ApiIgnore @LoginUser BaseUserEntity userEntity) {
        try {
            return R.ok(shoesInfoService.selectLastSeason());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }
}
