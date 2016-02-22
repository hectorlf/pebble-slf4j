package com.hectorlopezfernandez.pebble.slf4j;

import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.AbstractTokenParser;

public class DefaultLoggerTokenParser extends AbstractTokenParser {

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
    	TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip the tag token
        stream.next();
        // get the name expression
        Expression<?> value = parser.getExpressionParser().parseExpression();
        // end of tag
        stream.expect(Token.Type.EXECUTE_END);

        return new DefaultLoggerNode(lineNumber, value);
    }

    @Override
    public String getTag() {
        return "defaultLoggerName";
    }

}