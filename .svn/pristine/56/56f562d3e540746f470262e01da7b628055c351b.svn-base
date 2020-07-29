package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;
import io.nuite.modules.sr_base.service.GoodsColorService;
import io.nuite.modules.sr_base.service.GoodsSeasonService;
import io.nuite.modules.system.dao.OrderPlanManageDao;
import io.nuite.modules.system.dao.order_platform.OrderProductManagementDao;
import io.nuite.modules.system.entity.OrderManageEntity;
import io.nuite.modules.system.service.OrderPlanManageService;
import io.nuite.modules.system.service.SystemGoodsCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-22
 */
@Service
public class OrderPlanManageServiceImpl extends ServiceImpl<OrderPlanManageDao, OrderManageEntity> implements OrderPlanManageService {
    
    @Autowired
    SystemGoodsCategoryService categoryService;
    
    @Autowired
    GoodsSeasonService goodsSeasonService;
    
    @Autowired
    GoodsColorService goodsColorService;
    
    @Autowired
    private OrderProductManagementDao orderProductManagementDao;
    
    @Autowired
    OrderPlanManageDao orderPlanManageDao;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void parseExcelToSave(MultipartFile file, BaseUserEntity userEntity) throws Exception {
        List<List<Object>> rows = ImportMultipartExcelUtil.importExcel(file);
        
        if (rows.isEmpty()) {
            throw new RRException("内容为空");
        }
        //标题匹配判断
        List titleRow = rows.get(0);
        String[] titles = {"货号", "货品名称", "分类", "年份", "季节"};
        for (int i = 0; i < titleRow.size(); i++) {
            if (!titles[i].equals(titleRow.get(i).toString().trim())) {
                throw new RRException("标题不匹配");
            }
        }
        
        List<OrderManageEntity> saveDOs = new ArrayList<>();
        Integer companySeq = userEntity.getCompanySeq();
        Integer brandSeq = userEntity.getBrandSeq();
        OrderManageEntity orderManageEntity = null;
        List row;
        for (int i = 1; i < rows.size(); i++) {
            row = rows.get(i);
            //单元格内容不能有空
            if (StringUtils.isBlank(row.get(0).toString()) ||
                StringUtils.isBlank(row.get(1).toString()) ||
                StringUtils.isBlank(row.get(2).toString()) ||
                StringUtils.isBlank(row.get(3).toString()) ||
                StringUtils.isBlank(row.get(4).toString())
            ) {
                throw new RRException("第" + (i + 1) + "行的数据有空白，请检查！");
            }
            orderManageEntity = new OrderManageEntity();
            
            //颜色
            List<GoodsColorEntity> colors = goodsColorService.selectList(new EntityWrapper<GoodsColorEntity>()
                .eq("Name", row.get(1).toString())
                .eq("Brand_Seq", brandSeq));
            if (colors.isEmpty()) {
                throw new RRException("第" + (i + 1) + "行的颜色未查询到，请在颜色管理里维护");
            }
            
            //分类
            List<GoodsCategoryEntity> categoryEntityList = categoryService.selectList(new EntityWrapper<GoodsCategoryEntity>()
                .eq("Name", row.get(2).toString())
                .eq("Company_Seq", companySeq));
            
            if (categoryEntityList.isEmpty()) {
                throw new RRException("第" + (i + 1) + "行的分类未查询到，请在分类管理里维护");
            }
            orderManageEntity.setCategorySeq(categoryEntityList.get(0).getSeq());
            //季节
            List<GoodsSeasonEntity> seasons = goodsSeasonService.selectList(new EntityWrapper<GoodsSeasonEntity>()
                .eq("SeasonName", row.get(4).toString())
                .eq("Brand_Seq", brandSeq));
            if (seasons.isEmpty()) {
                throw new RRException("第" + (i + 1) + "行的季节未查询到，请在季节管理里维护");
            }
            
            orderManageEntity.setSeasonSeq(seasons.get(0).getSeq());
            orderManageEntity.setGoodID(row.get(0).toString());
            orderManageEntity.setColorSeq(colors.get(0).getSeq());
            orderManageEntity.setYear(Integer.parseInt(row.get(3).toString()));
            orderManageEntity.setCompanySeq(companySeq);
            
            saveDOs.add(orderManageEntity);
        }
        
        //持久化数据
        for (OrderManageEntity manageEntity : saveDOs) {
            super.insert(manageEntity);
        }
        
    }
    
