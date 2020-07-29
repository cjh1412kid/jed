package io.nuite.modules.sr_base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.sr_base.dao.BaseAreaDao;
import io.nuite.modules.sr_base.dao.BaseRegionDao;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseRegionEntity;
import io.nuite.modules.sr_base.service.BaseRegionService;

@Service
public class BaseRegionServiceImpl extends ServiceImpl<BaseRegionDao, BaseRegionEntity> implements BaseRegionService {

    @Autowired
    BaseRegionDao baseRegionDao;

    @Override
    public List<Integer> nOcategoryList(Integer brand_Seq) {
        List<BaseRegionEntity> list=baseRegionDao.selectList(new EntityWrapper<BaseRegionEntity>().eq("Brand_Seq",brand_Seq).eq("ParentSeq", 0).eq("Del", 0));
        List<Integer> nOcategoryList=new ArrayList<>();

        if (list !=null && !list.isEmpty()){
            for (BaseRegionEntity baseRegionEntity : list){
                List<BaseRegionEntity> nextList=baseRegionDao.selectList(new EntityWrapper<BaseRegionEntity>().eq("ParentSeq", baseRegionEntity.getSeq()).eq("Del", 0));

                if (nextList !=null && !nextList.isEmpty()){
                    for (BaseRegionEntity nextBaseRegionEntity : nextList){
                        nOcategoryList.add(nextBaseRegionEntity.getSeq());
                    }
                }
            }
        }
        return nOcategoryList;
    }
}
