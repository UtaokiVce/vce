#注意
1.不建议使用@pathvirable
2.@ApiOperation(value = "登录", notes = "手机号密码登录")
      @ApiImplicitParams({
              @ApiImplicitParam(value = "手机号", name = "phone", required = true),
              @ApiImplicitParam(value = "密码", name = "password", required = true)
      })
3. @ApiOperation(value = "登出", notes = "用户操作退出登录")
4.@MustLogin
5.@MustLogin(isAdmin=true)