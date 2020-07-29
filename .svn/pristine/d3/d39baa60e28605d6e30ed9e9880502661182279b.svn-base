package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.dao.BaseAgentDao;
import io.nuite.modules.sr_base.entity.BaseAgentEntity;
import io.nuite.modules.sr_base.service.BaseAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseAgentServiceImpl extends ServiceImpl<BaseAgentDao, BaseAgentEntity> implements BaseAgentService {

    @Autowired
    private BaseAgentDao baseAgentDao;

    @Override
    public PageUtils getLsit(Integer brandSeq, Integer page, Integer limit) {
        List<BaseAgentEntity> agentList = new ArrayList<BaseAgentEntity>();
        Integer totalCount = 0;
        if (brandSeq != null) {
            agentList = baseAgentDao.selectPage(new Page<BaseAgentEntity>(page, limit),
                    new EntityWrapper<BaseAgentEntity>().eq("Brand_Seq", brandSeq).eq("Del", 0));
            totalCount = baseAgentDao
                    .selectList(new EntityWrapper<BaseAgentEntity>().eq("Brand_Seq", brandSeq).eq("Del", 0)).size();
        }
        return new PageUtils(agentList, totalCount, limit, page);
    }

    @Override
    public BaseAgentEntity edit(Integer seq) {
        BaseAgentEntity baseAgentEntity = new BaseAgentEntity();
        baseAgentEntity.setSeq(seq);
        baseAgentEntity = baseAgentDao.selectById(baseAgentEntity);
        return baseAgentEntity;
    }

    @Override
    public Integer delete(Integer seq) {
        BaseAgentEntity baseAgentEntity = new BaseAgentEntity();
        baseAgentEntity.setSeq(seq);
        return baseAgentDao.deleteById(baseAgentEntity);
    }

    @Override
    public int save(BaseAgentEntity baseAgentEntity) {
        return baseAgentDao.insert(baseAgentEntity);
    }

    @Override
    public int update(BaseAgentEntity baseAgentEntity) {
        return baseAgentDao.updateById(baseAgentEntity);
    }

}
