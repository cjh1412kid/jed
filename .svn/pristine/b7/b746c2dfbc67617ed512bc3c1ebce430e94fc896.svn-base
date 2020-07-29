package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsCategoryDao;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryDao, GoodsCategoryEntity> implements GoodsCategoryService {

    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    
	/**
	 * 根据seq获取GoodsView对象
	 */
	@Override
	public GoodsCategoryEntity getGoodsCategoryBySeq(Integer categorySeq) {
		return goodsCategoryDao.selectById(categorySeq);
	}

	
	
	/**
	 * 获取属于该分类序号的最后一级分类序号List
	 */
	@Override
	public List<Integer> getFinalCategorySeqList(Integer categorySeq) {

		List<Integer> list = new ArrayList<Integer>();

		// 有下级分类
		if (hasSonCategory(categorySeq)) {

			// 查询下级分类序号List
			List<Integer> sonSeqList = getSonCategorySeqList(categorySeq);
			for (Integer sonSeq : sonSeqList) {
				List<Integer> l = getFinalCategorySeqList(sonSeq);
				list.addAll(l);
			}

		} else { // 没有下级分类，自己就是最后一级
			list.add(categorySeq);
		}
		return list;

	}
	
	
	
	//判断是否有下级分类
	private boolean hasSonCategory(Integer categorySeq) {
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
        wrapper.where("ParentSeq = {0}", categorySeq);
        Integer count = goodsCategoryDao.selectCount(wrapper);
        if(count > 0) {
        	return true;
        } else {
        	return false;
        }
	}
	
	//获取下级分类序号列表
	private List<Integer> getSonCategorySeqList(Integer categorySeq) {
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
        wrapper.where("ParentSeq = {0}", categorySeq);
        List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryDao.selectList(wrapper);
        
        List<Integer> list = new ArrayList<Integer>();
        for(GoodsCategoryEntity goodsCategoryEntity : goodsCategoryList) {
        	list.add(goodsCategoryEntity.getSeq());
        }
        
		return list;
	}



	/**
	 * 根据分类名称模糊查询所有分类
	 */
	@Override
	public List<GoodsCategoryEntity> getCategoryByFuzzyName(Integer companySeq, String param) {
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
        wrapper.where("Company_Seq = {0}", companySeq).like("Name", param);
        List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryDao.selectList(wrapper);
		return goodsCategoryList;
	}



	@Override
	public List<Integer> getCategorySeqList(Integer categorySeq) {
		  Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
	        wrapper.where("ParentSeq = {0}", categorySeq);
	        List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryDao.selectList(wrapper);
	        
	        List<Integer> list = new ArrayList<Integer>();
	        for(GoodsCategoryEntity goodsCategoryEntity : goodsCategoryList) {
	        	list.add(goodsCategoryEntity.getSeq());
	        }
		return list;
	}



	/**
	 * 获取所有二级分类
	 */
	@Override
	public List<GoodsCategoryEntity> getSecondCategoryList(Integer companySeq) {
		
		Wrapper<GoodsCategoryEntity> wrapper1 = new EntityWrapper<GoodsCategoryEntity>();
		wrapper1.setSqlSelect("Seq");
        wrapper1.where("ParentSeq = 0 AND Company_Seq = {0}", companySeq);
		List<Object> firstCategorySeqs = goodsCategoryDao.selectObjs(wrapper1);
		
		Wrapper<GoodsCategoryEntity> wrapper2 = new EntityWrapper<GoodsCategoryEntity>();
        wrapper2.in("ParentSeq", firstCategorySeqs);
		return goodsCategoryDao.selectList(wrapper2);
		
	}
}
