package io.bizbee.onechat.common.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.bizbee.onechat.common.core.serializer.DateToLongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class CommonMessageInfo implements Serializable {

  protected Long sendId;
  protected Long id;
  protected String content;
  protected Integer type;

  @JsonSerialize(using = DateToLongSerializer.class)
  protected Date sendTime;
}
