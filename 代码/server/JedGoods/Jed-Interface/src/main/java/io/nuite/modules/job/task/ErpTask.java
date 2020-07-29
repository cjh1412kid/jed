package io.nuite.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.job.service.ErpService;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.service.ShoesDataDailyDetailService;
import io.nuite.modules.sr_base.service.BaseShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Deprecated  /** 老ERP同步，已废弃 **/
@Component("erpTask")
public class ErpTask {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ErpService erpService;
    @Autowired
    private ShoesDataDailyDetailService shoesDataDailyDetailService;
    @Autowired
    private BaseShopService baseShopService;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    public void getDetailDataFromErp() {
        Wrapper<ShoesDataDailyDetailEntity> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("Seq AS seq,ErpOrderNum AS erpOrderNum,Shop_Seq AS shopSeq,ShopName AS shopName,Shoes_Seq AS shoesSeq,ShoesId AS shoesId,Type AS type,Color AS color,Size AS size,Count AS count,Price AS price,UpdateTime AS updateTime,Tag_Price AS tagPrice,Input_ERP_Time as inputERPTime").orderBy("Input_ERP_Time",false);
        List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntities = shoesDataDailyDetailService.selectList(wrapper);
        String lastupdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(shoesDataDailyDetailEntities.get(0).getInputERPTime());
        try {
            erpService.getErpData(null,null,lastupdate);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
    }

    public void getStockDataFromErp() {
        try {
            erpService.getShopStock();
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
    }
}
