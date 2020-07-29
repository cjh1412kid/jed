package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.FileUtils;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.dao.BaseAreaDao;
import io.nuite.modules.sr_base.dao.BaseRegionDao;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseRegionEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.service.SystemShopMmanageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class SystemShopMmanageServiceImpl implements SystemShopMmanageService {
    
    @Autowired
    private BaseShopDao baseShopDao;
    
    @Autowired
    private BaseUserDao baseUserDao;
    
    @Autowired
    private BaseAreaDao baseAreaDao;
    
    @Autowired
    private ConfigUtils configUtils;
    
    @Autowired
    private BaseRegionDao baseRegionDao;
    
    @Override
    public PageUtils queryShopByUser(BaseUserEntity user, Page page) {
        List<BaseShopEntity> shopEntities = baseShopDao.listPageByCompanySeq(page, user.getCompanySeq());
        
        //拼接门店图片的访问地址
        for (BaseShopEntity shopEntity : shopEntities) {
            if (StringUtils.isNotBlank(shopEntity.getShopImage())) {
                String imageSrc = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getShopImage() + "/" + shopEntity.getShopImage();
                shopEntity.setShopImage(imageSrc);
            }
        }
        
        page.setRecords(shopEntities);
        return new PageUtils(page);
    }
    
    @Override
    public void delete(Integer seq) {
        baseShopDao.deleteById(seq);
    }
    
    // 根据该门店的Seq返回该门店的信息
    public BaseShopEntity getShopBySeq(Integer seq) {
        
        BaseShopEntity shopEntity = baseShopDao.getShopBySeq(seq);
        if (StringUtils.isNotBlank(shopEntity.getShopImage())) {
            String imageSrc = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getShopImage() + "/" + shopEntity.getShopImage();
            shopEntity.setShopImage(imageSrc);
        }
        return shopEntity;
    }
    
    @Override
    public List<BaseAreaEntity> areaList(Long userSeq) {
        BaseUserEntity baseUser = new BaseUserEntity();
        baseUser.setSeq(userSeq.intValue());
        baseUser = baseUserDao.selectById(baseUser);
        EntityWrapper<BaseAreaEntity> entityWrapper = new EntityWrapper<BaseAreaEntity>();
        entityWrapper.eq("brand_Seq", baseUser.getBrandSeq()).eq("Del", 0);
        List<BaseAreaEntity> baseAreaEntitys = baseAreaDao.selectList(entityWrapper);
        return baseAreaEntitys;
    }
    
    @Override
    public void updateShop(BaseShopEntity baseShopEntity) {
      /*  if (baseShopEntity.getInstallDate() == null) {
            return;
        }
        */
        if (baseShopEntity.getShopImageFile() != null) {
            String shopPath = FileUtils.getWebAppsPath()
                + configUtils.getPictureBaseUploadProject() + File.separator
                + configUtils.getBaseDir() + File.separator
                + configUtils.getShopImage() + File.separator;
            
            String shopImageNewName = System.currentTimeMillis() + "_" + baseShopEntity.getShopImageFile().getOriginalFilename();
            try {
                FileUtils.upLoadFile(shopPath,
                    shopImageNewName, baseShopEntity.getShopImageFile());
            } catch (IOException e) {
                throw new RRException("图片保存失败");
            }
            baseShopEntity.setShopImage(shopImageNewName);
        }
        
        baseShopDao.updateById(baseShopEntity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveShop(BaseShopEntity baseShopEntity) {
        if (baseShopEntity.getInstallDate() == null) {
            Date date = new Date();
            baseShopEntity.setInstallDate(date);
        }
        
        if (baseShopEntity.getShopImageFile() != null) {
            String shopImageNewName = System.currentTimeMillis() + "_" + baseShopEntity.getShopImageFile().getOriginalFilename();
            
            String shopPath = FileUtils.getWebAppsPath()
                + configUtils.getPictureBaseUploadProject() + File.separator
                + configUtils.getBaseDir() + File.separator
                + configUtils.getShopImage() + File.separator;
            try {
                FileUtils.upLoadFile(shopPath,
                    shopImageNewName, baseShopEntity.getShopImageFile());
            } catch (IOException e) {
                throw new RRException("图片保存失败");
            }
            
            baseShopEntity.setShopImage(shopImageNewName);
        }
        
        baseShopDao.insert(baseShopEntity);
        
    }
    
    @Override
    public List<BaseShopEntity> selectAllList(Integer brandSeq) {
        return baseShopDao.selectAllList(brandSeq);
    }

	@Override
	public List<BaseRegionEntity> regionList(Long userSeq) {
		BaseUserEntity baseUser = new BaseUserEntity();
        baseUser.setSeq(userSeq.intValue());
        baseUser = baseUserDao.selectById(baseUser);
        EntityWrapper<BaseRegionEntity> entityWrapper = new EntityWrapper<BaseRegionEntity>();
        entityWrapper.eq("brand_Seq", baseUser.getBrandSeq()).eq("Del", 0);
        List<BaseRegionEntity> BaseRegionEntitys = baseRegionDao.selectList(entityWrapper);
        return BaseRegionEntitys;
	}
    
}