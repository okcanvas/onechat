package io.bizbee.onechat.server.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_user")
public class User extends Model<User> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("user_name")
  private String userName;

  @TableField("nick_name")
  private String nickName;

  @TableField("sex")
  private Integer sex;

  @TableField("head_image")
  private String headImage;

  @TableField("head_image_thumb")
  private String headImageThumb;

  @TableField("signature")
  private String signature;

  @TableField("password")
  private String password;

  @TableField("last_login_time")
  private Date lastLoginTime;

  @TableField("created_time")
  private Date createdTime;

  @TableField("register_from")
  private Integer registerFrom;

  @TableField("oauth_src")
  private String oauthSrc;

  @TableField("account_type")
  private Integer accountType;

  @TableField("anonymou_id")
  private String anonymouId;

  @TableField("last_login_ip")
  private String lastLoginIp;

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
