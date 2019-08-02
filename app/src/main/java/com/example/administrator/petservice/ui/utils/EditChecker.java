package com.example.administrator.petservice.ui.utils;

/**
 * @author rfa
 * 主要用于Edit的检测工具类
 */
public class EditChecker {

    /**
     * 检验输入的内容是否为空
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.replaceAll(" ","" ).equals("")) {
            return true;
        }
        return false;
    }



}
