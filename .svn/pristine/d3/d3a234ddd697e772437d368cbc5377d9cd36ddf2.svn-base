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
import io.nuite.modules.system.from.ChatHistoryForm;
import io.nuite.modules.system.from.ChatMessageForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<ChatMessageForm> getChatMessageList(ChatHistoryForm toUser, BaseUserEntity me) {
        String userAvatarUrl = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getBaseUser() + "/";

        String chatSourceUrl = configUtils.getPictureBaseUrl() + "/" + configUtils.getOrderPlatformApp().getOrderPlatformDir()
                + "/" + configUtils.getOrderPlatformApp().getOnlineMessage() + "/";

        Integer[] seqArray = new Integer[]{toUser.getId(), me.getSeq()};

        BaseUserEntity to = baseUserDao.selectById(toUser.getId());

        List<OnlineMessageEntity> onlineMessages = onlineMessageDao.selectTop21List(seqArray, toUser.getTime());
        List<ChatMessageForm> resultMessages = new ArrayList<>();
        for (OnlineMessageEntity messageEntity : onlineMessages) {
            ChatMessageForm chatMessageForm = new ChatMessageForm();
            chatMessageForm.setId(messageEntity.getSenderUserSeq());

            BaseUserEntity messageFromUser;
            if (messageEntity.getSenderUserSeq().equals(me.getSeq())) {
                chatMessageForm.setMine(true);
                messageFromUser = me;
            } else {
                chatMessageForm.setMine(false);
                messageFromUser = to;
            }
            chatMessageForm.setUsername(messageFromUser.getUserName());
            chatMessageForm.setAvatar(userAvatarUrl + getUserHeader(messageFromUser.getHeadImg(), messageFromUser.getSeq()));

            chatMessageForm.setType("friend");
            chatMessageForm.setTimestamp(messageEntity.getInputTime().getTime());
            String content = messageEntity.getContent();
            String filePath = messageEntity.getImgPath();
            if (StringUtils.isNotBlank(content)) {
                chatMessageForm.setContent(content);
            } else if (StringUtils.isNotBlank(filePath)) {
                String detailPath = chatSourceUrl + messageFromUser.getSeq() + "/" + filePath;
                if (filePath.endsWith(".jpg")
                        || filePath.endsWith(".jpeg")
                        || filePath.endsWith(".bmp")
                        || filePath.endsWith(".gif")
                        || filePath.endsWith(".png")) {
                    chatMessageForm.setContent("img[" + detailPath + "]");
                } /*else if (filePath.endsWith(".amr") || filePath.endsWith(".mp3")) {
                    chatMessageForm.setContent("audio[" + detailPath + "]");
                }*/ else {
                    chatMessageForm.setContent("暂不支持此类型消息");
                }
            } else {
                chatMessageForm.setContent("暂不支持此类型消息");
            }
            resultMessages.add(chatMessageForm);
        }
        resultMessages.sort((o1, o2) -> {
            int result = Long.compare(o2.getTimestamp(), o1.getTimestamp());
            return result * (-1);
        });
        return resultMessages;
    }

    private String getUserHeader(String header, Integer seq) {
        String resultHeader;
        if (StringUtils.isBlank(header)) {
            resultHeader = "default.png";
        } else {
            resultHeader = seq + "/" + header;
        }
        return resultHeader;
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
