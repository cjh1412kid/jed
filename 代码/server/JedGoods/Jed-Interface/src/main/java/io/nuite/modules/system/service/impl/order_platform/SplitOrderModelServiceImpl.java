package io.nuite.modules.system.service.impl.order_platform;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.nuite.modules.sr_base.entity.GoodsBrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.SplitOrderModelDao;
import io.nuite.modules.order_platform_app.dao.SplitOrderModelDetailDao;
import io.nuite.modules.order_platform_app.entity.SplitOrderModelDetailEntity;
import io.nuite.modules.order_platform_app.entity.SplitOrderModelEntity;
import io.nuite.modules.sr_base.dao.GoodsBrandDao;
import io.nuite.modules.sr_base.dao.GoodsCategoryDao;
import io.nuite.modules.sr_base.dao.GoodsPeriodDao;
import io.nuite.modules.sr_base.dao.GoodsSXDao;
import io.nuite.modules.sr_base.dao.GoodsSXOptionDao;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import io.nuite.modules.system.service.order_platform.SplitOrderModelService;

@Service
public class SplitOrderModelServiceImpl extends ServiceImpl<SplitOrderModelDao, SplitOrderModelEntity> implements SplitOrderModelService {

    @Autowired
    private SplitOrderModelDao splitOrderModelDao;
    
    @Autowired
    private SplitOrderModelDetailDao splitOrderModelDetailDao;
    
    @Autowired
    private GoodsBrandDao goodsBrandDao;
    
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    
    @Autowired
    private GoodsPeriodDao goodsPeriodDao;
    
    @Autowired
    private GoodsSXDao goodsSXDao;
    
    @Autowired
    private GoodsSXOptionDao goodsSXOptionDao;
    
    

    
    /**
     * 全部模板list
     */
	@Override
	public List<SplitOrderModelEntity> getSplitOrderModel(Integer companySeq) {
        Wrapper<SplitOrderModelEntity> wrapper = new EntityWrapper<SplitOrderModelEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        return splitOrderModelDao.selectList(wrapper);
	}

	

	/**
	 * 默认模板
	 */
	@Override
	public SplitOrderModelEntity getDefaultSplitOrderModel(Integer companySeq) {
        SplitOrderModelEntity splitOrderModelEntity = new SplitOrderModelEntity();
        splitOrderModelEntity.setCompanySeq(companySeq);
        splitOrderModelEntity.setIsDefault(1);
        return splitOrderModelDao.selectOne(splitOrderModelEntity);
	}
	
	
    /**
     * 分页查询查询拆单模板
     */
    @Override
    public Page<SplitOrderModelEntity> getSplitOrderModelPage(Integer companySeq, Integer pageNum, Integer pageSize) {
        Wrapper<SplitOrderModelEntity> wrapper = new EntityWrapper<SplitOrderModelEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        Page<SplitOrderModelEntity> page = new Page<SplitOrderModelEntity>(pageNum, pageSize);
        return this.selectPage(page, wrapper);
    }


	
    /**
     * 判断模板是否已存在
     */
    @Override
    public boolean modelNameExisted(Integer seq, Integer companySeq, String modelName) {
        Wrapper<SplitOrderModelEntity> wrapper = new EntityWrapper<SplitOrderModelEntity>();
        wrapper.where("Company_Seq = {0} AND ModelName = {1}", companySeq, modelName);
        List<SplitOrderModelEntity> list = splitOrderModelDao.selectList(wrapper);
        if (list.size() == 0) {
            return false;
        } else if (list.size() == 1){
            if (seq != null) { //修改，判断seq是否相同
                if (list.get(0).getSeq().equals(seq)) {
                    return false;  //相同，返回false
                } else {
                    return true;
                }
            } else { //新增，返回true
                return true;
            }
        } else {
        	return true;
        }
    }



    /**
     * 新增模板
     */
	@Override
	public Integer addSplitOrderModel(SplitOrderModelEntity splitOrderModelEntity) {
		splitOrderModelDao.insert(splitOrderModelEntity);
		return splitOrderModelEntity.getSeq();
	}



