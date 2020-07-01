package com.weilai9.common.constant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel("统一返回结果")
@Data
@AllArgsConstructor
public class Result {
    @ApiModelProperty("响应码")
    private int code;
    @ApiModelProperty("响应消息体")
    private String msg;
    @ApiModelProperty("响应结果内容")
    private Object data;
    @ApiModelProperty("响应开始页")
    private long curPage;
    @ApiModelProperty("响应总数")
    private long totalCount;
    @ApiModelProperty("响应页码")
    private long pageSize;

    public Result(int code) {
        this.code = code;

    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 分页封装
     *
     * @param page
     */
    public Result(IPage page) {
        this.code = 1;
        this.msg = "success";
        this.data = page.getRecords();
        this.curPage = page.getCurrent();
        this.pageSize = page.getSize();
        this.totalCount = page.getTotal();
    }

    public Result(Object data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public Result(Object data, int curPage, int pageSize, int totalCount) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public int getCode() {
        return code;
    }

    public static Result OK() {
        return new Result(200, "success");
    }

    public static Result OK(String msg) {
        return new Result(200, msg);
    }

    public static Result NotLogin() {
        return Error(110, "未登录");
    }

    public static Result NotAccess() {
        return Error(110, "没有权限,或登录已过期");
    }

    public static Result ErrorBusy() {
        return new Result(-1, "网络繁忙，请稍后再试...");
    }

    public static Result Error(String msg) {
        return new Result(0, msg);
    }

    public static Result Error(int code, String msg) {
        return new Result(code, msg);
    }
}
