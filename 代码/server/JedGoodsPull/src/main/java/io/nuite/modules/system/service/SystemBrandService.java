package io.nuite.modules.system.service;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.system.model.CompanyBrand;

public interface SystemBrandService {

    PageUtils queryBrandByUser(Long userSeq);

    CompanyBrand queryOneBrandByUser(Long userSeq);

    CompanyBrand queryUserCompany(Long userSeq);

    R saveCompanyBrand(Long userSeq, CompanyBrand companyBrand);

    R updateCompanyBrand(Long userSeq, CompanyBrand companyBrand);
}
