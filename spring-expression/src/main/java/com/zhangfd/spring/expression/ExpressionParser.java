//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression;

/**
 * 用来解析表达式字符串，并且返回一个表达式对象Expression，
 * 这个接口ExpressionParser有个安全并且可以重用的实现类SpelExpressionParser
 */
public interface ExpressionParser {
    Expression parseExpression(String var1) throws ParseException;

    Expression parseExpression(String var1, ParserContext var2) throws ParseException;
}
