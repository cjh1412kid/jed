package io.nuite.modules.order_platform_app.service;

import io.nuite.modules.order_platform_app.entity.CommunityCOMMENTEntity;
import io.nuite.modules.order_platform_app.entity.CommunityCONTENTEntity;
import io.nuite.modules.order_platform_app.entity.CommunityRECORDEntity;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.util.List;
import java.util.Map;

public interface CommunityService {

	BaseCompanyEntity getBaseCompanyByCompanySeq(Integer companySeq);

	List<CommunityCONTENTEntity> getCommunityCONTENTList(Integer shoesSeq, Integer start, Integer num);

	BaseUserEntity getBaseUserBySeq(Integer userSeq);

	List<CommunityCOMMENTEntity> getFirstCommentList(Integer contentSeq);

	List<CommunityCOMMENTEntity> getSecondCommentList(Integer commentSeq);
	
	int getContentSeeNum(Integer contentSeq);

	int getContentUpNum(Integer contentSeq);
	
	boolean getIsUpByUserSeq(Integer contentSeq, Integer userSeq);

	void addCommunityCONTENT(CommunityCONTENTEntity communityCONTENT);

	void addCommunityRECORD(CommunityRECORDEntity communityRECORD);

	void addCommunityCOMMENT(CommunityCOMMENTEntity communityCOMMENT);

	void deleteCommunityCONTENT(Integer seq);
	
	void cancelUpCommunity(Map<String, Object> map);

	void deleteCommunityCOMMENT(Integer seq);


}
