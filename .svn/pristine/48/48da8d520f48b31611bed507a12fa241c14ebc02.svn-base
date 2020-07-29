package io.nuite.modules.system.service.impl.homePageManagement;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;

import io.nuite.common.utils.FileUtils;
import io.nuite.common.utils.MapUtils;
import io.nuite.common.utils.PageInfo;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.service.GoodsPeriodService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.dao.home_page_management.SystemSowingMapDao;
import io.nuite.modules.system.entity.home_page_management.SystemSowingMapEntity;
import io.nuite.modules.system.service.home_page_management.SystemSowingMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SystemSowingMapServiceImpl extends ServiceImpl<SystemSowingMapDao, SystemSowingMapEntity> implements SystemSowingMapService {

    @Autowired
    private SystemSowingMapDao systemSowingMapDao;
    @Autowired
    private ConfigUtils configUtils;
    @Autowired
    private GoodsPeriodService goodsPeriodService;

    @Override
    public PageUtils sowingMapList(Integer brandSeq) {
        List<SystemSowingMapEntity> sowingMapList;

        EntityWrapper<SystemSowingMapEntity> ew = new EntityWrapper<SystemSowingMapEntity>();
        ew.eq("Brand_Seq", brandSeq).eq("Del", 0);
        sowingMapList = systemSowingMapDao.selectList(ew);
        for (SystemSowingMapEntity systemSowingMapEntity : sowingMapList) {
            String imageSrc = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/"
                    + configUtils.getSowingMap() + "/" + systemSowingMapEntity.getBrandSeq() + "/"
                    + systemSowingMapEntity.getImage();
            systemSowingMapEntity.setImage(imageSrc);
        }
        return new PageUtils(sowingMapList, sowingMapList.size(), 1, 1);
    }

    @Override
    public PageUtils goodList(Integer brandSeq, PageInfo pageInfo) {
        if (brandSeq == null) {
            return new PageUtils(Collections.emptyList(), 0, 1, 1);
        }
        List<Integer> periodSeqList = new ArrayList<Integer>();
        List<GoodsPeriodEntity> goodsPeriodList = goodsPeriodService.selectByMap(new MapUtils().put("Brand_Seq", brandSeq).put("Del", 0));
        for (GoodsPeriodEntity goodsPeriodEntity : goodsPeriodList) {
            if (goodsPeriodEntity != null) {
                periodSeqList.add(goodsPeriodEntity.getSeq());
            }
        }

        if (periodSeqList.isEmpty()) {
            return new PageUtils(Collections.emptyList(), 0, 1, 1);
        }

        Page<Map<String, Object>> page = new Page<>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setAsc("asc".equalsIgnoreCase(pageInfo.getOrder()));
        page.setOrderByField(pageInfo.getSort());
        pageInfo.getCondition().put("periodSeq", Joiner.on(",").join(periodSeqList));
        List<Map<String, Object>> list = systemSowingMapDao.selectGoodsList(page, pageInfo.getCondition());
        for (Map<String, Object> maps : list) {
            String goodId = (String) maps.get("goodID");
            String img1 = (String) maps.get("img1");
            String imageUrl = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getGoodsShoes()
                    + "/" + goodId + "/" + img1;
            maps.replace("img1", imageUrl);

        }
        return new PageUtils(list, page.getTotal(), pageInfo.getSize(), pageInfo.getNowpage());
    }

    @Override
    public SystemSowingMapEntity edit(Integer seq) {
        SystemSowingMapEntity systemSowingMapEntity = new SystemSowingMapEntity();
        if (seq != null) {
            systemSowingMapEntity.setSeq(seq);
            systemSowingMapEntity = systemSowingMapDao.selectById(systemSowingMapEntity);
            String imageSrc = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/"
                    + configUtils.getSowingMap() + "/" + systemSowingMapEntity.getBrandSeq() + "/"
                    + systemSowingMapEntity.getImage();
            systemSowingMapEntity.setImage(imageSrc);
        }
        return systemSowingMapEntity;
    }

    @Override
    public Integer getCreatedNumber(Integer brandSeq) {
        List<SystemSowingMapEntity> sowingMapList;
        EntityWrapper<SystemSowingMapEntity> ew = new EntityWrapper<SystemSowingMapEntity>();
        ew.eq("Brand_Seq", brandSeq).eq("Del", 0);
        sowingMapList = systemSowingMapDao.selectList(ew);

        return sowingMapList.size();
    }

    @Override
    public void save(Integer brandSeq, SystemSowingMapEntity systemSowingMapEntity) throws IOException {
        systemSowingMapEntity.setBrandSeq(brandSeq);

        String imageGoodsDir = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + File.separator
                + configUtils.getBaseDir() + File.separator + configUtils.getSowingMap() + File.separator
                + brandSeq + File.separator;

        MultipartFile image = systemSowingMapEntity.getImageFile();
        if (image == null || image.getSize() <= 0) {
            return;
        } else {
            String fileName = FileUtils.upLoadFile(imageGoodsDir, null, image);
            systemSowingMapEntity.setImage(fileName);
        }
        systemSowingMapDao.insert(systemSowingMapEntity);
    }

    @Override
    public void update(Integer brandSeq, SystemSowingMapEntity systemSowingMapEntity) throws IOException {
        if (systemSowingMapEntity.getSeq() != null) {
            systemSowingMapEntity.setBrandSeq(brandSeq);

            if (systemSowingMapEntity.getImageFile() != null) {

                String imageGoodsDir = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject()
                        + File.separator + configUtils.getBaseDir() + File.separator + configUtils.getSowingMap()
                        + File.separator + brandSeq + File.separator;

                MultipartFile imageFile = systemSowingMapEntity.getImageFile();
                if (imageFile == null || imageFile.getSize() <= 0) {
                    return;
                } else {
                    String fileName = FileUtils.upLoadFile(imageGoodsDir, null, imageFile);
                    systemSowingMapEntity.setImage(fileName);
                }
            }
            systemSowingMapDao.updateById(systemSowingMapEntity);
        }

    }

    /**
     * 波次列表
     */
	@Override
	public PageUtils periodList(Integer brandSeq, Map<String, Object> params) {
        Page<GoodsPeriodEntity> nowPage = new Page<GoodsPeriodEntity>(Integer.parseInt((String)params.get("page")), Integer.parseInt((String)params.get("limit")));
        nowPage.setAsc("asc".equalsIgnoreCase((String) params.get("order")));
        nowPage.setOrderByField((String) params.get("sidx"));
        Wrapper<GoodsPeriodEntity> wrapper = new EntityWrapper<GoodsPeriodEntity>();
		wrapper.where("Brand_Seq = {0}", brandSeq);
        Page<GoodsPeriodEntity> page = goodsPeriodService.selectPage(nowPage, wrapper);
        return new PageUtils(page);
    }
}
