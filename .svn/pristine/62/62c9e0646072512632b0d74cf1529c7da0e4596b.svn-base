package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.system.dao.order_platform.GoodsAllotOrderDao;
import io.nuite.modules.system.entity.order_platform.GoodsAllotOrderEntity;
import io.nuite.modules.system.service.order_platform.GoodsAllotOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-01
 */
@Service
public class GoodsAllotOrderServiceImpl extends ServiceImpl<GoodsAllotOrderDao, GoodsAllotOrderEntity> implements GoodsAllotOrderService {
    
    /*@Autowired
    GoodsAllotOrderDao goodsAllotOrderDao;
    
    @Autowired
    GoodsAllotOrderDetailDao goodsAllotOrderDetailDao;
    
    @Autowired
    ShopAllotOrderDao shopAllotOrderDao;
    
    @Autowired
    ShopAllotOrderDetailDao shopAllotOrderDetailDao;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsAllotOrderEntity saveGoodsAllotOrder(GoodsAllotOrderEntity goodsAllotOrderEntity) {
        super.insert(goodsAllotOrderEntity);
        
        for (GoodsAllotOrderDetailEntity detail : goodsAllotOrderEntity.getDetails()) {
            detail.setOrderSeq(goodsAllotOrderEntity.getSeq());
            detail.setYu(detail.getCount()); //每个尺码的剩余量
            goodsAllotOrderDetailDao.insert(detail);
        }
        
        return goodsAllotOrderEntity;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsAllotOrderEntity updateGoodsAllotOrder(GoodsAllotOrderEntity goodsAllotOrderEntity) {
        //更新订单信息
        super.updateById(goodsAllotOrderEntity);
        //删除订单细节，重新插入
        goodsAllotOrderDetailDao.delete(new EntityWrapper<GoodsAllotOrderDetailEntity>()
            .eq("orderSeq", goodsAllotOrderEntity.getSeq()));
        for (GoodsAllotOrderDetailEntity detail : goodsAllotOrderEntity.getDetails()) {
            detail.setOrderSeq(goodsAllotOrderEntity.getSeq());
            detail.setYu(detail.getCount());
            goodsAllotOrderDetailDao.insert(detail);
        }
        return goodsAllotOrderEntity;
    }
    
    @Override
    public GoodsAllotOrderEntity queryGoodsAllotOrder(Integer companySeq, String goodID, Integer year, Integer seasonSeq) {
        return goodsAllotOrderDao.selectGoodsAllotOrder(companySeq, goodID, year, seasonSeq);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoodsAllotOrder(Integer allotOrderSeq) {
        //删除订单细节
        goodsAllotOrderDetailDao.delete(new EntityWrapper<GoodsAllotOrderDetailEntity>()
            .eq("orderSeq", allotOrderSeq));
        //删除货品配码单
        goodsAllotOrderDao.deleteById(allotOrderSeq);
    }
    
    @Override
    public List<Map<String, String>> downloadGoodsAllotExcel(Integer companySeq, Integer year, Integer seasonSeq) {
        //配码单集合
        List<GoodsAllotOrderEntity> list = goodsAllotOrderDao.downloadGoodsAllotExcel(companySeq, year, seasonSeq);
        
        List<Map<String, String>> result = new ArrayList<>();
        for (GoodsAllotOrderEntity goodsAllotOrderEntity : list) {
            Map<String, String> map = new HashMap<>();
            map.put("goodID", goodsAllotOrderEntity.getGoodID());
            map.put("total", goodsAllotOrderEntity.getTotal().toString());
            map.put("year", goodsAllotOrderEntity.getYear().toString());
            map.put("seasonName", goodsAllotOrderEntity.getSeasonName());
            map.put("categoryName", goodsAllotOrderEntity.getCategoryName());
            //尺码-数量
            for (GoodsAllotOrderDetailEntity detail : goodsAllotOrderEntity.getDetails()) {
                map.put(detail.getSize().toString(), detail.getCount().toString());
            }
            result.add(map);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Integer> selectMaxAndMinSize(Integer companySeq, Integer year, Integer seasonSeq) {
        return goodsAllotOrderDao.selectMaxAndMinSize(companySeq, year, seasonSeq);
    }
    
    @Override
    public GoodsAllotOrderEntity queryGoodsAllotOrder(Integer goodsAllotSeq) {
        GoodsAllotOrderEntity goodsAllotOrderEntity = goodsAllotOrderDao.selectGoodsAllotOrder2(goodsAllotSeq);
        //System.out.println("-------------门店配码查询货品订单----------" + goodsAllotOrderEntity.getDetails().size());
        
        return goodsAllotOrderEntity;
    }
    
    @Override
    public List<String> queryAllotShopName(Integer companySeq, String goodID, Integer year, Integer seasonSeq) {
        
        return goodsAllotOrderDao.selectAllotShopName(companySeq, goodID, year, seasonSeq);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShopAllot(Integer companySeq, String goodID, Integer year, Integer seasonSeq) {
        //查询存在的门店订单序号
        List<Integer> seqs = goodsAllotOrderDao.selectShopAllotSeq(companySeq, goodID, year, seasonSeq);
        
        //删除门店配码订单
        shopAllotOrderDao.deleteBatchIds(seqs);
        //删除门店配码订单详情
        shopAllotOrderDetailDao.delete(new EntityWrapper<ShopAllotOrderDetailEntity>().in("orderSeq",seqs));
        
        //goodsAllotOrderDao.deleteShopAllot(companySeq, goodID, year, seasonSeq);
    }*/
}
