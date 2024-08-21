package io.bizbee.onechat.common.log.entity;

import lombok.Data;

@Data
public class ApiLogEntity {

  private String apiModule;

  private String apiName;

  private String beginTime;

  private Object[] inputParams;

  private Object outputParams;

}
