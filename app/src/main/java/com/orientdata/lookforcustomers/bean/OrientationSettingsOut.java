package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/12/6.
 */

public class OrientationSettingsOut implements Serializable {
    private List<String> jixing;//	是	list<String>	机型数组
    private List<String> xingqu;//	是	list<String>	兴趣数组
    private int userId;//	是	int	用户id
    private int taskId;//	是	int	任务id
    private String ageF;//	是	String	用户年龄1
    private String ageB;//	是	String	用户年龄2
    private String educationLevelF;//	是	String	教育程度1
    private String educationLevelB;//	是	String	教育程度2
    private String sex;//	是	String	性别
    private String consumptionCapacityF;//	是	String	消费能力1
    private String consumptionCapacityB;//	是	String	消费能力2

    private String ascription;//	是	STring	归属地
    private Map<String,List<String>> modeMap;

    private Map<String,List<String>> hobbyMap;

    private String industryMark;
    private String industryName;

    private int customFlag;


    public int getCustomFlag() {
        return customFlag;
    }

    public void setCustomFlag(int customFlag) {
        this.customFlag = customFlag;
    }

    public String getIndustryMark() {
        return industryMark;
    }

    public void setIndustryMark(String industryMark) {
        this.industryMark = industryMark;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public List<String> getJixing() {
        return jixing;
    }

    public void setJixing(List<String> jixing) {
        this.jixing = jixing;
    }

    public List<String> getXingqu() {
        return xingqu;
    }

    public void setXingqu(List<String> xingqu) {
        this.xingqu = xingqu;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getAgeF() {
        return ageF;
    }

    public void setAgeF(String ageF) {
        this.ageF = ageF;
    }

    public String getAgeB() {
        return ageB;
    }

    public void setAgeB(String ageB) {
        this.ageB = ageB;
    }

    public String getEducationLevelF() {
        return educationLevelF;
    }

    public void setEducationLevelF(String educationLevelF) {
        this.educationLevelF = educationLevelF;
    }

    public String getEducationLevelB() {
        return educationLevelB;
    }

    public void setEducationLevelB(String educationLevelB) {
        this.educationLevelB = educationLevelB;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getConsumptionCapacityF() {
        return consumptionCapacityF;
    }

    public void setConsumptionCapacityF(String consumptionCapacityF) {
        this.consumptionCapacityF = consumptionCapacityF;
    }

    public String getConsumptionCapacityB() {
        return consumptionCapacityB;
    }

    public void setConsumptionCapacityB(String consumptionCapacityB) {
        this.consumptionCapacityB = consumptionCapacityB;
    }

    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public Map<String, List<String>> getModeMap() {
        return modeMap;
    }

    public void setModeMap(Map<String, List<String>> modeMap) {
        this.modeMap = modeMap;
    }

    public Map<String, List<String>> getHobbyMap() {
        return hobbyMap;
    }

    public void setHobbyMap(Map<String, List<String>> hobbyMap) {
        this.hobbyMap = hobbyMap;
    }


    @Override
    public String toString() {
        return "OrientationSettingsOut{" +
                "jixing 机型数组：=" + jixing +
                ", xingqu 兴趣=" + xingqu +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", ageF 用户年龄：='" + ageF + '\'' +
                ", ageB  用户年龄到='" + ageB + '\'' +
                ", educationLevelF 教育程度 ='" + educationLevelF + '\'' +
                ", educationLevelB='" + educationLevelB + '\'' +
                ", sex 性别='" + sex + '\'' +
                ", consumptionCapacityF 消费能力='" + consumptionCapacityF + '\'' +
                ", consumptionCapacityB='" + consumptionCapacityB + '\'' +
                ", ascription 归属地='" + ascription + '\'' +
                '}';
    }
}
