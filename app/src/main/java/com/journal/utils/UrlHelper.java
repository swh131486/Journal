package com.journal.utils;

/**
 * 类名称：com.exing.widget
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.05
 */
public class UrlHelper {
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static String DOMAIN = "http://1.194.233.208:8088";
    private static final String ACTION_LOGIN = "/api/Login";
    private static final String GET_LIST_DATA = "/TodoData/GetListData";
    private static final String TODO_DATA = "/api/TodoData";
    private static final String EDITDETAILDATA = "/TodoData/EditDetailData";
    private static final String GETDETAILDATA = "/TodoData/GetDetailData";
    private static final String DELETEDETAILDATA = "/TodoData/DeleteDetailData";


    public static String getDomain() {
        return DOMAIN;
    }

    public static String loadLogin() {
        return getDomain() + ACTION_LOGIN;
    }

    public static String loadGetListDATA() {
        return getDomain() + GET_LIST_DATA;
    }

    public static String loadTodoData(){
        return getDomain() + TODO_DATA;
    }
    public static String loadEditDetailData(){
        return getDomain() + EDITDETAILDATA;
    }
    public static String loadGetDetailData(){
        return getDomain() + GETDETAILDATA;
    }
    public static String loadDeleteDetailData(){
        return getDomain() + DELETEDETAILDATA;
    }

}
