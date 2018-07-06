package com.orientdata.lookforcustomers.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;


public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	
	public AppManager(){
		
	}
	/**
	 * 单一实例
	 */
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	
	
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		synchronized(this) { 
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            	activityStack.get(i).overridePendingTransition(0, 0);  
            }
	    }
		activityStack.clear();
	}
	
	public Boolean isContainActivity(Class<?> cls){
		Boolean flag=false;
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				flag=true;
			}
		}
		return flag;
	}
/*
	  *//**
     * 获得所有IdeaCodeActivity
     * @return
     *//*
    public List<BaseActivity> getAllActivity(){
        ArrayList<BaseActivity> listActivity = new ArrayList<BaseActivity>();
        for (Activity activity : activityStack) {
            listActivity.add((BaseActivity)activity);
        }
        return listActivity;
    }
    *//**
     * 根据Activity名称返回指定的Activity
     * @param name
     * @return
     *//*
    public BaseActivity getActivityByName(String name){
        for (Activity ia : activityStack) {
            if (ia.getClass().getName().indexOf(name) >= 0) {
                return (BaseActivity)ia;
            }
        }
        return null;
    }
    */
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {	}
	}
}