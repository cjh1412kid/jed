package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.common.exception.RRException;
import io.nuite.modules.system.dao.order_platform.GoodsAllotOrderDetailDao;
import io.nuite.modules.system.dao.order_platform.ShopAllotOrderDao;
import io.nuite.modules.system.dao.order_platform.ShopAllotOrderDetailDao;
import io.nuite.modules.system.entity.order_platform.GoodsAllotOrderDetailEntity;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderDetailEntity;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderEntity;
import io.nuite.modules.system.service.order_platform.ShopAllotOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-03
 */
@Service
public class ShopAllotOrderServiceImpl extends ServiceImpl<ShopAllotOrderDao, ShopAllotOrderEntity> implements ShopAllotOrderService {
    
    @Autowired
    ShopAllotOrderDetailDao shopAllotOrderDetailDao;
    
    @Autowired
    ShopAllotOrderDao shopAllotOrderDao;
    
    @Autowired
    GoodsAllotOrderDetailDao goodsAllotOrderDetailDao;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAllotOrderEntity saveShopAllotOrder(ShopAllotOrderEntity shopAllotOrderEntity, Integer goodsAllotSeq) {
        
        //判断是否已存在存在门店配码订单
        ShopAllotOrderEntity orderEntity = super.selectOne(new EntityWrapper<ShopAllotOrderEntity>()
            .eq("SilevePlateDistributeSeq", shopAllotOrderEntity.getSilevePlateDistributeSeq()));
        
        if (orderEntity == null) {
            //创建门店配码订单
            super.insert(shopAllotOrderEntity);
            
            //创建门店配码订单细节
            for (ShopAllotOrderDetailEntity detail : shopAllotOrderEntity.getDetails()) {
                
                GoodsAllotOrderDetailEntity allotOrderDetailEntity = new GoodsAllotOrderDetailEntity();
                allotOrderDetailEntity.setOrderSeq(goodsAllotSeq);
                allotOrderDetailEntity.setSize(detail.getSize());
                GoodsAllotOrderDetailEntity goodsAllotOrderDetailEntity = goodsAllotOrderDetailDao.selectOne(allotOrderDetailEntity);
                Integer cha = goodsAllotOrderDetailEntity.getYu() - detail.getCount();
                if (cha < 0) {
                    throw new RRException("尺码" + detail.getSize() + "库存不足");
                }
                goodsAllotOrderDetailEntity.setYu(cha);
                goodsAllotOrderDetailDao.update(goodsAllotOrderDetailEntity, new EntityWrapper<GoodsAllotOrderDetailEntity>()
                    .eq("OrderSeq", goodsAllotOrderDetailEntity.getOrderSeq())
                    .eq("Size", goodsAllotOrderDetailEntity.getSize()));
                
                detail.setOrderSeq(shopAllotOrderEntity.getSeq());
                shopAllotOrderDetailDao.insert(detail);
                
            }
        } else {
            //更新门店配码订单
            shopAllotOrderEntity.setSeq(orderEntity.getSeq());
            super.updateById(shopAllotOrderEntity);
            
            //先退还重新分配
            for (ShopAllotOrderDetailEntity detail : shopAllotOrderEntity.getDetails()) {
                //查询门店分配尺码细节
                ShopAllotOrderDetailEntity detailEntity = new ShopAllotOrderDetailEntity();
                detailEntity.setOrderSeq(shopAllotOrderEntity.getSeq());
                detailEntity.setSize(detail.getSize());
                ShopAllotOrderDetailEntity shopAllotOrderDetailEntity = shopAllotOrderDetailDao.selectOne(detailEntity);
                //查询货品尺码细节
                GoodsAllotOrderDetailEntity allotOrderDetailEntity = new GoodsAllotOrderDetailEntity();
                allotOrderDetailEntity.setOrderSeq(goodsAllotSeq);
                allotOrderDetailEntity.setSize(detail.getSize());
                GoodsAllotOrderDetailEntity goodsAllotOrderDetailEntity = goodsAllotOrderDetailDao.selectOne(allotOrderDetailEntity);
                
                Integer cha = goodsAllotOrderDetailEntity.getYu() + shopAllotOrderDetailEntity.getCount() - detail.getCount();
                if (cha < 0) {
                    throw new RRException("尺码" + detail.getSize() + "库存不足");
                }
                //更新货号剩余量
                goodsAllotOrderDetailEntity.setYu(cha);
                goodsAllotOrderDetailDao.update(goodsAllotOrderDetailEntity, new EntityWrapper<GoodsAllotOrderDetailEntity>()
                    .eq("OrderSeq", goodsAllotOrderDetailEntity.getOrderSeq())
                    .eq("Size", goodsAllotOrderDetailEntity.getSize()));
                
                //更新门店配码尺码数量
                detail.setOrderSeq(orderEntity.getSeq());
                shopAllotOrderDetailDao.update(detail, new EntityWrapper<ShopAllotOrderDetailEntity>()
                    .eq("OrderSeq", orderEntity.getSeq())
                    .eq("Size", detail.getSize()));
                
            }
        }
        
        return shopAllotOrderEntity;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShopAllotOrder(Integer orderSeq,Integer goodAllotSeq) {
        List<ShopAllotOrderDetailEntity> shopAllotOrderDetailEntities = shopAllotOrderDetailDao.selectList(new EntityWrapper<ShopAllotOrderDetailEntity>()
            .eq("OrderSeq", orderSeq));
    
        //退还门店货品分配额
        for (ShopAllotOrderDetailEntity shopAllotOrderDetailEntity : shopAllotOrderDetailEntities) {
            GoodsAllotOrderDetailEntity goodsAllotOrderDetailEntity = new GoodsAllotOrderDetailEntity();
            goodsAllotOrderDetailEntity.setOrderSeq(goodAllotSeq);
            goodsAllotOrderDetailEntity.setSize(shopAllotOrderDetailEntity.getSize());
            goodsAllotOrderDetailEntity = goodsAllotOrderDetailDao.selectOne(goodsAllotOrderDetailEntity);
    
            goodsAllotOrderDetailEntity.setYu(goodsAllotOrderDetailEntity.getYu()+shopAllotOrderDetailEntity.getCount());
    
            goodsAllotOrderDetailDao.update(goodsAllotOrderDetailEntity,new EntityWrapper<GoodsAllotOrderDetailEntity>()
                .eq("OrderSeq",goodAllotSeq)
                .eq("Size",shopAllotOrderDetailEntity.getSize()));
        }
        
        //删除门店配码详细和订单
        shopAllotOrderDetailDao.delete(new EntityWrapper<ShopAllotOrderDetailEntity>()
            .eq("OrderSeq", orderSeq));
        super.deleteById(orderSeq);
    }
    
    @Override
    public List<Map<String, String>> downloadShopAllotExcel(Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq) {
        //配码单集合
        List<Map<String, String>> list = shopAllotOrderDao.downloadShopAllotExcel(companySeq, shopSeq, year, seasonSeq);
        
        for (Map<String, String> map : list) {
            List<ShopAllotOrderDetailEntity> detailEntities = shopAllotOrderDetailDao.selectList(new EntityWrapper<ShopAllotOrderDetailEntity>()
                .eq("OrderSeq", map.get("seq")));
            for (ShopAllotOrderDetailEntity detailEntity : detailEntities) {
                map.put(detailEntity.getSize().toString(), detailEntity.getCount().toString());
            }
        }
        return list;
    }
    
    @Override
    public Map<String, Integer> selectMaxAndMinSize(Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq) {
        
        return shopAllotOrderDao.selectMaxAndMinSize(companySeq, shopSeq, year, seasonSeq);
    }
    
    @Override
    public ShopAllotOrderEntity getShopAllot(Integer shopAllotOrderSeq) {
        
        return shopAllotOrderDao.getShopAllot(shopAllotOrderSeq);
    }
    
    /**
     * 根据筛板分配序号获取门店配码实体
     */
    @Override
    public ShopAllotOrderEntity getShopAllotBySilevePlateDistributeSeq(Integer silevePlateDistributeSeq) {
        ShopAllotOrderEntity shopAllotOrderEntity = new ShopAllotOrderEntity();
        shopAllotOrderEntity.setSilevePlateDistributeSeq(silevePlateDistributeSeq);
        shopAllotOrderEntity.setDel(0);
        return shopAllotOrderDao.selectOne(shopAllotOrderEntity);
    }
}
