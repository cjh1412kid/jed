package io.nuite.modules.order_platform_app.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.modules.order_platform_app.service.OnlineGroupService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 群聊接口
 * @author yy
 * @date 2018-05-8 9:47
 */
@RestController
@RequestMapping("/order/app/onlineGroup")
@Api(tags = "订货平台 - 群聊", description = "群相关接口")
public class OnlineGroupController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OnlineGroupService onlineGroupService;

	
	
	@Login
	@PostMapping("createGroup")
	@ApiOperation("创建群")
	public R createGroup(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("群组名称") @RequestParam("groupName") String groupName,
			@ApiParam("成员序号列表") @RequestParam("userSeqArr") String userSeqArr,
			HttpServletRequest request) {
		try {
			
    		/*if(!isFactoryAdmin(loginUser)) {
    			return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
    		}*/
			/**
			 * 涉及修改表：
			 * 1.群组表添加记录
			 * 2.成员表循环添加多个记录
			 * 3.请求融云创建群
			 */
    		@SuppressWarnings("unchecked")
    		List<Integer> userSeqList = JSONArray.toList(JSONArray.fromObject(userSeqArr), Integer.class, new JsonConfig());
    		
			// 添加创建人
			userSeqList.add(0, loginUser.getSeq());
			// list有序去重
			userSeqList = new ArrayList<Integer>(new LinkedHashSet<Integer>(userSeqList));
			
			Integer groupSeq = onlineGroupService.createGroup(loginUser.getSeq(), groupName, userSeqList);
    		List<Integer> list = new ArrayList<Integer>();
    		list.add(groupSeq);
			return R.ok(list).put("msg", "创建成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("创建失败");
		}
    }
	
	
                                      
	@Login
	@PostMapping("inviteJoinGroup")
	@ApiOperation("邀请用户加入群")
	public R inviteJoinGroup(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("群组序号") @RequestParam("groupSeq") Integer groupSeq,
			@ApiParam("群组名称") @RequestParam("groupName") String groupName,
			@ApiParam("用户序号") @RequestParam("userSeq") Integer userSeq,
			HttpServletRequest request) {
		try {
			
    		/*if(!isFactoryAdmin(loginUser)) {
    			return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
    		}*/
    		
			/**
			 * 涉及修改表：
			 * 1.成员表添加一个记录
			 * 2.请求融云邀请加入
			 */
			onlineGroupService.inviteJoinGroup(groupSeq, groupName, userSeq);
			return R.ok("加入成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("加入群失败");
		}
    }


	
	@Login
	@PostMapping("quitGroup")
	@ApiOperation("用户退出群")
	public R quitGroup(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("群组序号") @RequestParam("groupSeq") Integer groupSeq,
			@ApiParam("用户序号") @RequestParam("userSeq") Integer userSeq,
			HttpServletRequest request) {
		try {
			
    		/*if(!isFactoryAdmin(loginUser)) {
    			return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
    		}*/
    		
			/**
			 * 涉及修改表：
			 * 1.成员表删除一条记录
			 * 2.请求融云退出群
			 */
			onlineGroupService.quitGroup(groupSeq, userSeq);
			return R.ok("退出成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("退出群失败");
		}
    }
	
	
	
	
	@Login
	@GetMapping("getAllGroups")
	@ApiOperation("获取用户已创建的所有群组的信息")
	public R getAllGroups(@ApiIgnore @RequestAttribute("userSeq") Integer loginUserSeq,
			HttpServletRequest request) {
		try {
			//用户创建的所有群
			List<Map<String, Object>> onlineGrouplist = onlineGroupService.getGroupsByCreateUserSeq(loginUserSeq);
			
			//群里的所有成员
			List<Map<String, Object>> memberList;
			for(Map<String, Object> onlineGroupMap : onlineGrouplist) {
				memberList = onlineGroupService.getMembersByGroupSeq((Integer) onlineGroupMap.get("seq"));
				//处理头像路径
				for(Map<String, Object> memberMap : memberList) {
					memberMap.put("headImg", getBaseUserPictureUrl(memberMap.get("userSeq").toString()) + memberMap.get("headImg"));
				}
				onlineGroupMap.put("memberList", memberList);
			}
			
			return R.ok(onlineGrouplist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("加入群失败");
		}
    }

	
	
	/**
	 * 根据群组序号Seqs获取群组的信息
	 * @param loginUserSeq
	 * @param groupSeqArr
	 * @return
	 */
	@Login
	@GetMapping("getGroupBySeqs")
	@ApiOperation("根据群组序号Seqs获取群组的信息")
	public R getGroupBySeqs(@ApiIgnore @RequestAttribute("userSeq") Integer loginUserSeq,
			@ApiParam("群组序号") @RequestParam("groupSeqArr") String groupSeqArr) {
		try {
    		@SuppressWarnings("unchecked")
    		List<Integer> groupSeqList = JSONArray.toList(JSONArray.fromObject(groupSeqArr), Integer.class, new JsonConfig());
			//根据群组Seq获取群组的信息
    		List<Map<String, Object>> onlineGrouplist = onlineGroupService.getGroupBySeqs(groupSeqList);
    		
			//群里的所有成员
			List<Map<String, Object>> memberList;
			for(Map<String, Object> onlineGroupMap : onlineGrouplist) {
				memberList = onlineGroupService.getMembersByGroupSeq((Integer) onlineGroupMap.get("seq"));
				//处理头像路径
				for(Map<String, Object> memberMap : memberList) {
					memberMap.put("headImg", getBaseUserPictureUrl(memberMap.get("userSeq").toString()) + memberMap.get("headImg"));
				}
				onlineGroupMap.put("memberList", memberList);
			}
			
			return R.ok(onlineGrouplist);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("加入群失败");
		}
    }
	
	
	
	
	
	//TODO 修改群名称，群状态
	
	
	
	//删除群
	
	@Login
	@PostMapping("deleteGroup")
	@ApiOperation("删除群")
	public R deleteGroup(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("群组序号") @RequestParam("groupSeq") Integer groupSeq,
			HttpServletRequest request) {
		try {
			
    		/*if(!isFactoryAdmin(loginUser)) {
    			return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
    		}*/
    		
			/**
			 * 涉及修改表：
			 * 1.删除群组
			 * 2.删除群组所有成员
			 * 3.请求融云解散群
			 */
			onlineGroupService.deleteGroup(groupSeq);
			return R.ok("删除群成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("删除群失败");
		}
    }
	
	
	
	
}
