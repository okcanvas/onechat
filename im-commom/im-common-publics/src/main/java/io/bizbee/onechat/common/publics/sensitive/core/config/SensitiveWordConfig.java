package io.bizbee.onechat.common.publics.sensitive.core.config;

import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.instance.Instances;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordAllow;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordDeny;
import io.bizbee.onechat.common.publics.sensitive.core.support.allow.SystemDefaultWordAllow;
import io.bizbee.onechat.common.publics.sensitive.core.support.check.SensitiveCheckWord;
import io.bizbee.onechat.common.publics.sensitive.core.support.deny.SystemDefaultWordDeny;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SensitiveWordConfig extends WordContext {

  private List<IWordDeny> wordDenys;

  private List<IWordAllow> wordAllows;

  public SensitiveWordConfig() {
    this.setIgnoreRepeat(false);
    this.setSensitiveCheckNumLen(20);
    this.addSensitiveChecks(Instances.singleton(SensitiveCheckWord.class));
  }

  public static SensitiveWordConfig defaultConfig() {
    SensitiveWordConfig sensitiveWordConfig = new SensitiveWordConfig();
    sensitiveWordConfig.addWordAllows(new SystemDefaultWordAllow());
    sensitiveWordConfig.addWordDenys(new SystemDefaultWordDeny());
    return sensitiveWordConfig;
  }

  public void addWordAllows(IWordAllow iWordAllow) {
    if (wordAllows == null) {
      wordAllows = new ArrayList<>(8);
    }
    wordAllows.add(iWordAllow);
  }

  public void addWordDenys(IWordDeny iWordDeny) {
    if (wordDenys == null) {
      wordDenys = new ArrayList<>(8);
    }
    wordDenys.add(iWordDeny);
  }

  public List<IWordDeny> findWordDenys() {
    if (this.wordDenys == null) {
      return Collections.emptyList();
    }
    return this.wordDenys;
  }

  public List<IWordAllow> findWordAllows() {
    if (this.wordAllows == null) {
      return Collections.emptyList();
    }
    return this.wordAllows;
  }

}
