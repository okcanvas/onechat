package io.bizbee.onechat.common.core.utils;

import lombok.Getter;

@Getter
public class TPair<L, R> {

  private L left;

  private R right;

  public TPair(L left, R right) {
    this.left = left;
    this.right = right;
  }

}
