package io.bizbee.onechat.common.publics.sensitive.common.core;

public class SensitiveCheckResult {

  private int index;

  private Class<?> checkClass;

  public static SensitiveCheckResult of(final int index, final Class<?> checkClass) {
    SensitiveCheckResult result = new SensitiveCheckResult();
    result.index(index).checkClass(checkClass);
    return result;
  }

  public int index() {
    return index;
  }

  public SensitiveCheckResult index(int index) {
    this.index = index;
    return this;
  }

  public Class<?> checkClass() {
    return checkClass;
  }

  public SensitiveCheckResult checkClass(Class<?> checkClass) {
    this.checkClass = checkClass;
    return this;
  }

  @Override
  public String toString() {
    return "SensitiveCheckResult{" + "index=" + index + ", checkClass=" + checkClass + '}';
  }

}
