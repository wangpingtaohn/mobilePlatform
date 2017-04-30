package com.wpt.frame.net;

/**
 * @author liujun
 * @param <T>
 */
public class Result<T> {
    //正常
    public static final int OK = 0;
    
    //客户端错误
    public static final int CLIENT_ERROR_NETWORK = 1;
    public static final int CLIENT_ERROR_NO_RESULT = 2;
    public static final int CLIENT_ERROR_JSON_ERROR = 3;
    
    //接口通用错误代码
    public static final int COMMON_ERROR_APP_INVALID = 101;
    public static final int COMMON_ERROR_NO_PERMISSION = 102;
    public static final int COMMON_ERROR_CLIENT_ID_INVALID = 103;
    public static final int COMMON_ERROR_USER_AGENT_INVALID = 104;
    public static final int COMMON_ERROR_TIMESTAMP_UNSYNCRONIZED = 105;
    public static final int COMMON_ERROR_PARAMETER_ERROR = 106;
    public static final int COMMON_ERROR_SERVER_MAINTAINING = 107;
    public static final int COMMON_ERROR_UNKOWN_ERROR = 108;
    public static final int COMMON_ERROR_DATABASE_ERROR = 109;
    public static final int COMMON_ERROR_DATA_NOT_MATCHING = 110;
    
    //用户信息模块接口错误代码
    public static final int USER_ERROR_NO_USER = 201;
    public static final int USER_ERROR_SIGNIN_ALREADY = 202;
    public static final int USER_ERROR_BUY_BEHAVIOR_NOT_EXIST = 203;
    public static final int USER_ERROR_ORDER_NOT_EXIST = 204;
    
    //新闻模块接口错误代码
    public static final int NEWS_ERROR_COMMENT_ADD_ERROR = 301;
    public static final int NEWS_ERROR_COMMENT_DELETE_ERROR = 302;
    
    //商品接口模块错误代码
    public static final int SHOP_ERROR_AUCTION_NOT_EXIST = 401;
    public static final int SHOP_ERROR_GOODS_NOT_EXIST = 402;
    public static final int SHOP_ERROR_NOT_ENOUGH_CREDITS = 403;
    public static final int SHOP_ERROR_GOODS_SALE_OVER = 404;
    
    //活动接口模块错误代码
    public static final int ACTIVITY_ERROR_NO_TOPIC = 501;
    public static final int ACTIVITY_ERROR_TOPIC_NOT_START_OR_OVER = 502;
    public static final int ACTIVITY_ERROR_SIDE_ERROR = 503;
    public static final int ACTIVITY_ERROR_COMMENT_NO_CONTENT = 504;
    
    //辅助接口模块错误代码

    
    private int total;
    private int errorCode = OK;
    private T result;

    public Result(T result) {
        this.result = result;
    }

    public Result(int errorCode, T result) {
        this.errorCode = errorCode;
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSucceeded() {
        return errorCode == OK;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
