package io.nuite.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.dao.OrderDao;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.order_platform_app.utils.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 订单相关定时任务
 */
@Component("orderTask")
public class OrderTask {
    @Autowired
    private OrderDao orderDao;
	

    /**
     * 订单自动确认收货
     */
    public void autoConfirmReceived() {
    	//查询所有已发货的订单
    	Wrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
    	wrapper.where("OrderStatus = 5");
    	List<OrderEntity> orderList = orderDao.selectList(wrapper);
	    	
    	//判断收货时间是否在当前时间之前，如果是，状态改为已收货
    	for(OrderEntity orderEntity : orderList) {
    		if(orderEntity.getReceiveTime() != null && orderEntity.getReceiveTime().before(new Date())) {
    			orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_SIX.getValue());
    			orderDao.updateById(orderEntity);
		    }
    	}
    }
    
    
}
