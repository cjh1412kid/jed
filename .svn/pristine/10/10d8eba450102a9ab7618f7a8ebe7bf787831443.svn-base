package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author admin
 * @email
 * @date 2018-04-11 11:38:09
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_BASE + "YHSR_Base_User")
@ApiModel("用户实体类")
public class BaseUserEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  
  /**
   * 序号(主键)
   */
  @TableId
  @TableField(value = "Seq")
  private Integer seq;
  /**
   * 账号
   */
  @TableField(value = "AccountName")
  private String accountName;
  /**
   * 密码
   */
  @TableField(value = "Password")
  private String password;
  /**
   * 公司序号(外键:YHSR_Base_Company表)
   */
  @TableField(value = "Company_Seq")
  private Integer companySeq;
  /**
   * 品牌序号
   */
  @TableField(value = "Brand_Seq")
  private Integer brandSeq;
  
  /**
   * 门店序号
   */
  @TableField(value = "Shop_Seq")
  private Integer shopSeq;
  
  /**
   * 姓名
   */
  @TableField(value = "UserName")
  private String userName;
  /**
   * 电话
   */
  @TableField(value = "Telephone")
  private String telephone;
  
  /**
   * 地址
   */
  @TableField(value = "Address")
  private String address;
  /**
   * 头像
   */
  @TableField(value = "HeadImg")
  private String headImg;
  /**
   * 融云平台token
   */
  @TableField(value = "RongCloudToken")
  private String rongCloudToken;
  /**
   * 极光推送平台token
   */
  @TableField(value = "JPushToken")
  private String jPushToken;
  /**
   * 亲加直播平台token
   */
  @TableField(value = "GotyeToken")
  private String gotyeToken;
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
  
  // --自定义字段-- //
  /**
   * 是否禁用
   */
  @TableField(exist = false)
  private Integer isDisable;
  /**
   * 门店（用于判断用户是否是直营店的用户）
   */
  @TableField(exist = false)
  private String shopName;
  /**
   * 公司名称
   */
  @TableField(exist = false)
  private String companyName;
  /**
   * 公司类别序号
   */
  @TableField(exist = false)
  private Integer companyTypeSeq;
  /**
   * 品牌名称
   */
  @TableField(exist = false)
  private String brandName;
  /**
   * 有效期
   */
  @TableField(exist = false)
  private Date effectiveDate;
  
  /**
   * 公司的描述
   */
  @TableField(exist = false)
  private String remark;
  
  /**
   * 公司地址
   */
  @TableField(exist = false)
  private String companyAddress;
  
  /**
   * 工厂管理员seq
   */
  @TableField(exist = false)
  private Integer adminUserSeq;
  
  /**
   * 所属类型名称
   */
  @TableField(exist = false)
  private String attachTypeName;
  
  /**
   * 所属类型公司名称
   */
  @TableField(exist = false)
  private String attachCompanyName;
  
  /**
   * 所属公司/代理的描述
   */
  @TableField(exist = false)
  private String attachRemark;
  
  /**
   * 权限集合
   */
  @TableField(exist = false)
  private Set<String> permissions;
  
  /**
   * （所属工厂的）订货计划权限（0:无权限 1:有权限）
   */
  @TableField(exist = false)
  private Integer meetingPlanPermission;
  /**
   * 扫码提醒（0:不提醒,1:提醒）
   */
  @TableField(exist = false)
  private Integer scanRemind;
  /**
   * 下单提醒（0:不提醒,1:提醒）
   */
  @TableField(exist = false)
  private Integer orderRemind;
  
  /**
   * 用户角色类型Code
   */
  @TableField(exist = false)
  private String roleCode;
  
  /**
   * 用户的角色
   */
  @TableField(exist = false)
  private BaseRoleEntity userRole;
  
  /**
   * 总部用户标志
   */
  @TableField(exist = false)
  private boolean factoryUserFlag = false;
  
  /**
   * 门店店长标志
   */
  @TableField(exist = false)
  private boolean shopAdminFlag = false;

  /**
   * 角色序号
   */
  @TableField(exist = false)
  private Integer roleSeq;
}
