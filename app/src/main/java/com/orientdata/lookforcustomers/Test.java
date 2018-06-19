package com.orientdata.lookforcustomers;

/**
 * Created by ckx on 2018/6/19.
 */

public class Test {

    public static void main(String[] args) {
        String number = "123.456567890";
        String intNumber = number.substring(0,number.indexOf(".")+3);
        System.out.println(intNumber);
    }
}
