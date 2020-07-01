package com.weilai9.web.controller.wechat;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.weilai9.common.utils.wechat.ApiStatus;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.common.utils.wechat.ReturnUtil;
import com.weilai9.dao.entity.TestOrder;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.vo.wechat.WxOrderVo;
import com.weilai9.service.wechat.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xjh
 * 微信支付相关
 * com.github.binarywang 文档
 */
/**
 * @author xjh
 */
@RequestMapping("/wxpay")
@Api(tags = "微信支付相关接口")
@RestController
public class WxPayController {
    @Resource
    private WxPayService wxService;
    @Resource
    WxUserService userService;
    @Resource
    HttpServletRequest request;
    @Resource
    RedisHandle redisHandle;

    /**
     * 微信支付测试
     *
     * @return message
     */
    @PostMapping("/bindPhoneNum")
    @ApiOperation(value = "微信支付测试", notes = "微信支付测试")
    @ApiImplicitParams({
    })
    public Map<String, Object> payTest() throws WxPayException {
        Map<String, Object> data = new HashMap<>(16);
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        String ipAdrress = JobUtil.getIpAdrress(request);
        System.out.println(ipAdrress);
        TestOrder order = new TestOrder();
        orderRequest.setNotifyUrl("http://132.232.54.221:8888/wxpay/bind");
        orderRequest.setBody("测试商品")
                .setDetail("测试商品详情")
                .setTotalFee(1)
                .setOutTradeNo("20190902000" + RandomUtil.randomNumbers(8))
                .setOpenid("oAJQh5Wk0s2hGFlzrHlSSmC93T9o")
                .setSpbillCreateIp("171.221.150.249")
                .setTradeType("JSAPI");
        order.setNum(orderRequest.getOutTradeNo());
        order.setCreateTime(LocalDateTime.now());
        // order.insertOrUpdate();
        WxPayMpOrderResult result = wxService.createOrder(orderRequest);
        data.put("Result", result);
        System.out.println("--------------------------------------------------");
        return ReturnUtil.returnMap(ApiStatus.SUCCESS, data);

    }

    @PostMapping("/bind")
    @ApiOperation(value = "微信支付回调测试test")
    public String payAcction(@RequestBody String xmlData) throws WxPayException {
        System.out.println("11111+" + xmlData);
        Map<String, Object> data = new HashMap<>(16);
        WxPayOrderNotifyResult wxPayOrderNotifyResult = wxService.parseOrderNotifyResult(xmlData);
        //商户订单号
        String outTradeNo = wxPayOrderNotifyResult.getOutTradeNo();
        //用户标识
        String openid = wxPayOrderNotifyResult.getOpenid();

        QueryWrapper<TestOrder> wrapper = new QueryWrapper();
        wrapper.eq("num", outTradeNo);

        System.out.println("****************************");
        System.out.println(wxPayOrderNotifyResult.toString());
        String success = WxPayNotifyResponse.success("成功");
        System.out.println(success);
        return WxPayNotifyResponse.success("成功");

    }


