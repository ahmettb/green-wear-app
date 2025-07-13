package com.finalYearProject.product.controller;

public abstract  class BaseController {

    static final String API = "/api";
    static final String PUBLIC = API + "/public";
    static final String AUTH = API + "/auth";
    static final String DASHBOARD= API + "/dashboard";
    static final String RECEIPT = API + "/receipt";
    static final String SCHEDULE = API + "/scheduled-jobs";
    static final String USER = API + "/user";
    static final String PAYMENT = API + "/payment";
    static final String COUPON = API + "/coupon";

    //auth
    static final String AUTH_REGISTER = AUTH + "/register";
    static final String AUTH_LOGIN = AUTH + "/login";
    static final String AUTH_LOGOUT= AUTH + "/logout";


    //USER
    static final String USER_GET_INFO_BY_TOKEN = USER + "/getInfoByToken/{token}";
    static final String USER_GET_WARDROBE = USER + "/get-wardrobe";
    static final String USER_GET_WARDROBE_MONTH = USER + "/get-wardrobe-month";
    static final String USER_GET_BY_ID = USER + "/get/{id}";

    static final String USER_UPDATE = USER + "/update-user";


    //USER
    static final String PAYMENT_CREATE_ORDER = PAYMENT + "/create-order";
    static final String COUPUN_GET_COUPON = COUPON + "/get-coupon/{userId}";
    static final String GET_ORDER_LIST = PAYMENT + "/get-order-list/{userId}";
    static final String GET_ORDER_DETAIL = PAYMENT + "/get-order-detail/{orderId}";

}
