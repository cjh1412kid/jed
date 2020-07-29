package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.entity.UEditorContentEntity;
import io.nuite.modules.system.dao.order_platform.UEditorDao;
import io.nuite.modules.system.service.order_platform.UEditorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/7/17 14:27
 * @Version: 1.0
 * @Description:
 */

@Service
public class UEditorServiceImpl extends ServiceImpl<UEditorDao, UEditorContentEntity> implements UEditorService {
    
    @Override
    public List<UEditorContentEntity> getList(Integer companySeq) {
        
        EntityWrapper<UEditorContentEntity> wrapper = new EntityWrapper();
        wrapper.eq("Company_Seq", companySeq).eq("Del", 0);
        
        return super.selectList(wrapper);
    }
    
    @Override
    public List<UEditorContentEntity> listByCompanySeqAndUType(Integer companySeq, String uType) {
        List<UEditorContentEntity> uEditorContentEntities = super.selectList(new EntityWrapper<UEditorContentEntity>()
            .eq("Company_Seq", companySeq)
            .eq("UType", uType));
        
        return uEditorContentEntities;
    }
    
    @Override
    public UEditorContentEntity getById(Integer seq) {
        
        return super.selectById(seq);
    }
    
    @Override
    public UEditorContentEntity getByCompanySeqAndUsedAndUType(Integer companySeq, Integer usedFlag, String uType) {
        
        EntityWrapper<UEditorContentEntity> wrapper = new EntityWrapper();
        wrapper.eq("Company_Seq", companySeq)
            .eq("Used", usedFlag)
            .eq("UType", uType);
        
        return super.selectOne(wrapper);
    }
    
    /**
     * 保存用户编辑的内容
     *
     * @param ur
     */
    @Override
    public Boolean saveOrUpdate(UEditorContentEntity ur) {
        
        ur.setUsed(0);
        ur.setDel(0);
        ur.setInputTime(new Date());
        return super.insert(ur);
    }
    
    /**
     * 根据模版id删除记录
     *
     * @param seq
     */
    @Override
    public Boolean delete(Integer seq) {
        return super.deleteById(seq);
    }
    
    /**
     * 根据id更新用户信息
     *
     * @param ur
     * @return
     */
    @Override
    public Boolean update(UEditorContentEntity ur) {
        
        ur.setInputTime(new Date());
        return super.updateById(ur);
    }
    
    /**
     * 根据companySeq查询所有记录used 置为0，传入的 置为1
     *
     * @param ur
     * @return
     */
    @Transactional
    @Override
    public void setUsed(UEditorContentEntity ur) {
        
        EntityWrapper<UEditorContentEntity> ew = new EntityWrapper<>();
        ew.eq("Company_Seq", ur.getCompanySeq());
        ew.eq("Used", 1);
        ew.eq("UType", ur.getUType());
        List<UEditorContentEntity> urs = super.selectList(ew);
        
        for (UEditorContentEntity ue : urs) {
            ue.setUsed(0);
            super.updateById(ue);
        }
        ur.setUsed(1);
        super.updateById(ur);
        
    }
    
    /**
     * 取消用户的主模版设置
     *
     * @param ur
     * @return
     */
    @Override
    public void cancelUsed(UEditorContentEntity ur) {
        ur.setUsed(0);
        super.updateById(ur);
    }
    
    @Override
    public UEditorContentEntity getDefaultContent(Integer companySeq) {
        return getByCompanySeqAndUsedAndUType(companySeq, 1, "1");
    }
    
    @Override
    public UEditorContentEntity getHomepageContent(Integer companySeq) {
        return getByCompanySeqAndUsedAndUType(companySeq, 1, "0");
    }
    
    @Override
    public List<UEditorContentEntity> listDefaultPageByUsed() {
        
        return super.selectList(new EntityWrapper()
            .eq("Used", 1)
            .eq("UType", 1));
    }
    
}
