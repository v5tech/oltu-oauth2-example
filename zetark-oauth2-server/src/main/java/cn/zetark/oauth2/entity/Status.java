package cn.zetark.oauth2.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/28.
 */
public class Status implements Serializable {

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
