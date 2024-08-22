package io.bizbee.onechat.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.cache.CachePrefix;
import io.bizbee.onechat.common.core.contant.AppConst;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.enums.ResultCode;
import io.bizbee.onechat.common.core.utils.JwtUtil;
import io.bizbee.onechat.common.core.utils.MixUtils;
import io.bizbee.onechat.server.common.contant.Constant;
import io.bizbee.onechat.server.common.entity.Friend;
import io.bizbee.onechat.server.common.entity.Group;
import io.bizbee.onechat.server.common.entity.GroupMember;
import io.bizbee.onechat.server.common.entity.User;
import io.bizbee.onechat.server.common.enums.GroupEnum;
import io.bizbee.onechat.server.common.enums.UserEnum;
import io.bizbee.onechat.server.common.vo.user.*;
import io.bizbee.onechat.server.exception.BusinessException;
import io.bizbee.onechat.server.exception.GlobalException;
import io.bizbee.onechat.server.mapper.UserMapper;
import io.bizbee.onechat.server.service.IFriendService;
import io.bizbee.onechat.server.service.IGroupMemberService;
import io.bizbee.onechat.server.service.IGroupService;
import io.bizbee.onechat.server.service.IUserService;
import io.bizbee.onechat.server.util.*;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private IGroupMemberService groupMemberService;

  @Autowired
  private IFriendService friendService;

  @Autowired
  private IGroupService iGroupService;

  @Autowired
  private IGroupMemberService iGroupMemberService;


  @Autowired
  private AppCache appCache;

  @Override
  public LoginResp login(LoginReq dto) {
    User user = findUserByName(dto.getUserName());
    if (null == user) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "");
    }
    if (UserEnum.AccountType.Anonymous.equals(user.getAccountType())) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "");
    }
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new GlobalException(ResultCode.PASSWOR_ERROR);
    }
    user.setLastLoginIp(IpUtil.getIpAddr(SessionContext.getRequest()));
    this.updateById(user);
    // 生成token
    return buildLoginResp(user);
  }

  public LoginResp buildLoginResp(User user) {
    SessionContext.UserSessionInfo session = BeanUtils.copyProperties(user,
        SessionContext.UserSessionInfo.class);
    String strJson = JSON.toJSONString(session);
    String accessToken = JwtUtil
        .sign(user.getId(), strJson, AppConst.ACCESS_TOKEN_EXPIRE, AppConst.ACCESS_TOKEN_SECRET);
    String refreshToken = JwtUtil
        .sign(user.getId(), strJson, AppConst.REFRESH_TOKEN_EXPIRE, AppConst.REFRESH_TOKEN_SECRET);
    LoginResp vo = new LoginResp();
    vo.setAccessToken(accessToken);
    vo.setAccessTokenExpiresIn(AppConst.ACCESS_TOKEN_EXPIRE);
    vo.setRefreshToken(refreshToken);
    vo.setRefreshTokenExpiresIn(AppConst.REFRESH_TOKEN_EXPIRE);
    return vo;
  }

  
  @Override
  public LoginResp refreshToken(String refreshToken) {
    try {
      
      JwtUtil.checkSign(refreshToken, AppConst.REFRESH_TOKEN_SECRET);
      String strJson = JwtUtil.getInfo(refreshToken);
      Long userId = JwtUtil.getUserId(refreshToken);
      String accessToken = JwtUtil
          .sign(userId, strJson, AppConst.ACCESS_TOKEN_EXPIRE, AppConst.ACCESS_TOKEN_SECRET);
      String newRefreshToken = JwtUtil
          .sign(userId, strJson, AppConst.REFRESH_TOKEN_EXPIRE, AppConst.REFRESH_TOKEN_SECRET);
      LoginResp vo = new LoginResp();
      vo.setAccessToken(accessToken);
      vo.setAccessTokenExpiresIn(AppConst.ACCESS_TOKEN_EXPIRE);
      vo.setRefreshToken(newRefreshToken);
      vo.setRefreshTokenExpiresIn(AppConst.REFRESH_TOKEN_EXPIRE);
      return vo;
    } catch (JWTVerificationException e) {
      throw new GlobalException("refreshToken");
    }
  }

  @Override
  public void register(RegisterReq dto) {
    String userName = dto.getUserName();
    validUserName(userName);
    User user = findUserByName(userName);
    if (null != user) {
      throw new GlobalException(ResultCode.USERNAME_ALREADY_REGISTER);
    }
    user = BeanUtils.copyProperties(dto, User.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setSignature("");
    this.save(user);
    log.info("id:{},{},{}", user.getId(), dto.getUserName(), dto.getNickName());
  }

  private void validUserName(String userName) {
    if (StrUtil.isBlank(userName)) {
      throw new BusinessException("이름이 비어있다");
    }
    userName = userName.trim();
    if (userName.indexOf("익명") == 0) {
      throw new BusinessException("사용할수 없는 키워드가 포함되었다");
    }
    for (UserEnum.RegisterRromEnum e : UserEnum.RegisterRromEnum.values()) {
      if (userName.indexOf(e.getMsg()) == 0) {
        throw new BusinessException("이 이름은 사용할 수 없다");
      }
    }
  }

  @Override
  public User findUserByName(String username) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(User::getUserName, username);
    return this.getOne(queryWrapper);
  }

  @Transactional
  @Override
  public void update(UserVO vo) {
    SessionContext.UserSessionInfo session = SessionContext.getSession();
    if (!session.getId().equals(vo.getId())) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "Modification of other users' information is not allowed!");
    }
    User user = this.getById(vo.getId());
    if (null == user) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "User does not exist");
    }
    if (!user.getNickName().equals(vo.getNickName()) || !user.getHeadImageThumb()
        .equals(vo.getHeadImageThumb())) {
      QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().eq(Friend::getFriendId, session.getId());
      List<Friend> friends = friendService.list(queryWrapper);
      for (Friend friend : friends) {
        friend.setFriendNickName(vo.getNickName());
        friend.setFriendHeadImage(vo.getHeadImageThumb());
      }
      friendService.updateBatchById(friends);
    }
    if (!user.getHeadImageThumb().equals(vo.getHeadImageThumb())) {
      List<GroupMember> members = groupMemberService.findByUserId(session.getId());
      for (GroupMember member : members) {
        member.setHeadImage(vo.getHeadImageThumb());
      }
      groupMemberService.updateBatchById(members);
    }
    user.setNickName(vo.getNickName());
    user.setSex(vo.getSex());
    user.setSignature(vo.getSignature());
    user.setHeadImage(vo.getHeadImage());
    user.setHeadImageThumb(vo.getHeadImageThumb());
    this.updateById(user);
    log.info("{}", user.toString());
  }

  @Override
  public List<UserVO> findUserByNickName(String nickname) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
        .eq(User::getAccountType, UserEnum.AccountType.Plain.getCode())
        .like(User::getNickName, nickname)
        .last("limit 20");
    List<User> users = this.list(queryWrapper);
    List<UserVO> vos = users.stream().map(u -> {
      UserVO vo = BeanUtils.copyProperties(u, UserVO.class);
      vo.setOnline(isOnline(u.getId()));
      return vo;
    }).collect(Collectors.toList());
    return vos;
  }

  @Override
  public List<Long> checkOnline(String userIds) {
    String[] idArr = userIds.split(",");
    List<Long> onlineIds = new LinkedList<>();
    for (String userId : idArr) {
      if (isOnline(Long.parseLong(userId))) {
        onlineIds.add(Long.parseLong(userId));
      }
    }
    return onlineIds;
  }

  @Override
  public UserVO findByUserIdAndFriendId(Long userId, Long friendId) {
    if (!friendService.isFriend(userId, friendId)) {
      return null;
    }
    User user = getById(userId);
    if (user == null) {
      return null;
    }
    return BeanUtils.copyProperties(user, UserVO.class);
  }

  @Override
  public LoginResp oauthLogin(String type, AuthUser authUser) {
    UserEnum.RegisterRromEnum registerRromEnum = UserEnum.RegisterRromEnum.findByMsg(type);
    String userName = type + "@" + authUser.getUsername();
    User user = findUserByName(userName);
    Date date = new Date();
    if (null == user) {
      user = new User();
      user.setUserName(userName);
      user.setRegisterFrom(registerRromEnum.getCode());
      user.setCreatedTime(date);
      user.setPassword(passwordEncoder.encode(userName));
    }
    user.setLastLoginTime(date);
    user.setHeadImage(authUser.getAvatar());
    user.setHeadImageThumb(authUser.getAvatar());
    user.setNickName(authUser.getNickname());
    user.setOauthSrc(JSONObject.toJSONString(authUser));
    user.setSignature(OauthLoginUtils.parseBio(authUser.getRawUserInfo()));
    user.setLastLoginIp(IpUtil.getIpAddr(SessionContext.getRequest()));
    this.saveOrUpdate(user);

    log.info("oauthLogin :{}", user);
    return buildLoginResp(user);
  }

  @Override
  public LoginResp anonymousLogin(AnonymousLoginReq req) {
    User user = lambdaQuery().eq(User::getAnonymouId, req.getAnonymouId()).one();
    boolean newUserFlag = false;
    if (user == null) {
      user = new User();
      user.setAnonymouId(req.getAnonymouId());
      String generatorId = IdUtils.generatorId();
      String name = "익명-" + appCache.incr(CachePrefix.ANONYMOUS_USER_NICK_NAME.name());
      user.setUserName(generatorId);
      user.setNickName(name);
      user.setAccountType(UserEnum.AccountType.Anonymous.getCode());
      user.setCreatedTime(new Date());
      user.setPassword(passwordEncoder.encode("123456"));
      String img = MixUtils.random(Constant.defaultAnonymousUserHeader);
      user.setHeadImageThumb(img);
      user.setHeadImage(img);
      newUserFlag = true;
    }
    user.setLastLoginTime(new Date());
    user.setLastLoginIp(IpUtil.getIpAddr(SessionContext.getRequest()));
    this.saveOrUpdate(user);

    if (newUserFlag) {
      this.anonyUserInit(user);
    }
    return buildLoginResp(user);
  }

  @Override
  public UserVO findByIde(long id) {
    User user = this.getById(id);
    UserVO userVO = BeanUtils.copyProperties(user, UserVO.class);
    userVO.setIpAddress(IpUtil.search(user.getLastLoginIp()));
    return userVO;
  }

  private void anonyUserInit(User user) {
    List<Group> groupList = iGroupService.findByGroupType(GroupEnum.GroupType.Anonymous.getCode());
    if (CollUtil.isEmpty(groupList)) {
      return;
    }
    for (Group g : groupList) {
      iGroupMemberService.joinGroup(g.getId(), g.getName(), user);
    }

  }

  public boolean isOnline(Long userId) {
    String key = RedisKey.IM_USER_SERVER_ID + userId;
    Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
    return serverId != null && serverId >= 0;
  }
}
