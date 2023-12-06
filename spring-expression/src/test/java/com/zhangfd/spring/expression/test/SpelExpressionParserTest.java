package com.zhangfd.spring.expression.test;

import com.zhangfd.spring.expression.Expression;
import com.zhangfd.spring.expression.ExpressionParser;
import com.zhangfd.spring.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangfd
 * @date: 2023/12/6 20:56
 * @version: 1.0
 * @describe: 参考：https://blog.51cto.com/u_14120/6802047
 */
public class SpelExpressionParserTest {

    public static void main(String[] args) {
      //  test1();

        ExpressionParser parser = new SpelExpressionParser();
        SpringTestObject rootObj = new SpringTestObject();

        String[] value = (String[]) parser.parseExpression("item").getValue(rootObj);
        System.out.println(value[1]);

        List<String> list = (List<String>) parser.parseExpression("list").getValue(rootObj);
        System.out.println(list.get(1));
        list.set(1,"laji");

         list = (List<String>) parser.parseExpression("list").getValue(rootObj);
        System.out.println(list.get(1));

        String property = (String) parser.parseExpression("property").getValue(rootObj);
        System.out.println(property);// 输出结果：测试类

        String property01 = (String) parser.parseExpression("getListItem(1)").getValue(rootObj);
        System.out.println(property01);// 输出结果：测试类
    }

    private static void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        System.out.println(message);
        double avogadrosNumber = (Double) parser.parseExpression("3.0221415E+23").getValue();
        System.out.println(avogadrosNumber + "");

        int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
        System.out.println(maxValue + "");

        boolean falseValue = (Boolean) parser.parseExpression("false").getValue();

        Object nullValue = parser.parseExpression("null").getValue();
    }
}

 class SpringTestObject {
    public String[] item = new String[]{"苹果", "香蕉", "梨", "西瓜"};
    public List<String> list = Arrays.asList("矿泉水", "雪碧", "可乐", "牛奶");
    public Map<String, String> map = new HashMap() {
        {
            put("man", "男");
            put("woman", "女");
            put("other", "中性");
        }
    };
    public String property = "测试类";

    public String getListItem(int index) {
        return list.get(index);
    }
}
