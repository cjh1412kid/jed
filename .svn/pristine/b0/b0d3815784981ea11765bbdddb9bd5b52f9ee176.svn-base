package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.SievePlateDistributeDao;
import io.nuite.modules.order_platform_app.entity.SievePlateDistributeEntity;
import io.nuite.modules.order_platform_app.service.SievePlateDistributeService;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.system.dao.order_platform.GoodsAllotOrderDao;
import io.nuite.modules.system.dao.order_platform.OrderProductManagementDao;
import io.nuite.modules.system.entity.OrderManageEntity;
import io.nuite.modules.system.entity.order_platform.GoodsAllotOrderEntity;
import io.nuite.modules.system.from.GoodsAllotForm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SievePlateDistributeServiceImpl extends ServiceImpl<SievePlateDistributeDao, SievePlateDistributeEntity> implements SievePlateDistributeService {
    
    @Autowired
    private SievePlateDistributeDao sievePlateDistributeDao;
    
    @Autowired
    private OrderProductManagementDao orderProductManagementDao;
    
    @Autowired
    private GoodsAllotOrderDao goodsAllotOrderDao;
    
    @Autowired
    private BaseShopDao baseShopDao;
    
    /**
     * 判断货号是否已分配 0:未分配 1:已分配
     */
    @Override
    public Integer getIsGoodIdbeDistributed(Integer companySeq, String goodId) {
        Wrapper<SievePlateDistributeEntity> wrapper = new EntityWrapper<SievePlateDistributeEntity>();
        wrapper.setSqlSelect("Top 1 Seq")
            .where("Company_Seq = {0} AND GoodID = {1} ", companySeq, goodId);
        List<Object> list = sievePlateDistributeDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * 订货分配（某一货号，各个门店分配一定数量）
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void distributeSievePlate(OrderManageEntity orderManageEntity, String shopDistributeNum) {
        
        JSONArray jsonArray = JSONArray.fromObject(shopDistributeNum);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int shopSeq = jsonObject.getInt("shopSeq");
            int num = jsonObject.getInt("num");
            
            //新增或修改分配记录
//            Seq				int	0	0	0	-1	0	0		0	0	0	0	序号(主键)		-1			
//            Company_Seq		int	0	0	0	0	0	0		0	0	0	0	公司序号		0			
//            Year				int	0	0	0	0	0	0		0	0	0	0	年份		0			
//            Season_Seq		int	0	0	0	0	0	0		0	0	0	0	季节序号		0			
//            GoodID			varchar	50	0	-1	0	0	0		0	0	0	0	货号	Chinese_PRC_CI_AS	0			
//            Shop_Seq			int	0	0	0	0	0	0		0	0	0	0	门店序号		0			
//            distributeNum		int	0	0	-1	0	0	0		0	0	0	0	分配数量		0			
//            OrderShoes_Seq	int	0	0	0	0	0	0		0	0	0	0	订货会鞋子序号		0			
//            Category_Seq		int	0	0	-1	0	0	0		0	0	0	0	分类序号		0			
//            InputTime			datetime	0	0	-1	0	0	0	(getdate())	0	0	0	0	入库时间		0			
//            Del				int	0	0	0	0	0	0	((0))	0	0	0	0	删除标识(0:未删除,1:已删除)		0			
            
            //查询该货号该门店是否有分配过
            SievePlateDistributeEntity sievePlateDistributeEntity = new SievePlateDistributeEntity();
            sievePlateDistributeEntity.setGoodID(orderManageEntity.getGoodID());
            sievePlateDistributeEntity.setShopSeq(shopSeq);
            sievePlateDistributeEntity = sievePlateDistributeDao.selectOne(sievePlateDistributeEntity);
            if (sievePlateDistributeEntity == null) {
                sievePlateDistributeEntity = new SievePlateDistributeEntity();
                sievePlateDistributeEntity.setCompanySeq(orderManageEntity.getCompanySeq());
                sievePlateDistributeEntity.setYear(orderManageEntity.getYear());
                sievePlateDistributeEntity.setSeasonSeq(orderManageEntity.getSeasonSeq());
                sievePlateDistributeEntity.setGoodID(orderManageEntity.getGoodID());
                sievePlateDistributeEntity.setShopSeq(shopSeq);
                sievePlateDistributeEntity.setDistributeNum(num);
                sievePlateDistributeEntity.setOrderShoesSeq(orderManageEntity.getSeq());
                sievePlateDistributeEntity.setCategorySeq(orderManageEntity.getCategorySeq());
                sievePlateDistributeEntity.setInputTime(new Date());
                sievePlateDistributeEntity.setDel(0);
                sievePlateDistributeDao.insert(sievePlateDistributeEntity);
            } else {
                sievePlateDistributeEntity.setDistributeNum(num);
                sievePlateDistributeDao.updateById(sievePlateDistributeEntity);
            }
            
        }
        
    }
    
    /**
     * 查询分配详情（某货号已分配的情况）
     */
    @Override
    public List<Map<String, Object>> getSievePlateDistributedDetail(Integer companySeq, String goodId) {
        Wrapper<SievePlateDistributeEntity> wrapper = new EntityWrapper<SievePlateDistributeEntity>();
        wrapper.setSqlSelect("Shop_Seq AS shopSeq, distributeNum")
            .where("Company_Seq = {0} AND GoodID = {1} ", companySeq, goodId);
        List<Map<String, Object>> list = sievePlateDistributeDao.selectMaps(wrapper);
        return list;
    }
    
    /**
     * 查询门店某品类分配数量、款数
     */
    @Override
    public Map<String, Object> getShopDistributeNumOneCategory(Integer companySeq, Integer year, Integer seasonSeq, Integer shopSeq,
                                                               Integer categorySeq) {
        Wrapper<SievePlateDistributeEntity> wrapper = new EntityWrapper<SievePlateDistributeEntity>();
        wrapper.setSqlSelect("COUNT (1) AS distributeGoodIds, SUM (distributeNum) AS distributeNum")
        .where("Company_Seq = {0} AND Year = {1} AND Season_Seq = {2} AND Shop_Seq = {3} AND Category_Seq = {4} ", companySeq, year, seasonSeq, shopSeq, categorySeq);
        List<Map<String, Object>> list = sievePlateDistributeDao.selectMaps(wrapper);
        
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return list.get(0);
        } else {
            return null;
        }
        
    }
    
    /**
     * 重订（将门店该品类所有货号已分配的数量清空）
     */
    @Override
    public void resetDistributedSievePlate(List<Integer> sievePlateDistributeSeqList) {
        sievePlateDistributeDao.deleteBatchIds(sievePlateDistributeSeqList);
    }
    
    /**
     * 查询需要重订的筛板分配实体
     */
    @Override
    public List<SievePlateDistributeEntity> getResetDistributedSievePlateList(Integer companySeq, Integer year, Integer seasonSeq,
                                                                              Integer categorySeq, List<Integer> shopSeqs) {
        
        Wrapper<SievePlateDistributeEntity> wrapper = new EntityWrapper<SievePlateDistributeEntity>();
        wrapper.in("Shop_Seq", shopSeqs)
            .where("Company_Seq = {0} AND Year = {1} AND Season_Seq = {2} AND Category_Seq = {3} ", companySeq, year, seasonSeq, categorySeq);
        return sievePlateDistributeDao.selectList(wrapper);
    }
    
    @Override
    public List<Map<String, Object>> queryShopsByCompanySeq(Integer companySeq) {
        return sievePlateDistributeDao.selectShopsByCompanySeq(companySeq);
    }
    
    @Override
    public Page listPageByCondition(Page page, Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq) {
        //分页查询门店订单
        List<SievePlateDistributeEntity> list = sievePlateDistributeDao.listByCondition(page, companySeq, shopSeq, year, seasonSeq);
        
        for (SievePlateDistributeEntity sievePlateDistributeEntity : list) {
            //判断是否有货号配码单, 用于判断是否可以门店配码，无则不显示配码按钮
            List<GoodsAllotOrderEntity> goodsAllotOrderEntities = goodsAllotOrderDao.selectList(new EntityWrapper<GoodsAllotOrderEntity>()
                .eq("CompanySeq", companySeq)
                .eq("GoodID", sievePlateDistributeEntity.getGoodID())
                .eq("Year", year)
                .eq("SeasonSeq", seasonSeq)
                .orderBy("InputTime", false));
            if (goodsAllotOrderEntities != null && goodsAllotOrderEntities.size() > 0) {
                sievePlateDistributeEntity.setGoodsAllotSeq(goodsAllotOrderEntities.get(0).getSeq());
            }
        }
        
        page.setRecords(list);
        return page;
    }
    
    @Override
    public List<Integer> queryYearsByCompanySeq(Integer companySeq) {
        return sievePlateDistributeDao.selectYearsByCompanySeq(companySeq);
    }
    
    @Override
    public List<Map<String, Object>> querySeaSonsByCompanySeq(Integer companySeq) {
        return sievePlateDistributeDao.selectSeaSonsByCompanySeq(companySeq);
    }
    
    @Override
    public Page listPageByGoodID(Page page, Integer companySeq, Integer year, Integer seasonSeq) {
        
        //分页查询货号订单
        List<GoodsAllotForm> list = sievePlateDistributeDao.listByGoodID(page, companySeq, year, seasonSeq);
        
        for (GoodsAllotForm form : list) {
            //查询配码订单，存在则表示已配码
            List<GoodsAllotOrderEntity> goodsAllotOrderEntitys = goodsAllotOrderDao.selectList(new EntityWrapper<GoodsAllotOrderEntity>()
                .eq("CompanySeq", companySeq)
                .eq("GoodID", form.getGoodID())
                .eq("Year", year)
                .eq("SeasonSeq", seasonSeq)
                .orderBy("InputTime", false));
            if (goodsAllotOrderEntitys != null && goodsAllotOrderEntitys.size() > 0) {
                form.setAllotOrderSeq(goodsAllotOrderEntitys.get(0).getSeq());
            }
        }
        
        page.setRecords(list);
        return page;
    }
}
