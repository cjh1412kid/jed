package io.nuite.modules.erp.service.impl;

import io.nuite.modules.erp.domain.ProductDo;
import io.nuite.modules.order_platform_app.dao.ShoesInfoDao;
import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import io.nuite.modules.sr_base.dao.GoodsCategoryDao;
import io.nuite.modules.sr_base.dao.GoodsSeasonDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.BaseBrandService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class ErpGoodsSyncServiceTransactionAddition {
    
    @Value("${jrd.companySeq}")
    private Integer companySeq;
    
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    
    @Autowired
    private GoodsSeasonDao goodsSeasonDao;
    
    @Autowired
    private ShoesInfoDao shoesInfoDao;
    
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;

	@Autowired
	private BaseBrandService baseBrandService;

	@Autowired
	protected ConfigUtils configUtils;
    
    private static OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
			.readTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
			.writeTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
			.build();
    
    
	//新增或修改鞋子
    @Transactional(propagation = Propagation.REQUIRED)
	public void insertOrUpdateGoods(List<ProductDo> productDoList) {
    	for(ProductDo productDo : productDoList) {
	    	//判断货号是否已存在
			String goodsId = productDo.getName();
			Integer shoesSeq = getExistGoodsIdShoesSeq(goodsId);
			
			if(shoesSeq == null) { //货号不存在，新增
				
				//1.新增鞋子
				GoodsShoesEntity goodsShoesEntity = new GoodsShoesEntity();
				goodsShoesEntity.setCompanySeq(companySeq);
					//解析年份
				Integer year = Integer.parseInt(productDo.getDim2Attribname().substring(0, productDo.getDim2Attribname().length() - 1));
				goodsShoesEntity.setYear(year);
					//查询季节对应季节序号
				Integer seasonSeq = getSeasonSeqBySeasonName(productDo.getDim3Attribname());
				if (seasonSeq == null) {
					continue;
				}
				goodsShoesEntity.setSeasonSeq(seasonSeq);
					//解析分类：根据大类、小类、系列，组合出分类层级结构，判断此层级是否存在,如果已存在，直接获取最后一级的seq，不存在，分层判断每一个层级是否存在，不存在则创建
				Integer categorySeq = analysisCategory(productDo.getDim4Attribname(), productDo.getDim6Attribname(), productDo.getDim5Attribname(),productDo.getDim8Attribname());
				goodsShoesEntity.setCategorySeq(categorySeq);
				goodsShoesEntity.setGoodName(goodsId);
				goodsShoesEntity.setGoodID(goodsId);
				goodsShoesEntity.setColor(productDo.getColorsAlias());
				goodsShoesEntity.setInputTime(productDo.getModifiedDate());
				goodsShoesEntity.setDel(0);
				if (productDo!=null&&"鞋子".equals(productDo.getDim8Attribname())){
					//鞋子对应为类8，插入主库区分标志为0；皮具对应类为40，插入主库标志为1
					goodsShoesEntity.setGoodsType(0);
				}else if (productDo!=null&&"皮具".equals(productDo.getDim8Attribname())){
					goodsShoesEntity.setGoodsType(1);
				}
				goodsShoesDao.insert(goodsShoesEntity);
				
				
				//2.新增鞋子信息
				ShoesInfoEntity shoesInfoEntity = new ShoesInfoEntity();
				shoesInfoEntity.setShoesSeq(goodsShoesEntity.getSeq());
				shoesInfoEntity.setTagPrice1(productDo.getPriceList());
				shoesInfoEntity.setTagPrice2(productDo.getPriceList2());
				shoesInfoEntity.setTagPrice3(productDo.getPackcharge());
				shoesInfoEntity.setDealPrice1(productDo.getPreCost());
				shoesInfoEntity.setDealPrice2(productDo.getRetprecost());
				shoesInfoEntity.setDealPrice3(productDo.getPerCost());
				shoesInfoEntity.setWholesalePrice1(productDo.getSaleprice());
				shoesInfoEntity.setWholesalePrice2(productDo.getDlPrice());
				shoesInfoEntity.setWholesalePrice3(null);
				shoesInfoEntity.setDel(0);
				shoesInfoDao.insert(shoesInfoEntity);
				
			} else { //货号已存在，修改（注意要修改鞋子表InputTime，即使是修改的价格（shoesInfo表））
				
				//1.修改鞋子
				GoodsShoesEntity goodsShoesEntity = new GoodsShoesEntity();
				goodsShoesEntity.setSeq(shoesSeq);
				goodsShoesEntity.setCompanySeq(companySeq);
					//解析年份
				Integer year = Integer.parseInt(productDo.getDim2Attribname().substring(0, productDo.getDim2Attribname().length() - 1));
				goodsShoesEntity.setYear(year);
					//查询季节对应季节序号
				Integer seasonSeq = getSeasonSeqBySeasonName(productDo.getDim3Attribname());
				if (seasonSeq == null) {
					continue;
				}
				goodsShoesEntity.setSeasonSeq(seasonSeq);
					//解析分类：根据大类、小类、系列，组合出分类层级结构，判断此层级是否存在,如果已存在，直接获取最后一级的seq，不存在，分层判断每一个层级是否存在，不存在则创建
				Integer categorySeq = analysisCategory(productDo.getDim4Attribname(), productDo.getDim6Attribname(), productDo.getDim5Attribname(),productDo.getDim8Attribname());
				goodsShoesEntity.setCategorySeq(categorySeq);
				goodsShoesEntity.setGoodName(goodsId);
				goodsShoesEntity.setGoodID(goodsId);
				goodsShoesEntity.setColor(productDo.getColorsAlias());
				goodsShoesEntity.setInputTime(productDo.getModifiedDate());
				goodsShoesEntity.setDel(0);
				if (productDo!=null&&"8".equals(productDo.getDim8Attribname())){
					//鞋子对应为类8，插入主库区分标志为0；皮具对应类为40，插入主库标志为1
					goodsShoesEntity.setGoodsType(0);
				}else if (productDo!=null&&"40".equals(productDo.getDim8Attribname())){
					goodsShoesEntity.setGoodsType(1);
				}
				
				goodsShoesDao.updateById(goodsShoesEntity);
				
				//2.修改鞋子信息
				ShoesInfoEntity shoesInfoEntity = new ShoesInfoEntity();
				shoesInfoEntity.setShoesSeq(goodsShoesEntity.getSeq());
				shoesInfoEntity = shoesInfoDao.selectOne(shoesInfoEntity);
				
				shoesInfoEntity.setTagPrice1(productDo.getPriceList());
				shoesInfoEntity.setTagPrice2(productDo.getPriceList2());
				shoesInfoEntity.setTagPrice3(productDo.getPackcharge());
				shoesInfoEntity.setDealPrice1(productDo.getPreCost());
				shoesInfoEntity.setDealPrice2(productDo.getRetprecost());
				shoesInfoEntity.setDealPrice3(productDo.getPerCost());
				shoesInfoEntity.setWholesalePrice1(productDo.getSaleprice());
				shoesInfoEntity.setWholesalePrice2(productDo.getDlPrice());
				shoesInfoEntity.setWholesalePrice3(null);
				shoesInfoEntity.setDel(0);
				shoesInfoDao.updateById(shoesInfoEntity);
				
			}
	    }
	}
    
    
    
    
    
	//判断货号在我们的数据库中是否存在
	private Integer getExistGoodsIdShoesSeq(String goodsId) {
		GoodsShoesEntity goodsShoesEntity = new GoodsShoesEntity();
		goodsShoesEntity.setCompanySeq(companySeq);
		goodsShoesEntity.setGoodID(goodsId);
		goodsShoesEntity = goodsShoesDao.selectOne(goodsShoesEntity);
		if(goodsShoesEntity == null) {
			return null;
		} else {
			return goodsShoesEntity.getSeq();
		}
	}
	
	
	
	
	//季晓君2020年5月29日修改皮具分类存储规则
	//解析大类、小类、系列为我们的层级结构的分类
	private Integer analysisCategory(String dim4Attribname, String dim6Attribname, String dim5Attribname,String dim8Attribname) {
		//类如果是皮具，把类作为第一级分类
    	if ("皮具".equals(dim8Attribname)) {
			//如果没有具体类型，我们这边给他一个其他类型，保证鞋子表中所存的分类序号始终未第三级
			if(dim8Attribname == null || dim8Attribname.equals("")) {
				dim8Attribname = "其他";
			}
			if(dim4Attribname == null || dim4Attribname.equals("")) {
				dim4Attribname = "其他";
			}
			if(dim6Attribname == null || dim6Attribname.equals("")) {
				dim6Attribname = "其他";
			}

			//查询分类表根级（第一级），获取大类对应的序号
			Integer rootSeq;
			GoodsCategoryEntity rootGoodsCategoryEntity = new GoodsCategoryEntity();
			rootGoodsCategoryEntity.setParentSeq(0);
			rootGoodsCategoryEntity.setCompanySeq(companySeq);
			rootGoodsCategoryEntity.setName(dim8Attribname);
			GoodsCategoryEntity rootExistEntity = goodsCategoryDao.selectOne(rootGoodsCategoryEntity);
			if(rootExistEntity == null) {
				goodsCategoryDao.insert(rootGoodsCategoryEntity);
				rootSeq = rootGoodsCategoryEntity.getSeq();
			} else {
				rootSeq = rootExistEntity.getSeq();
			}

			//根据根级（第一级）序号 + 第二级名称，查询分类表第二级，获取小类对应的序号
			Integer firstSonSeq;
			GoodsCategoryEntity firstSonCategoryEntity = new GoodsCategoryEntity();
			firstSonCategoryEntity.setParentSeq(rootSeq);
			firstSonCategoryEntity.setCompanySeq(companySeq);
			firstSonCategoryEntity.setName(dim4Attribname);
			GoodsCategoryEntity firstSonExistEntity = goodsCategoryDao.selectOne(firstSonCategoryEntity);
			if(firstSonExistEntity == null) {
				goodsCategoryDao.insert(firstSonCategoryEntity);
				firstSonSeq = firstSonCategoryEntity.getSeq();
			} else {
				firstSonSeq = firstSonExistEntity.getSeq();
			}

			//根据第二级序号 + 第三级名称，查询分类表第三级，获取系列对应的序号
			Integer secondSonSeq;
			GoodsCategoryEntity secondSonCategoryEntity = new GoodsCategoryEntity();
			secondSonCategoryEntity.setParentSeq(firstSonSeq);
			secondSonCategoryEntity.setCompanySeq(companySeq);
			secondSonCategoryEntity.setName(dim6Attribname);
			GoodsCategoryEntity secondSonExistEntity = goodsCategoryDao.selectOne(secondSonCategoryEntity);
			if(secondSonExistEntity == null) {
				goodsCategoryDao.insert(secondSonCategoryEntity);
				secondSonSeq = secondSonCategoryEntity.getSeq();
			} else {
				secondSonSeq = secondSonExistEntity.getSeq();
			}

			return secondSonSeq;
		//否则把大类作为第一级分类
		}else {
			//如果没有具体类型，我们这边给他一个其他类型，保证鞋子表中所存的分类序号始终未第三级
			if(dim4Attribname == null || dim4Attribname.equals("")) {
				dim4Attribname = "其他";
			}
			if(dim6Attribname == null || dim6Attribname.equals("")) {
				dim6Attribname = "其他";
			}
			if(dim5Attribname == null || dim5Attribname.equals("")) {
				dim5Attribname = "其他";
			}

			//查询分类表根级（第一级），获取大类对应的序号
			Integer rootSeq;
			GoodsCategoryEntity rootGoodsCategoryEntity = new GoodsCategoryEntity();
			rootGoodsCategoryEntity.setParentSeq(0);
			rootGoodsCategoryEntity.setCompanySeq(companySeq);
			rootGoodsCategoryEntity.setName(dim4Attribname);
			GoodsCategoryEntity rootExistEntity = goodsCategoryDao.selectOne(rootGoodsCategoryEntity);
			if(rootExistEntity == null) {
				goodsCategoryDao.insert(rootGoodsCategoryEntity);
				rootSeq = rootGoodsCategoryEntity.getSeq();
			} else {
				rootSeq = rootExistEntity.getSeq();
			}

			//根据根级（第一级）序号 + 第二级名称，查询分类表第二级，获取小类对应的序号
			Integer firstSonSeq;
			GoodsCategoryEntity firstSonCategoryEntity = new GoodsCategoryEntity();
			firstSonCategoryEntity.setParentSeq(rootSeq);
			firstSonCategoryEntity.setCompanySeq(companySeq);
			firstSonCategoryEntity.setName(dim6Attribname);
			GoodsCategoryEntity firstSonExistEntity = goodsCategoryDao.selectOne(firstSonCategoryEntity);
			if(firstSonExistEntity == null) {
				goodsCategoryDao.insert(firstSonCategoryEntity);
				firstSonSeq = firstSonCategoryEntity.getSeq();
			} else {
				firstSonSeq = firstSonExistEntity.getSeq();
			}

			//根据第二级序号 + 第三级名称，查询分类表第三级，获取系列对应的序号
			Integer secondSonSeq;
			GoodsCategoryEntity secondSonCategoryEntity = new GoodsCategoryEntity();
			secondSonCategoryEntity.setParentSeq(firstSonSeq);
			secondSonCategoryEntity.setCompanySeq(companySeq);
			secondSonCategoryEntity.setName(dim5Attribname);
			GoodsCategoryEntity secondSonExistEntity = goodsCategoryDao.selectOne(secondSonCategoryEntity);
			if(secondSonExistEntity == null) {
				goodsCategoryDao.insert(secondSonCategoryEntity);
				secondSonSeq = secondSonCategoryEntity.getSeq();
			} else {
				secondSonSeq = secondSonExistEntity.getSeq();
			}

			return secondSonSeq;
		}
	}

	
	
	
	//根据季节名称获取季节序号
	private Integer getSeasonSeqBySeasonName(String dim3Attribname) {
    	if(dim3Attribname == null || "".equals(dim3Attribname)) {
    		return null;
		}
		GoodsSeasonEntity goodsSeasonEntity = new GoodsSeasonEntity();
		goodsSeasonEntity.setBrandSeq(baseBrandService.getBrandSeqByCompanySeq(companySeq));
		goodsSeasonEntity.setSeasonName(dim3Attribname);
		goodsSeasonEntity = goodsSeasonDao.selectOne(goodsSeasonEntity);
		return goodsSeasonEntity.getSeq();
	}


	public void upLoadImg(String imgUrl,String dirPath) throws Exception {
    	File dir = new File(dirPath);
    	if(!dir.exists()) {
    		dir.mkdirs();
		}
		URL url = new URL(imgUrl);
		DataInputStream dataInputStream = new DataInputStream(url.openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(new File(dir,System.currentTimeMillis()+".jpg"));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = dataInputStream.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		fileOutputStream.write(output.toByteArray());
		dataInputStream.close();
		fileOutputStream.close();
	}
}
