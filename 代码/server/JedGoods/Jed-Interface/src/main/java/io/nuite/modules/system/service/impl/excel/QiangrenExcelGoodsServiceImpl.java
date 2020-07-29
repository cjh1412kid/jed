package io.nuite.modules.system.service.impl.excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.exception.RRException;
import io.nuite.modules.order_platform_app.dao.ShoesCompanyTypeDao;
import io.nuite.modules.order_platform_app.entity.ShoesCompanyTypeEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsSXOptionService;
import io.nuite.modules.sr_base.service.GoodsSXService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.model.excel.qiangren.Sample;
import io.nuite.modules.system.model.excel.qiangren.SampleColor;
import io.nuite.modules.system.model.excel.qiangren.SampleSize;
import io.nuite.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Service("qiangrenExcelGoods")
public class QiangrenExcelGoodsServiceImpl implements ExcelGoodsService {
    @Autowired
    protected ConfigUtils configUtils;

    @Autowired
    private SysPeriodService sysPeriodService;

    @Autowired
    private GoodsShoesService goodsShoesService;

    @Autowired
    private SystemGoodsCategoryService systemGoodsCategoryService;

    @Autowired
    private SystemGoodsColorService systemGoodsColorService;

    @Autowired
    private SystemGoodsSizeService systemGoodsSizeService;

    @Autowired
    private io.nuite.modules.order_platform_app.service.ShoesDataService orderShoesDataService;

    @Autowired
    private io.nuite.modules.order_platform_app.service.ShoesInfoService orderShoesInfoService;

    @Autowired
    private ShoesCompanyTypeDao shoesCompanyTypeDao;

    @Autowired
    private GoodsSXService goodsSXService;

    @Autowired
    private GoodsSXOptionService goodsSXOptionService;

    @Transactional
    @Override
    public void importExcel(Integer companySeq, Integer brandSeq, MultipartFile excelFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(true);
        params.setNeedVerfiy(true);

        params.setStartSheetIndex(0);
        List<Sample> sampleList = ExcelImportUtil.importExcel(excelFile.getInputStream(), Sample.class, params);
        Map<String, Integer> sampleMap = new HashMap<>();
        for (Sample sample : sampleList) {
            Integer count = sampleMap.get(sample.getGoodsId());
            if (count == null) count = 0;
            count++;
            sampleMap.put(sample.getGoodsId(), count);
        }
        List<String> idArray = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sampleMap.entrySet()) {
            if (entry.getValue() > 1) {
                idArray.add(entry.getKey());
            }
        }
        if (!idArray.isEmpty()) {
            throw new RRException("文件中存在重复的货号:" + String.join(",", idArray));
        }

        StringBuilder sxExist = new StringBuilder();
        for (int i = 1; i <= 16; i++) {
            Set<String> sampleValue = new HashSet<>();
            for (Sample sample : sampleList) {
                Method sxCodeMethod = Sample.class.getMethod("getSx" + i + "Code");
                String sxCode = (String) sxCodeMethod.invoke(sample);
                sampleValue.add(sxCode.trim());
            }

            sxExist.append(checkSXCode(companySeq, "SX" + i, sampleValue));
        }

        if (sxExist.length() > 0) {
            throw new RRException("属性值不存在[" + sxExist + "]");
        }

        params.setStartSheetIndex(1);
        List<SampleColor> sampleColorList = ExcelImportUtil.importExcel(excelFile.getInputStream(), SampleColor.class, params);

        params.setStartSheetIndex(2);
        List<SampleSize> sampleSzieList = ExcelImportUtil.importExcel(excelFile.getInputStream(), SampleSize.class, params);

