package com.weilai9.web.runner;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.weilai9.common.constant.RedisKey;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.dao.entity.SysInterface;
import com.weilai9.service.base.RoleService;
import com.weilai9.service.base.SysInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Component
public class InterfaceAndRoleUtil {

    @Value("${knife4j.basic.enable}")
    Boolean enable;

    @Value("${server.port}")
    String port;


    @Resource
    RoleService roleService;
    @Resource
    SysInterfaceService sysInterfaceService;
    @Resource
    RedisUtil redisUtil;

    /**
     * 初始化接口列表 加密环境，不初始化
     */
    public void getInterfaceInfo() {
        //加密环境，不初始化
        if (!enable) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:"+port+"/v2/api-docs";
            System.out.println(url+"dsushddjrghyrddbdrghhdf");
            ResponseEntity<JSONObject> entity = restTemplate.getForEntity(url, JSONObject.class);
            JSONObject body = entity.getBody();
            String tags = body.get("tags", String.class);
            JSONArray tagsArray = JSONUtil.parseArray(tags);
            Map<String, Integer> pid = new HashMap<>();
            for (Object o : tagsArray) {
                JSONObject obj = JSONUtil.parseObj(o);
                String name = obj.get("name").toString();
                String description = obj.get("description").toString();
                SysInterface anInterface = new SysInterface();
                anInterface.setIfName(name);
                anInterface.setIfDs(description);
                anInterface.setIfPid(0);
                anInterface.setIfStatus(1);
                SysInterface selectOne = sysInterfaceService.getOne(new QueryWrapper<SysInterface>().eq("if_name", name));
                Integer id;
                if (null == selectOne) {
                    //新增
                    anInterface.setAddUid(0L);
                    anInterface.setUpdateUid(0L);
                    anInterface.setUpdateTime(new Date());
                    anInterface.setAddTime(new Date());
                    sysInterfaceService.save(anInterface);
                    id = anInterface.getIfId();
                } else {
                    //更新
                    selectOne.setIfDs(description);
                    anInterface.setUpdateUid(0L);
                    anInterface.setUpdateTime(new Date());
                    sysInterfaceService.update(selectOne, new UpdateWrapper<SysInterface>().eq("if_name", description));
                    id = selectOne.getIfId();
                }
                pid.put(name, id);
            }
            String paths = body.get("paths", String.class);
            Map<String, Map> map = JSONUtil.toBean(paths, Map.class);
            Set<String> keySet = map.keySet();
            Map<String, String> res = new HashMap<>();
            for (String key : keySet) {
                Map<String, Map> method = MapUtil.get(map, key, Map.class);
                Set<String> m = method.keySet();
                for (String s : m) {
                    Map map1 = MapUtil.get(method, s, Map.class);
                    String ids = MapUtil.get(map1, "tags", String.class);
                    String parameters = MapUtil.get(map1, "parameters", String.class);
                    String paramType = null;
                    if (StringUtils.isNotBlank(parameters)) {
                        JSONArray jsonArray = JSONUtil.parseArray(parameters);
                        Object o = jsonArray.get(0);
                        JSONObject jsonObject = JSONUtil.parseObj(o);
                        Object in = jsonObject.get("in");
                        paramType = StrUtil.toString(in);
                    }
                    String summary = MapUtil.get(map1, "summary", String.class);
                    String description = MapUtil.get(map1, "description", String.class);
                    ids = ids.replace("[\"", "").replace("\"]", "");
                    SysInterface in = new SysInterface();
                    in.setIfPid(pid.get(ids));
                    in.setIfName(summary);
                    in.setIfUrl(key);
                    in.setIfMethod(s);
                    in.setIfDs(description);
                    in.setIfQpt(paramType);
                    in.setIfStatus(1);
                    in.setAddUid(0L);
                    in.setUpdateUid(0L);
                    in.setAddTime(new Date());
                    in.setUpdateTime(new Date());
                    boolean save = sysInterfaceService.saveOrUpdate(in, new QueryWrapper<SysInterface>().eq("if_url", key));
                    if (!save) {
                        System.err.println("接口名称：" + key + ",请求方法：" + s + ",描述：" + ids);
                    }
                }
            }
        }
    }

    /**
     * 缓存接口
     */
    public List<SysInterface> redisUrl() {
        return sysInterfaceService.redisUrl();
    }


    /**
     * 获取缓存接口
     */
    public Object getRedisUrl() {
        Object ifList = redisUtil.get(RedisKey.IF_LIST);
        if (Objects.isNull(ifList)) {
            ifList = redisUrl();
        }
        return ifList;
    }

    /**
     * 缓存角色
     */
    public void redisRole() {
        roleService.redisRole();
    }

    /**
     * 初始化超级管理员接口权限
     */
    public void initIf() {
        //获取全部接口
        List<SysInterface> list = sysInterfaceService.list();
        //获取id列表
        StringBuilder sb = new StringBuilder();
        for (SysInterface sysInterface : list) {
            sb.append(sysInterface.getIfId()).append(",");
        }

        String ifIds = "";
        if (sb.length() > 0) {
            ifIds = sb.substring(0, sb.length() - 1);
        }
        //修改权限
        roleService.updateIf(1, ifIds, 0L);
    }
}
