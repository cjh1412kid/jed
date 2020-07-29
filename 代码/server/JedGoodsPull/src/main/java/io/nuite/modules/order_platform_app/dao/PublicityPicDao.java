package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.PublicityPicEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface PublicityPicDao extends BaseMapper<PublicityPicEntity> {
	
	//根据 品牌编号 查询波次编号列表
	List<Integer> getPeriodListByBrandSeq(Integer brandSeq);
	
	//3个爆款鞋子的图片，按是否标记为爆款+总销量降序
	List<Map<String, Object>> getHotsaleShoes(@Param("periodSeq") String periodSeq,
                                              @Param("companyTypeSeq") Integer companyTypeSeq);

	//5个新品鞋子，按是否标记为新品 or 上架时间7天内 + 时间降序
	List<Map<String, Object>> getNewestShoes(@Param("periodSeq") String periodSeq,
                                             @Param("companyTypeSeq") Integer companyTypeSeq);


	//爆款鞋子列表
	List<Map<String, Object>> getHotsaleShoesList(@Param("periodSeq") String periodSeq,
                                                  @Param("companyTypeSeq") Integer companyTypeSeq,
                                                  @Param("start") Integer start,
                                                  @Param("num") Integer num);

	//新品鞋子列表
	List<Map<String, Object>> getNewestShoesList(@Param("periodSeq") String periodSeq,
                                                 @Param("companyTypeSeq") Integer companyTypeSeq,
                                                 @Param("start") Integer start,
                                                 @Param("num") Integer num);
	
}
