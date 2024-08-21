package io.bizbee.onechat.common.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;

import java.util.Date;

public class JwtUtil {

  /**
   * @param userId
   * @param info
   * @param expireIn
   * @param secret
   * @return
   */
  public static String sign(Long userId, String info, long expireIn, String secret) {
    try {
      Date date = new Date(System.currentTimeMillis() + expireIn * 1000);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withAudience(userId.toString())
          .withClaim("info", info)
          .withExpiresAt(date)
          .sign(algorithm);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @param token
   * @return
   */
  public static Long getUserId(String token) {
    try {
      String userId = JWT.decode(token).getAudience().get(0);
      return Long.parseLong(userId);
    } catch (JWTDecodeException e) {
      return null;
    }
  }

  /**
   * @param token
   * @return
   */
  public static String getInfo(String token) {
    try {
      return JWT.decode(token).getClaim("info").asString();
    } catch (JWTDecodeException e) {
      return null;
    }
  }

  /**
   * @param token
   * @param secret
   * @return
   */
  public static boolean checkSign(String token, String secret) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    JWTVerifier verifier = JWT.require(algorithm)
        // .withClaim("username, username)
        .build();
    verifier.verify(token);
    return true;
  }
}
