package com.weilai9.common.utils.wechat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.RedisKey;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.vo.wechat.WxOrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static com.weilai9.common.utils.wechat.SysConst.WX_PAY_NOTIFY_URL;
import static com.weilai9.common.utils.wechat.SysConst.WX_PAY_TRADE_TYPE;

/**
 * 项目特有工具类,与项目具体业务相关
 */
public class JobUtil {

    /**
     * 获取后台登陆用户信息
     * @param request
     * @param redisHandle
     * @return
     */
    public static TokenUser getTokenUser(HttpServletRequest request,RedisHandle redisHandle){
        String c_authorization = request.getHeader("C_Authorization");
        TokenUser tokenUser = (TokenUser) redisHandle.get(RedisKey.TOKEN_USER + c_authorization);
        return tokenUser;
    }

    /**
     * 获取访问携带的的token校验
     * 网页、图片访问与其他接口访问token携带有区别
     *
     * @param request 访问的HttpServletRequest
     * @return
     */

    public static String getAuthorizationToken(HttpServletRequest request) {
        String url = request.getRequestURI();
        String token;
        //网页、图片类访问区别其他接口访问的路径
        String str = "webPages";
        if (url.contains(str)) {
            token = request.getParameter("Authorization");
        } else {
            token = request.getHeader("Authorization");
        }
        String s = StrUtil.subSufByLength(token, token.length() - 4);
        return s;
    }

    /**
     * 轮流获取平台账号ID
     * @param redisHandle
     * @return
     */
    public  static int getXiaoQinId(RedisHandle redisHandle){
        Integer xiaoqin_id = (Integer)redisHandle.get("xiaoqin_id");
        if (xiaoqin_id==null||xiaoqin_id==0){
            xiaoqin_id= -5;
        }
        return xiaoqin_id;
    }

    /**
     * 将emoji表情替换成空串
     *
     * @param source 需要替换的昵称
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (source != null && source.length() > 0) {
            return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        } else {
            return source;
        }
    }


    /**
     * 更新天气情况
     *
     * @return
     */
    public static JSONObject upDateWeatherData() {
        JSONObject weather = getWeatherNow();
        String condCode = weather.getString("cond_code");
        String condTxt = weather.getString("cond_txt");
        String tmp = weather.getString("tmp");
        JSONObject air = getAirNow();
        String qlty = air.getString("qlty");
        JSONObject weatherData = new JSONObject();
        weatherData.put("condCode", condCode);
        weatherData.put("iconUrl", "/assets/weatherIcon/" + 100 + ".png");
        weatherData.put("condTxt", condTxt);
        weatherData.put("tmp", tmp);
        weatherData.put("qlty", qlty);
        return weatherData;
    }

