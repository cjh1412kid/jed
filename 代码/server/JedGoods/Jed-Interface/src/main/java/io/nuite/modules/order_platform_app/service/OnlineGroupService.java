package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

public interface OnlineGroupService {

	Integer createGroup(Integer loginUserSeq, String groupName, List<Integer> userSeqList);

	void inviteJoinGroup(Integer groupSeq, String groupName, Integer userSeq);
	
	void quitGroup(Integer groupSeq, Integer userSeq);

	List<Map<String, Object>> getGroupsByCreateUserSeq(Integer loginUserSeq);

	List<Map<String, Object>> getMembersByGroupSeq(Integer groupSeq);
	
	List<Map<String, Object>> getGroupBySeqs(List<Integer> groupSeqList);

	void deleteGroup(Integer groupSeq);

	
}
