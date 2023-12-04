//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.standard;


import com.zhangfd.spring.expression.ParseException;
import com.zhangfd.spring.expression.ParserContext;
import com.zhangfd.spring.expression.common.TemplateAwareExpressionParser;
import com.zhangfd.spring.expression.spel.SpelParserConfiguration;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;

public class SpelExpressionParser extends TemplateAwareExpressionParser {
    private final SpelParserConfiguration configuration;

    public SpelExpressionParser() {
        this.configuration = new SpelParserConfiguration();
    }

    public SpelExpressionParser(SpelParserConfiguration configuration) {
        Assert.notNull(configuration, "SpelParserConfiguration must not be null");
        this.configuration = configuration;
    }

    public SpelExpression parseRaw(String expressionString) throws ParseException {
        return this.doParseExpression(expressionString, (ParserContext)null);
    }

    protected SpelExpression doParseExpression(String expressionString, @Nullable ParserContext context) throws ParseException {
        return (new InternalSpelExpressionParser(this.configuration)).doParseExpression(expressionString, context);
    }
}
