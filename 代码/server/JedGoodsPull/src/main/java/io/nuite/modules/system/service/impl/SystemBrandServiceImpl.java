package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.utils.FileUtils;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.dao.BaseBrandDao;
import io.nuite.modules.sr_base.dao.BaseCompanyDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseBrandEntity;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.dao.SystemBrandDao;
import io.nuite.modules.system.model.CompanyBrand;
import io.nuite.modules.system.service.SystemBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SystemBrandServiceImpl implements SystemBrandService {
    @Autowired
    private SystemBrandDao systemBrandDao;

    @Autowired
    private BaseBrandDao baseBrandDao;

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private BaseCompanyDao baseCompanyDao;

    @Autowired
    private ConfigUtils configUtils;

    @Override
    public PageUtils queryBrandByUser(Long userSeq) {
        List<CompanyBrand> companyBrands = systemBrandDao.queryBrandByUser(userSeq);
        for (CompanyBrand companyBrand : companyBrands) {
            String imageName = companyBrand.getBrandImage();
            if (imageName != null) {
                String imageSrc = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/"
                        + configUtils.getBaseBrand() + "/" + companyBrand.getBrandSeq() + "/" + imageName;
                companyBrand.setBrandImage(imageSrc);
            }
        }
        return new PageUtils(companyBrands, companyBrands.size(), 1, 1);
    }

    @Override
    public CompanyBrand queryOneBrandByUser(Long userSeq) {
        List<CompanyBrand> companyBrands = systemBrandDao.queryBrandByUser(userSeq);
        if (companyBrands != null && !companyBrands.isEmpty()) {
            CompanyBrand companyBrand = companyBrands.get(0);
            String imageName = companyBrand.getBrandImage();
            if (imageName != null) {
                String imageSrc = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/"
                        + configUtils.getBaseBrand() + "/" + companyBrand.getBrandSeq() + "/" + imageName;
                companyBrand.setBrandImage(imageSrc);
            }
            return companyBrand;
        }
        return null;
    }

    @Override
    public CompanyBrand queryUserCompany(Long userSeq) {
        return systemBrandDao.queryUserCompany(userSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveCompanyBrand(Long userSeq, CompanyBrand companyBrand) {
        List checkList = systemBrandDao.checkCompany(userSeq, companyBrand.getCompanySeq());
        if (checkList.isEmpty()) {
            return R.error("当前公司不存在！");
        }
        String brandImageNewName = System.currentTimeMillis() + "_" + companyBrand.getBrandImageFile().getOriginalFilename();

        BaseCompanyEntity baseCompanyEntity = new BaseCompanyEntity();
        baseCompanyEntity.setSeq(companyBrand.getCompanySeq().intValue());
        baseCompanyEntity.setName(companyBrand.getCompanyName());
        baseCompanyEntity.setRemark(companyBrand.getCompanyDescript());
        baseCompanyEntity.setAddress(companyBrand.getCompanyAddress());
        baseCompanyDao.updateById(baseCompanyEntity);

        BaseBrandEntity baseBrandEntity = new BaseBrandEntity();
        baseBrandEntity.setCompanySeq(companyBrand.getCompanySeq().intValue());
        baseBrandEntity.setName(companyBrand.getBrandName());
        baseBrandEntity.setImage(brandImageNewName);
        baseBrandDao.insert(baseBrandEntity);

        String brandPath = FileUtils.getWebAppsPath()
                + configUtils.getPictureBaseUploadProject() + File.separator
                + configUtils.getBaseDir() + File.separator
                + configUtils.getBaseBrand() + File.separator;
        try {
            FileUtils.upLoadFile(brandPath + baseBrandEntity.getSeq() + File.separator,
                    brandImageNewName, companyBrand.getBrandImageFile());
        } catch (IOException e) {
            return R.error("图片保存失败");
        }

        BaseUserEntity baseUserEntity = new BaseUserEntity();
        baseUserEntity.setBrandSeq(baseBrandEntity.getSeq());
        EntityWrapper<BaseUserEntity> ew = new EntityWrapper<BaseUserEntity>();
        ew.where("Seq={0} and Company_Seq={1}", userSeq.intValue(), companyBrand.getCompanySeq().intValue());
        baseUserDao.update(baseUserEntity, ew);
        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateCompanyBrand(Long userSeq, CompanyBrand companyBrand) {
        List checkList = systemBrandDao.checkBrandAndCompany(userSeq, companyBrand.getBrandSeq(), companyBrand.getCompanySeq());
        if (checkList.isEmpty()) {
            return R.error("更新的品牌不存在！");
        }
        String brandImageNewName = null;
        if (companyBrand.getBrandImageFile() != null) {
            String brandPath = FileUtils.getWebAppsPath()
                    + configUtils.getPictureBaseUploadProject() + File.separator
                    + configUtils.getBaseDir() + File.separator
                    + configUtils.getBaseBrand() + File.separator;

            try {
                brandImageNewName = FileUtils.upLoadFile(brandPath + companyBrand.getBrandSeq() + File.separator,
                        null, companyBrand.getBrandImageFile());
            } catch (IOException e) {
                return R.error("图片保存失败");
            }
        }

        BaseCompanyEntity baseCompanyEntity = new BaseCompanyEntity();
        baseCompanyEntity.setSeq(companyBrand.getCompanySeq().intValue());
        baseCompanyEntity.setName(companyBrand.getCompanyName());
        baseCompanyEntity.setRemark(companyBrand.getCompanyDescript());
        baseCompanyEntity.setAddress(companyBrand.getCompanyAddress());
        baseCompanyDao.updateById(baseCompanyEntity);
        BaseBrandEntity baseBrandEntity = new BaseBrandEntity();
        baseBrandEntity.setSeq(companyBrand.getBrandSeq().intValue());
        baseBrandEntity.setName(companyBrand.getBrandName());
        baseBrandEntity.setImage(brandImageNewName);
        baseBrandDao.updateById(baseBrandEntity);
        return R.ok();
    }
}
