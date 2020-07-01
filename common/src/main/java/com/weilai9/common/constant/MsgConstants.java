package com.weilai9.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息
 */
public class MsgConstants {

    /**
     * 消息状态
     */
    //已读
    public static Integer MSG_IS_READ = 1;
    //未读
    public static Integer MSG_NOT_READ = 0;


    /**
     * 消息类型
     */
    public enum MsgType {
        MSG_SYSTEM(0, "系统消息"),
        MSG_NORMAL(1, "用户消息");
        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        MsgType(int code, String value) {
            this.code = code;
            this.value = value;
        }
    }


}
