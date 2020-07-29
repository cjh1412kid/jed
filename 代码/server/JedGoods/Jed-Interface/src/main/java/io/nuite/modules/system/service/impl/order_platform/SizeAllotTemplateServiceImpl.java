package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.system.dao.order_platform.SizeAllotDetailDao;
import io.nuite.modules.system.dao.order_platform.SizeAllotTemplateDao;
import io.nuite.modules.system.entity.order_platform.SizeAllotDetailEntity;
import io.nuite.modules.system.entity.order_platform.SizeAllotTemplateEntity;
import io.nuite.modules.system.service.order_platform.SizeAllotTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-29
 */
@Service
public class SizeAllotTemplateServiceImpl extends ServiceImpl<SizeAllotTemplateDao, SizeAllotTemplateEntity> implements SizeAllotTemplateService {
    
    @Autowired
    SizeAllotTemplateDao sizeAllotTemplateDao;
    
    @Autowired
    SizeAllotDetailDao sizeAllotDetailDao;
    
    @Override
    public List<SizeAllotTemplateEntity> listTemplateByCategorySeq(Integer categorySeq) {
        return sizeAllotTemplateDao.selectByCategorySeq(categorySeq);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SizeAllotTemplateEntity saveSizeAllotTemplate(SizeAllotTemplateEntity sizeAllotTemplateEntity) {
        //保存模版
        sizeAllotTemplateDao.insert(sizeAllotTemplateEntity);
        System.out.println("模版插入成功： " + sizeAllotTemplateEntity);
        
        //保存模版详情
        List<SizeAllotDetailEntity> details = sizeAllotTemplateEntity.getDetails();
        for (SizeAllotDetailEntity detail : details) {
            detail.setTemplateSeq(sizeAllotTemplateEntity.getSeq());
            sizeAllotDetailDao.insert(detail);
        }
        
        return sizeAllotTemplateEntity;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSizeAllotTemplate(Integer templateSeq) {
        //删除细节
        sizeAllotDetailDao.delete(new EntityWrapper<SizeAllotDetailEntity>().eq("TemplateSeq", templateSeq));
        //删除模版
        super.deleteById(templateSeq);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SizeAllotTemplateEntity updateSizeAllotTemplate(SizeAllotTemplateEntity sizeAllotTemplateEntity) {
        
        //更新模版信息
        //sizeAllotTemplateEntity.setInputTime(new Date());
        super.updateById(sizeAllotTemplateEntity);
        //更新模版细节
        List<SizeAllotDetailEntity> details = sizeAllotTemplateEntity.getDetails();
        for (SizeAllotDetailEntity detail : details) {
            sizeAllotDetailDao.update(detail, new EntityWrapper<SizeAllotDetailEntity>()
                .eq("Size", detail.getSize())
                .eq("TemplateSeq", detail.getTemplateSeq()));
        }
        
        return sizeAllotTemplateEntity;
    }
}
