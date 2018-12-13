package com.tk.eventanalysis.support;

/**
 * 返回信息
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(1,"success"),
    /**
     * 失败
     */
    FAIL(-1,"fail"),
    /**
     * 错误
     */
    ERROR(-2,"error"),
    /**
     * 需要登陆
     */
    REQUIRED_LOGIN(1001,"the api required login!"),
    /**
     * 参数非法
     */
    PARAMETER_INVALID(1002,"parameter illegal!"),
    /**
     * 没有权限
     */
    FORBIDDEN(1003,"token expired or doesn't exists!"),
    /**
     * 令牌过期或不存在
     */
    TOKEN_EXPIRED(1004,"token expired or doesn't exists!");
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
