package com.lola.digiccy.util;

import java.util.Random;

/**
 * @author MECHREVO
 */
public class StrUtil {
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
