package com.orientdata.lookforcustomers.view.mine;

/**
 * Created by wy on 2017/12/6.
 */

public enum TransactionType {
    //101支付宝充值 102微信充值 103线下充值 104佣金转余额 201余额支出
    ALIPAY_RECHARGE("支付宝充值",101),WEIXIN_RECHARGE("微信充值",102),LINE_RECHARGE("线下充值",103),
    COMMISSION_RECHARGE("佣金转余额",104),BALANCE_EXPENDITURE("余额支出",201);


    private String name ;
    private int index ;

    private TransactionType(String name , int index ){
        this.name = name ;
        this.index = index ;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public static String getName(int index){
        String name = "";
        switch (index){
            case 101:
                name = ALIPAY_RECHARGE.getName();
                break;
            case 102:
                name =WEIXIN_RECHARGE.getName();
                break;
            case 103:
                name =LINE_RECHARGE.getName();
                break;
            case 104:
                name =COMMISSION_RECHARGE.getName();
                break;
            case 201:
                name =BALANCE_EXPENDITURE.getName();
                break;
        }
        return name;
    }
    public static int getIndex(String name){
        int index = -1;
        if(name.equals(ALIPAY_RECHARGE.getName())){
            index = 101;
        }else if(name.equals(WEIXIN_RECHARGE.getName())){
            index = 102;
        }else if(name.equals(LINE_RECHARGE.getName())){
            index = 103;
        }else if(name.equals(COMMISSION_RECHARGE.getName())){
            index = 104;
        }else if(name.equals(BALANCE_EXPENDITURE.getName())){
            index = 201;
        }

        return index;
    }
}