	/**
	 * 设置其他模板为非默认
	 */
	@Override
	public void setIsNotDefaultExceptSeq(Integer companySeq, Integer seq) {
		SplitOrderModelEntity splitOrderModelEntity = new SplitOrderModelEntity();
		splitOrderModelEntity.setIsDefault(0);
		Wrapper<SplitOrderModelEntity> wrapper = new EntityWrapper<SplitOrderModelEntity>();
		wrapper.where("Company_Seq = {0} AND Seq != {1}", companySeq, seq);
		splitOrderModelDao.update(splitOrderModelEntity, wrapper);
	}


	/**
	 * 修改模板
	 */
	@Override
	public void updateSplitOrderModel(SplitOrderModelEntity splitOrderModelEntity) {
		splitOrderModelDao.updateById(splitOrderModelEntity);
	}


	/**
	 * 删除模板
	 */
	@Override
	public void deleteSplitOrderModel(Integer seq) {
		splitOrderModelDao.deleteById(seq);
	}


	/**
	 * 根据模板序号查询模板步骤详情
	 */
	@Override
	public List<SplitOrderModelDetailEntity> getModelDetailByModelSeq(Integer modelSeq) {
		Wrapper<SplitOrderModelDetailEntity> wrapper = new EntityWrapper<SplitOrderModelDetailEntity>();
		wrapper.where("Model_Seq = {0}", modelSeq).orderBy("Seq ASC");
		return splitOrderModelDetailDao.selectList(wrapper);
	}


