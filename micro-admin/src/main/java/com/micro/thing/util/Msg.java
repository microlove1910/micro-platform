package com.micro.thing.util;

public class Msg {

    public Integer code;

    public String msg;

    public Object data;

    public Msg() {
    }

    public Msg(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Msg success(Object data) {
        return new Msg(200, "success", data);
    }

    public static Msg error(Integer code, String msg) {
        return new Msg(code, msg, null);
    }

    public static Msg error(String msg) {
        return new Msg(500, msg, null);
    }

    public static Msg of(Integer code, String msg, Object data) {
        return new Msg(code, msg, data);
    }
}
