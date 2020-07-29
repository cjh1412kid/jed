package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.ShoesCompanyTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoesCompanyTypeDao extends BaseMapper<ShoesCompanyTypeEntity> {
    void insertBatch(@Param("shoesSeq") Integer shoesSeq, @Param("list") List<Integer> list);

    void deleteByShoesSeq(@Param("seq") Integer seq);
}