        for (Sample sample : sampleList) {
            GoodsShoesEntity goodsShoesEntity;
            GoodsShoesEntity oldShoes = goodsShoesService.getGoodsByGoodId(companySeq, sample.getGoodsId());
            if (oldShoes == null) {
                goodsShoesEntity = new GoodsShoesEntity();
            } else {
                goodsShoesEntity = oldShoes;
            }
            goodsShoesEntity.setCompanySeq(companySeq);
            goodsShoesEntity.setShoesBrand(sample.getBrandCode());
            goodsShoesEntity.setMnemonic(sample.getMnemonic());
            String sessonCode = sample.getSessionCode();
            String sessionName;
            switch (sessonCode) {
                case "001":
                    sessionName = "春季";
                    break;
                case "002":
                    sessionName = "春季";
                    break;
                case "003":
                    sessionName = "春季";
                    break;
                case "004":
                    sessionName = "春季";
                    break;
                case "007":
                    sessionName = "全年";
                    break;
                default:
                    sessionName = "未定义";
            }
            String yearCode = sample.getYearCode();
            Integer periodSeq = sysPeriodService.selectSeqByName(brandSeq, yearCode, sessionName);
            goodsShoesEntity.setPeriodSeq(periodSeq);
            String categoryCode = sample.getCategoryCode();
            GoodsCategoryEntity goodsCategoryEntity = systemGoodsCategoryService.selectOne(new EntityWrapper<GoodsCategoryEntity>().eq("Company_Seq", companySeq)
                    .eq("Category_Code", categoryCode));
            Integer categorySeq = goodsCategoryEntity.getSeq();
            goodsShoesEntity.setCategorySeq(categorySeq);

            goodsShoesEntity.setGoodID(sample.getGoodsId());
            goodsShoesEntity.setGoodName(sample.getGoodsName());
            goodsShoesEntity.setIntroduce(sample.getIntroduce());
            for (int i = 1; i <= 16; i++) {
                Method sxMethod = GoodsShoesEntity.class.getMethod("setSX" + i, String.class);

                Method sxCodeMethod = Sample.class.getMethod("getSx" + i + "Code");
                String sxCode = (String) sxCodeMethod.invoke(sample);
                sxMethod.invoke(goodsShoesEntity, sxCode);
            }

            goodsShoesService.insertOrUpdate(goodsShoesEntity);

            Integer goodsSeq = goodsShoesEntity.getSeq();

            //goods_shoes 处理完毕

            ShoesInfoEntity shoesInfoEntity;
            ShoesInfoEntity oldShoesInfo = orderShoesInfoService.getShoesInfoByShoesSeq(goodsSeq);
            if (oldShoesInfo != null) {
                shoesInfoEntity = oldShoesInfo;
            } else {
                shoesInfoEntity = new ShoesInfoEntity();
            }
            shoesInfoEntity.setShoesSeq(goodsSeq);
//            BigDecimal price2 = new BigDecimal(sample.getSalePrice());
//            shoesInfoEntity.setStorePrice(price2);
//
//            BigDecimal price1 = new BigDecimal(sample.getPurchasePrice());
//            shoesInfoEntity.setOemPrice(price1);
//            shoesInfoEntity.setSalePrice(price1);
//            shoesInfoEntity.setWholesalerPrice(price1);
            shoesInfoEntity.setOnSale(1);

            orderShoesInfoService.insertOrUpdate(shoesInfoEntity);
            //shoes_info 处理完毕

            List<SampleColor> currentColors = new ArrayList<>();
            for (SampleColor sampleColor : sampleColorList) {
                if (sampleColor.getGoodsId().equals(sample.getGoodsId())) {
                    currentColors.add(sampleColor);
                }
            }

            List<SampleSize> currentSizes = new ArrayList<>();
            for (SampleSize sampleSize : sampleSzieList) {
                if (sampleSize.getGoodsId().equals(sample.getGoodsId())) {
                    currentSizes.add(sampleSize);
                }
            }

            for (SampleColor sampleColor : currentColors) {
                String colorCode = sampleColor.getColorCode();
                Integer colorSeq = systemGoodsColorService.selectColorSeqByCode(companySeq, colorCode);
                for (SampleSize sampleSize : currentSizes) {
                    String sizeCode = sampleSize.getSizeCode();
                    Integer sizeSeq = systemGoodsSizeService.getSizeSeqByCodeAndCompanySeq(companySeq, sizeCode);
                    ShoesDataEntity shoesDataEntity;
                    ShoesDataEntity oldShoesData = orderShoesDataService.selectShoesDataByColorAndSize(colorSeq, sizeSeq, goodsSeq);
                    if (oldShoesData == null) {
                        shoesDataEntity = new ShoesDataEntity();
                    } else {
                        shoesDataEntity = oldShoesData;
                    }
                    shoesDataEntity.setShoesSeq(goodsSeq);
                    shoesDataEntity.setColorSeq(colorSeq);
                    shoesDataEntity.setSizeSeq(sizeSeq);
                    shoesDataEntity.setNum(0);
                    shoesDataEntity.setStock(0);
                    shoesDataEntity.setStockDate(new Date());
                    orderShoesDataService.insertOrUpdate(shoesDataEntity);
                }
            }

            //增加鞋子对批发、贴牌、总代可见
            List<ShoesCompanyTypeEntity> typeEntities = shoesCompanyTypeDao.selectList(new EntityWrapper<ShoesCompanyTypeEntity>().eq("Shoes_Seq", goodsSeq));
            List<Integer> list = new ArrayList<Integer>() {{
                add(1);
                add(3);
            }};
            if (typeEntities == null) {
                shoesCompanyTypeDao.insertBatch(goodsSeq, list);
            } else {
                for (ShoesCompanyTypeEntity typeEntity : typeEntities) {
                    list.remove(typeEntity.getCompanyTypeSeq());
                }
                shoesCompanyTypeDao.insertBatch(goodsSeq, list);
            }
        }
    }

    private String checkSXCode(Integer companySeq, String sxName, Set<String> set) {
        GoodsSXEntity goodsSXEntity = goodsSXService.selectOne(new EntityWrapper<GoodsSXEntity>().eq("Company_Seq", companySeq).eq("SXID", sxName));
        if (goodsSXEntity != null) {
            Integer seq = goodsSXEntity.getSeq();
            List<String> codeArray = new ArrayList<>();
            for (String code : set) {
                GoodsSXOptionEntity goodsSXOptionEntity = goodsSXOptionService.selectOne(new EntityWrapper<GoodsSXOptionEntity>().eq("SX_Seq", seq).eq("Code", code));
                if (goodsSXOptionEntity == null) {
                    codeArray.add(code);
                }
            }
            if (codeArray.isEmpty()) {
                return "";
            } else {
                return goodsSXEntity.getSXName() + ":" + String.join(",", codeArray) + ";";
            }
        } else {
            return "";
        }
    }
}
