package io.nuite.modules.system.dao;

import io.nuite.modules.system.model.CompanyBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公司品牌相关查询
 */
@Mapper
public interface SystemBrandDao {
    List<CompanyBrand> queryBrandByUser(@Param(value = "userSeq") Long userSeq);

    CompanyBrand queryUserCompany(@Param(value = "userSeq") Long userSeq);

    List<Map<String, Object>> checkCompany(@Param(value = "userSeq") Long userSeq,
                                           @Param(value = "companySeq") Long companySeq);

    List<Map<String, Object>> checkBrandAndCompany(@Param(value = "userSeq") Long userSeq,
                                                   @Param(value = "brandSeq") Long brandSeq,
                                                   @Param(value = "companySeq") Long companySeq);

}
