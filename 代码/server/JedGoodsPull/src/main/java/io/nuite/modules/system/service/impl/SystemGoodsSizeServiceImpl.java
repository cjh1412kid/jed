package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.system.service.SystemGoodsSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemGoodsSizeServiceImpl extends ServiceImpl<GoodsSizeDao, GoodsSizeEntity> implements SystemGoodsSizeService {



}
