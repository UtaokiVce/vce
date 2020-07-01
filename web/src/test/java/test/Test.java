package test;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.Func;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.weilai9.common.config.aliyun.oss.OSSConfig;
import com.weilai9.common.utils.wechat.ExpressUtil;
import com.weilai9.dao.mapper.RoleMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

public class Test {
    public static void main(String[] args) {

        method1();

    }

    public static String method1(){
        try {
            System.out.println("111111");
            return "666";
        }catch (Exception e){
            return "ex";
        }finally {
            System.out.println("final");
        }

    }

}
