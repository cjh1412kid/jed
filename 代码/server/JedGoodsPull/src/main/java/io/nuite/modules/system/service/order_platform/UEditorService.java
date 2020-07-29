package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.UEditorContentEntity;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/7/17 14:24
 * @Version: 1.0
 * @Description:
 */
public interface UEditorService extends IService<UEditorContentEntity> {
    
    /**
     * 根据企业id获取所有相关ueditor记录
     *
     * @param companySeq
     * @return
     */
    List<UEditorContentEntity> getList(Integer companySeq);
    
    /**
     * 根据企业id和模版类型获取所有相关模版
     *
     * @param companySeq
     * @param uType      模版类型： 0首页模版；1 未登录模版
     * @return
     */
    List<UEditorContentEntity> listByCompanySeqAndUType(Integer companySeq, String uType);
    
    /**
     * 根据记录id查询记录
     *
     * @param seq
     * @return
     */
    UEditorContentEntity getById(Integer seq);
    
    /**
     * 根据企业ID和被使用状态查询记录 唯一
     *
     * @param companySeq
     * @param usedFlag   被使用标记，1：被使用 ； 0未使用
     * @param uType      模版类型： 0首页模版；1 未登录模版
     * @return
     */
    UEditorContentEntity getByCompanySeqAndUsedAndUType(Integer companySeq, Integer usedFlag, String uType);
    
    /**
     * 保存用户编辑的内容
     *
     * @param ur
     */
    Boolean saveOrUpdate(UEditorContentEntity ur);
    
    /**
     * 根据模版id删除记录
     *
     * @param seq
     */
    Boolean delete(Integer seq);
    
    /**
     * 根据id更新用户信息
     *
     * @param ur
     * @return
     */
    Boolean update(UEditorContentEntity ur);
    
    /**
     * 根据companySeq查询所有记录used置为0，传入的置为1
     *
     * @param ur
     * @return
     */
    void setUsed(UEditorContentEntity ur);
    
    /**
     * 取消用户的主模版设置
     *
     * @param ur
     * @return
     */
    void cancelUsed(UEditorContentEntity ur);
    
    /**
     * 根据公司序号获取默认展示页内容
     *
     * @param companySeq
     * @return
     */
    UEditorContentEntity getDefaultContent(Integer companySeq);
    
    /**
     * 根据公司序号获取主页展示内容
     *
     * @param companySeq
     * @return
     */
    UEditorContentEntity getHomepageContent(Integer companySeq);
    
    /**
     * 获取被使用的默认展示页列表
     *
     * @return
     */
    List<UEditorContentEntity> listDefaultPageByUsed();
    
}
