package com.weilai9.web.controller.wxtest;

import java.util.Map;

public interface WxService {

    String payBack(String resXml);

    Map doUnifiedOrder() throws Exception;
}