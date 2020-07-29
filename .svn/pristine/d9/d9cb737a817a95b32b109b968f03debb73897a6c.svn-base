package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.OnlineMessageDao;
import io.nuite.modules.order_platform_app.entity.OnlineMessageEntity;
import io.nuite.modules.order_platform_app.service.OnlineMessageService;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OnlineMessageServiceImpl extends ServiceImpl<OnlineMessageDao, OnlineMessageEntity> implements OnlineMessageService {

    @Autowired
    private OnlineMessageDao onlineMessageDao;

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private ConfigUtils configUtils;


    @Override
    public void addOnlineMessage(OnlineMessageEntity onlineMessageEntity) {
        onlineMessageDao.insert(onlineMessageEntity);
    }


    @Override
    public List<Map<String, Object>> getBaseUserInfoBySeqs(String userSeqs) {
        return baseUserDao.getBaseUserInfoBySeqs(userSeqs);
    }

    

    /**
     * 客服列表
     */
    @Override
    public List<Map<String, Object>> getCustomServiceList(BaseUserEntity loginUser) {
        Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
        wrapper.setSqlSelect("Seq AS seq, UserName AS userName, HeadImg AS headImg")
                .where("Company_Seq = {0} AND Brand_Seq = {1} AND SaleType = {2} ", loginUser.getCompanySeq(), loginUser.getBrandSeq(), null);
        List<Map<String, Object>> userList = baseUserDao.selectMaps(wrapper);
        return userList;
    }
}
