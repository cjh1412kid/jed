package io.nuite.modules.order_platform_app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import io.nuite.modules.order_platform_app.dao.OnlineGroupDao;
import io.nuite.modules.order_platform_app.dao.OnlineGroupMemberDao;
import io.nuite.modules.order_platform_app.entity.OnlineGroupEntity;
import io.nuite.modules.order_platform_app.entity.OnlineGroupMemberEntity;
import io.nuite.modules.order_platform_app.service.OnlineGroupService;
import io.nuite.modules.order_platform_app.utils.RongCloudUtils;

@Service
public class OnlineGroupServiceImpl implements OnlineGroupService {
    
    @Autowired
    private OnlineGroupDao onlineGroupDao;
    
    @Autowired
    private OnlineGroupMemberDao onlineGroupMemberDao;
    
	@Autowired
	private RongCloudUtils rongCloudUtils;

	
	
    /**
     * 创建群
     * @return 
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer createGroup(Integer loginUserSeq, String groupName, List<Integer> userSeqList) {
		Date nowDate = new Date();
		//1.群组表新增记录
		OnlineGroupEntity onlineGroupEntity = new OnlineGroupEntity();
		onlineGroupEntity.setGroupName(groupName);
		onlineGroupEntity.setCreateUserSeq(loginUserSeq);
		onlineGroupEntity.setInputTime(nowDate);
		onlineGroupEntity.setState(1);
		onlineGroupEntity.setDel(0);
		onlineGroupDao.insert(onlineGroupEntity);
		
		//2.成员表新增多条记录
		Integer groupSeq = onlineGroupEntity.getSeq();
		OnlineGroupMemberEntity onlineGroupMemberEntity;
		for(Integer userSeq : userSeqList) {
			onlineGroupMemberEntity =  new OnlineGroupMemberEntity();
			onlineGroupMemberEntity.setGroupSeq(groupSeq);
			onlineGroupMemberEntity.setUserSeq(userSeq);
			onlineGroupMemberEntity.setInputTime(nowDate);
			onlineGroupMemberEntity.setDel(0);
			onlineGroupMemberDao.insert(onlineGroupMemberEntity);
		}
		
		//3.请求融云创建群
		boolean flag = rongCloudUtils.createGroup(groupSeq, groupName, userSeqList);
		if(!flag) {
			throw new RuntimeException("融云创建群组失败");
		}
		return groupSeq;
		
	}



	/**
	 * 邀请用户加入群
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void inviteJoinGroup(Integer groupSeq, String groupName, Integer userSeq) {

		// 1.成员表添加一个记录
		OnlineGroupMemberEntity onlineGroupMemberEntity = new OnlineGroupMemberEntity();
		onlineGroupMemberEntity.setGroupSeq(groupSeq);
		onlineGroupMemberEntity.setUserSeq(userSeq);
		onlineGroupMemberEntity.setInputTime(new Date());
		onlineGroupMemberEntity.setDel(0);
		onlineGroupMemberDao.insert(onlineGroupMemberEntity);

		// 2.请求融云邀请加入
		boolean flag = rongCloudUtils.inviteJoinGroup(groupSeq, groupName, userSeq);
		if (!flag) {
			throw new RuntimeException("融云邀请用户加入群失败");
		}

	}


	
	/**
	 * 用户退出群
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void quitGroup(Integer groupSeq, Integer userSeq) {

		// 1.成员表删除一条记录
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("Group_Seq", groupSeq);
		columnMap.put("User_Seq", userSeq);
		onlineGroupMemberDao.deleteByMap(columnMap);

		// 2.请求融云退出群
		boolean flag = rongCloudUtils.quitGroup(groupSeq, userSeq);
		if (!flag) {
			throw new RuntimeException("融云用户退出群失败");
		}
		
	}
	
	
	
	/**
	 * 根据创建人seq获取用户创建的所有群
	 */
	@Override
	public List<Map<String, Object>> getGroupsByCreateUserSeq(Integer loginUserSeq) {
		Wrapper<OnlineGroupEntity> wrapper = new EntityWrapper<OnlineGroupEntity>();
		wrapper.setSqlSelect("Seq AS seq, GroupName AS groupName").where("CreateUser_Seq = {0} AND State = 1", loginUserSeq);
		return onlineGroupDao.selectMaps(wrapper);
	}



	/**
	 * 根据群组seq获取群成员列表
	 */
	@Override
	public List<Map<String, Object>> getMembersByGroupSeq(Integer groupSeq) {
		return onlineGroupMemberDao.getMembersByGroupSeq(groupSeq);
	}



	/**
	 * 根据群组Seq获取群组的信息
	 */
	@Override
	public List<Map<String, Object>> getGroupBySeqs(List<Integer> groupSeqList){
		Wrapper<OnlineGroupEntity> wrapper = new EntityWrapper<OnlineGroupEntity>();
		wrapper.setSqlSelect("Seq AS seq, GroupName AS groupName").in("Seq", groupSeqList).where("State = 1");
		return onlineGroupDao.selectMaps(wrapper);
	}
	
	

	/**
	 * 删除群
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteGroup(Integer groupSeq) {
		
		// 1.删除群组
		onlineGroupDao.deleteById(groupSeq);
		
		// 2.删除群组所有成员
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("Group_Seq", groupSeq);
		onlineGroupMemberDao.deleteByMap(columnMap);

		// 2.请求融云解散群
		boolean flag = rongCloudUtils.dismissGroup(groupSeq);
		if (!flag) {
			throw new RuntimeException("融云解散群失败");
		}
		
	}





	
}
