package io.nuite.modules.job.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.dao.ShoesInfoDao;
import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 货品相关定时任务
 */
@Component("shoesTask")
public class ShoesTask {
    @Autowired
    private ShoesInfoDao shoesInfoDao;
	

    /**
     * 鞋子定时上架
     */
    public void onSale() {
    	
    	//查询所有onsale为0，onsaleTime不为空的shoesInfo
    	Wrapper<ShoesInfoEntity> wrapper = new EntityWrapper<ShoesInfoEntity>();
    	wrapper.where("OnSale = 0 AND OnSaleTime IS NOT NULL");
    	List<ShoesInfoEntity> shoesInfoList = shoesInfoDao.selectList(wrapper);
    	
    	//判断上架时间是否在当前时间之前，如果是，上架
    	for(ShoesInfoEntity shoesInfo : shoesInfoList) {
    		if(shoesInfo.getOnSaleTime() != null && shoesInfo.getOnSaleTime().before(new Date())) {
    			shoesInfo.setOnSale(1);
    			shoesInfoDao.updateById(shoesInfo);
		    }
    	}
    }
    
    
}