	/**
	 * 封装模板步骤详情
		[
		{
		 Seq:4, 
		 Model_Seq:2, 
		 SXList: [{id:"Category_Seq", code:"2,6", name:"分类", value:"男鞋,女鞋"}, 
				  {id:"SX1", code:"001,002", name:"鞋跟高度", value:"高跟,扁平底"}, 
				  {id:"SX2", code:"002,003",name:"鞋面材质", value:"皮革,PU"}]
		}
		{
		 Seq:5, 
		 Model_Seq:2, 
		 SXList: [{id:"Category_Seq", code:"2,6", name:"分类", value:"男鞋,女鞋"}, 
		 		  {id:"SX1", code:"001,002", name:"鞋跟高度", value:"高跟,扁平底"}, 
		 		  {id:"SX2", code:"002,003",name:"鞋面材质", value:"皮革,PU"}]
		}
		]
	 */
	@Override
	public List<Map<String, Object>> getModelDetailMap(Integer companySeq, Integer modelSeq) throws Exception {
		Wrapper<SplitOrderModelDetailEntity> wrapper = new EntityWrapper<SplitOrderModelDetailEntity>();
		wrapper.where("Model_Seq = {0}", modelSeq).orderBy("Seq ASC");
		List<SplitOrderModelDetailEntity> modelDetailList = splitOrderModelDetailDao.selectList(wrapper);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(SplitOrderModelDetailEntity modelDetail : modelDetailList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("seq", modelDetail.getSeq());
			map.put("modelSeq", modelDetail.getModelSeq());
			
			List<Map<String, Object>> sxList = new ArrayList<Map<String, Object>>();
			Map<String, Object> sxMap;
			if(modelDetail.getShoesBrand() != null) {
				sxMap = new HashMap<String, Object>();
				sxMap.put("id", "ShoesBrand");
				sxMap.put("code", modelDetail.getShoesBrand());
				sxMap.put("name", "品牌");
				String[] brandSeqArr = modelDetail.getShoesBrand().split(",");
				StringBuilder brandName = new StringBuilder();
				for(String seq : brandSeqArr) {
					GoodsBrandEntity goodsBrandEntity = new GoodsBrandEntity();
					goodsBrandEntity.setBrandCode(seq);
					goodsBrandEntity.setCompanySeq(companySeq);
					goodsBrandEntity = goodsBrandDao.selectOne(goodsBrandEntity);
					brandName.append(goodsBrandEntity.getBrandName()).append(",");
				}
				sxMap.put("value", brandName.deleteCharAt(brandName.length() - 1));
				sxList.add(sxMap);
			}
			if(modelDetail.getCategorySeq() != null) {
				sxMap = new HashMap<String, Object>();
				sxMap.put("id", "Category_Seq");
				sxMap.put("code", modelDetail.getCategorySeq());
				sxMap.put("name", "大类");
				String[] categorySeqArr = modelDetail.getCategorySeq().split(",");
				StringBuilder categoryName = new StringBuilder();
				for(String seq : categorySeqArr) {
					categoryName.append(goodsCategoryDao.selectById(Integer.parseInt(seq)).getName()).append(",");
				}
				sxMap.put("value", categoryName.deleteCharAt(categoryName.length() - 1));
				sxList.add(sxMap);
			}
			if(modelDetail.getPeriodSeq() != null) {
				sxMap = new HashMap<String, Object>();
				sxMap.put("id", "Period_Seq");
				sxMap.put("code", modelDetail.getPeriodSeq());
				sxMap.put("name", "波次");
				String[] periodSeqArr = modelDetail.getPeriodSeq().split(",");
				StringBuilder periodName = new StringBuilder();
				for(String seq : periodSeqArr) {
					periodName.append(goodsPeriodDao.selectById(Integer.parseInt(seq)).getName()).append(",");
				}
				sxMap.put("value", periodName.deleteCharAt(periodName.length() - 1));
				sxList.add(sxMap);
			}
			
			GoodsSXEntity goodsSXEntity;
			GoodsSXOptionEntity goodsSXOptionEntity;
			for(int i = 1; i <= 20; i++) {
				Method method = SplitOrderModelDetailEntity.class.getMethod("getSX" + i);
				Object SXiCode = method.invoke(modelDetail);
				
				if(SXiCode != null && SXiCode.toString().length() > 0) {
					sxMap = new HashMap<String, Object>();
					sxMap.put("id", "SX" + i);
					sxMap.put("code", SXiCode.toString());
					
					//查询SX对应中文名
					goodsSXEntity = new GoodsSXEntity();
					goodsSXEntity.setCompanySeq(companySeq);
					goodsSXEntity.setSXId("SX" + i);
					goodsSXEntity = goodsSXDao.selectOne(goodsSXEntity);
					sxMap.put("name", goodsSXEntity.getSXName());
					
					String[] codeArr = SXiCode.toString().split(",");
					StringBuilder SXiOptionName = new StringBuilder();
					for(String code : codeArr) {
						//根据SXi的code查询属性可选值对应中文名
						goodsSXOptionEntity = new GoodsSXOptionEntity();
						goodsSXOptionEntity.setSXSeq(goodsSXEntity.getSeq());
						goodsSXOptionEntity.setCode(code);
						goodsSXOptionEntity = goodsSXOptionDao.selectOne(goodsSXOptionEntity);
						SXiOptionName.append(goodsSXOptionEntity.getValue()).append(",");
					}
					sxMap.put("value", SXiOptionName.deleteCharAt(SXiOptionName.length() - 1));
					sxList.add(sxMap);
				}
			}

			map.put("sxList", sxList);
			list.add(map);
			
		}
		return list;
	}



	@Override
	public Integer addSplitOrderModelDetail(SplitOrderModelDetailEntity splitOrderModelDetailEntity) {
		splitOrderModelDetailDao.insert(splitOrderModelDetailEntity);
		return splitOrderModelDetailEntity.getSeq();
	}



	@Override
	public Integer updateSplitOrderModelDetail(SplitOrderModelDetailEntity splitOrderModelDetailEntity) {
		SplitOrderModelDetailEntity oldModelDetail = splitOrderModelDetailDao.selectById(splitOrderModelDetailEntity.getSeq());
		splitOrderModelDetailEntity.setInputTime(oldModelDetail.getInputTime());
		splitOrderModelDetailEntity.setDel(0);
		splitOrderModelDetailDao.updateAllColumnById(splitOrderModelDetailEntity);
		return splitOrderModelDetailEntity.getSeq();
	}



	@Override
	public void deleteSplitOrderModelDetail(Integer seq) {
		splitOrderModelDetailDao.deleteById(seq);
	}

}
