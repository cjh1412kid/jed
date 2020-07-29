package io.nuite.modules.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.job.service.ExportShoesService;
import io.nuite.modules.order_platform_app.dao.AllocateTransferFictitiousMallDao;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.AllocateTransferFictitiousMallEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 滞销款调出ServiceImpl
 * @author: jxj
 * @create: 2019-12-02 14:51
 */

@Service
public class ExportShoesServiceImpl implements ExportShoesService {

    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;

    @Autowired
    private ShoesDataDao shoesDataDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    @Autowired
    private AllocateTransferFictitiousMallDao allocateTransferFictitiousMallDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    public void exportShoes() throws Exception {
        //获取公司下全部门店
        Wrapper<BaseShopEntity> shopEntityWrapper = new EntityWrapper<>();
        shopEntityWrapper.eq("Company_Seq",companySeq);
        List<BaseShopEntity> shopEntities = baseShopDao.selectList(shopEntityWrapper);
        List<Integer> yearList = new ArrayList<>(10);
        yearList.add(2019);
        //清空虚拟商城自动调出的数据
        allocateTransferFictitiousMallDao.deleteShoesByShopSeqAndShoesSeq(new HashMap<>(10));
        for(BaseShopEntity shopEntity : shopEntities) {
            List<Integer> shopSeqList = new ArrayList<>(10);
            shopSeqList.add(shopEntity.getSeq());
            //获取滞销前10名鞋子
            Map<String,Object> map = new HashMap<>(10);
            map.put("type",1);
            map.put("shopSeqList",shopSeqList);
            map.put("shopSeq",shopEntity.getSeq());
            map.put("companySeq",companySeq);
            map.put("yearList",yearList);
            map.put("ranking",1);
            map.put("orderBy",1);
            map.put("orderDir",0);
            map.put("saleTimeStart",new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01"));
            map.put("saleTimeEnd",new Date());
            List<Map<String,Object>> allShoesList = saleShoesDetailDao.selectUnsalableShoes(map);
            //循环滞销的鞋子
            for(Map<String,Object> shoes : allShoesList) {
                //如果该鞋子存在手动调出的,则不做自动调出
                Wrapper<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntityWrapper = new EntityWrapper<>();
                allocateTransferFictitiousMallEntityWrapper.eq("Shoes_Seq",shoes.get("seq"))
                .eq("Shop_Seq",shopEntity.getSeq()).eq("TransferOutType",1);
                List<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntities = allocateTransferFictitiousMallDao.selectList(allocateTransferFictitiousMallEntityWrapper);
                if(allocateTransferFictitiousMallEntities.size() > 0) {
                    continue;
                }
                //获取该店该鞋子的库存
                Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
                shoesDataEntityWrapper.eq("Shop_Seq",shopEntity.getSeq())
                        .eq("Shoes_Seq",shoes.get("seq"));
                List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(shoesDataEntityWrapper);
                //循环库存库存,新增虚拟商城数据
                for(ShoesDataEntity shoesDataEntity : shoesDataEntityList) {
                    AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity = new AllocateTransferFictitiousMallEntity();
                    allocateTransferFictitiousMallEntity.setTransferOutType(0);
                    allocateTransferFictitiousMallEntity.setShopSeq(shoesDataEntity.getShopSeq());
                    allocateTransferFictitiousMallEntity.setShoesSeq(shoesDataEntity.getShoesSeq());
                    allocateTransferFictitiousMallEntity.setSizeSeq(shoesDataEntity.getSizeSeq());
                    allocateTransferFictitiousMallEntity.setColorSeq(shoesDataEntity.getColorSeq());
                    allocateTransferFictitiousMallEntity.setTransferOutNum(shoesDataEntity.getStock());
                    allocateTransferFictitiousMallEntity.setInputTime(new Date());
                    allocateTransferFictitiousMallEntity.setDel(0);
                    allocateTransferFictitiousMallDao.insert(allocateTransferFictitiousMallEntity);
                }
            }
            //删除数量是0的数据
            allocateTransferFictitiousMallDao.deleteShoesByNumIsZero(new HashMap<>(10));
        }
    }

    @Override
    public void updateManualShoesNum() throws Exception {
        //查询所有的手动调出的数据
        Wrapper<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntityWrapper = new EntityWrapper<>();
        allocateTransferFictitiousMallEntityWrapper.eq("TransferOutType",1);
        List<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntities = allocateTransferFictitiousMallDao.selectList(allocateTransferFictitiousMallEntityWrapper);
        //循环手动调出数据
        for(AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity : allocateTransferFictitiousMallEntities) {
            //获取库存数据
            ShoesDataEntity shoesDataEntity = new ShoesDataEntity();
            shoesDataEntity.setColorSeq(allocateTransferFictitiousMallEntity.getColorSeq());
            shoesDataEntity.setSizeSeq(allocateTransferFictitiousMallEntity.getSizeSeq());
            shoesDataEntity.setShopSeq(allocateTransferFictitiousMallEntity.getShopSeq());
            shoesDataEntity.setShoesSeq(allocateTransferFictitiousMallEntity.getShoesSeq());
            shoesDataEntity = shoesDataDao.selectOne(shoesDataEntity);
            //如果手动调出数量大于库存,用库存数量作为调出数量更新调出数据
            if(allocateTransferFictitiousMallEntity.getTransferOutNum() > shoesDataEntity.getNum()) {
                allocateTransferFictitiousMallEntity.setTransferOutNum(shoesDataEntity.getNum());
                allocateTransferFictitiousMallDao.updateById(allocateTransferFictitiousMallEntity);
            }
        }
    }

    @Override
    public void deleteManualShoesNotThisYear() throws Exception {
        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        //查询所有的手动调出的数据
        Wrapper<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntityWrapper = new EntityWrapper<>();
        allocateTransferFictitiousMallEntityWrapper.eq("TransferOutType",1);
        List<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntities = allocateTransferFictitiousMallDao.selectList(allocateTransferFictitiousMallEntityWrapper);
        //循环手动调出数据
        for(AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity : allocateTransferFictitiousMallEntities) {
            GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(allocateTransferFictitiousMallEntity.getShoesSeq());
            //删除不是当前年份的鞋子所对应的虚拟商城数据
            if (!goodsShoesEntity.getYear().equals(year)) {
                allocateTransferFictitiousMallDao.deleteShoesBySeq(allocateTransferFictitiousMallEntity.getSeq());
            }
        }
    }
}
