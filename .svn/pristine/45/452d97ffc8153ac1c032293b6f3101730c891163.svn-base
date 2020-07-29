package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.sr_base.service.GoodsSizeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 尺码service实现类
 * @author: jxj
 * @create: 2019-04-29 09:13
 */
@Service
public class GoodsSizeServiceImpl extends ServiceImpl<GoodsSizeDao, GoodsSizeEntity> implements GoodsSizeService {

    @Override
    public List<GoodsSizeEntity> selectGoodsSize(Integer companySeq) throws Exception {
        Wrapper<GoodsSizeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Company_Seq",companySeq).orderBy("SizeName",true);
        return baseMapper.selectList(wrapper);
    }
}
