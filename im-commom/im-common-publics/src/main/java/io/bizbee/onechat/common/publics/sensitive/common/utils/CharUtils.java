package io.bizbee.onechat.common.publics.sensitive.common.utils;

import java.util.HashMap;
import java.util.Map;

public final class CharUtils {

  private CharUtils() {
  }

  private static final String LETTERS_ONE = "ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ"
      + "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ"
      + "⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵";

  private static final String LETTERS_TWO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
      + "abcdefghijklmnopqrstuvwxyz"
      + "abcdefghijklmnopqrstuvwxyz";

  private static final Map<Character, Character> LETTER_MAP = new HashMap<>(LETTERS_ONE.length());

  static {
    final int size = LETTERS_ONE.length();

    for (int i = 0; i < size; i++) {
      LETTER_MAP.put(LETTERS_ONE.charAt(i), LETTERS_TWO.charAt(i));
    }
  }

  public static Character getMappingChar(final Character character) {
    final Character mapChar = LETTER_MAP.get(character);
    if (ObjectUtil.isNotNull(mapChar)) {
      return mapChar;
    }

    return character;
  }

}
