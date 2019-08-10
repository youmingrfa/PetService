package com.example.administrator.petservice.ui.utils;

import java.util.regex.Pattern;

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

    /**
     * @param str
     * @return
     * 检测密码长度是否符合6-20位的数字和密码组合的规范
     */
    public static boolean checkPassword(String str){
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        return str.matches(regex);
    }

    /**
     * @param str
     * @return
     * 检测密码长度是否符合6-20的要求
     */
    public static boolean checkPasswordLength(String str){
        if(str.length()<6||str.length()>20)
            return false;
        else
            return true;
    }

    /**
     * @param str
     * @return
     * 检测密码是否全为数字
     */
    public static boolean checkPasswordIsAllNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * @param str
     * @return
     * 检测密码是否全为字母
     */
    public static boolean checkPasswordIsAllCharacter(String str){
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        return pattern.matcher(str).matches();
    }

}
