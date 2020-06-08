package com.excel.excelproject.res;

public class WsResult {
    /**
     * 请求成功常量
     */
    public static final String SUCCESS = "0000";

    /**
     * 请求失败常量
     */
    public static final String FAILD = "9999";

    /**
     * 返回结果 0表示失败 1表示成功
     */
    private String result;
    /**
     * 返回提示信息
     */
    private String msg;
    /**
     * 附加对象，用来存储一些特定的返回信息
     */
    private Object obj;

    /**
     * 构造函数
     */
    public WsResult() {
        result = WsResult.SUCCESS;
    }

    /**
     * 构造函数
     */
    public WsResult(String result) {
        this.result = result;
    }

    /**
     * 构造函数
     */
    public WsResult(String result, Object obj) {
        this.result = result;
        this.obj = obj;
    }

    /**
     * 构造函数
     */
    public WsResult(String result, String msg, Object obj) {
        this.result = result;
        this.msg = msg;
        this.obj = obj;
    }

    /**
     * @return result 调用结果
     */
    public String getResult() {
        return result;
    }
    /**
     * @param result 调用结果
     */
    public void setResult(String result) {
        this.result = result;
    }
    /**
     * @return msg 提示信息
     */
    public String getMsg() {
        return msg;
    }
    /**
     * @param msg 提示信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
    /**
     * @return obj 附加对象
     */
    public Object getObj() {
        return obj;
    }
    /**
     * @param obj 附加对象
     */
    public void setObj(Object obj) {
        this.obj = obj;
    }
}