    public static JSONObject getWeatherNow() {
        HashMap<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("key", SysConst.HE_WEATHER_KEY);
        paramMap.put("location", SysConst.HE_WEATHER_LOCATION);
        String result = HttpUtil.get("https://free-api.heweather.net/s6/weather/now", paramMap);
        JSONObject json = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("HeWeather6").getString(0));
        return json.getJSONObject("now");
    }

    public static JSONObject getAirNow() {
        HashMap<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("key", SysConst.HE_WEATHER_KEY);
        paramMap.put("location", SysConst.HE_WEATHER_LOCATION);
        String result = HttpUtil.get("https://free-api.heweather.net/s6/air/now", paramMap);
        JSONObject json = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("HeWeather6").getString(0));
        return json.getJSONObject("air_now_city");
    }

    private static final Integer RANDOM_START_UPPER = 65;
    private static final Integer RANDOM_END_UPPER = 90;
    private static final Integer RANDOM_START_LOWER = 97;
    private static final Integer RANDOM_END_LOWER = 122;
    private static final String RANDOM_SEED = "bvqdhfg730o91utpskaln64xij2w8mryec5";
    private static final Integer RANDOM_SEED_LENGTH = RANDOM_SEED.length();

    /**
     * 随机数，随机字符串相关方法
     * @param bit
     * @param isUpper
     * @return
     */
    public static String getRandomStr(Integer bit, Boolean isUpper) {
        StringBuffer sBuffer = new StringBuffer();

        for(int i = 0; i < bit; ++i) {
            Random random = new Random();
            char bitChar;
            if (isUpper) {
                bitChar = (char)(random.nextInt(RANDOM_END_UPPER) % (RANDOM_END_UPPER - RANDOM_START_UPPER + 1) + RANDOM_START_UPPER);
                sBuffer.append(bitChar);
            } else {
                bitChar = (char)(random.nextInt(RANDOM_END_LOWER) % (RANDOM_END_LOWER - RANDOM_START_LOWER + 1) + RANDOM_START_LOWER);
                sBuffer.append(bitChar);
            }
        }

        return sBuffer.toString();
    }

    /**
     * 生成唯一身份码
     * @param id 用户id 保证唯一性
     * @param len 长度
     * @return
     */
    public static String getSN(int id, int len) {
        StringBuffer sb = new StringBuffer();
        sb.append(calc(id, ""));
        if (sb.length() < len) {
            sb.append("z");

            while(sb.length() < len) {
                sb.append(RANDOM_SEED.charAt((int)Math.round(Math.random() * 100.0D) % RANDOM_SEED_LENGTH));
            }
        }

        return sb.toString().toUpperCase();
    }

    private static String calc(int id, String str) {
        str = RANDOM_SEED.charAt(id % RANDOM_SEED_LENGTH) + str;
        int l = (int)Math.floor((double)(id / RANDOM_SEED_LENGTH));
        return l == 0 ? str : calc(l, str);
    }

    public static String randomStr(int length) {
        StringBuilder sb = new StringBuilder();
        String base = "0123456789";
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            sb.append(base.charAt(random.nextInt(9)));
        }

        return sb.toString();
    }

    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }


    /**
     * 订单信息转化为通用下单信息
     * @param wxOrderVo
     * @return
     */
    public static WxPayUnifiedOrderRequest orderInfo2WxPayOrder(WxOrderVo wxOrderVo){
        String timeExpire= DateUtil.format(DateUtil.offsetMinute(DateUtil.date(),5),"yyyyMMddHHmmss");
        String timeStart= DateUtil.format(DateUtil.date(),"yyyyMMddHHmmss");

        WxPayUnifiedOrderRequest orderRequest =new WxPayUnifiedOrderRequest();

        orderRequest.setTradeType(WX_PAY_TRADE_TYPE);
        orderRequest.setTotalFee(wxOrderVo.getTotalFee());
        orderRequest.setBody(wxOrderVo.getBody());
        orderRequest.setTimeExpire(timeExpire);
        orderRequest.setOutTradeNo(wxOrderVo.getOutTradeNo());
        orderRequest.setNotifyUrl(WX_PAY_NOTIFY_URL);
        orderRequest.setTimeStart(timeStart);
        orderRequest.setProductId(wxOrderVo.getProductId());

        return orderRequest;
    }

    /**
     * 存储图片到本地
     *
     * @param file 上传的文件
     * @param file 文件的类型枚举，其中包含了保存、访问路径
     * @return 文件访问路径
     */
    public static String multipartFileSaveLocal(MultipartFile file, FileTypeEnum fte) {
        // 获取文件后缀
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        String fileNameNew = RandomUtil.randomString(24) + prefix;
        String fileLocalPath = fte.getSavePath() + fileNameNew;
        File fl = FileUtil.touch(fileLocalPath);
        FileWriter writer = new FileWriter(fl);
        try {
            writer.writeFromStream(file.getInputStream());
        } catch (IORuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fte.getAccessPath() + fileNameNew;
    }

    public static String[] splitSite(String site){

        site = site.replaceAll("\"","");
        site = site.replaceAll(" ","");
        site = site.substring(1,site.length()-1);
        String[] split = site.split(",");
        return split;

    }


}
