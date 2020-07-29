package io.nuite.modules.system.controller;

import io.nuite.modules.system.model.CompanyBrand;
import io.nuite.modules.system.service.SystemBrandService;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/brand")
public class SystemBrandController extends AbstractController {

    @Autowired
    private SystemBrandService systemBrandService;

    @GetMapping("list")
    public R list() {
        PageUtils companyBrandPage = systemBrandService.queryBrandByUser(getUserSeq());
        return R.ok().put("page", companyBrandPage);
    }

    /**
     * 编辑时查询当前品牌
     */
    @GetMapping("edit")
    public R edit() {
        CompanyBrand companyBrand = systemBrandService.queryOneBrandByUser(getUserSeq());
        return R.ok().put("companyBrand", companyBrand);
    }

    /**
     * 添加品牌前查询当前公司
     */
    @GetMapping("company")
    public R company() {
        CompanyBrand companyBrand = systemBrandService.queryUserCompany(getUserSeq());
        return R.ok().put("companyBrand", companyBrand);
    }

    /**
     * 添加品牌
     */
    @PostMapping("save")
    public R save(CompanyBrand companyBrand) {
        if (companyBrand.getCompanySeq() == null || companyBrand.getBrandImageFile() == null
                || companyBrand.getBrandName() == null || companyBrand.getBrandName().trim().isEmpty()) {
            return R.error("更新参数错误！");
        }
        try {
            return systemBrandService.saveCompanyBrand(getUserSeq(), companyBrand);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 更新当前品牌
     */
    @PostMapping("update")
    public R update(CompanyBrand companyBrand) {
        if (companyBrand.getBrandSeq() == null || companyBrand.getCompanySeq() == null) {
            return R.error("更新参数错误！");
        }
        try {
            return systemBrandService.updateCompanyBrand(getUserSeq(), companyBrand);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
