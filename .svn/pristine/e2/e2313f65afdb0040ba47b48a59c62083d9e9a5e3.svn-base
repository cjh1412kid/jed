package io.nuite.modules.system.controller.order_platform;

import com.alibaba.fastjson.JSON;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.OnlineMessageEntity;
import io.nuite.modules.order_platform_app.service.OnlineMessageService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.from.ChatHistoryForm;
import io.nuite.modules.system.from.ChatMessageForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("system/chat")
public class OpServiceChatController extends AbstractController {

    @Autowired
    private OnlineMessageService onlineMessageService;

    @GetMapping("history")
    public R history(String users) {
        List<ChatHistoryForm> chatUsers;
        if (users == null) {
            chatUsers = new ArrayList<>();
        } else {
            chatUsers = JSON.parseArray(users, ChatHistoryForm.class);
        }
        BaseUserEntity currentUser = getUser();

        Map<String, List<ChatMessageForm>> history = new HashMap<>();
        for (ChatHistoryForm form : chatUsers) {
            List<ChatMessageForm> oneUserHistory = onlineMessageService.getChatMessageList(form, currentUser);
            history.put("friend" + form.getId(), oneUserHistory);
        }

        return R.ok().put("history", history);
    }

    @GetMapping("upload")
    public R upload(Integer from, Integer to, String content) {
        if (from == null || to == null || StringUtils.isBlank(content)) {
            return R.error("数据为空");
        }
        Integer curUserId = getUserSeq().intValue();
        if (!from.equals(curUserId)) {
            return R.error("session 过期");
        }
        OnlineMessageEntity onlineMessageEntity = new OnlineMessageEntity();
        onlineMessageEntity.setSenderUserSeq(from);
        onlineMessageEntity.setReceiveObjectType(1);
        onlineMessageEntity.setReceiveObjectSeq(to);
        onlineMessageEntity.setContent(content);
        onlineMessageService.insert(onlineMessageEntity);
        return R.ok();
    }
}
