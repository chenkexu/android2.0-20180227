package com.orientdata.lookforcustomers.view.home.fragment;

/**
 * Created by wy on 2017/12/6.
 */

public enum TaskStatus {
    //    1 审核中(平台) 2审核未通过(平台) 3准备中(运营商) 4审核未通过(运营商) 5准备中 6待投放 7投放中 8投放完
    //9暂停任务 10恢复任务 11终止任务
    AUDIT("审核中(平台)",1),NOT_THROUGH("审核未通过(平台)",2),IN_PREPERATION_OPERATOR("准备中(运营商)",3),
    NOT_THROUGH_OPERATOR("审核未通过(运营商)",4),IN_PREPERATION("准备中",5),TO_BE_PUT_ON("待投放",6),IN_THE_LAUNCH("7投放中",7),RUN_OUT("投放完",8),
    SUSPEND_TASK("暂停任务",9),RECOVERY("恢复任务",10),TERMINATION("终止任务",11);


    private String name ;
    private int index ;

    private TaskStatus( String name , int index ){
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
                name = AUDIT.getName();
            case 2:
                name =NOT_THROUGH.getName();
            case 3:
                name =IN_PREPERATION_OPERATOR.getName();
            case 4:
                name =NOT_THROUGH_OPERATOR.getName();
            case 5:
                name =IN_PREPERATION.getName();
            case 6:
                name =TO_BE_PUT_ON.getName();
            case 7:
                name =IN_THE_LAUNCH.getName();
            case 8:
                name =RUN_OUT.getName();
            case 9:
                name =SUSPEND_TASK.getName();
            case 10:
                name =RECOVERY.getName();
            case 11:
                name =TERMINATION.getName();
        }
        return name;
    }
    public static int getIndex(String name){
        int index = -1;
        if(name.equals(AUDIT.getName())){
            index = 1;
        }else if(name.equals(NOT_THROUGH.getName())){
            index = 2;
        }else if(name.equals(IN_PREPERATION_OPERATOR.getName())){
            index = 3;
        }else if(name.equals(NOT_THROUGH_OPERATOR.getName())){
            index = 4;
        }else if(name.equals(IN_PREPERATION.getName())){
            index = 5;
        }else if(name.equals(TO_BE_PUT_ON.getName())){
            index = 6;
        }else if(name.equals(IN_THE_LAUNCH.getName())){
            index = 7;
        }else if(name.equals(RUN_OUT.getName())){
            index = 8;
        }else if(name.equals(SUSPEND_TASK.getName())){
            index = 9;
        }else if(name.equals(RECOVERY.getName())){
            index = 10;
        }else if(name.equals(TERMINATION.getName())){
            index = 11;
        }

        return index;
    }
}