    @Override
    public PageUtils queryPage(Page<OrderManageEntity> page, Map<String, Object> params, Integer companySeq) {
        List<Integer> categoryList = new ArrayList<>();
        Object categorySeqObj = params.get("categorySeq");
        int categorySeq;
        if (categorySeqObj == null || StringUtils.isEmpty(categorySeqObj.toString()) || categorySeqObj.toString().equals("-1")) {
            //categorySeq = 0;
        } else {
            categorySeq = Integer.valueOf(categorySeqObj.toString());
            // 不为空则判断是否有子类
            getCategorySeqList(categoryList, Collections.singletonList(categorySeq));
            params.put("categorySeqs", Joiner.on(",").join(categoryList));
        }
        
        //模糊查询 关键字
        String keywords = (String) params.get("keywords");
        if (keywords != null) {
            keywords = keywords.trim();
        }
        params.put("goodNameId", keywords);
        
        List<OrderManageEntity> orderManageEntities = orderPlanManageDao.queryPage(page, params, companySeq);
        page.setRecords(orderManageEntities);
        
        return new PageUtils(page);
    }
    
    // 递归查询子类型
    private void getCategorySeqList(List<Integer> categoryList, List<Integer> list) {
        for (Integer integer : list) {
            categoryList.add(integer);
            List<Integer> listson = orderProductManagementDao.getCategorySeqList(integer);
            if (listson != null && listson.size() > 0) {
                getCategorySeqList(categoryList, listson);
            }
        }
    }
    
