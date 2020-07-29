package io.nuite.modules.system.service.impl.order_platform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.OrderStatisticsModelDao;
import io.nuite.modules.order_platform_app.entity.OrderStatisticsModelEntity;
import io.nuite.modules.system.service.order_platform.OrderStatisticsModelService;

@Service
public class OrderStatisticsModelServiceImpl extends ServiceImpl<OrderStatisticsModelDao, OrderStatisticsModelEntity> implements OrderStatisticsModelService {

    @Autowired
    private OrderStatisticsModelDao orderStatisticsModelDao;
    

    
    /**
     * 全部模板list
     */
	@Override
	public List<OrderStatisticsModelEntity> getOrderStatisticsModel(Integer companySeq) {
        Wrapper<OrderStatisticsModelEntity> wrapper = new EntityWrapper<OrderStatisticsModelEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        return orderStatisticsModelDao.selectList(wrapper);
	}
	
	
	
    /**
     * 分页模板list
     */
    @Override
    public Page<OrderStatisticsModelEntity> getOrderStatisticsModelPage(Integer companySeq, Integer pageNum, Integer pageSize) {
        Wrapper<OrderStatisticsModelEntity> wrapper = new EntityWrapper<OrderStatisticsModelEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        Page<OrderStatisticsModelEntity> page = new Page<OrderStatisticsModelEntity>(pageNum, pageSize);
        return this.selectPage(page, wrapper);
    }


	
    /**
     * 判断模板是否已存在
     */
    @Override
    public boolean modelNameExisted(Integer seq, Integer companySeq, String modelName) {
        Wrapper<OrderStatisticsModelEntity> wrapper = new EntityWrapper<OrderStatisticsModelEntity>();
        wrapper.where("Company_Seq = {0} AND ModelName = {1}", companySeq, modelName);
        List<OrderStatisticsModelEntity> list = orderStatisticsModelDao.selectList(wrapper);
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
	public Integer addOrderStatisticsModel(OrderStatisticsModelEntity orderStatisticsModelEntity) {
		orderStatisticsModelDao.insert(orderStatisticsModelEntity);
		return orderStatisticsModelEntity.getSeq();
	}


	/**
	 * 修改模板
	 */
	@Override
	public void updateOrderStatisticsModel(OrderStatisticsModelEntity orderStatisticsModelEntity) {
		orderStatisticsModelDao.updateById(orderStatisticsModelEntity);
	}


	/**
	 * 删除模板
	 */
	@Override
	public void deleteOrderStatisticsModel(Integer seq) {
		orderStatisticsModelDao.deleteById(seq);
	}

}
