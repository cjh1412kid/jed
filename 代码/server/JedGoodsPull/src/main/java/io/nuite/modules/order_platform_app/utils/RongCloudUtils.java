package io.nuite.modules.order_platform_app.utils;

import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.rong.RongCloud;
import io.rong.methods.group.Group;
import io.rong.methods.user.User;
import io.rong.models.Result;
import io.rong.models.group.GroupMember;
import io.rong.models.group.GroupModel;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 融云聊天工具类
 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/
 * 源码: https://github.com/rongcloud/server-sdk-java
 * 例子源码: https://github.com/rongcloud/server-sdk-java/tree/master/src/main/java/io/rong/example
 * @author yy
 *
 */
@Component
public class RongCloudUtils {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigUtils configUtils;
	
	
	/**
	 * 注册融云，生成用户在融云的Token
	 * API 文档:http://rongcloud.github.io/server-sdk-nodejs/docs/v1/user/user.html#register
	 * @throws Exception
	 */
	public String registerUser(Integer userSeq, String userName, String headImgUrl) {
		try {
			// 融云对接参数
			String rongCloudAppKey = configUtils.getOrderPlatformApp().getRongCloudAppKey();
			String rongCloudAppSecret = configUtils.getOrderPlatformApp().getRongCloudAppSecret();
			
			RongCloud rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
			User User = rongCloud.user;
			UserModel user = new UserModel()
					.setId(userSeq.toString())
					.setName(userName)
					.setPortrait(headImgUrl);
			TokenResult result = User.register(user);
			if (result.getCode() == 200) {
				return result.getToken();
			} else {
				logger.error("注册融云获取Token失败,参数:" + userSeq + "," + userName + "," + headImgUrl + ",返回:" + result);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("注册融云获取Token异常:" + e.getMessage(), e);
			return null;
		}
	}
	
	
	
	/**
	 * 刷新融云用户信息
	 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/user/user.html#refresh
	 * @param seq
	 * @param userName
	 * @param headImgUrl
	 * @throws Exception 
	 */
	public boolean refreshUserInfo(Integer userSeq, String userName, String headImgUrl) {
		try {
			// 融云对接参数
			String rongCloudAppKey = configUtils.getOrderPlatformApp().getRongCloudAppKey();
			String rongCloudAppSecret = configUtils.getOrderPlatformApp().getRongCloudAppSecret();

			RongCloud rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
			User User = rongCloud.user;
			UserModel user = new UserModel()
					.setId(userSeq.toString())
					.setName(userName)
					.setPortrait(headImgUrl);
			Result refreshResult = User.update(user);
			if (refreshResult.getCode() == 200) {
				return true;
			} else {
				logger.error("刷新融云用户信息失败,参数:" + userSeq + "," + userName + "," + headImgUrl + ",返回:" + refreshResult);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("刷新融云用户信息异常:" + e.getMessage(), e);
			return false;
		}
	}
	
	
	
	/**
	 * 创建群组
	 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/group/group.html#create
	 * @param groupSeq
	 * @param groupName
	 * @param userSeqList
	 * @return
	 */
	public boolean createGroup(Integer groupSeq, String groupName, List<Integer> userSeqList) {
		try {
			// 融云对接参数
			String rongCloudAppKey = configUtils.getOrderPlatformApp().getRongCloudAppKey();
			String rongCloudAppSecret = configUtils.getOrderPlatformApp().getRongCloudAppSecret();

			RongCloud rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);

			Group Group = rongCloud.group;


			GroupMember[] members = new GroupMember[userSeqList.size()];
			for(int i = 0; i < userSeqList.size(); i++) {
				members[i] = new GroupMember().setId(userSeqList.get(i).toString());
			}

			GroupModel group = new GroupModel()
					.setId(groupSeq.toString())
					.setMembers(members)
					.setName(groupName);
			Result groupCreateResult = (Result)Group.create(group);
			
			if (groupCreateResult.getCode() == 200) {
				return true;
			} else {
				logger.error("融云创建群组失败,参数:" + groupSeq + "," + groupName + "," + userSeqList + ",返回:" + groupCreateResult);
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("融云创建群组异常:" + e.getMessage(), e);
			return false;
		}
	 }


	
	
	/**
	 * 邀请用户加入群组
	 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/group/group.html#join
	 * @param groupSeq
	 * @param groupName 
	 * @param userSeq
	 * @return
	 */
	public boolean inviteJoinGroup(Integer groupSeq, String groupName, Integer userSeq) {
		try {
			// 融云对接参数
			String rongCloudAppKey = configUtils.getOrderPlatformApp().getRongCloudAppKey();
			String rongCloudAppSecret = configUtils.getOrderPlatformApp().getRongCloudAppSecret();

			RongCloud rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);

			Group Group = rongCloud.group;


			GroupMember[] members = {new GroupMember().setId(userSeq.toString())};

			GroupModel group = new GroupModel()
					.setId(groupSeq.toString())
					.setMembers(members)
					.setName(groupName);
			Result groupInviteResult = (Result)Group.invite(group);
			
			if (groupInviteResult.getCode() == 200) {
				return true;
			} else {
				logger.error("融云邀请用户加入群组失败,参数:" + groupSeq + "," + userSeq + ",返回:" + groupInviteResult);
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("融云邀请用户加入群组异常:" + e.getMessage(), e);
			return false;
		}
	 }


	
	/**
	 * 退出群组
	 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/group/group.html#quit
	 * @param groupSeq
	 * @param userSeq
	 * @return
	 */
	public boolean quitGroup(Integer groupSeq, Integer userSeq) {
		try {
			// 融云对接参数
			String rongCloudAppKey = configUtils.getOrderPlatformApp().getRongCloudAppKey();
			String rongCloudAppSecret = configUtils.getOrderPlatformApp().getRongCloudAppSecret();

			RongCloud rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);

			Group Group = rongCloud.group;


			GroupMember[] members = {new GroupMember().setId(userSeq.toString())};

			GroupModel group = new GroupModel()
							.setId(groupSeq.toString())
							.setMembers(members);
			Result groupQuitResult = (Result)Group.quit(group);
			
			if (groupQuitResult.getCode() == 200) {
				return true;
			} else {
				logger.error("融云退出群组失败,参数:" + groupSeq + "," + userSeq + ",返回:" + groupQuitResult);
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("融云退出群组异常:" + e.getMessage(), e);
			return false;
		}

	}



	/**
	 * 解散群组
	 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/group/group.html#dismiss
	 * @param groupSeq
	 * @return
	 */
	public boolean dismissGroup(Integer groupSeq) {
		try {
			// 融云对接参数
			String rongCloudAppKey = configUtils.getOrderPlatformApp().getRongCloudAppKey();
			String rongCloudAppSecret = configUtils.getOrderPlatformApp().getRongCloudAppSecret();

			RongCloud rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);

			Group Group = rongCloud.group;


			GroupMember[] members = {new GroupMember().setId("")};

			GroupModel group = new GroupModel()
							.setId(groupSeq.toString())
							.setMembers(members);
			Result groupDismissResult = (Result)Group.dismiss(group);
			
			if (groupDismissResult.getCode() == 200) {
				return true;
			} else {
				logger.error("融云解散群组失败,参数:" + groupSeq + ",返回:" + groupDismissResult);
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("融云解散群组异常:" + e.getMessage(), e);
			return false;
		}

	}


	
	
}
