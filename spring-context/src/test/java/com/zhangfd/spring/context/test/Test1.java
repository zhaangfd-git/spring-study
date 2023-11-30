package com.zhangfd.spring.context.test;

/**
 * @author: zhangfd
 * @date: 2023/11/30 21:04
 * @version: 1.0
 * @describe:
 */
public class Test1 {

    public static void main(String[] args) {
        SimpleAbstractApplicationContext simpleContext  = new SimpleAbstractApplicationContext();

        simpleContext.refresh();
    }

}