    /**
     * 根据货号获取订货会鞋子
     */
    @Override
    public OrderManageEntity getOrderManageEntityByGoodId(String goodId) {
        OrderManageEntity orderManageEntity = new OrderManageEntity();
        orderManageEntity.setGoodID(goodId);
        return orderPlanManageDao.selectOne(orderManageEntity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void parseJRDExcelToSave(MultipartFile file, BaseUserEntity userEntity) throws Exception {
        List<List<Object>> rows = ImportMultipartExcelUtil.importExcel(file);
        
        if (rows.isEmpty()) {
            throw new RRException("Excel内容为空");
        }
        
        //所有季节
        List<GoodsSeasonEntity> seasonList = goodsSeasonService.selectList(new EntityWrapper<GoodsSeasonEntity>()
            .eq("Brand_Seq", userEntity.getBrandSeq()));
        
        //所有颜色
        List<GoodsColorEntity> colorList = goodsColorService.selectList(new EntityWrapper<GoodsColorEntity>()
            .eq("Company_Seq", userEntity.getCompanySeq()));
        
        //大类+中类，所有子分类map
        HashMap<String, List<GoodsCategoryEntity>> categoryMap = new HashMap<>();
        
        //记录大类中类的序号
        Map<String, String> daLeiAndZhLeiMap = new HashMap<>();
        
        List row;
        String goodsID = null;
        OrderManageEntity orderManageEntity;
        //待存储列表
        List<OrderManageEntity> saveDOs = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            row = rows.get(i);
            //货号
            goodsID = row.get(0).toString();
            if (StringUtils.isNotBlank(goodsID)) {
                orderManageEntity = new OrderManageEntity();
                orderManageEntity.setGoodID(goodsID);
                orderManageEntity.setCompanySeq(userEntity.getCompanySeq());
                
                String[] strings = goodsID.split("-");
                //货品生产厂家编码
                //String str1 = strings[0].substring(0, strings[0].length() - 2);
                //大类
                char daLei = strings[0].charAt(strings[0].length() - 2);
                //中类
                char zhLei = strings[0].charAt(strings[0].length() - 1);
                
                if (!categoryMap.containsKey(daLei + "+" + zhLei)) {
                    GoodsCategoryEntity goodsCategoryEntity1 = categoryService.selectOne(new EntityWrapper<GoodsCategoryEntity>()
                        .eq("Company_Seq", userEntity.getCompanySeq())
                        .eq("Category_Code", daLei));
                    
                    if (goodsCategoryEntity1 == null) {
                        throw new RRException("第" + (i + 1) + "行数据：大类编号" + daLei + "不存在，请在分类管理里添加");
                    }
                    
                    GoodsCategoryEntity zhongLei = categoryService.selectOne(new EntityWrapper<GoodsCategoryEntity>()
                        .eq("ParentSeq", goodsCategoryEntity1.getSeq())
                        .eq("Category_Code", zhLei)
                    );
                    
                    if (zhongLei == null) {
                        throw new RRException("第" + (i + 1) + "行数据：中类编号" + zhLei + "不存在，请在分类管理里添加");
                    } else {
                        List<GoodsCategoryEntity> list = new ArrayList();
                        getChildCategoryList(list, Collections.singletonList(zhongLei));
                        categoryMap.put(daLei + "+" + zhLei, list);
                        daLeiAndZhLeiMap.put(daLei + "+" + zhLei, goodsCategoryEntity1.getSeq() + "-" + zhongLei.getSeq());
                        orderManageEntity.setDaLeiSeq(goodsCategoryEntity1.getSeq());
                        orderManageEntity.setZhLeiSeq(zhongLei.getSeq());
                    }
                    
                } else {
                    String str = daLeiAndZhLeiMap.get(daLei + "+" + zhLei);
                    String[] seqArr = str.split("-");
                    Integer daleiSeq = Integer.valueOf(seqArr[0]);
                    Integer zhleiSeq = Integer.valueOf(seqArr[1]);
                    orderManageEntity.setDaLeiSeq(daleiSeq);
                    orderManageEntity.setZhLeiSeq(zhleiSeq);
                }
                
                //年份
                char yearNum = strings[1].charAt(0);
                //季节
                char seasonNum = strings[1].charAt(1);
                String substring = strings[1].substring(2);
                //货品生产厂家给的样品时自带的货号
                /*                String regEx = "[^0-9]";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(substring);
                String huoHao = matcher.replaceAll("").trim();*/
                //颜色
                String regEx2 = "[^\u4e00-\u9fa5]";
                String colorName = substring.replaceAll(regEx2, "");
                //货号中的尾部字符代表的是系列 或风格，其中T代表特价
                String regEx3 = "[^A-z]";
                String fengge = substring.replaceAll(regEx3, "");
                
                //System.out.printf("货号： '%s',大类：'%s',中类：'%s',年份：'%s',季节：'%s',颜色：'%s',风格：'%s'\n", row.get(0), daLei, zhLei, yearNum, seasonNum, colorName, fengge);
                
                //判断风格是否存在
                List<GoodsCategoryEntity> goodsCategoryEntities = categoryMap.get(daLei + "+" + zhLei);
                boolean fenggeExist = false;
                for (GoodsCategoryEntity goodsCategoryEntity : goodsCategoryEntities) {
                    if (goodsCategoryEntity.getCatetoryCode() != null && goodsCategoryEntity.getCatetoryCode().equals(fengge)) {
                        fenggeExist = true;
                        orderManageEntity.setCategorySeq(goodsCategoryEntity.getSeq());
                        break;
                    }
                }
                if (!fenggeExist) {
                    throw new RRException("第" + (i + 1) + "行数据：风格编号" + fengge + "不存在，请在分类管理里添加");
                }
                
                //判断季节是否存在
                String seasonName = getSeasonName(seasonNum);
                if (seasonName == null) {
                    throw new RRException("第" + (i + 1) + "行数据：季节【" + seasonNum + "】不在合法范围（1、2、3、5），请检查");
                }
                boolean seasonExist = false;
                for (GoodsSeasonEntity seasonEntity : seasonList) {
                    if (seasonEntity.getSeasonName().equals(seasonName)) {
                        orderManageEntity.setSeasonSeq(seasonEntity.getSeq());
                        seasonExist = true;
                        break;
                    }
                }
                
                if (!seasonExist) {
                    throw new RRException("第" + (i + 1) + "行数据：季节【" + seasonName + "】不存在，请在季节管理里添加");
                }
                
                //颜色
                boolean colorExist = false;
                for (GoodsColorEntity goodsColorEntity : colorList) {
                    if (goodsColorEntity.getName().equals(colorName)) {
                        colorExist = true;
                        orderManageEntity.setColorSeq(goodsColorEntity.getSeq());
                        break;
                    }
                }
                if (!colorExist) {
                    throw new RRException("第" + (i + 1) + "行数据：颜色【" + colorName + "】不存在，请在颜色管理里添加");
                }
                
                //年份
                Integer year = getYear(yearNum);
                if (year == null) {
                    throw new RRException("第" + (i + 1) + "行数据：年份【" + yearNum + "】不在合法范围[0-9]，请检查");
                }
                orderManageEntity.setYear(year);
                
                saveDOs.add(orderManageEntity);
            }
        }
        
        //持久化数据
        for (OrderManageEntity manageEntity : saveDOs) {
            OrderManageEntity manageEntity1 = super.selectOne(new EntityWrapper<OrderManageEntity>()
                .eq("Company_Seq", manageEntity.getCompanySeq())
                .eq("GoodID", manageEntity.getGoodID()));
            if (manageEntity1 == null) {
                super.insert(manageEntity);
            } else {
                manageEntity.setSeq(manageEntity1.getSeq());
                manageEntity.setInputTime(new Date());
                super.updateById(manageEntity);
            }
            
        }
    }
    
    /**
     * 吉尔达货号解析规则
     * 0、1、2、3、S、5、6、7、8、9   分别代表如2010、2011、2012、2013、2014、2015、2016、2017、2018、2019年，后面循环使用
     *
     * @param yearNum
     * @return
     */
    private Integer getYear(char yearNum) {
        String yyyy = DateUtils.format(new Date(), "yyyy");
        String yearPrefix = yyyy.substring(0, 3);
        String year = null;
        switch (yearNum) {
            case '0':
                year = yearPrefix + yearNum;
                break;
            case '1':
                year = yearPrefix + yearNum;
                break;
            case '2':
                year = yearPrefix + yearNum;
                break;
            case '3':
                year = yearPrefix + yearNum;
                break;
            case 'S':
                year = yearPrefix + "4";
                break;
            case '5':
                year = yearPrefix + yearNum;
                break;
            case '6':
                year = yearPrefix + yearNum;
                break;
            case '7':
                year = yearPrefix + yearNum;
                break;
            case '8':
                year = yearPrefix + yearNum;
                break;
            case '9':
                year = yearPrefix + yearNum;
                break;
            
        }
        
        if (year != null) {
            return Integer.valueOf(year);
        }
        
        return null;
    }
    
    /**
     * 吉尔达货号规则
     * 1、2、3、5，分别代表春季、夏季、秋季、冬季
     *
     * @param seasonNum
     * @return
     */
    private String getSeasonName(char seasonNum) {
        
        String seasonName = null;
        switch (seasonNum) {
            case '1':
                seasonName = "春季";
                break;
            case '2':
                seasonName = "夏季";
                break;
            case '3':
                seasonName = "秋季";
                break;
            case '5':
                seasonName = "冬季";
                break;
            
        }
        return seasonName;
    }
    
    /**
     * 获取父级分类下的所有子分类
     *
     * @param categoryList
     * @param parentCategorys
     */
    private void getChildCategoryList(List<GoodsCategoryEntity> categoryList, List<GoodsCategoryEntity> parentCategorys) {
        for (GoodsCategoryEntity categoryEntity : parentCategorys) {
            
            List<GoodsCategoryEntity> categoryEntities = categoryService.selectList(new EntityWrapper<GoodsCategoryEntity>()
                .eq("ParentSeq", categoryEntity.getSeq()));
            if (categoryEntities != null && !categoryEntities.isEmpty()) {
                categoryList.addAll(categoryEntities);
                getChildCategoryList(categoryList, categoryEntities);
            }
            
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllExistSeasons(Integer companySeq) {
        return orderPlanManageDao.selectAllExistSeasons(companySeq);
    }
    
    @Override
    public List<Integer> getAllExistYears(Integer companySeq) {
        return orderPlanManageDao.selectAllExistYears(companySeq);
    }
}
