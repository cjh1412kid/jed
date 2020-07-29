package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.base.Joiner;
import io.nuite.modules.order_platform_app.dao.PublicityPicDao;
import io.nuite.modules.order_platform_app.service.HomepageService;
import io.nuite.modules.sr_base.dao.HomeCarouselDao;
import io.nuite.modules.sr_base.entity.HomeCarouselEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HomepageServiceImpl implements HomepageService {

    @Autowired
    private PublicityPicDao publicityPicDao;

    @Autowired
    private HomeCarouselDao homeCarouselDao;

    /**
     * 根据品牌编号获取轮播图
     */
    @Override
    public List<HomeCarouselEntity> getHomeCarouselList(Integer brandSeq) {
        Wrapper<HomeCarouselEntity> wrapper = new EntityWrapper<HomeCarouselEntity>();
        wrapper.eq("Brand_Seq", brandSeq);
        return homeCarouselDao.selectList(wrapper);
    }

    /**
     * 订货爆款图片
     */
    @Override
    public List<Map<String, Object>> getHotsaleShoes(List<Integer> periodSeqList, Integer companyTypeSeq) {
        String periodSeq = Joiner.on(",").join(periodSeqList);
        List<Map<String, Object>> list = publicityPicDao.getHotsaleShoes(periodSeq, companyTypeSeq);
        return list;
    }

    /**
     * 新品推荐图片
     */
    @Override
    public List<Map<String, Object>> getNewestShoes(List<Integer> periodSeqList, Integer companyTypeSeq) {
        String periodSeq = Joiner.on(",").join(periodSeqList);
        List<Map<String, Object>> list = publicityPicDao.getNewestShoes(periodSeq, companyTypeSeq);
        return list;
    }

    /**
     * 订货爆款列表
     */
    @Override
    public List<Map<String, Object>> getHotsaleShoesList(List<Integer> periodSeqList, Integer companyTypeSeq, Integer start, Integer num) {
        String periodSeq = Joiner.on(",").join(periodSeqList);
        List<Map<String, Object>> list = publicityPicDao.getHotsaleShoesList(periodSeq, companyTypeSeq, start - 1, num);
        return list;
    }

    /**
     * 新品推荐列表
     */
    @Override
    public List<Map<String, Object>> getNewestShoesList(List<Integer> periodSeqList, Integer companyTypeSeq, Integer start, Integer num) {
        String periodSeq = Joiner.on(",").join(periodSeqList);
        List<Map<String, Object>> list = publicityPicDao.getNewestShoesList(periodSeq, companyTypeSeq, start - 1, num);
        return list;
    }

}
