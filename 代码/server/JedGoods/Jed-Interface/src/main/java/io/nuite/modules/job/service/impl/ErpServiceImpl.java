package io.nuite.modules.job.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.job.service.ErpService;
import io.nuite.modules.job.utils.LargeCategoryEnums;
import io.nuite.modules.job.utils.LastCategoryEnums;
import io.nuite.modules.job.utils.MiddleCategoryEnums;
import io.nuite.modules.job.utils.SeasonEnums;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.dao.ShoesInfoDao;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import io.nuite.modules.sr_base.dao.*;
import io.nuite.modules.sr_base.entity.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: ERPService实现类
 * @author: jxj
 * @create: 2019-04-04 09:06
 */
@Deprecated   /** 老ERP同步，已废弃 **/
@Service
public class ErpServiceImpl implements ErpService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShoesDataDailyDetailDao shoesDataDailyDetailDao;
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    @Autowired
    private BaseShopDao baseShopDao;
    @Autowired
    private GoodsColorDao goodsColorDao;
    @Autowired
    private GoodsSizeDao goodsSizeDao;
    @Autowired
    private GoodsSeasonDao goodsSeasonDao;
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;
    @Autowired
    private ShoesDataDao shoesDataDao;
    @Autowired
    private ShoesInfoDao shoesInfoDao;

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
            .readTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
            .writeTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
            .build();

    @Value("${jed.url}")
    private String jedUrl;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Value("${jrd.brandSeq}")
    private Integer brandSeq;

    @Value("${jrd.areaSeq}")
    private Integer areaSeq;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getErpData(String startDate,String endDate,String lastupdate) throws Exception {
        //拉取销售数据
        String detailUrl = "";
        if (lastupdate != null) {
            lastupdate = lastupdate.replace("-", "/");
            detailUrl = jedUrl + "/detail?lastupdate=" + lastupdate;
            /*startDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date(System.currentTimeMillis()-1000*60*60*24));
            endDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());*/
        } else {
            detailUrl = jedUrl + "/detail?startDate=" + startDate + "&endDate=" + endDate;
        }
        Request detailRequest = new Request.Builder().url(detailUrl).build();
        Response detailResponse = client.newCall(detailRequest).execute();
        String detailResult = detailResponse.body().string();
        JSONObject detailJsonObject = JSONObject.parseObject(detailResult);
        //解析数据为maplist
        List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailJsonObject.get("result");
        //来往记录列表
        List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntities = new ArrayList<>(100);
        List<SaleShoesDetailEntity> saleShoesDetailEntities = new ArrayList<>(100);

        //循环取出需要的值
        if (detailList != null) {
            for (int i = 0; i < detailList.size(); i++) {

                ShoesDataDailyDetailEntity shoesDataDailyDetailEntity = new ShoesDataDailyDetailEntity();
                //取出订单号
                String evidenceNumber = (String) detailList.get(i).get("Evidence_Number");
                shoesDataDailyDetailEntity.setErpOrderNum(evidenceNumber);
                //取出收货方
                String contactCode = (String) detailList.get(i).get("ContactCode");
                String contactName = (String) detailList.get(i).get("contactName_L");
                //取除出货方
                String posterCode = (String) detailList.get(i).get("posterCode");
                String posterName = (String) detailList.get(i).get("posterName");
                //取出货物id
                String inventoryId = (String) detailList.get(i).get("Inventory_id");
                String first = inventoryId.split("-")[0];
                String sexCode = first.substring(first.length() - 2, first.length() - 1);
                String billName = (String) detailList.get(i).get("BillName");
                if ("收货单".equals(billName)) {
                    if ("cnt".equals(contactCode)) {
                        shoesDataDailyDetailEntity = parseShoes(inventoryId, shoesDataDailyDetailEntity);
                    } else if (evidenceNumber.startsWith("H") || evidenceNumber.startsWith("h") || evidenceNumber.startsWith("T") || evidenceNumber.startsWith("t")) {
                        shoesDataDailyDetailEntity = parseShoes(inventoryId, shoesDataDailyDetailEntity);
                    }
                } else if ("零售出仓单".equals(billName)) {
                    shoesDataDailyDetailEntity = parseShoes(inventoryId, shoesDataDailyDetailEntity);
                }
                if ("门口特卖".equals(posterName)) {
                    shoesDataDailyDetailEntity = parseShoes(inventoryId, shoesDataDailyDetailEntity);
                }
                if (shoesDataDailyDetailEntity == null) {
                    continue;
                }
                //尺码列表
                List<Integer> sizeList = new ArrayList<>(10);
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size1").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size2").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size3").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size4").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size5").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size6").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size7").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size8").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size9").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size10").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size11").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size12").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size13").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size14").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size15").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size16").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size17").toString()).split("\\.")[0]));
                sizeList.add(Integer.parseInt((detailList.get(i).get("Size18").toString()).split("\\.")[0]));
                //取出单价
                BigDecimal price = new BigDecimal(detailList.get(i).get("price").toString());
                //获取日期
                String date = (String) detailList.get(i).get("date");
                String lastDate = (String) detailList.get(i).get("lastupdate");
                //替换日期分隔符
                String saleDate = date.replace("/", "-");
                String lastUpdate = lastDate.replace("/", "-");
                //日期格式化
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int j = 0; j < sizeList.size(); j++) {
                    BaseShopEntity baseShopEntity = new BaseShopEntity();
                    if (sizeList.get(j) != 0) {
                        if ("B".equals(sexCode)) {
                            shoesDataDailyDetailEntity = checkSize(j + 37 + "", shoesDataDailyDetailEntity);
                        } else {
                            shoesDataDailyDetailEntity = checkSize(j + 33 + "", shoesDataDailyDetailEntity);
                        }
                        shoesDataDailyDetailEntity.setPrice(price);
                        //日期解析给更新日期赋值
                        shoesDataDailyDetailEntity.setUpdateTime(sdf.parse(saleDate));
                        shoesDataDailyDetailEntity.setInputERPTime(sdf.parse(lastUpdate));
                        shoesDataDailyDetailEntity.setCount(Math.abs(sizeList.get(j)));
                        if ("收货单".equals(billName)) {
                            if ("cnt".equals(contactCode)) {
                                shoesDataDailyDetailEntity.setShopSeq(0);
                                shoesDataDailyDetailEntity.setShopName(posterName);
                                shoesDataDailyDetailEntity.setType(0);
                                shoesDataDailyDetailEntities.add(shoesDataDailyDetailEntity);
                            } else if (evidenceNumber.startsWith("H") || evidenceNumber.startsWith("h") || evidenceNumber.startsWith("T") || evidenceNumber.startsWith("t")) {
                                baseShopEntity.setId(posterCode);
                                baseShopEntity.setName(posterName);
                                shoesDataDailyDetailEntity = checkShop(baseShopEntity, shoesDataDailyDetailEntity);
                                if (sizeList.get(j) > 0) {
                                    shoesDataDailyDetailEntity.setType(1);
                                } else {
                                    shoesDataDailyDetailEntity.setType(2);
                                }
                                shoesDataDailyDetailEntities.add(shoesDataDailyDetailEntity);
                            }
                        } else if ("零售出仓单".equals(billName)) {
                            SaleShoesDetailEntity saleShoesDetailEntity = new SaleShoesDetailEntity();
                            baseShopEntity.setId(posterCode);
                            baseShopEntity.setName(posterName);
                            shoesDataDailyDetailEntity = checkShop(baseShopEntity, shoesDataDailyDetailEntity);
                            shoesDataDailyDetailEntity.setType(3);
                            saleShoesDetailEntity.setAreaSeq(areaSeq);
                            saleShoesDetailEntity.setErpOrderNum(shoesDataDailyDetailEntity.getErpOrderNum());
                            saleShoesDetailEntity.setBranchOfficeSeq(brandSeq);
                            saleShoesDetailEntity.setShoeID(shoesDataDailyDetailEntity.getShoesId());
                            saleShoesDetailEntity.setShoeSeq(shoesDataDailyDetailEntity.getShoesSeq().intValue());
                            saleShoesDetailEntity.setShopSeq(shoesDataDailyDetailEntity.getShopSeq().intValue());
                            saleShoesDetailEntity.setSaleDate(sdf.parse(saleDate));
                            saleShoesDetailEntity.setSaleType(3);
                            saleShoesDetailEntity.setRealPrice(price);
                            saleShoesDetailEntity.setInputTime(new Date());
                            saleShoesDetailEntity.setSaleCount(sizeList.get(j));
                            saleShoesDetailEntities.add(saleShoesDetailEntity);
                            shoesDataDailyDetailEntities.add(shoesDataDailyDetailEntity);
                        }
                        if ("门口特卖".equals(posterName)) {
                            shoesDataDailyDetailEntity.setShopSeq(0);
                            shoesDataDailyDetailEntity.setShopName(posterName);
                            shoesDataDailyDetailEntity.setType(3);
                            shoesDataDailyDetailEntities.add(shoesDataDailyDetailEntity);
                        }
                    }
                }
            }
        }
        if(shoesDataDailyDetailEntities.size() > 0) {
            List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityList = new ArrayList<>(100);
            for(int i = 0;i < shoesDataDailyDetailEntities.size();i++) {
                shoesDataDailyDetailEntityList.add(shoesDataDailyDetailEntities.get(i));
                if(shoesDataDailyDetailEntityList.size() == 100) {
                    shoesDataDailyDetailDao.insertShoesDataDailyDetail(shoesDataDailyDetailEntityList);
                    shoesDataDailyDetailEntityList.clear();
                }else if(i == shoesDataDailyDetailEntities.size() - 1){
                    shoesDataDailyDetailDao.insertShoesDataDailyDetail(shoesDataDailyDetailEntityList);
                }
            }
        }
        if(saleShoesDetailEntities.size() > 0) {
            List<SaleShoesDetailEntity> saleShoesDetailEntityList = new ArrayList<>(100);
            for(int i = 0;i < saleShoesDetailEntities.size();i++) {
                saleShoesDetailEntityList.add(saleShoesDetailEntities.get(i));
                if(saleShoesDetailEntityList.size() == 100) {
                    saleShoesDetailDao.insertSaleShoesDetail(saleShoesDetailEntityList);
                    saleShoesDetailEntityList.clear();
                }else if(i == saleShoesDetailEntities.size() - 1) {
                    saleShoesDetailDao.insertSaleShoesDetail(saleShoesDetailEntityList);
                }
            }
        }

        List<ShoesInfoEntity> shoesInfoEntities = new ArrayList<>(100);
        Wrapper<GoodsShoesEntity> goodsShoesEntityWrapper = new EntityWrapper<>();
        goodsShoesEntityWrapper.eq("Company_Seq",companySeq);
        List<GoodsShoesEntity> goodsShoesEntities = goodsShoesDao.selectList(goodsShoesEntityWrapper);
        for(int i = 0;i < goodsShoesEntities.size();i++) {
            ShoesInfoEntity shoesInfoEntity = new ShoesInfoEntity();
            Wrapper<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityWrapper = new EntityWrapper<>();
            shoesDataDailyDetailEntityWrapper.eq("ShoesId",goodsShoesEntities.get(i).getGoodID()).orderBy("Price",false).setSqlSelect("Seq","Price");
            List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityList = shoesDataDailyDetailDao.selectList(shoesDataDailyDetailEntityWrapper);
            if(shoesDataDailyDetailEntityList.size() > 0) {
                if(shoesDataDailyDetailEntityList.size() == 1) {
                    shoesInfoEntity.setDealPrice1(shoesDataDailyDetailEntityList.get(0).getPrice());
                }else if(shoesDataDailyDetailEntityList.size() == 2) {
                    shoesInfoEntity.setDealPrice1(shoesDataDailyDetailEntityList.get(0).getPrice());
                    shoesInfoEntity.setDealPrice2(shoesDataDailyDetailEntityList.get(1).getPrice());
                }else {
                    shoesInfoEntity.setDealPrice1(shoesDataDailyDetailEntityList.get(0).getPrice());
                    shoesInfoEntity.setDealPrice2(shoesDataDailyDetailEntityList.get(1).getPrice());
                    shoesInfoEntity.setDealPrice3(shoesDataDailyDetailEntityList.get(2).getPrice());
                }
            }else {
                shoesInfoEntity.setDealPrice1(new BigDecimal(0));
            }
            shoesInfoEntity.setShoesSeq(goodsShoesEntities.get(i).getSeq());
            shoesInfoEntity.setOnSale(1);
            shoesInfoEntities.add(shoesInfoEntity);
        }
        if(shoesInfoEntities.size() > 0) {
            List<ShoesInfoEntity> insertShoesInfoEntities = new ArrayList<>(100);
            List<ShoesInfoEntity> updateShoesInfoEntities = new ArrayList<>(100);
            for(int i = 0;i < shoesInfoEntities.size();i++) {
                Wrapper<ShoesInfoEntity> shoesInfoEntityWrapper = new EntityWrapper<>();
                shoesInfoEntityWrapper.eq("Shoes_Seq",shoesInfoEntities.get(i).getShoesSeq());
                List<ShoesInfoEntity> shoesInfoEntityList = shoesInfoDao.selectList(shoesInfoEntityWrapper);
                if(shoesInfoEntityList.size() > 0) {
                    shoesInfoEntities.get(i).setSeq(shoesInfoEntityList.get(0).getSeq());
                    updateShoesInfoEntities.add(shoesInfoEntities.get(i));
                }else {
                    insertShoesInfoEntities.add(shoesInfoEntities.get(i));
                }
            }
            List<ShoesInfoEntity> updateShoesInfoEntityList = new ArrayList<>(100);
            for(int i = 0;i < updateShoesInfoEntities.size();i++) {
                updateShoesInfoEntityList.add(updateShoesInfoEntities.get(i));
                if(updateShoesInfoEntityList.size() == 100) {
                    shoesInfoDao.updateShoesInfo(updateShoesInfoEntityList);
                    updateShoesInfoEntityList.clear();
                }else if(i == updateShoesInfoEntities.size() - 1) {
                    shoesInfoDao.updateShoesInfo(updateShoesInfoEntityList);
                }
            }
            List<ShoesInfoEntity> insertShoesInfoEntityList = new ArrayList<>(100);
            for(int i = 0;i < insertShoesInfoEntities.size();i++) {
                insertShoesInfoEntityList.add(insertShoesInfoEntities.get(i));
                if(insertShoesInfoEntityList.size() == 100) {
                    shoesInfoDao.insertShoesInfo(insertShoesInfoEntityList);
                    insertShoesInfoEntityList.clear();
                }else if(i == insertShoesInfoEntities.size() - 1) {
                    shoesInfoDao.insertShoesInfo(insertShoesInfoEntityList);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getShopStock() throws Exception {
        //获取所有门店列表
        Wrapper<BaseShopEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Company_Seq",companySeq);
        List<BaseShopEntity> shopList = baseShopDao.selectList(wrapper);
        //在所有门店列表中加入总仓
        BaseShopEntity baseShopEntity = new BaseShopEntity();
        baseShopEntity.setSeq(0);
        baseShopEntity.setId("cnt036");
        shopList.add(baseShopEntity);
        //获取所有颜色
        Wrapper<GoodsColorEntity> goodsColorEntityWrapper = new EntityWrapper<>();
        goodsColorEntityWrapper.eq("Company_Seq",companySeq);
        List<GoodsColorEntity> goodsColorEntities = goodsColorDao.selectList(goodsColorEntityWrapper);
        //获取所有鞋子
        Wrapper<GoodsShoesEntity> goodsShoesEntityWrapper = new EntityWrapper<>();
        goodsShoesEntityWrapper.eq("Company_Seq",companySeq);
        List<GoodsShoesEntity> goodsShoesEntities = goodsShoesDao.selectList(goodsShoesEntityWrapper);
        //获取所有尺码
        Wrapper<GoodsSizeEntity> goodsSizeEntityWrapper = new EntityWrapper<>();
        goodsSizeEntityWrapper.eq("Company_Seq",companySeq);
        List<GoodsSizeEntity> goodsSizeEntities = goodsSizeDao.selectList(goodsSizeEntityWrapper);
        //库存列表
        List<ShoesDataEntity> shoesDataEntities = new ArrayList<>(100);
        //颜色序号
        Integer colorSeq = 0;
        //鞋子序号
        Integer shoesSeq = 0;
        //门店序号
        Integer shopSeq = 0;
        //循环拉取各门店的库存数据
        for(int k = 0;k < shopList.size();k++) {
            //门店序号
            shopSeq = shopList.get(k).getSeq();
            //拉取库存数据
            String stockUrl = jedUrl + "/stock?cnt=" + shopList.get(k).getId();
            Request stockRequest = new Request.Builder().url(stockUrl).build();
            Response stockResponse = client.newCall(stockRequest).execute();
            String stockData = stockResponse.body().string();
            JSONObject jsonObject = JSONObject.parseObject(stockData);
            List<Map<String,Object>> list = (List<Map<String,Object>>)jsonObject.get("result");
            //循环取出需要的值
            for(int i = 0;i < list.size();i++) {

                String inventoryId = (String)list.get(i).get("Inventory_ID");
                String fitst = inventoryId.split("-")[0];
                String sexCode = fitst.substring(fitst.length()-2,fitst.length()-1);
                //尺码列表
                List<Integer> sizeList = new ArrayList<>(10);
                //尺码序号列表
                List<Integer> sizeSeqList = new ArrayList<>(10);
                Integer size1 = Integer.parseInt((list.get(i).get("Size1").toString()).split("\\.")[0]);
                if(size1 != 0) {
                    sizeList.add(size1);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if(goodsSizeEntities.get(j).getSizeName().equals(37 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if(goodsSizeEntities.get(j).getSizeName().equals(33 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }

                    }
                }
                Integer size2 = Integer.parseInt((list.get(i).get("Size2").toString()).split("\\.")[0]);
                if(size2 != 0) {
                    sizeList.add(size2);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if(goodsSizeEntities.get(j).getSizeName().equals((38) + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if(goodsSizeEntities.get(j).getSizeName().equals(34 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }

                    }
                }
                Integer size3 = Integer.parseInt((list.get(i).get("Size3").toString()).split("\\.")[0]);
                if(size3 != 0) {
                    sizeList.add(size3);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if(goodsSizeEntities.get(j).getSizeName().equals(39 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if(goodsSizeEntities.get(j).getSizeName().equals(35 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size4 = Integer.parseInt((list.get(i).get("Size4").toString()).split("\\.")[0]);
                if(size4 != 0) {
                    sizeList.add(size4);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(40 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(36 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size5 = Integer.parseInt((list.get(i).get("Size5").toString()).split("\\.")[0]);
                if(size5 != 0) {
                    sizeList.add(size5);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(41 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(37 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size6 = Integer.parseInt((list.get(i).get("Size6").toString()).split("\\.")[0]);
                if(size6 != 0) {
                    sizeList.add(size6);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(42 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(38 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size7 = Integer.parseInt((list.get(i).get("Size7").toString()).split("\\.")[0]);
                if(size7 != 0) {
                    sizeList.add(size7);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(43 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(39 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size8 = Integer.parseInt((list.get(i).get("Size8").toString()).split("\\.")[0]);
                if(size8 != 0) {
                    sizeList.add(size8);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(44 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(40 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size9 = Integer.parseInt((list.get(i).get("Size9").toString()).split("\\.")[0]);
                if(size9 != 0) {
                    sizeList.add(size9);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(45 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(41 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size10 = Integer.parseInt((list.get(i).get("Size10").toString()).split("\\.")[0]);
                if(size10 != 0) {
                    sizeList.add(size10);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(46 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(42 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size11 = Integer.parseInt((list.get(i).get("Size11").toString()).split("\\.")[0]);
                if(size11 != 0) {
                    sizeList.add(size11);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(47 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(43 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size12 = Integer.parseInt((list.get(i).get("Size12").toString()).split("\\.")[0]);
                if(size12 != 0) {
                    sizeList.add(size12);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(48 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(44 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size13 = Integer.parseInt((list.get(i).get("Size13").toString()).split("\\.")[0]);
                if(size13 != 0) {
                    sizeList.add(size13);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(49 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(45 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size14 = Integer.parseInt((list.get(i).get("Size14").toString()).split("\\.")[0]);
                if(size14 != 0) {
                    sizeList.add(size14);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(50 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(46 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }Integer size15 = Integer.parseInt((list.get(i).get("Size15").toString()).split("\\.")[0]);
                if(size15 != 0) {
                    sizeList.add(size15);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(51 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(47 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }Integer size16 = Integer.parseInt((list.get(i).get("Size16").toString()).split("\\.")[0]);
                if(size16 != 0) {
                    sizeList.add(size16);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(52 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(48 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size17 = Integer.parseInt((list.get(i).get("Size17").toString()).split("\\.")[0]);
                if(size17 != 0) {
                    sizeList.add(size17);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(53 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(49 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer size18 = Integer.parseInt((list.get(i).get("Size18").toString()).split("\\.")[0]);
                if(size18 != 0) {
                    sizeList.add(size18);
                    for(int j = 0;j < goodsSizeEntities.size();j++) {
                        if("B".equals(sexCode)) {
                            if (goodsSizeEntities.get(j).getSizeName().equals(54 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }else {
                            if (goodsSizeEntities.get(j).getSizeName().equals(50 + "")) {
                                sizeSeqList.add(goodsSizeEntities.get(j).getSeq());
                            }
                        }
                    }
                }
                Integer index = getChineseIndex(inventoryId);
                String color = inventoryId.substring(index,inventoryId.length());

                for(int j = 0;j < goodsColorEntities.size();j++) {
                    if(goodsColorEntities.get(j).getName().equals(color)) {
                        colorSeq = goodsColorEntities.get(j).getSeq();
                    }
                }
                for(int j = 0;j < goodsShoesEntities.size();j++) {
                    if(goodsShoesEntities.get(j).getGoodID().equals(inventoryId)) {
                        shoesSeq = goodsShoesEntities.get(j).getSeq();
                    }
                }
                for(int j = 0;j < sizeSeqList.size();j++) {
                    ShoesDataEntity shoesDataEntity = new ShoesDataEntity();
                    shoesDataEntity.setShopSeq(shopSeq);
                    shoesDataEntity.setShoesSeq(shoesSeq);
                    shoesDataEntity.setColorSeq(colorSeq);
                    shoesDataEntity.setSizeSeq(sizeSeqList.get(j));
                    shoesDataEntity.setStockDate(new Date());
                    shoesDataEntity.setStock(sizeList.get(j));
                    shoesDataEntities.add(shoesDataEntity);
                }
            }
        }

        if(shoesDataEntities.size() > 0) {
            List<ShoesDataEntity> insertShoesDataEntities = new ArrayList<>(100);
            List<ShoesDataEntity> updateShoesDataEntities = new ArrayList<>(100);
            for(int i = 0;i < shoesDataEntities.size();i++) {
                Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
                shoesDataEntityWrapper.eq("Shop_Seq",shoesDataEntities.get(i).getShopSeq());
                shoesDataEntityWrapper.eq("Shoes_Seq",shoesDataEntities.get(i).getShoesSeq());
                shoesDataEntityWrapper.eq("Color_Seq",shoesDataEntities.get(i).getColorSeq());
                shoesDataEntityWrapper.eq("Size_Seq",shoesDataEntities.get(i).getSizeSeq());
                List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(shoesDataEntityWrapper);
                if(shoesDataEntityList.size() > 0) {
                    shoesDataEntities.get(i).setSeq(shoesDataEntityList.get(0).getSeq());
                    shoesDataEntityList.add(shoesDataEntities.get(i));
                }else {
                    insertShoesDataEntities.add(shoesDataEntities.get(i));
                }
            }
            List<ShoesDataEntity> updateShoesDataEntityList = new ArrayList<>(100);
            for(int i = 0;i < updateShoesDataEntities.size();i++) {
                updateShoesDataEntityList.add(updateShoesDataEntities.get(i));
                if(updateShoesDataEntityList.size() == 100) {
                    shoesDataDao.updateShoesData(updateShoesDataEntityList);
                    updateShoesDataEntityList.clear();;
                }else if(i == updateShoesDataEntities.size() - 1) {
                    shoesDataDao.updateShoesData(updateShoesDataEntityList);
                }
            }
            List<ShoesDataEntity> insertShoesDataEntityList = new ArrayList<>(100);
            for(int i = 0;i < insertShoesDataEntities.size();i++) {
                insertShoesDataEntityList.add(insertShoesDataEntities.get(i));
                if(insertShoesDataEntityList.size() == 100) {
                    shoesDataDao.insertShoesData(insertShoesDataEntityList);
                    insertShoesDataEntityList.clear();
                }else if(i == insertShoesDataEntities.size() - 1) {
                    shoesDataDao.insertShoesData(insertShoesDataEntityList);
                }
            }
        }

        Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
        List<ShoesDataEntity> shoesDataList = shoesDataDao.selectList(shoesDataEntityWrapper);
        Wrapper<ShoesDataDailyDetailEntity> shoesWrapper = new EntityWrapper<>();
        shoesWrapper.eq("Type",0);
        shoesWrapper.setSqlSelect("Shop_Seq,Size,Color,Shoes_Seq,SUM(Count) AS num");
        shoesWrapper.groupBy("Shop_Seq,Size,Color,Shoes_Seq");
        List<ShoesDataDailyDetailEntity> shoesEntities = shoesDataDailyDetailDao.selectList(shoesWrapper);
        Wrapper<ShoesDataDailyDetailEntity> shoesInWrapper = new EntityWrapper<>();
        shoesInWrapper.eq("Type",1);
        shoesInWrapper.setSqlSelect("Shop_Seq,Size,Color,Shoes_Seq,SUM(Count) AS num");
        shoesInWrapper.groupBy("Shop_Seq,Size,Color,Shoes_Seq");
        List<ShoesDataDailyDetailEntity> shoesInEntities = shoesDataDailyDetailDao.selectList(shoesInWrapper);
        Wrapper<ShoesDataDailyDetailEntity> shoesOutWrapper = new EntityWrapper<>();
        shoesOutWrapper.eq("Type",2);
        shoesOutWrapper.setSqlSelect("Shop_Seq,Size,Color,Shoes_Seq,SUM(Count) AS num");
        shoesOutWrapper.groupBy("Shop_Seq,Size,Color,Shoes_Seq");
        List<ShoesDataDailyDetailEntity> shoesOutEntities = shoesDataDailyDetailDao.selectList(shoesOutWrapper);
        for(int i = 0;i < shoesDataList.size();i++) {
            if(new Integer(0).equals(shoesDataList.get(i).getShopSeq())) {
                for(int j = 0;j < shoesEntities.size();j++) {
                    if(shoesEntities.get(j) != null) {
                        if(shoesDataList.get(i).getShopSeq().equals(shoesEntities.get(j).getShopSeq().intValue()) && shoesDataList.get(i).getShoesSeq().equals(shoesEntities.get(j).getShoesSeq().intValue()) && shoesDataList.get(i).getSizeSeq().equals(shoesEntities.get(j).getSize().intValue()) && shoesDataList.get(i).getColorSeq().equals(shoesEntities.get(j).getColor().intValue())) {
                            shoesDataList.get(i).setNum(shoesEntities.get(j).getNum());
                            break;
                        }
                    }
                }
            }else {
                Integer num = 0;
                for(int j = 0;j < shoesInEntities.size();j++) {
                    if(shoesInEntities.get(j) != null) {
                        if(shoesDataList.get(i).getShopSeq().equals(shoesInEntities.get(j).getShopSeq().intValue()) && shoesDataList.get(i).getShoesSeq().equals(shoesInEntities.get(j).getShoesSeq().intValue()) && shoesDataList.get(i).getSizeSeq().equals(shoesInEntities.get(j).getSize().intValue()) && shoesDataList.get(i).getColorSeq().equals(shoesInEntities.get(j).getColor().intValue())) {
                            num = shoesInEntities.get(j).getNum();
                            break;
                        }
                    }
                }
                for(int j = 0;j < shoesOutEntities.size();j++) {
                    if(shoesOutEntities.get(j) != null) {
                        if(shoesDataList.get(i).getShopSeq().equals(shoesOutEntities.get(j).getShopSeq().intValue()) && shoesDataList.get(i).getShoesSeq().equals(shoesOutEntities.get(j).getShoesSeq().intValue()) && shoesDataList.get(i).getSizeSeq().equals(shoesOutEntities.get(j).getSize().intValue()) && shoesDataList.get(i).getColorSeq().equals(shoesOutEntities.get(j).getColor().intValue())) {
                            num = num - shoesOutEntities.get(j).getNum();
                             break;
                        }
                    }
                }
                shoesDataList.get(i).setNum(num);
            }
        }
        List<ShoesDataEntity> shoesNumEntityList = new ArrayList<>(100);
        for(int i = 0;i < shoesDataList.size();i++) {
            shoesNumEntityList.add(shoesDataList.get(i));
            if(shoesNumEntityList.size() == 100) {
                shoesDataDao.updateShoesData(shoesNumEntityList);
                shoesNumEntityList.clear();
            }else if(i == shoesDataList.size() - 1) {
                shoesDataDao.updateShoesData(shoesNumEntityList);
            }
        }
        return true;
    }

    /**
     * 解析鞋子编码
     * @param inventoryId
     * @param shoesDataDailyDetailEntity
     * @return
     */
    private ShoesDataDailyDetailEntity parseShoes(String inventoryId,ShoesDataDailyDetailEntity shoesDataDailyDetailEntity) {
        GoodsShoesEntity goodsShoesEntity = new GoodsShoesEntity();
        String[] code = inventoryId.split("-");
        if(code.length == 1) {
            return null;
        }
        //第一段鞋子编码
        String firstCode = code[0];
        //第二段鞋子编码
        String secondCode = code[1];
        if(secondCode . length() < 4) {
            return null;
        }

        String season = secondCode.substring(1,2);
        if(!(season.equals("1") || season.equals("2") || season.equals("3") || season.equals("5"))) {
            return null;
        }

        //厂家编号
        String factoryCode = firstCode.substring(0,firstCode.length()-2);
        //大类
        String largeCategory = firstCode.substring(firstCode.length()-2,firstCode.length()-1);
        //中类
        String middleCategory = firstCode.substring(firstCode.length()-1,firstCode.length());
        //解析类别
        goodsShoesEntity = parseCategory(largeCategory,middleCategory,secondCode,goodsShoesEntity);


        //年份编码
        String yearCode = secondCode.substring(0,1);
        String yearStr = jointYear(yearCode);
        Integer year = 0;
        if(yearStr == "") {
            return null;
        }else {
            year = Integer.parseInt(yearStr);
        }
        goodsShoesEntity.setYear(year);


        //季节
        Integer seasonCode = Integer.parseInt(secondCode.substring(1,2));
        goodsShoesEntity = checkSeason(seasonCode,goodsShoesEntity);

        //颜色
        Integer index = getChineseIndex(secondCode);
        String color = "";
        if(index == 0) {
            color = "无颜色";
        }else {
            color = secondCode.substring(index,secondCode.length());
        }
        shoesDataDailyDetailEntity = checkColor(color,shoesDataDailyDetailEntity);

        goodsShoesEntity.setGoodID(inventoryId);
        shoesDataDailyDetailEntity = checkShoes(goodsShoesEntity,shoesDataDailyDetailEntity);
        return shoesDataDailyDetailEntity;
    }

    /**
     * 拼接年份
     * @param yearCode
     * @return
     */
    private String jointYear(String yearCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = "";
        if(yearCode.equals("S")) {
            year = sdf.format(new Date()).substring(0,3) + 4;
        }else {
            char yearChar = yearCode.charAt(0);
            boolean isLetter = (yearChar >= 'a' && yearChar <= 'z') || (yearChar >= 'A' && yearChar <= 'Z');
            if(isLetter) {
                return "";
            }else {
                year = sdf.format(new Date()).substring(0,3) + yearCode;
            }
        }
        if(Integer.parseInt(year) > Integer.parseInt(sdf.format(new Date()))) {
            year = (Integer.parseInt(sdf.format(new Date()).substring(0,3))- 1) + yearCode;
        }
        return year;
    }

    /**
     * 获取secondCode里第一个字母的位置
     * @param secondCode
     * @return
     */
    private Integer getLetterIndex(String secondCode) {
        Integer index = new Integer(0);
        for(int i = 0;i < secondCode.length();i++) {
            char first = secondCode.charAt(i);
            boolean isLetter = (first >= 'a' && first <= 'z') || (first >= 'A' && first <= 'Z');
            if(isLetter) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 获取secondCode里第一个中文字符的位置
     * @param secondCode
     * @return
     */
    private Integer getChineseIndex(String secondCode) {
        Integer index = new Integer(0);
        for(int i = 0;i < secondCode.length();i++) {
            char first = secondCode.charAt(i);
            char[] firsts = {first};
            Character.UnicodeScript sc = Character.UnicodeScript.of(first);
            if (sc == Character.UnicodeScript.HAN && !(new String(firsts).equals("休"))) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 解析类别
     * @param largeCategory
     * @param middleCategory
     * @param secondCode
     * @param goodsShoesEntity
     * @return
     */
    private GoodsShoesEntity parseCategory(String largeCategory,String middleCategory,String secondCode,GoodsShoesEntity goodsShoesEntity) {
        //大类名称
        String largeCategoryName = "";
        //中类名称
        String middleCategoryName = "";
        //风格名称
        String lastCategoryName = "";
        //风格编码
        String lastCategory = "";
        //获取第二段编码的第一个字母的下标
        Integer index = getLetterIndex(secondCode);
        //判断下一个字符是不是字母
        boolean isLetter = false;
        if(index == secondCode.length() - 1) {

        }else {
            char nextCode = secondCode.charAt(index + 1);
            isLetter = (nextCode >= 'a' && nextCode <= 'z') || (nextCode >= 'A' && nextCode <= 'Z');
        }
        //如果第二段没有字母
        if(index == 0) {
            //只有两种情况，男鞋的单鞋欧版鞋和男鞋的休闲新大众休闲鞋
            if(largeCategory.equals(LargeCategoryEnums.B.getKey())) {
                if(middleCategory.equals(MiddleCategoryEnums.D.getKey())) {
                    lastCategory = "OBX";
                    lastCategoryName = "欧版鞋";
                }else if(middleCategory.equals(MiddleCategoryEnums.U.getKey())) {
                    lastCategory = "XDZXXX";
                    lastCategoryName = "新大众休闲鞋";
                }else {
                    lastCategory = "WLX";
                    lastCategoryName = "无类型";
                }
            }else {
                lastCategory = "WLX";
                lastCategoryName = "无类型";
            }
        //如果第二段有两个字母
        }else if(isLetter) {
            //截取两位作为类型编码
            lastCategory = secondCode.substring(index,index + 2);
            //用类型编码获取类型名称
            lastCategoryName = LastCategoryEnums.getMessage(lastCategory);
            if("".equals(lastCategoryName)) {
                lastCategory = "WLX";
                lastCategoryName = "无类型";
            }
        //如果第二段的字母是K，并且字母K后面是休
        }else if((index + 2) < secondCode.length() && LastCategoryEnums.getKey(LastCategoryEnums.KX.getValue()).equals(secondCode.substring(index,index + 2))) {
            //截取两位作为类型编码
            lastCategory = secondCode.substring(index,index + 2);
            //用类型编码获取类型名称
            lastCategoryName = LastCategoryEnums.getMessage(LastCategoryEnums.KX.getValue());
            if("".equals(lastCategoryName)) {
                lastCategory = "WLX";
                lastCategoryName = "无类型";
            }
        //如果风格类型是S
        }else if(LastCategoryEnums.getKey(LastCategoryEnums.BS.getValue()).equals(secondCode.substring(index,index + 1))) {
            //大类是男鞋的话，风格是绅士鞋，大类是女鞋的话，风格是淑女鞋
            if(largeCategory.equals(LargeCategoryEnums.B.getKey())) {
                //截取一位作为类型编码
                lastCategory = secondCode.substring(index,index + 1);
                //用类型编码获取类型名称
                lastCategoryName = LastCategoryEnums.getMessage(LastCategoryEnums.BS.getValue());
                if("".equals(lastCategoryName)) {
                    lastCategory = "WLX";
                    lastCategoryName = "无类型";
                }
            }else {
                //截取一位作为类型编码
                lastCategory = secondCode.substring(index,index + 1);
                //用类型编码获取类型名称
                lastCategoryName = LastCategoryEnums.getMessage(LastCategoryEnums.GS.getValue());
                if("".equals(lastCategoryName)) {
                    lastCategory = "WLX";
                    lastCategoryName = "无类型";
                }
            }
        //如果中类是L或者M，没有风格编码，风格名称为对应的中类的名称
        }else if(middleCategory.equals(MiddleCategoryEnums.M.getKey()) || middleCategory.equals(MiddleCategoryEnums.L.getKey())) {
            lastCategoryName = LastCategoryEnums.getMessage(middleCategory);
            if("".equals(lastCategoryName)) {
                lastCategory = "WLX";
                lastCategoryName = "无类型";
            }
        //一般情况下
        }else {
            //截取一位作为类型编码
            lastCategory = secondCode.substring(index,index + 1);
            //用类型编码获取类型名称
            lastCategoryName = LastCategoryEnums.getMessage(lastCategory);
            if("".equals(lastCategoryName)) {
                lastCategory = "WLX";
                lastCategoryName = "无类型";
            }
        }
        //获取大类名称
        largeCategoryName = LargeCategoryEnums.getMessage(largeCategory);
        if("".equals(lastCategoryName)) {
            lastCategory = "WLX";
            lastCategoryName = "无类型";
        }
        //获取中类名称
        middleCategoryName = MiddleCategoryEnums.getMessage(middleCategory);
        if("".equals(lastCategoryName)) {
            lastCategory = "WLX";
            lastCategoryName = "无类型";
        }
        //判断大类是否存在
        GoodsCategoryEntity largeCategoryEntity = checkCategory(largeCategory,largeCategoryName,0);
        //判断中类类是否存在
        GoodsCategoryEntity middleCategoryEntity = checkCategory(middleCategory,middleCategoryName,largeCategoryEntity.getSeq());
        //判断风格是否存在
        GoodsCategoryEntity lastCategoryEntity = checkCategory(lastCategory,lastCategoryName,middleCategoryEntity.getSeq());
        goodsShoesEntity.setCategorySeq(lastCategoryEntity.getSeq());
        goodsShoesEntity.setCategoryName(lastCategoryEntity.getName());
        return goodsShoesEntity;
    }

    /**
     * 查询类别是否存在，不存在的插入数据库
     * @param categoryCode
     * @param categoryName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private GoodsCategoryEntity checkCategory(String categoryCode,String categoryName,Integer categorySeq) {
        //如果类型名称不存在,默认为无类型
        if(categoryName == null || categoryName.isEmpty()) {
            categoryCode = "WLX";
            categoryName = "无类型";
            categorySeq = 0;
        }
        Wrapper<GoodsCategoryEntity> goodsCategoryEntityWrapper = new EntityWrapper<>();
        goodsCategoryEntityWrapper.eq("Category_Code",categoryCode).eq("Name",categoryName).eq("Company_Seq",companySeq).eq("ParentSeq",categorySeq);
        List<GoodsCategoryEntity> largeCategoryList = goodsCategoryDao.selectList(goodsCategoryEntityWrapper);
        GoodsCategoryEntity goodsCategoryEntity = new GoodsCategoryEntity();
        if(largeCategoryList == null || largeCategoryList.size() == 0) {
            goodsCategoryEntity.setCompanySeq(companySeq);
            goodsCategoryEntity.setName(categoryName);
            goodsCategoryEntity.setCatetoryCode(categoryCode);
            goodsCategoryEntity.setDel(0);
            goodsCategoryEntity.setVisible(0);
            goodsCategoryEntity.setInputTime(new Date());
            goodsCategoryEntity.setParentSeq(categorySeq);
            goodsCategoryDao.insert(goodsCategoryEntity);
            return goodsCategoryEntity;
        }else {
            return largeCategoryList.get(0);
        }

    }

    /**
     * 查询季节是否存在，不存在的插入数据库
     * @param seasonCode
     * @param goodsShoesEntity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private GoodsShoesEntity checkSeason(Integer seasonCode,GoodsShoesEntity goodsShoesEntity) {
        Wrapper<GoodsSeasonEntity> goodsSeasonEntityWrapper = new EntityWrapper<>();
        String seasonName = "";
        //判断获取季节名称
        if(SeasonEnums.SPRING.getValue().equals(seasonCode)) {
            seasonName = SeasonEnums.getMessage(SeasonEnums.SPRING.getValue());
        }else if(SeasonEnums.SUMMER.getValue().equals(seasonCode)) {
            seasonName = SeasonEnums.getMessage(SeasonEnums.SUMMER.getValue());
        }else if(SeasonEnums.AUTUMN.getValue().equals(seasonCode)) {
            seasonName = SeasonEnums.getMessage(SeasonEnums.AUTUMN.getValue());
        }else if(SeasonEnums.WINTER.getValue().equals(seasonCode)) {
            seasonName = SeasonEnums.getMessage(SeasonEnums.WINTER.getValue());
        }
        goodsSeasonEntityWrapper.eq("SeasonName",seasonName).eq("Brand_Seq",brandSeq);
        List<GoodsSeasonEntity> goodsSeasonEntities = goodsSeasonDao.selectList(goodsSeasonEntityWrapper);
        if(goodsSeasonEntities == null || goodsSeasonEntities.size() == 0) {
            GoodsSeasonEntity goodsSeasonEntity = new GoodsSeasonEntity();
            goodsSeasonEntity.setBrandSeq(brandSeq);
            goodsSeasonEntity.setInputTime(new Date());
            goodsSeasonEntity.setSeasonName(seasonName);
            goodsSeasonEntity.setDel(0);
            goodsSeasonDao.insert(goodsSeasonEntity);
            goodsShoesEntity.setSeasonSeq(goodsSeasonEntity.getSeq());
        }else {
            goodsShoesEntity.setSeasonSeq(goodsSeasonEntities.get(0).getSeq());
        }
        return goodsShoesEntity;
    }

    /**
     * 查询颜色是否存在，不存在的插入数据库
     * @param color
     * @param shoesDataDailyDetailEntity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private ShoesDataDailyDetailEntity checkColor(String color,ShoesDataDailyDetailEntity shoesDataDailyDetailEntity) {
        Wrapper<GoodsColorEntity> goodsColorEntityWrapper = new EntityWrapper<>();
        goodsColorEntityWrapper.eq("Name",color).eq("Company_Seq",companySeq);
        List<GoodsColorEntity> goodsColorEntities = goodsColorDao.selectList(goodsColorEntityWrapper);
        if(goodsColorEntities == null || goodsColorEntities.size() == 0) {
            GoodsColorEntity goodsColorEntity = new GoodsColorEntity();
            goodsColorEntity.setName(color);
            goodsColorEntity.setInputTime(new Date());
            goodsColorEntity.setDel(0);
            goodsColorEntity.setCompanySeq(companySeq);
            goodsColorDao.insert(goodsColorEntity);
            shoesDataDailyDetailEntity.setColor(goodsColorEntity.getSeq());
        }else {
            shoesDataDailyDetailEntity.setColor(goodsColorEntities.get(0).getSeq());
        }
        return shoesDataDailyDetailEntity;
    }

    /**
     * 查询鞋子是否存在，不存在的插入数据库
     * @param goodsShoesEntity
     * @param shoesDataDailyDetailEntity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private ShoesDataDailyDetailEntity checkShoes(GoodsShoesEntity goodsShoesEntity,ShoesDataDailyDetailEntity shoesDataDailyDetailEntity) {
        Wrapper<GoodsShoesEntity> goodsShoesEntityWrapper = new EntityWrapper<>();
        goodsShoesEntityWrapper.eq("GoodId",goodsShoesEntity.getGoodID()).eq("Company_Seq",companySeq);
        List<GoodsShoesEntity> goodsShoesEntities = goodsShoesDao.selectList(goodsShoesEntityWrapper);
        if(goodsShoesEntities == null || goodsShoesEntities.size() == 0) {
            goodsShoesEntity.setGoodID(goodsShoesEntity.getGoodID());
            goodsShoesEntity.setCompanySeq(companySeq);
            goodsShoesEntity.setDel(0);
            goodsShoesEntity.setInputTime(new Date());
            goodsShoesEntity.setShoesBrand("吉尔达");
            goodsShoesDao.insert(goodsShoesEntity);
            shoesDataDailyDetailEntity.setShoesSeq(goodsShoesEntity.getSeq());
            shoesDataDailyDetailEntity.setShoesId(goodsShoesEntity.getGoodID());
        }else {
            shoesDataDailyDetailEntity.setShoesSeq(goodsShoesEntities.get(0).getSeq());
            shoesDataDailyDetailEntity.setShoesId(goodsShoesEntities.get(0).getGoodID());
        }
        return shoesDataDailyDetailEntity;
    }

    /**
     * 查询门店是否存在，不存在的插入数据库
     * @param baseShopEntity
     * @param shoesDataDailyDetailEntity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private ShoesDataDailyDetailEntity checkShop(BaseShopEntity baseShopEntity,ShoesDataDailyDetailEntity shoesDataDailyDetailEntity) {
        Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
        baseShopEntityWrapper.eq("Id",baseShopEntity.getId()).eq("Company_Seq",companySeq);
        List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(baseShopEntityWrapper);
        if(baseShopEntities == null || baseShopEntities.size() == 0) {
            baseShopEntity.setCompanySeq(companySeq);
            baseShopEntity.setDel(0);
            baseShopEntity.setInputTime(new Date());
            baseShopEntity.setAreaSeq(areaSeq);
            baseShopDao.insert(baseShopEntity);
            shoesDataDailyDetailEntity.setShopSeq(baseShopEntity.getSeq());
            shoesDataDailyDetailEntity.setShopName(baseShopEntity.getName());
        }else {
            shoesDataDailyDetailEntity.setShopSeq(baseShopEntities.get(0).getSeq());
            shoesDataDailyDetailEntity.setShopName(baseShopEntities.get(0).getName());
        }
        return shoesDataDailyDetailEntity;
    }

    /**
     * 查询尺码是否存在，不存在的插入数据库
     * @param size
     * @param shoesDataDailyDetailEntity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private ShoesDataDailyDetailEntity checkSize(String size,ShoesDataDailyDetailEntity shoesDataDailyDetailEntity) {
        Wrapper<GoodsSizeEntity> goodsSizeEntityWrapper = new EntityWrapper<>();
        goodsSizeEntityWrapper.eq("SizeName",size).eq("Company_Seq",companySeq);
        List<GoodsSizeEntity> goodsSizeEntities = goodsSizeDao.selectList(goodsSizeEntityWrapper);
        if(goodsSizeEntities == null || goodsSizeEntities.size() == 0) {
            GoodsSizeEntity goodsSizeEntity = new GoodsSizeEntity();
            goodsSizeEntity.setCompanySeq(companySeq);
            goodsSizeEntity.setDel(0);
            goodsSizeEntity.setSizeName(size);
            goodsSizeEntity.setInputTime(new Date());
            goodsSizeDao.insert(goodsSizeEntity);
            shoesDataDailyDetailEntity.setSize(goodsSizeEntity.getSeq());
        }else {
            shoesDataDailyDetailEntity.setSize(goodsSizeEntities.get(0).getSeq());
        }
        return shoesDataDailyDetailEntity;
    }
}
