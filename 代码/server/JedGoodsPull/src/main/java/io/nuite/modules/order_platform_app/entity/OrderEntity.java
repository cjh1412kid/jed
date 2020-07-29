package io.nuite.modules.order_platform_app.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author admin
 * @email
 * @date 2018-04-11 11:29:57
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_Order")
@ExcelTarget("orderEntity")
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 客户Seq(外键:userInfo表)
     */
    @TableField(value = "User_Seq")
    private Integer userSeq;
    /**
     * 订单编号
     */
    @TableField(value = "OrderNum")
    @Excel(name = "订单号", orderNum = "0", width = 20)
    private String orderNum;
    /**
     * 是否拆单（0:未拆单 1:已拆单 2:已合并3:已归类拆单合并）
     */
    @TableField(value = "IsSplit")
    private Integer isSplit;
    /**
     * 母单Seq(用于子单存储原订单的seq。可以多个，用逗号隔开。拆单后的子单有一个，合并后的子单有多个，归类拆单合并后的归类订单有多个)
     */
    @TableField(value = "ParentSeqs")
    private String parentSeqs;
    /**
     * 是否归类拆单合并订单（0:否 1:是） 【用来标记订单类型，因为归类拆单同步后的订单不能再进行归类拆单同步操作】
     */
    @TableField(value = "ClassifySplitOrder")
    private Integer classifySplitOrder;
    
    /**
     * 是否导入ERP的状态 0:未导入 1:已导入 -1:无需导入
     */
    @TableField(value = "ImportErpState")
    private Integer importErpState;
    
    /**
     * 订单价格
     */
    @TableField(value = "OrderPrice")
    @Excel(name = "订单价格", orderNum = "2")
    private BigDecimal orderPrice;

    /**
     * 已付金额
     */
    @TableField(value = "Paid")
    @Excel(name = "已付金额", orderNum = "1")
    private BigDecimal paid;

    /**
     * 订单状态 
     * 工厂方：0待确认、1待审核、2待入库、3未发货、4部分发货、5已发货、6已到货、7已取消
	 * 订货方：0待确认、1待支付、2待入库、3待发货、4部分发货、5已发货、6已收货、7已取消
     */
    @TableField(value = "OrderStatus")
    private Integer orderStatus;
    /**
     * 商户序号
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;
    /**
     * 收货人姓名
     */
    @TableField(value = "ReceiverName")
    private String receiverName;
    /**
     * 收货人电话
     */
    @TableField(value = "Telephone")
    private String telephone;
    /**
     * 收货人地址
     */
    @TableField(value = "FullAddress")
    private String fullAddress;
    /**
     * 快递公司序号
     */
    @TableField(value = "ExpressCompany_Seq")
    private Integer expressCompanySeq;
    /**
     * 快递单号
     */
    @TableField(value = "ExpressNo")
    private String expressNo;
    /**
     * 快递单图片
     */
    @TableField(value = "ExpressImg")
    private String expressImg;

    /**
     * 插入时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;

    /**
     * 要求到货时间
     */
    @TableField(value = "RequireTime")
    private Date requireTime;

    /**
     * 确认订单时间
     */
    @TableField(value = "ConfirmTime")
    private Date confirmTime;

    /**
     * 付款时间
     */
    @TableField(value = "PaymentTime")
    private Date paymentTime;

    /**
     * 入库时间
     */
    @TableField(value = "StoreTime")
    private Date storeTime;

    /**
     * 发货时间
     */
    @TableField(value = "DeliverTime")
    private Date deliverTime;
    /**
     * 收货时间
     */
    @TableField(value = "ReceiveTime")
    private Date receiveTime;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    @Excel(name = "备注", orderNum = "5", width = 20)
    private String remark;
    /**
     * 留言
     */
    @TableField(value = "Suggestion")
    @Excel(name = "留言", orderNum = "6", width = 20)
    private String suggestion;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;

    // --自定义参数-- //
    /**
     * 快递公司名称
     */
    @TableField(exist = false)
    private String expressName;
    /**
     * 快递公司代码
     */
    @TableField(exist = false)
    private String expressCode;
    /**
     * 订单状态名称
     */
    @TableField(exist = false)
    @Excel(name = "订单状态", orderNum = "3", width = 20)
    private String statusName;
    /**
     * 订单中鞋子总数
     */
    @TableField(exist = false)
    private Integer species;
    /**
     * 该订单中所有鞋子对应的图片
     */
    @TableField(exist = false)
    private List<String> photo;
    /**
     * 姓名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 客户公司名称(订货方名称)
     */
    @TableField(exist = false)
    @Excel(name = "订货方", orderNum = "4", width = 20)
    private String orderingparty;

    /**
     * 插入时间inputTime字符串格式，如：'2018-05-15'
     */
    @TableField(exist = false)
    private String dateStr;
    
    
    
    
    

    public String getOrderingparty() {
        return orderingparty;
    }

    public void setOrderingparty(String orderingparty) {
        this.orderingparty = orderingparty;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }
    
	public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

	public String getParentSeqs() {
		return parentSeqs;
	}

	public void setParentSeqs(String parentSeqs) {
		this.parentSeqs = parentSeqs;
	}

	public Integer getClassifySplitOrder() {
		return classifySplitOrder;
	}

	public void setClassifySplitOrder(Integer classifySplitOrder) {
		this.classifySplitOrder = classifySplitOrder;
	}

	public Integer getImportErpState() {
		return importErpState;
	}

	public void setImportErpState(Integer importErpState) {
		this.importErpState = importErpState;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getCompanySeq() {
        return companySeq;
    }

    public void setCompanySeq(Integer companySeq) {
        this.companySeq = companySeq;
    }

    public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public Integer getExpressCompanySeq() {
        return expressCompanySeq;
    }

    public void setExpressCompanySeq(Integer expressCompanySeq) {
        this.expressCompanySeq = expressCompanySeq;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressImg() {
        return expressImg;
    }

    public void setExpressImg(String expressImg) {
        this.expressImg = expressImg;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Date getRequireTime() {
        return requireTime;
    }

    public void setRequireTime(Date requireTime) {
        this.requireTime = requireTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(Date storeTime) {
        this.storeTime = storeTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getSpecies() {
        return species;
    }

    public void setSpecies(Integer species) {
        this.species = species;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderEntity{");
        sb.append("seq=").append(seq);
        sb.append(", userSeq=").append(userSeq);
        sb.append(", orderNum='").append(orderNum).append('\'');
        sb.append(", orderPrice=").append(orderPrice);
        sb.append(", paid=").append(paid);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", companySeq=").append(companySeq);
        sb.append(", receiverName='").append(receiverName).append('\'');
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append(", fullAddress='").append(fullAddress).append('\'');
        sb.append(", expressCompanySeq=").append(expressCompanySeq);
        sb.append(", expressNo='").append(expressNo).append('\'');
        sb.append(", expressImg='").append(expressImg).append('\'');
        sb.append(", inputTime=").append(inputTime);
        sb.append(", requireTime=").append(requireTime);
        sb.append(", confirmTime=").append(confirmTime);
        sb.append(", paymentTime=").append(paymentTime);
        sb.append(", storeTime=").append(storeTime);
        sb.append(", deliverTime=").append(deliverTime);
        sb.append(", receiveTime=").append(receiveTime);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", suggestion='").append(suggestion).append('\'');
        sb.append(", del=").append(del);
        sb.append(", expressName='").append(expressName).append('\'');
        sb.append(", expressCode='").append(expressCode).append('\'');
        sb.append(", statusName='").append(statusName).append('\'');
        sb.append(", species=").append(species);
        sb.append(", photo=").append(photo);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", orderingparty='").append(orderingparty).append('\'');
        sb.append(", dateStr='").append(dateStr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
