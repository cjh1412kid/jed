package io.nuite.modules.erp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.domain.Storage1Do;
import io.nuite.modules.order_platform_app.dao.ShoesDataCopyDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataCopyEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsColorDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErpShoesDataServiceTransactionAddition {
    
    @Value("${jrd.companySeq}")
    private Integer companySeq;
    
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    
    @Autowired
    private BaseShopDao baseShopDao;
	
    @Autowired
    private GoodsColorDao goodsColorDao;
    
    @Autowired
    private GoodsSizeDao goodsSizeDao;
    
    @Autowired
    private ShoesDataDao shoesDataDao;

	@Autowired
	private ShoesDataCopyDao shoesDataCopyDao;

    
    
	//新增或修改库存
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void insertOrUpdateShoesData(List<Storage1Do> storage1DoList, Integer goodsType) throws Exception {
    	List<ShoesDataCopyEntity> shoesDataCopyEntities = new ArrayList<>(10);
    	for(Storage1Do storage1Do : storage1DoList) {
	    	//门店序号
    		Integer shopSeq = getShopSeqByShopName(storage1Do);
    		if(shopSeq == null) {
    			continue;
			}
	    	//鞋子序号
			GoodsShoesEntity goodsShoesEntity = getShoesSeqByGoodsId(storage1Do.getProductName());
    		if(goodsShoesEntity == null) {
    			continue;
			}
	    	//颜色序号
	    	Integer colorSeq = getColorSeqByColorName(storage1Do.getColorName());
	    	//尺码序号
	      	Integer sizeSeq = getSizeSeqBySizeName(storage1Do.getSizeName());


            ShoesDataCopyEntity shoesDataCopyEntity = new ShoesDataCopyEntity();
            //			Seq			int	0	0	0	-1	0	0		0	0	0	0	序号(主键)		-1
            //			Shop_Seq	int	0	0	-1	0	0	0		0	0	0	0	门店序号		0
            //			Shoes_Seq	int	0	0	0	0	0	0		0	0	0	0	鞋子序号(外键:YHSR_Goods_Shoes表)		0
            //			Color_Seq	int	0	0	0	0	0	0		0	0	0	0	颜色序号(外键:YHSR_Goods_Color表)		0
            //			Size_Seq	int	0	0	0	0	0	0		0	0	0	0	尺码		0
            //			Num			int	0	0	-1	0	0	0		0	0	0	0	总数量		0
            //			StockDate	datetime	0	0	-1	0	0	0	(getdate())	0	0	0	0	库存修改时间		0
            //			Stock		int	0	0	-1	0	0	0		0	0	0	0	库存量		0
            //			Del			int	0	0	0	0	0	0	((0))	0	0	0	0	删除标识(0:未删除,1:已删除)		0
            //			Remark		varchar	10	0	-1	0	0	0		0	0	0	0		Chinese_PRC_CI_AS	0
            shoesDataCopyEntity.setShopSeq(shopSeq);
            shoesDataCopyEntity.setCompanySeq(companySeq);
            shoesDataCopyEntity.setShoesSeq(goodsShoesEntity.getSeq());
            shoesDataCopyEntity.setColorSeq(colorSeq);
            shoesDataCopyEntity.setSizeSeq(sizeSeq);
            shoesDataCopyEntity.setNum(0);
            shoesDataCopyEntity.setStockDate(storage1Do.getModifiedDate());
            shoesDataCopyEntity.setStock(storage1Do.getqTY());
            shoesDataCopyEntity.setRemark(null);
            shoesDataCopyEntity.setDel(0);
            shoesDataCopyEntity.setGoodsType(goodsShoesEntity.getGoodsType());
            
            
            shoesDataCopyEntities.add(shoesDataCopyEntity);
			//Integer shoesDataSeq = getExistShoesDataSeq(shopSeq, shoesSeq, colorSeq, sizeSeq);
			/*if(shoesDataSeq == null) { //库存数据不存在，新增
				
				//新增库存信息

				
			} else { //库存信息已存在，修改（注意要修改StockDate）
				//修改库存信息
				ShoesDataEntity shoesDataEntity = new ShoesDataEntity();
				shoesDataEntity.setSeq(shoesDataSeq);
				shoesDataEntity.setStockDate(storage1Do.getModifiedDate());
				shoesDataEntity.setStock(storage1Do.getqTY());
				shoesDataDao.updateById(shoesDataEntity);
			}*/
    	}
    	List<ShoesDataCopyEntity> shoesDataCopyList = new ArrayList<>(10);
		for(int i = 0;i < shoesDataCopyEntities.size();i++) {
    		shoesDataCopyList.add(shoesDataCopyEntities.get(i));
    		if(shoesDataCopyList.size() == 100) {
				shoesDataCopyDao.insertShoesData(shoesDataCopyList);
				shoesDataCopyList.clear();
			}else if(i == shoesDataCopyEntities.size() - 1) {
    			shoesDataCopyDao.insertShoesData(shoesDataCopyList);
			}
		}
		shoesDataDao.deleteShoesData(companySeq,goodsType);
		shoesDataDao.selectShoesDataCopyIntoShoesData(goodsType);
	}
    
    
    //季晓君2020年5月29日修改皮具总仓库存
	//根据门店名称获取门店序号
	private Integer getShopSeqByShopName(Storage1Do storage1Do) {
		if("温州一仓".equals(storage1Do.getStoreName())) {   //总仓，门店序号填0
			return 0;
		}else if("温州调货仓".equals(storage1Do.getStoreName())) {
		    return -1;
        }else if("温州皮具仓".equals(storage1Do.getStoreName())) {
		    return 0;
        }else {
			BaseShopEntity baseShopEntity = new BaseShopEntity();
			baseShopEntity.setCompanySeq(companySeq);
			baseShopEntity.setId(storage1Do.getStoreId());
			if(baseShopDao.selectOne(baseShopEntity) != null) {
				return baseShopDao.selectOne(baseShopEntity).getSeq();
			}else {
				return null;
			}
		}
	}
	
	//根据货号获取鞋子序号
	private GoodsShoesEntity getShoesSeqByGoodsId(String goodsId) {
		GoodsShoesEntity goodsShoesEntity = new GoodsShoesEntity();
		goodsShoesEntity.setCompanySeq(companySeq);
		goodsShoesEntity.setGoodID(goodsId);
		if(goodsShoesDao.selectOne(goodsShoesEntity) == null) {
			return null;
		}
		return goodsShoesDao.selectOne(goodsShoesEntity);
	}
	
	//根据颜色名称获取颜色序号
	private Integer getColorSeqByColorName(String colorName) {
		Wrapper<GoodsColorEntity> wrapper = new EntityWrapper<GoodsColorEntity>();
		wrapper.where("Company_Seq = {0} AND Name = {1}", companySeq, colorName);
//		GoodsColorEntity goodsColorEntity = new GoodsColorEntity();
//		goodsColorEntity.setCompanySeq(companySeq);
//		goodsColorEntity.setName(colorName);
		GoodsColorEntity goodsColorEntity = goodsColorDao.selectList(wrapper).get(0);
		return goodsColorDao.selectOne(goodsColorEntity).getSeq();
	}
	
	//根据尺码名称获取尺码序号
	private Integer getSizeSeqBySizeName(String sizeName) {
		GoodsSizeEntity goodsSizeEntity = new GoodsSizeEntity();
		goodsSizeEntity.setCompanySeq(companySeq);
		goodsSizeEntity.setSizeName(sizeName);
		return goodsSizeDao.selectOne(goodsSizeEntity).getSeq();
	}
	

	//判断库存数据在我们的数据库中是否存在
	private Integer getExistShoesDataSeq(Integer shopSeq, Integer shoesSeq, Integer colorSeq, Integer sizeSeq) {
		ShoesDataEntity shoesDataEntity = new ShoesDataEntity();
		shoesDataEntity.setShopSeq(shopSeq);
		shoesDataEntity.setShoesSeq(shoesSeq);
		shoesDataEntity.setColorSeq(colorSeq);
		shoesDataEntity.setSizeSeq(sizeSeq);
		shoesDataEntity.setCompanySeq(companySeq);
		shoesDataEntity = shoesDataDao.selectOne(shoesDataEntity);
		if(shoesDataEntity == null) {
			return null;
		} else {
			return shoesDataEntity.getSeq();
		}
	}
    
}
