package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.MessageDao;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.service.MessageService;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 消息Service实现类
 * @author: jxj
 * @create: 2019-03-30 13:17
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {
    @Autowired
    private BaseUserRoleDao baseUserRoleDao;

    @Autowired
    protected ConfigUtils configUtils;
    @Override
    public Map<String, Object> selectMessage(BaseUserEntity baseUserEntity, Integer type, Page<MessageEntity> page) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        Wrapper<BaseUserRoleEntity> baseUserRoleEntityWrapper = new EntityWrapper<>();
        baseUserRoleEntityWrapper.eq("User_Seq",baseUserEntity.getSeq());
        List<BaseUserRoleEntity> baseUserRoleEntities = baseUserRoleDao.selectList(baseUserRoleEntityWrapper);
        if(baseUserRoleEntities.get(0).getRoleSeq().equals(new Integer(3))) {
            map.put("userSeq",baseUserEntity.getSeq());
        }
        map.put("type",type);
        map.put("companySeq",baseUserEntity.getCompanySeq());
        Map<String,Object> result = new HashMap<>(10);
        List<MessageEntity> messageEntities = baseMapper.selectMessage(map,page);
        for (MessageEntity messageEntity : messageEntities) {
            if(!"".equals(messageEntity.getImageName())) {
                String imgBasePath = configUtils.getPictureBaseUrl() + "/notice/" + baseUserEntity.getCompanySeq() + "/";
                messageEntity.setImgSrc(imgBasePath + messageEntity.getImageName());
            }
        }
        result.put("result",baseMapper.selectMessage(map,page));
        return result;
    }

    @Override
    public Map<String, Object> selectMessageBySeq(Integer seq) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("seq",seq);
        Map<String,Object> result = new HashMap<>(10);
        result.put("result",baseMapper.selectMessageBySeq(map));
        return result;
    }
}
