package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wy on 2017/11/19.
 * 定向设置
 */

public class SettingOut implements Serializable {
    private List<String> age;//用户年龄
    private List<String> sex;//性别
    private List<String> education;//教育程度
    private List<String> belong;//归属地
    private List<String> xiaofei;//消费能力
    private List<BaseTagImportOut> jixing;//机型
    private List<InterestTagImportOut> ico;//兴趣
    private boolean visibility;


    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public List<String> getAge() {
        return age;
    }

    public void setAge(List<String> age) {
        this.age = age;
    }

    public List<String> getSex() {
        return sex;
    }

    public void setSex(List<String> sex) {
        this.sex = sex;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getBelong() {
        return belong;
    }

    public void setBelong(List<String> belong) {
        this.belong = belong;
    }

    public List<String> getXiaofei() {
        return xiaofei;
    }

    public void setXiaofei(List<String> xiaofei) {
        this.xiaofei = xiaofei;
    }

    public List<BaseTagImportOut> getJixing() {
        return jixing;
    }

    public void setJixing(List<BaseTagImportOut> jixing) {
        this.jixing = jixing;
    }

    public List<InterestTagImportOut> getIco() {
        return ico;
    }

    public void setIco(List<InterestTagImportOut> ico) {
        this.ico = ico;
    }
}
