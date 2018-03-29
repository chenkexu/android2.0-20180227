package com.orientdata.lookforcustomers.base;

import java.util.ArrayList;

/**
 * activity 关闭事件
 */
public class CloseEvent {
    public static final int CLOSE_IN_LIST_ACTIVITY = 1;//关闭在列表中的activity

    private ArrayList<String> classNameList;
    private int closeType;
    public CloseEvent(int closeType, Class...classes) {
        this.closeType = closeType;
        classNameList = new ArrayList<>();
        if (classes != null) {
            for (int i=0;i<classes.length;i++) {
                classNameList.add(classes[i].getName());
            }
        }
    }
    public ArrayList<String> getMsg(){
        return classNameList;
    }

    public int getCloseType() {
        return closeType;
    }
}