    /**
     * @param wxOrderVo 订单信息
     * @return
     * @throws WxPayException
     */
    @ApiOperation(value = "统一下单，并组装所需支付参数")
    @PostMapping("/createOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "商品描述，如：腾讯充值中心-QQ会员充值", dataType = "String", required = true),
            @ApiImplicitParam(name = "outTradeNo", value = "商户订单号", dataType = "String", required = true),
            @ApiImplicitParam(name = "totalFee", value = "订单总金额，单位为分", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "productId", value = "商品id", dataType = "String"),
            @ApiImplicitParam(name = "attach", value = "备注信息", dataType = "String"),
    })
    public Map<String, Object> createOrder(WxOrderVo wxOrderVo) throws WxPayException {
        String token = JobUtil.getAuthorizationToken(request);
        WxUser user = (WxUser) redisHandle.get(token);
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        //组装所需支付参数
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = JobUtil.orderInfo2WxPayOrder(wxOrderVo);
        wxPayUnifiedOrderRequest.setSpbillCreateIp(JobUtil.getIpAdrress(request));
        wxPayUnifiedOrderRequest.setOpenid(user.getOpenid());
        System.out.println(wxPayUnifiedOrderRequest.getNotifyUrl());
        wxPayUnifiedOrderRequest.setNotifyUrl("http://132.232.54.221:8888/wxpay/notify/order");

        WxPayMpOrderResult result = wxService.createOrder(wxPayUnifiedOrderRequest);
        System.out.println(result);
        Map<String, Object> data = new HashMap<>(16);
        data.put("result",result);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * <pre>
     * 查询订单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2)
     * 该接口提供所有微信支付订单的查询，商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况：
     * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
     * ◆ 调用被扫支付API，返回USERPAYING的状态；
     * ◆ 调用关单或撤销接口API之前，需确认支付状态；
     * 接口地址：https://api.mch.weixin.qq.com/pay/orderquery
     * </pre>
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     */
    @ApiOperation(value = "查询订单，参数二选一")
    @GetMapping("/queryOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transactionId", value = "微信订单号,参数二选一", dataType = "String"),
            @ApiImplicitParam(name = "outTradeNo", value = "商户系统内部的订单号,参数二选一", dataType = "String"),

    })
    public Map<String, Object> queryOrder(String transactionId, String outTradeNo) throws WxPayException {
        System.out.println(transactionId);
        System.out.println(outTradeNo);
        WxPayOrderQueryResult result= wxService.queryOrder(transactionId, outTradeNo);
        Map<String, Object> data = new HashMap<>(16);
        data.put("result",result);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);

    }

    /**
     * <pre>
     * 关闭订单
     * 应用场景
     * 以下情况需要调用关单接口：
     * 1. 商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2. 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * 注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
     * 接口地址：https://api.mch.weixin.qq.com/pay/closeorder
     * 是否需要证书：   不需要。
     * </pre>
     *
     * @param outTradeNo 商户系统内部的订单号
     */
    @ApiOperation(value = "关闭订单")
    @GetMapping("/closeOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outTradeNo", value = "需要关闭的订单号", dataType = "String", required = true),
    })
    public Map<String,Object> closeOrder(String outTradeNo) throws WxPayException {
        WxPayOrderCloseResult result = wxService.closeOrder(outTradeNo);
        Map<String, Object> data = new HashMap<>(16);
        data.put("result",result);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * <pre>
     * 微信支付-申请退款
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     * 接口链接：https://api.mch.weixin.qq.com/secapi/pay/refund
     * </pre>
     *
     * @param request 请求对象
     * @return 退款操作结果
     */
    @ApiOperation(value = "退款")
    @PostMapping("/refund")
    @ApiOperationSupport(ignoreParameters = {"deviceInfo","refundFee","refundAccount","refundDesc","appid",})
    public Map<String, Object> refund(WxPayRefundRequest request) throws WxPayException {
        WxPayRefundResult result = wxService.refund(request);
        Map<String, Object> data = new HashMap<>(16);
        data.put("result",result);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);

    }
    /**
     * <pre>
     * 微信支付-查询退款
     * 应用场景：
     *  提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，
     *  银行卡支付的退款3个工作日后重新查询退款状态。
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_5
     * 接口链接：https://api.mch.weixin.qq.com/pay/refundquery
     * </pre>
     * 以下四个参数四选一
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refundId      微信退款单号
     * @return 退款信息
     */
    @ApiOperation(value = "退款查询,以下四个参数四选一")
    @GetMapping("/refundQuery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transactionId", value = "微信订单号", dataType = "String"),
            @ApiImplicitParam(name = "outTradeNo", value = "商户订单号", dataType = "String"),
            @ApiImplicitParam(name = "outRefundNo", value = "商户退款单号", dataType = "String"),
            @ApiImplicitParam(name = "refundId", value = "微信退款单号", dataType = "String"),

    })
    public Map<String, Object> refundQuery(String transactionId, String outTradeNo, String outRefundNo, String refundId) throws WxPayException {
        WxPayRefundQueryResult result = wxService.refundQuery(transactionId, outTradeNo, outRefundNo, refundId);
        Map<String, Object> data = new HashMap<>(16);
        data.put("result",result);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/notify/order")
    public String parseOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        System.out.println("********支付成功回调函数********");
        System.out.println("********订单号********"+notifyResult.getOutTradeNo());
        // TODO 业务逻辑  修改订单状态，加总钻，亲钻类型，用户id
        return WxPayNotifyResponse.success("成功");
    }

    @ApiOperation(value = "退款回调通知处理")
    @PostMapping("/notify/refund")
    public String parseRefundNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayRefundNotifyResult result = this.wxService.parseRefundNotifyResult(xmlData);
        // TODO 业务逻辑
        return WxPayNotifyResponse.success("成功");
    }

    /**
     * <pre>
     * 撤销订单API
     * 文档地址：https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_11&index=3
     * 应用场景：
     *  支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，微信支付系统会将此订单关闭；如果用户支付成功，微信支付系统会将此订单资金退还给用户。
     *  注意：7天以内的交易单可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
     *  调用支付接口后请勿立即调用撤销订单API，建议支付后至少15s后再调用撤销订单接口。
     *  接口链接 ：https://api.mch.weixin.qq.com/secapi/pay/reverse
     *  是否需要证书：请求需要双向证书。
     * </pre>
     */
    @ApiOperation(value = "撤销订单")
    @PostMapping("/reverseOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transactionId", value = "微信的订单号，优先使用", dataType = "String"),
            @ApiImplicitParam(name = "outTradeNo", value = "商户订单号", dataType = "String"),
    })
    public Map<String, Object> reverseOrder(WxPayOrderReverseRequest request) throws WxPayException {
        WxPayOrderReverseResult result = wxService.reverseOrder(request);
        Map<String, Object> data = new HashMap<>(16);
        data.put("result",result);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }
    @ApiOperation(value = "企业付款测试")
    @PostMapping("/entPayTest")
    public Map<String,Object> entPayTest() throws WxPayException {
       return userService.entPayTest(wxService);
    }
}


