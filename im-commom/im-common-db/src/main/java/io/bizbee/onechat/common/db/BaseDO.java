
package io.bizbee.onechat.common.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDO {

  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  // @TableField(fill = FieldFill.INSERT)
  // private Integer delFlag;
}
