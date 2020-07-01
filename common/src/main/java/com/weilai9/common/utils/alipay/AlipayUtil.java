package com.weilai9.common.utils.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝
 */
@Component
public class AlipayUtil {

    //支付宝第三方支付API地址：https://docs.open.alipay.com/api_1/alipay.trade.create/
    private final String APP_ID = "2021001167669179";
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCS07l+BML7f8TLZuoe2OMXY64a7FuAJOVxMqyiVerIRHCvQ7XwasJvSJ2yJSI/RfDOvRkspc4rfBdRBGumh2RBXPXVjeEYh+dRr50ZwfrM1kTgQkiKFRuCQtmrJUAwhoVjYOvd4uIioW56sHfW2U31utp2KJeIPRc1+59KhdI+HXgo2sT05DcAqqMHLa4R8uQoMDgBsx/7RfUCE0CjVUejNeJHXvgAqYMBtKiYG0W6mCq6vvL0NnOfairj/iuhlcIVO+zL/b7Gh6W6sbTaiSS444PUezDTI0lvTlOw5EqqWJuVqA2rijArN5DAoN94yVCvxYMnRq0oqKcQCM3plVoVAgMBAAECggEBAI1Q1HZy1MvFNL7kZa+c5i0r1nW5SXPzjxW9RSTVdqycGqzFicZEqrD6/jRowyaNBXGl1SYAy4dU3wguLgJQy3CRrVaGQMEghMY9NQKGjgI6tH33reOg3yInf2LW7ooF3zpAHaQo66eTIkdZs24JJpy84+jXtsj8q2tcq/rJBAqdU0TCo9E7U20xF3ijRQLOti/7TepH+UhpRpfkMeZ+aiOxvse72QaIyzKZQIposVcOUQlvYGOJs3JCF7TscMj9i2In+SrLlxyyNSSPoyuzQrHMyyYWu04lqqLjK01Zef7I43rOt/LrX6c+GTKxmtjYyAXtSvzfqJb5xYQ5Uw0e0OECgYEAwxSvh2PVUVQDbMvea70umFmC/kp7YDWd31LIWG5bMhQPHZZJVRspMXARlrk6hrWLqfJCIaFs99dEAUu/siHubvcpD/87ujQpphDzUO13PGBGbyNIn/voKYpV64BlhJqRREiB3tQnSgKbrNKeghsqv+CGuSfS6BQMif37cYFt0C0CgYEAwK1/Haae3vOOw73Zabb9Cdls+75cef4AwnGOGdgTaTN2KM4/H6Rbxcun60/Xlksaw61IE+hy98Yx2efGQQOqBJ1QuKjYTLDRKgsOVycJRPpS5Sey8p2F1scYpa6ktRZ5yRRYWwQQvWiRf6wzcT0OJLINDEwflf2oSHjz7di5+okCgYA4XmzN6VoFUwpeUJZneVXSbeL9CZaW2UdR9YCwlMrDMKqheQCdl+iSQXjDnag8ro65v6e+Qf/TqZ8b5MByf31/EOoLlkuy5Um3k6RLGSLgDk2r5X0hxU9zWWa0UviClYsuOOlV8uWbbB+Bk6Z7n8/9e6pSLCC0ML7izMHwLV8GzQKBgHLcuwPVGa61qW0dV2fDE7P0bxjgUcbT2mYQRfM4MdIsQItw8IaPd7pBf1gzX8UT/t+aBHcBws/c0kkh97k4UvmvQrjixxdo7ThQIPtppQf+5r3/XZluuKPiStoyc7QUswNhYo7XrJcNqHdhE8W07SKJFmg3VTkYX2Lc4CXxZmVpAoGAIwDrKIvXAhQ1VUEH41wX35j7pL6tdQiYpHoDIMuQt/e6Z7JCb7oKxqV9HSFtknVkQf3NBTuxBVylEFyJxvh5qHX/XARQq8g4yHokYNcJPuW3qzTewt9o4y0biU2q46bntkPohMlFxJ/A1F4nR1hGwJTI+HSmN7vzhGUUV1wVNzI=";
    private final String CHARSET = "UTF-8";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApFdTn1kE6NYmMOaCJSCO8RGD9wOZTFCpVS61p9eg8ElnFZp3SwL1CVpOMnyY+JV9CrhM3a4NdHYyi1/zmjL7y5C4wgQb2zW7Vbz39lbdr9EtM0e9B+REQ0RlIM0oS9ClrHjlQbqzHOdTRw5cuWcWFtWi/R9ExNzI5H22iya/HthbKEckgF1U6M+XQbWC+piSeiuPB9PHukEHH+7o5gDWHPoVaaNqB++otuXekijU8JXA5QauHYt3VMCt31qDxzBj2OX84D3TF0OjvInnvz7Di84DGuUs+OjruGgt6YOSw6OO5ZNjPwuBcn3hnWcvPTBMGgY3+rTyi01YxdhyKJgYhQIDAQAB";
    //正式路径为 https://openapi.alipay.com/gateway.do  沙箱 https://openapi.alipaydev.com/gateway.do
    private final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "https://xqsh.cd-weilai.com/alipay/returnUrl";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://www.baidu.com";

    /**
     * app支付
     * @return
     */
    public String alipayApp() {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        //商品说明
        model.setSubject("App支付测试Java");
        //商家订单号 请保证OutTradeNo值每次保证唯一
        model.setOutTradeNo("2017090080002");
        model.setTimeoutExpress("30m");
        //设置金额
        model.setTotalAmount("0.02");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(null);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 当面付
     * @param httpResponse
     * @throws IOException
     */
    public void alipay(HttpServletResponse httpResponse) throws IOException {
        //实例化客户端,填入所需参数
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = "orde2rid13";
        //付款金额，必填
        String total_amount = "1232222.23";
        //订单名称，必填
        String subject = "这个是订单名称";
        //商品描述，可空
        String body = "";
        request.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 回调
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws AlipayApiException
     */
    public String returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        System.out.println("=================================同步回调=====================================");
// 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }

        System.out.println(params);//查看参数都有哪些
        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
//验证签名通过
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号=" + out_trade_no);
            System.out.println("支付宝交易号=" + trade_no);
            System.out.println("付款金额=" + total_amount);

            //支付成功，修复支付状态
            //payService.updateById(Integer.valueOf(out_trade_no));
            return "ok";//跳转付款成功页面
        } else {
            return "no";//跳转付款失败页面
        }
    }
}
