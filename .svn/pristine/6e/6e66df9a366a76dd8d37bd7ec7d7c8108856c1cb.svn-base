package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_CommunityCONTENT")
public class CommunityCONTENTEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 鞋子Seq
	 */
	@TableField(value = "Shoes_Seq")
	private Integer shoesSeq;
	/**
	 * 关联用户Seq
	 */
	@TableField(value = "User_Seq")
	private Integer userSeq;
	/**
	 * 分享内容（文本）
	 */
	@TableField(value = "Content")
	private String content;
	/**
	 * 内容类型序号
	 */
	@TableField(value = "ContentType_Seq")
	private Integer contentTypeSeq;
	/**
	 * 
	 */
	@TableField(value = "Img1")
	private String img1;
	/**
	 * 
	 */
	@TableField(value = "Img2")
	private String img2;
	/**
	 * 
	 */
	@TableField(value = "Img3")
	private String img3;
	/**
	 * 
	 */
	@TableField(value = "Img4")
	private String img4;
	/**
	 * 
	 */
	@TableField(value = "Img5")
	private String img5;
	/**
	 * 
	 */
	@TableField(value = "Img6")
	private String img6;
	/**
	 * 
	 */
	@TableField(value = "Img7")
	private String img7;
	/**
	 * 
	 */
	@TableField(value = "Img8")
	private String img8;
	/**
	 * 
	 */
	@TableField(value = "Img9")
	private String img9;
	/**
	 * 录入时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 状态(1:正常, 0:删除, 2:停用)

	 */
	@TableField(value = "State")
	private Integer state;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;

	
	//自定义字段
    /**
     * 图片数组（app端不关心img1，img2... ，只要数组）
     */
    @TableField(exist = false)
    private List<String> imgList;
    
    
    
    
    
    
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
	 * 设置：关联用户Seq
	 */
	public void setUserSeq(Integer userSeq) {
		this.userSeq = userSeq;
	}
	/**
	 * 获取：关联用户Seq
	 */
	public Integer getUserSeq() {
		return userSeq;
	}
	/**
	 * 设置：分享内容（文本）
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：分享内容（文本）
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：内容类型序号
	 */
	public void setContentTypeSeq(Integer contentTypeSeq) {
		this.contentTypeSeq = contentTypeSeq;
	}
	/**
	 * 获取：内容类型序号
	 */
	public Integer getContentTypeSeq() {
		return contentTypeSeq;
	}
	/**
	 * 设置：
	 */
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	/**
	 * 获取：
	 */
	public String getImg1() {
		return img1;
	}
	/**
	 * 设置：
	 */
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	/**
	 * 获取：
	 */
	public String getImg2() {
		return img2;
	}
	/**
	 * 设置：
	 */
	public void setImg3(String img3) {
		this.img3 = img3;
	}
	/**
	 * 获取：
	 */
	public String getImg3() {
		return img3;
	}
	/**
	 * 设置：
	 */
	public void setImg4(String img4) {
		this.img4 = img4;
	}
	/**
	 * 获取：
	 */
	public String getImg4() {
		return img4;
	}
	/**
	 * 设置：
	 */
	public void setImg5(String img5) {
		this.img5 = img5;
	}
	/**
	 * 获取：
	 */
	public String getImg5() {
		return img5;
	}
	/**
	 * 设置：
	 */
	public void setImg6(String img6) {
		this.img6 = img6;
	}
	/**
	 * 获取：
	 */
	public String getImg6() {
		return img6;
	}
	/**
	 * 设置：
	 */
	public void setImg7(String img7) {
		this.img7 = img7;
	}
	/**
	 * 获取：
	 */
	public String getImg7() {
		return img7;
	}
	/**
	 * 设置：
	 */
	public void setImg8(String img8) {
		this.img8 = img8;
	}
	/**
	 * 获取：
	 */
	public String getImg8() {
		return img8;
	}
	/**
	 * 设置：
	 */
	public void setImg9(String img9) {
		this.img9 = img9;
	}
	/**
	 * 获取：
	 */
	public String getImg9() {
		return img9;
	}
	/**
	 * 设置：录入时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：录入时间
	 */
	public Date getInputTime() {
		return inputTime;
	}
	/**
	 * 设置：状态(1:正常, 0:删除, 2:停用)

	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态(1:正常, 0:删除, 2:停用)

	 */
	public Integer getState() {
		return state;
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
	public List<String> getImgList() {
		return imgList;
	}
	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}
	public Integer getShoesSeq() {
		return shoesSeq;
	}
	public void setShoesSeq(Integer shoesSeq) {
		this.shoesSeq = shoesSeq;
	}
	
	
}
