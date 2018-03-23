package com.orientdata.lookforcustomers.network;

/**
 * Created by wy on 2017/10/30.
 */

public class HttpConstant {
   // public static final String HOST_URL = "http://wrtest.geekdana.com:1580/netrun-app/";
//    public static final String HOST_URL = "http://wrc.geekdana.com/netrun-app/";
//   public static final String HOST_URL = "http://wrtest.geekdana.com:1580/netrun-app/";
   public static final String HOST_URL = "http://172.31.9.91:8080/netrun-app/";
    /*发短信*/
    public static final String SEND_SMS = HOST_URL + "/SMSCode";
    /*注册接口*/
    public static final String REGISTER_ACCOUNT = HOST_URL + "/pub/userregist";
    /*登录接口*/
    public static final String LOGIN_ACCOUNT = HOST_URL + "/pub/userlogin";
    /*退出登录*/
    public static final String LOGOUT_ACCOUNT = HOST_URL + "pub/logOut";
    /*重置密码*/
    public static final String RESET_PASSWORD = HOST_URL + "/pub/resetpwd";
    /*验证码登录*/
    public static final String CODE_LOGIN_ACCOUNT = HOST_URL + "/pub/usercodelogin";
    /*获取省市*/
    public static final String SELECT_CITY = HOST_URL + "pub/selectPinYinCity";
    public static final String SELECT_PROVINCE_CITY = HOST_URL + "pub/selectPinYinCity";
    /*我的认证信息*/
    public static final String QUERY_CERTIFICATION_MSG = HOST_URL + "app/user/selectCertificate";
    /*一级行业*/
    public static final String SELECT_INDUSTRY = HOST_URL + "pub/selectTradeList";
    /*二级行业*/
    public static final String SELECT_SUB_STRY = HOST_URL + "pub/selectTradeTwoList";
    /*创建认证*/
    public static final String SUBMIT_CERTIFICATION_URL = HOST_URL + "app/user/insertcertificate";
    /*查询定向设置*/
    public static final String SELECT_SETTING = HOST_URL + "pub/selectSettingList";
    public static final String UPLOAD_PIC = HOST_URL + "app/user/uploadPic";
    /*展示模板行业列表*/
    public static final String SELECT_USER_MODEL_LIST = HOST_URL + "pub/selectusermubanList";
    /*图片库列表*/
    public static final String USER_PIC_LIST = HOST_URL + "app/user/selectUserPicList";
    /*删除图库*/
    public static final String DELETE_USER_PIC = HOST_URL + "app/user/delpicList";
    /*下一步验证*/
    public static final String SELECT_NEXT_STEP_CHECK = HOST_URL + "app/selectPretask";
    /*创建任务*/
    public static final String INSERT_CREATE_TASK = HOST_URL + "app/task/insertTask";//app/task/insertTask
    /*落地页提交*/
    public static final String INSERT_USER_PAGE = HOST_URL + "app/user/inserUserPage";
    /*寻客列表*/
    public static final String USER_TASK_LIST = HOST_URL + "app/task/usertasklist";
    /*寻客详情*/
    public static final String USER_TASK_INFO = HOST_URL + "app/task/userXunTaskInfo";
    /*删除寻客任务*/
    public static final String DELETE_TASK = HOST_URL + "app/task/delTask";
    /*报表数据*/
    public static final String REPORT_DATA = HOST_URL + "app/report/selectUserData";


    /*任务类型设置*/
    public static final String SELECT_TASK_TYPE = HOST_URL + "app/task/taskInfo";

    /*获取落地页列表*/
    public static final String SELECT_AD_PAGES = HOST_URL + "app/user/selectPageTemplateList";

    /*获取user信息*/
    public static final String SELECT_USER_INFO = HOST_URL + "app/user/pageForMeInfo";

    /*获取Banner信息*/
    public static final String SELECT_BANNER_INFO = HOST_URL + "pub/banner/selectBannerList";

    /*获取置顶公告列表信息*/
    public static final String SELECT_TOP_ANNOUNCEMENT = HOST_URL + "app/announcement/selectTopAnnouncement";

    /*获取公告列表信息*/
    public static final String SELECT_ANNOUNCEMENT_LISTS = HOST_URL + "app/announcement/selectAnnouncementList";

    /*微信支付*/
    public static final String PAY_URL = HOST_URL + "app/money/addChargeMoney";

    public static final String WX_APPID = "wx94a5977e9ef0661b";

    public static final String HOST_IMAGE_URL = "http://wrtest.geekdana.com:1580/netrun-app/";

    ///app/user/delUserPage

    public static final String DEL_USER_PAGE = HOST_URL + "app/user/delUserPage";

    //app/invoice/selectUserInvoiceTaskList
    /*可开发票的任务*/
    public static final String SELECT_CAN_INVOICES = HOST_URL + "app/invoice/selectUserInvoiceTaskList";
    /*转入佣金*/
    public static final String UPDATE_TO_BALANCE = HOST_URL + "app/commission/updateToBalance";
    /*佣金提现*/
    public static final String UPDATE_USER_COMMISSION = HOST_URL + "app/commission/updateUserCommission";
    /*佣金提现下一步验证*/
    public static final String UPDATE_USER_COMMISSION_VERTIFICATE = HOST_URL + "app/commission/tixianNext";
    /*佣金明细列表*/
    public static final String COMMISSION_LIST = HOST_URL + "app/commission/selectUserCommissionList";
    /*余额列表*/
    public static final String BALANCE_LIST = HOST_URL + "app/balance/selectUserBalanceList";
    /*余额详情*/
    public static final String BALANCE_DETAIL = HOST_URL + "app/balance/selectBalanceInfo";
    /*消息列表*/
    public static final String MSG_LIST = HOST_URL + "app/msg/selectMsgList";
    /*删除消息*/
    public static final String UPDATE_MSG = HOST_URL + "app/msg/updateMsg";
    public static final String MSG_DETAIL = HOST_URL + "app/msg/selectMsgInfo";
    /*我的-首页接口*/
    public static final String SELECT_SHOW_MY_INFO = HOST_URL + "app/user/selectShowMyInfo";

    /*线下支付-上传凭证*/
    public static final String SELECT_ADD_OFFLINE_CHARGE = HOST_URL + "app/user/addOfflineCharge";

    /*线下支付-上传凭证*/
    public static final String SELECT_OFFLINE_CHARGE_LISTS = HOST_URL + "app/user/offlineChargeList";

    /*线下支付-上传凭证*/
    public static final String SELECT_AREA_LISTS = HOST_URL + "pub/selectareaList";

    /*线下支付-上传凭证*/
    public static final String CREATE_URL = HOST_URL + "app/user/createUrl";

    /*线下支付-获取最小充值金额*/
    public static final String CHARGE_MIN_MONEY = HOST_URL + "app/user/chargeMinMoney";

    /*线下支付-获取最小充值金额*/
    public static final String UPDATE_INVOICE = HOST_URL + "app/invoice/updateInvoice";

    /*发票记录*/
    public static final String SELECT_INVOICE_LIST = HOST_URL + "app/invoice/selectInvoiceList";

    /*发票详情*/
    public static final String SELECT_USER_INVOICE_INFO = HOST_URL + "app/invoice/selectUserInvoiceInfo";

    /*发票详情*/
    public static final String UPDATE_INVOICE_AGAIN = HOST_URL + "app/invoice/updateInvoiceAgain";

    /*发票详情*/
    public static final String SELECT_ANNOUNCEMENT_INFO = HOST_URL + "app/announcement/selectAnnouncementInfo";

}
