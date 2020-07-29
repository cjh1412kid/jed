package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.dao.order_platform.OrderManageDao;
import io.nuite.modules.system.service.order_platform.OrderStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderStatisticsServiceImpl implements OrderStatisticsService {
	
    @Autowired
    private OrderManageDao orderManageDao;

    @Autowired
    private BaseUserDao baseUserDao;
    
    
    /**
     * 根据attachType、attachSeq获取用户Seq列表
     * @param attachType
     * @param attachSeq
     * @return
     */
    @Override
    public List<Object> getUserSeqByAttachTypeAndAttachSeq(Integer attachType, Integer attachSeq) {
        Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
        wrapper.setSqlSelect("Seq").eq("AttachType", attachType).eq("Attach_Seq", attachSeq);
        List<Object> userSeqList = baseUserDao.selectObjs(wrapper);
        return userSeqList;
    }
    

    
    /**
     * 订单统计分析列表
     */
	@Override
	public List<Map<String, Object>> orderStatisticsList(Integer companySeq, String userSeqs, Date startTime, Date endTime,
			Integer orderStatus, String keywords) {
        // 根据公司的Seq查询所有的订单
		List<Map<String, Object>> orderList = orderManageDao.orderStatisticsList(companySeq, userSeqs, startTime, endTime, orderStatus, keywords);
        return orderList;
    }


}
