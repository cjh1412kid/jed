package io.nuite.modules.system.dao.order_platform;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.modules.order_platform_app.entity.OrderProductsEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsViewEntity;
import io.nuite.modules.system.model.GoodsShoesForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderProductManagementDao extends BaseMapper<OrderProductsEntity> {

	/**
	 * 货品列表
	 * @param page
	 * @param map
	 * @return
	 */
	List<GoodsViewEntity> selectGoodsList(Page<GoodsViewEntity> page, Map<String, Object> map);
	
    /**
     * 查询所有的分类
     */
    List<Integer> getCategorySeqList(@Param("categorySeq") Integer categorySeq);

    /**
     * 根据上级seq查询子类别列表
     */
    List<GoodsCategoryEntity> getCategoryByParentSeq(@Param("seq") Integer seq);

    /**
     * 根据上级seq删除信息
     */
    int delete(@Param("seq") Integer seq);

    /**
     * 根据用户seq查询该用户所有的批次
     *
     * @param userSeq
     */
    List<Integer> getPeriodSeqList(@Param("userSeq") Long userSeq);

    /**
     * 根据Seq返回鞋子的详细信息
     */
    GoodsShoesForm edit(@Param("seq") Integer seq);
    
    /**
     * 轮播图关联商品-商品查询接口
     *
     * @param page
     * @param companySeq
     * @return
     */
    List<Map<String,Object>> listGoods(Page page, @Param("companySeq") Integer companySeq);
}
