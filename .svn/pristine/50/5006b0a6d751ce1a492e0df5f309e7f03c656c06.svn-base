package io.nuite.modules.erp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.TransferService;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDao;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationDetailEntity;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.system.dao.order_platform.AllocateTransferFactoryDao;
import io.nuite.modules.system.entity.order_platform.AllocateTransferFactoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description: 更新调拨状态ServiceImpl
 * @author: jxj
 * @create: 2019-11-26 13:25
 */
@Service
public class TransferServiceImpl implements TransferService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Autowired
    private AllocateTransferTransferInApplicationDao allocateTransferTransferInApplicationDao;

    @Autowired
    private AllocateTransferFactoryDao allocateTransferFactoryDao;

    @Autowired
    private AllocateTransferTransferInApplicationDetailDao allocateTransferTransferInApplicationDetailDao;

    @Autowired
    private ShoesDataDailyDetailDao shoesDataDailyDetailDao;

    @Autowired
    private BaseShopDao baseShopDao;

    @Override
    public void updateTransferStatus() throws Exception {
    	long startTime = System.currentTimeMillis();
    	logger.info("updateTransferStatus 开始执行:" + startTime);
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
    	Date aMonthBeforeDate = cal.getTime();
    	
        Wrapper<BaseShopEntity> shopEntityWrapper = new EntityWrapper<>();
        shopEntityWrapper.eq("Company_Seq",companySeq).setSqlSelect("Seq");
        List<Object> shopSeqList = baseShopDao.selectObjs(shopEntityWrapper);
        //获取未处理的调拨申请
        Wrapper<AllocateTransferTransferInApplicationEntity> allocateTransferTransferInApplicationEntityWrapper = new EntityWrapper<>();
        allocateTransferTransferInApplicationEntityWrapper.gt("InputTime", aMonthBeforeDate).eq("State",0).in("InShop_Seq",shopSeqList).orderBy("InputTime",true);
        List<AllocateTransferTransferInApplicationEntity> allocateTransferTransferInApplicationEntities = allocateTransferTransferInApplicationDao.selectList(allocateTransferTransferInApplicationEntityWrapper);
        logger.info("updateTransferStatus 未处理的调拨申请:" + allocateTransferTransferInApplicationEntities.size());
        //如果没有未处理的调拨申请则结束
        if(allocateTransferTransferInApplicationEntities == null || allocateTransferTransferInApplicationEntities.size() == 0) {
            return;
        }
        //获取进ERP时间大于未处理调拨记录中最早时间的所有的进出库记录
        Wrapper<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityWrapper = new EntityWrapper<>();
        shoesDataDailyDetailEntityWrapper.setSqlSelect("ErpOrderNum AS erpOrderNum,Shop_Seq AS shopSeq,Shoes_Seq AS shoesSeq,Type AS type,Color AS color,Size AS size,Count AS count,Input_ERP_Time AS inputERPTime").eq("Company_Seq",companySeq);
        shoesDataDailyDetailEntityWrapper.gt("Input_ERP_Time",allocateTransferTransferInApplicationEntities.get(0).getInputTime());;
        List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntities = shoesDataDailyDetailDao.selectList(shoesDataDailyDetailEntityWrapper);
        logger.info("updateTransferStatus 进ERP时间大于未处理调拨记录中最早时间的所有的进出库记录:" + shoesDataDailyDetailEntities.size());
        //循环配对进出库记录和调拨记录
        for(ShoesDataDailyDetailEntity shoesDataDailyDetailEntity : shoesDataDailyDetailEntities) {
            for (AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity : allocateTransferTransferInApplicationEntities) {
                //type是1的为总仓调往门店
                if(shoesDataDailyDetailEntity.getType() == 1) {
                    //调拨入库门店和进出库记录门店一致,调拨出库门店序号为0,调拨鞋子一致,入ERP时间大于调拨时间
                    boolean isConform = allocateTransferTransferInApplicationEntity.getInShopSeq().equals(shoesDataDailyDetailEntity.getShopSeq()) && allocateTransferTransferInApplicationEntity.getOutShopSeq() == 0 && allocateTransferTransferInApplicationEntity.getShoesSeq().equals(shoesDataDailyDetailEntity.getShoesSeq()) && shoesDataDailyDetailEntity.getInputERPTime().getTime() > allocateTransferTransferInApplicationEntity.getInputTime().getTime();
                    if(!isConform) {
                        continue;
                    }
                //type是2为门店调往总仓
                }else if(shoesDataDailyDetailEntity.getType() == 2) {
                    //调拨出库库门店和进出库记录门店一致,调拨入库门店序号为0,调拨鞋子一致,入ERP时间大于调拨时间
                    boolean isConform = allocateTransferTransferInApplicationEntity.getInShopSeq().equals(shoesDataDailyDetailEntity.getShopSeq()) && allocateTransferTransferInApplicationEntity.getOutShopSeq() == 0 && allocateTransferTransferInApplicationEntity.getShoesSeq().equals(shoesDataDailyDetailEntity.getShoesSeq()) && shoesDataDailyDetailEntity.getInputERPTime().getTime() > allocateTransferTransferInApplicationEntity.getInputTime().getTime();
                    if(!isConform) {
                        continue;
                    }
                //type是3为门店调拨
                }else if(shoesDataDailyDetailEntity.getType() == 5 && shoesDataDailyDetailEntity.getCount() > 0) {
                    //调拨入库门店
                    Integer inShopSeq = shoesDataDailyDetailEntity.getShopSeq();
                    Integer outShopSeq = 0;
                    //循环寻找对应调拨出库门店
                    for(ShoesDataDailyDetailEntity shoesDataDailyDetailOther : shoesDataDailyDetailEntities) {
                        //订单号一致,数量相加为0,鞋子序号一致,调拨类型一致,颜色一致,尺码一致
                        boolean isSame = shoesDataDailyDetailEntity.getErpOrderNum().equals(shoesDataDailyDetailOther.getErpOrderNum()) && shoesDataDailyDetailEntity.getCount() + shoesDataDailyDetailOther.getCount() == 0 && shoesDataDailyDetailEntity.getShoesSeq().equals(shoesDataDailyDetailOther.getShoesSeq()) && shoesDataDailyDetailEntity.getType().equals(shoesDataDailyDetailOther.getType()) && shoesDataDailyDetailEntity.getColor().equals(shoesDataDailyDetailOther.getColor()) && shoesDataDailyDetailEntity.getSize().equals(shoesDataDailyDetailOther.getSize());
                        if(isSame) {
                            //得到出库门店
                            outShopSeq = shoesDataDailyDetailOther.getShopSeq();
                            break;
                        }
                    }
                    //调拨出库门店和进出库记录门店一致,调拨入库门店和进出库记录门店一致,调拨鞋子一致,入ERP时间大于调拨时间
                    boolean isConform = allocateTransferTransferInApplicationEntity.getInShopSeq().equals(inShopSeq) && allocateTransferTransferInApplicationEntity.getOutShopSeq().equals(outShopSeq) && allocateTransferTransferInApplicationEntity.getShoesSeq().equals(shoesDataDailyDetailEntity.getShoesSeq()) && shoesDataDailyDetailEntity.getInputERPTime().getTime() > allocateTransferTransferInApplicationEntity.getInputTime().getTime();
                    if(!isConform) {
                        continue;
                    }
                }else {
                    continue;
                }
                //后台调拨列表
                List<AllocateTransferFactoryEntity> allocateTransferFactoryEntities = new ArrayList<>(10);
                //调拨申请列表
                List<AllocateTransferTransferInApplicationDetailEntity> allocateTransferTransferInApplicationDetailEntities = new ArrayList<>(10);
                //type是1为后台调拨
                if(allocateTransferTransferInApplicationEntity.getType() == 1) {
                    Wrapper<AllocateTransferFactoryEntity> allocateTransferFactoryEntityWrapper = new EntityWrapper<>();
                    allocateTransferFactoryEntityWrapper.eq("Shoes_Seq",allocateTransferTransferInApplicationEntity.getShoesSeq()).eq("InShop_Seq",allocateTransferTransferInApplicationEntity.getInShopSeq()).eq("OutShop_Seq",allocateTransferTransferInApplicationEntity.getOutShopSeq()).eq("InputTime",allocateTransferTransferInApplicationEntity.getInputTime());
                    allocateTransferFactoryEntities = allocateTransferFactoryDao.selectList(allocateTransferFactoryEntityWrapper);
                    logger.info("updateTransferStatus type是1为后台调拨:" + allocateTransferFactoryEntities.size());
                //type是0为前端调拨
                }else if (allocateTransferTransferInApplicationEntity.getType() == 0) {
                    Wrapper<AllocateTransferTransferInApplicationDetailEntity> allocateTransferTransferInApplicationDetailEntityWrapper = new EntityWrapper<>();
                    allocateTransferTransferInApplicationDetailEntityWrapper.eq("Application_Seq",allocateTransferTransferInApplicationEntity.getSeq() );
                    allocateTransferTransferInApplicationDetailEntities = allocateTransferTransferInApplicationDetailDao.selectList(allocateTransferTransferInApplicationDetailEntityWrapper);
                    logger.info("updateTransferStatus type是0为前端调拨:" + allocateTransferTransferInApplicationDetailEntities.size());
                }
                //如果存在后台调拨记录
                if(allocateTransferFactoryEntities.size() > 0) {
                    //循环后台调拨记录
                    for (AllocateTransferFactoryEntity allocateTransferFactoryEntity : allocateTransferFactoryEntities) {
                        //与进出库记录配对尺码和数量
                        if(allocateTransferFactoryEntity.getSizeSeq().equals(shoesDataDailyDetailEntity.getSize()) && allocateTransferFactoryEntity.getNum().equals(shoesDataDailyDetailEntity.getCount())) {
                            logger.error("后台调拨记，调拨状态更新了");
                            allocateTransferTransferInApplicationEntity.setState(1);
                            allocateTransferTransferInApplicationEntity.setHandleTime(shoesDataDailyDetailEntity.getInputERPTime());
                            allocateTransferTransferInApplicationEntity.setErpOrderNum(shoesDataDailyDetailEntity.getErpOrderNum());
                            allocateTransferTransferInApplicationDao.updateById(allocateTransferTransferInApplicationEntity);
                        }
                    }
                //如果存在前端调拨记录
                }else if (allocateTransferTransferInApplicationDetailEntities.size() > 0) {
                    //循环前端调拨记录
                    for (AllocateTransferTransferInApplicationDetailEntity allocateTransferTransferInApplicationDetailEntity : allocateTransferTransferInApplicationDetailEntities) {
                        //与进出库记录配对尺码和数量
                        if(allocateTransferTransferInApplicationDetailEntity.getSizeSeq().equals(shoesDataDailyDetailEntity.getSize()) && allocateTransferTransferInApplicationDetailEntity.getNum().equals(shoesDataDailyDetailEntity.getCount())) {
                            logger.error("前端调拨，调拨状态更新了");
                            allocateTransferTransferInApplicationEntity.setState(1);
                            allocateTransferTransferInApplicationEntity.setHandleTime(shoesDataDailyDetailEntity.getInputERPTime());
                            allocateTransferTransferInApplicationEntity.setErpOrderNum(shoesDataDailyDetailEntity.getErpOrderNum());
                            allocateTransferTransferInApplicationDao.updateById(allocateTransferTransferInApplicationEntity);
                        }
                    }
                }
            }
        }
        
    	long endTime = System.currentTimeMillis();
    	logger.info("updateTransferStatus 执行完毕,"+ endTime + " 耗时:" + (startTime - endTime));
        
    }
    
    
}
