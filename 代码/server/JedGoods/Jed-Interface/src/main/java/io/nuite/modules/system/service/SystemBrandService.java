package io.nuite.modules.system.service;

import io.nuite.modules.system.model.CompanyBrand;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;

public interface SystemBrandService {

    PageUtils queryBrandByUser(Long userSeq);

    CompanyBrand queryOneBrandByUser(Long userSeq);

    CompanyBrand queryUserCompany(Long userSeq);

    R saveCompanyBrand(Long userSeq, CompanyBrand companyBrand);

    R updateCompanyBrand(Long userSeq, CompanyBrand companyBrand);
}
