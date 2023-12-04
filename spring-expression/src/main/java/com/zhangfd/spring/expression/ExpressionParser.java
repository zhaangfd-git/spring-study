//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;


public interface ExpressionParser {
    Expression parseExpression(String var1) throws ParseException;

    Expression parseExpression(String var1, ParserContext var2) throws ParseException;
}
