package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.SalesDao;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.order_platform_app.service.SalesService;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.system.model.ShopSalesForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-19
 */
@Service
public class SalesServiceImpl extends ServiceImpl<SalesDao, SalesEntity> implements SalesService {
    
    @Autowired
    SalesDao salesDao;
    
    @Autowired
    BaseShopDao baseShopDao;
    
    @Override
    public List<Map<String, Object>> querySalesByShopSeq(Integer shopSeq) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        List<SalesEntity> salesEntities = salesDao.listSalesByShopSeq(shopSeq);
        if (salesEntities != null && salesEntities.size() > 0) {
            for(SalesEntity salesEntity : salesEntities) {
                String idNo = salesEntity.getIdCardNo();
                if(idNo != null) {
                    salesEntity.setIdCardNo(idNo.substring(0,idNo.length() - 3) + "***");
                }
            }
            Map<String, Object> shopInfo = new HashMap<>();
            shopInfo.put("shopSeq", shopSeq);
            shopInfo.put("shopName", salesEntities.get(0).getShopName());
            shopInfo.put("sales", salesEntities);
            
            result.add(shopInfo);
        }
        
        return result;
    }
    
    @Override
    public List<ShopSalesForm> querySalesByCompanySeq(Integer companySeq, Integer shopSeq) {
        List<ShopSalesForm> shopSalesForms = salesDao.listSalesByCompanySeq(companySeq,shopSeq);
        for(ShopSalesForm shopSalesForm : shopSalesForms) {
            List<SalesEntity> salesEntities = shopSalesForm.getSales();
            for(SalesEntity salesEntity : salesEntities) {
                String idNo = salesEntity.getIdCardNo();
                if(idNo != null) {
                    salesEntity.setIdCardNo(idNo.substring(0,idNo.length() - 3) + "***");
                }
            }
        }
        return shopSalesForms;
    }

    @Override
    public boolean insertSales(SalesEntity salesEntity) throws Exception {
        SalesEntity sales = baseMapper.selectOne(salesEntity);
        if(sales == null) {
            return retBool(baseMapper.insert(salesEntity));
        }else {
            throw new RuntimeException("导购员已存在");
        }
    }
}
