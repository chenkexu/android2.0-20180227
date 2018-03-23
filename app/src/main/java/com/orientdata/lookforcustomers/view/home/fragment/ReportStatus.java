package com.orientdata.lookforcustomers.view.home.fragment;

/**
 * Created by wy on 2017/12/6.
 */

public enum ReportStatus {
    //昨日 yesterday2 最近7天 3 上周  4本月  5上个月  6某一天
    YESTERDAY("昨日",1),LATEST_SEVEN("最近7天",2),LAST_WEEK("上周",3),THIS_MONTH("本月",4),LAST_MONTH("上个月",5),DAY("某一天",6);

    private String name ;
    private int index ;

    ReportStatus(String name , int index ){
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
            case 1:
                name = YESTERDAY.getName();
            case 2:
                name =LATEST_SEVEN.getName();
            case 3:
                name =LAST_WEEK.getName();
            case 4:
                name =THIS_MONTH.getName();
            case 5:
                name =LAST_MONTH.getName();
            case 6:
                name =DAY.getName();
        }
        return name;
    }
    public static int getIndex(String name){
        int index = -1;
        if(name.equals(YESTERDAY.getName())){
            index = 1;
        }else if(name.equals(LATEST_SEVEN.getName())){
            index = 2;
        }else if(name.equals(LAST_WEEK.getName())){
            index = 3;
        }else if(name.equals(THIS_MONTH.getName())){
            index = 4;
        }else if(name.equals(LAST_MONTH.getName())){
            index = 5;
        }else if(name.equals(DAY.getName())){
            index = 6;
        }
        return index;
    }
}
