package com.weilai9.web.controller.wechat;

import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.mapper.WxUserMapper;
import com.weilai9.service.admin.CustomerrelationService;
import com.weilai9.service.api.CustomerqinzuanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author xujinhao
 */
@RestController
@RequestMapping("/qz")
@Api(tags = "亲钻相关接口")
public class qinzuanController {

    @Resource
    CustomerrelationService customerrelationService;
    @Resource
    CustomerqinzuanService customerqinzuanService;
    @Resource
    WxUserMapper wxUserMapper;
    @PostMapping("/monthList")
    @ApiOperation(value = "个人月列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageno", value = "页数,从0开始", required = true),
            @ApiImplicitParam(name = "pagesize", value = "每页长度", required = true),

    })
    public Map<String, Object> monthList( Integer pageno, Integer pagesize) {
            return customerrelationService.monthList(pageno,pagesize);
    }
    /**
     *  亲钻首页
     * @return
     */
    @PostMapping("/homeInfo")
    @ApiOperation(value = "亲钻首页")
    public Map<String, Object> homeInfo() {
            return customerrelationService.homeInfo();
    }
    @PostMapping("/monthInfo")
    @ApiOperation(value = "个人月详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年",required = true),
            @ApiImplicitParam(name = "month", value = "月", required = true),
            @ApiImplicitParam(name = "pageno", value = "页数,从0开始", required = true),
            @ApiImplicitParam(name = "pagesize", value = "每页长度", required = true),
    })
    public Map<String, Object> monthInfo(Integer year,Integer month,Integer pageno,Integer pagesize) {
            return customerrelationService.monthInfo(year,month,pageno,pagesize);
    }

    @PostMapping("/todayInfo")
    @ApiOperation(value = "个人今日亲钻")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "订单类型", required = true),
    })
    public Map<String, Object> todayInfo(Integer type) {
        return customerrelationService.todyaInfo(type);
    }

    @PostMapping("/teamList")
    @ApiOperation(value = "团队列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageno", value = "页数,从0开始", required = true),
            @ApiImplicitParam(name = "pagesize", value = "每页长度", required = true),
    })
    public Map<String, Object> teamList(Integer pageno,Integer pagesize) {
            return customerrelationService.teamList(pageno,pagesize);
    }
    @PostMapping("/p_monthInfo")
    @ApiOperation(value = "个人本月亲钻")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "订单类型", required = true),
    })
    public Map<String, Object> monthInfo(Integer type) {
        return customerrelationService.monthInfo(type);
    }

    @PostMapping("/totalInfo")
    @ApiOperation(value = "个人累计亲钻")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", required = true),
    })
    public Map<String, Object> totalInfo(Integer year) {
        return customerrelationService.totalInfo(year);
    }
    @PostMapping("/teamTodayInfo")
    @ApiOperation(value = "团队今日亲钻")
    @ApiImplicitParams({
    })
    public Map<String, Object> teamTodayInfo() {
        return customerqinzuanService.teamTodayInfo();
    }
    @PostMapping("/teamMonthInfo")
    @ApiOperation(value = "团队本月亲钻")
    @ApiImplicitParams({
    })
    public Map<String, Object> teamMonthInfo() {
        return customerqinzuanService.teamMonthInfo();
    }
    @PostMapping("/teamTotalInfo")
    @ApiOperation(value = "团队累计亲钻")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", required = true),
    })
    public Map<String, Object> teamTotalInfo(Integer year) {
        return customerqinzuanService.teamTotalInfo(year);
    }
    @PostMapping("/p_monthWaitInfo")
    @ApiOperation(value = "本月待结算亲钻")
    public Map<String, Object> monthWaitInfo() {
        return customerqinzuanService.monthWaitInfo();
    }

    @PostMapping("/qzList")
    @ApiOperation(value = "亲钻关系列表")
    public Map<String, Object> qzList() {
        return customerqinzuanService.qzList();
    }

//    @PostMapping("/test")
//    @ApiOperation(value = "aaaaaaa")
//    public WxUser test() {
//        WxUser wxUser = new WxUser();
//        wxUser.setOpenid("test").setNickname("测试").setCreateTime(LocalDateTime.now()).insert();
//
//        return wxUser;
//    }
}
