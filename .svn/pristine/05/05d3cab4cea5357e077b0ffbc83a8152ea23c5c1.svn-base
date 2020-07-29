package io.nuite.modules.order_platform_app.service.impl;

import io.nuite.modules.order_platform_app.dao.CommunityCOMMENTDao;
import io.nuite.modules.order_platform_app.dao.CommunityCONTENTDao;
import io.nuite.modules.order_platform_app.dao.CommunityRECORDDao;
import io.nuite.modules.order_platform_app.entity.CommunityCOMMENTEntity;
import io.nuite.modules.order_platform_app.entity.CommunityCONTENTEntity;
import io.nuite.modules.order_platform_app.entity.CommunityRECORDEntity;
import io.nuite.modules.order_platform_app.service.CommunityService;
import io.nuite.modules.sr_base.dao.BaseCompanyDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommunityServiceImpl implements CommunityService {
    
    @Autowired
    private BaseCompanyDao baseCompanyDao;
    
    @Autowired
    private CommunityCONTENTDao communityCONTENTDao;
    
    @Autowired
    private CommunityCOMMENTDao communityCOMMENTDao;
    
    @Autowired
    private CommunityRECORDDao communityRECORDDao;
    
    @Autowired
    private BaseUserDao baseUserDao;
    
    /**
     * 根据公司编号查询公司
     */
	@Override
	public BaseCompanyEntity getBaseCompanyByCompanySeq(Integer companySeq) {
		return baseCompanyDao.selectById(companySeq);
	}

	/**
	 * 根据用户Seq，分页查询社区内容列表
	 */
	@Override
	public List<CommunityCONTENTEntity> getCommunityCONTENTList(Integer shoesSeq, Integer start, Integer num) {
		return communityCONTENTDao.getCommunityCONTENTList(shoesSeq, start-1, num);
	}

	/**
	 * 根据用户Seq，查询用户信息
	 */
	@Override
	public BaseUserEntity getBaseUserBySeq(Integer userSeq) {
		return baseUserDao.selectById(userSeq);
	}

	
	/**
	 * 获取一级评论
	 */
	@Override
	public List<CommunityCOMMENTEntity> getFirstCommentList(Integer contentSeq) {
		return communityCOMMENTDao.getFirstCommentList(contentSeq);
	}

	/**
	 * 获取二级评论（评论的评论）
	 */
	@Override
	public List<CommunityCOMMENTEntity> getSecondCommentList(Integer commentSeq) {
		return communityCOMMENTDao.getSecondCommentList(commentSeq);
	}

	
	/**
	 * 获取浏览数量
	 */
	@Override
	public int getContentSeeNum(Integer contentSeq) {
		return communityCOMMENTDao.getContentRecordNum(contentSeq, 1);
	}

	/**
	 * 获取点赞数量
	 */
	@Override
	public int getContentUpNum(Integer contentSeq) {
		return communityCOMMENTDao.getContentRecordNum(contentSeq, 2);
	}

	/**
	 * 根据Seq判断用户是否点赞
	 */
	@Override
	public boolean getIsUpByUserSeq(Integer contentSeq, Integer userSeq) {
		CommunityRECORDEntity communityRECORDEntity = new CommunityRECORDEntity();
		communityRECORDEntity.setTypeName(2);
		communityRECORDEntity.setUserSeq(userSeq);
		communityRECORDEntity.setContentSeq(contentSeq);
		communityRECORDEntity.setDel(0);
		communityRECORDEntity = communityRECORDDao.selectOne(communityRECORDEntity);
		if(communityRECORDEntity != null) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 新增社区内容
	 */
	@Override
	public void addCommunityCONTENT(CommunityCONTENTEntity communityCONTENT) {
		communityCONTENTDao.insert(communityCONTENT);
	}

	/**
	 * 给社区内容点赞
	 */
	@Override
	public void addCommunityRECORD(CommunityRECORDEntity communityRECORD) {
		communityRECORDDao.insert(communityRECORD);
	}

	/**
	 * 给发布的社区内容评论
	 */
	@Override
	public void addCommunityCOMMENT(CommunityCOMMENTEntity communityCOMMENT) {
		 communityCOMMENTDao.insert(communityCOMMENT);
	}

	/**
	 * 删除社区发布的内容
	 */
	@Override
	public void deleteCommunityCONTENT(Integer seq) {
		communityCONTENTDao.deleteById(seq);
	}

	/**
	 * 取消社区内容点赞
	 */
	@Override
	public void cancelUpCommunity(Map<String, Object> map) {
		communityRECORDDao.deleteByMap(map);
	}
	
	/**
	 * 删除社区评论
	 */
	@Override
	public void deleteCommunityCOMMENT(Integer seq) {
		communityCOMMENTDao.deleteById(seq);
	}

    
}
