package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.OnlineMessageEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.from.ChatHistoryForm;
import io.nuite.modules.system.from.ChatMessageForm;

import java.util.List;
import java.util.Map;

public interface OnlineMessageService extends IService<OnlineMessageEntity> {

    void addOnlineMessage(OnlineMessageEntity onlineMessageEntity);

    List<Map<String, Object>> getBaseUserInfoBySeqs(String userSeqs);

    List<ChatMessageForm> getChatMessageList(ChatHistoryForm toUser, BaseUserEntity me);

	List<Map<String, Object>> getCustomServiceList(BaseUserEntity loginUser);

}
