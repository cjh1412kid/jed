package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:58
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_ShoesValuate")
public class ShoesValuateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 用户序号(外键:YHSR_Base_User表)
	 */
	@TableField(value = "User_Seq")
	private Integer userSeq;
	/**
	 * 鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	@TableField(value = "Shoes_Seq")
	private Integer shoesSeq;
	/**
	 * 是否浏览：0，浏览；1，取消浏览
	 */
	@TableField(value = "IsBrowse")
	private Integer isBrowse;
	/**
	 * 浏览时间
	 */
	@TableField(value = "BrowseTime")
	private Date browseTime;
	/**
	 * 评分
	 */
	@TableField(value = "Score")
	private Float score;
	/**
	 * 是否收藏(0:未收藏, 1:已收藏)
	 */
	@TableField(value = "IsCollected")
	private Integer isCollected;
	/**
	 * 收藏时间
	 */
	@TableField(value = "CollectedTime")
	private Date collectedTime;
	/**
	 * 建议内容
	 */
	@TableField(value = "Suggest")
	private String suggest;
	/**
	 * 建议时间
	 */
	@TableField(value = "SuggestTime")
	private Date suggestTime;
	/**
	 * 插入时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;

	
	
	
	/**
	 * 设置：序号(主键)
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	/**
	 * 获取：序号(主键)
	 */
	public Integer getSeq() {
		return seq;
	}
	/**
	 * 设置：用户序号(外键:YHSR_Base_User表)
	 */
	public void setUserSeq(Integer userSeq) {
		this.userSeq = userSeq;
	}
	/**
	 * 获取：用户序号(外键:YHSR_Base_User表)
	 */
	public Integer getUserSeq() {
		return userSeq;
	}
	/**
	 * 设置：鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	public void setShoesSeq(Integer shoesSeq) {
		this.shoesSeq = shoesSeq;
	}
	/**
	 * 获取：鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	public Integer getShoesSeq() {
		return shoesSeq;
	}
	/**
	 * 设置：我的评分
	 */
	public void setScore(Float score) {
		this.score = score;
	}
	/**
	 * 获取：我的评分
	 */
	public Float getScore() {
		return score;
	}
	/**
	 * 设置：是否收藏(0:未收藏, 1:已收藏)
	 */
	public void setIsCollected(Integer isCollected) {
		this.isCollected = isCollected;
	}
	/**
	 * 获取：是否收藏(0:未收藏, 1:已收藏)
	 */
	public Integer getIsCollected() {
		return isCollected;
	}
	/**
	 * 设置：用户建议
	 */
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	/**
	 * 获取：用户建议
	 */
	public String getSuggest() {
		return suggest;
	}
	/**
	 * 设置：删除标识(0:未删除,1:已删除)
	 */
	public void setDel(Integer del) {
		this.del = del;
	}
	/**
	 * 获取：删除标识(0:未删除,1:已删除)
	 */
	public Integer getDel() {
		return del;
	}
	public Date getCollectedTime() {
		return collectedTime;
	}
	public void setCollectedTime(Date collectedTime) {
		this.collectedTime = collectedTime;
	}
	public Date getSuggestTime() {
		return suggestTime;
	}
	public void setSuggestTime(Date suggestTime) {
		this.suggestTime = suggestTime;
	}
	public Integer getIsBrowse() {
		return isBrowse;
	}
	public void setIsBrowse(Integer isBrowse) {
		this.isBrowse = isBrowse;
	}
	public Date getBrowseTime() {
		return browseTime;
	}
	public void setBrowseTime(Date browseTime) {
		this.browseTime = browseTime;
	}
	public Date getInputTime() {
		return inputTime;
	}
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	
}
