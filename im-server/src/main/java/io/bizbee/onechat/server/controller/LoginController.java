package io.bizbee.onechat.server.controller;

import io.bizbee.onechat.common.core.utils.Result;
import io.bizbee.onechat.common.core.utils.ResultUtils;
import io.bizbee.onechat.server.common.vo.user.AnonymousLoginReq;
import io.bizbee.onechat.server.common.vo.user.LoginReq;
import io.bizbee.onechat.server.common.vo.user.LoginResp;
import io.bizbee.onechat.server.common.vo.user.RegisterReq;
import io.bizbee.onechat.server.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "用户登录和注册")
@RestController
public class LoginController {

  @Autowired
  private IUserService userService;

  @PostMapping("/login")
  @ApiOperation(value = "", notes = "")
  public Result register(@Valid @RequestBody LoginReq dto) {
    LoginResp vo = userService.login(dto);
    return ResultUtils.success(vo);
  }

  @PutMapping("/refreshToken")
  @ApiOperation(value = "", notes = "")
  public Result refreshToken(@RequestHeader("refreshToken") String refreshToken) {
    LoginResp vo = userService.refreshToken(refreshToken);
    return ResultUtils.success(vo);
  }

  @PostMapping("/register")
  @ApiOperation(value = "", notes = "")
  public Result register(@Valid @RequestBody RegisterReq dto) {
    userService.register(dto);
    return ResultUtils.success();
  }

  @PostMapping("/anonymousLogin")
  @ApiOperation(value = "", notes = "")
  public Result anonymousLogin(@RequestBody @Valid AnonymousLoginReq req) {
    LoginResp vo = userService.anonymousLogin(req);
    return ResultUtils.success(vo);
  }
}
