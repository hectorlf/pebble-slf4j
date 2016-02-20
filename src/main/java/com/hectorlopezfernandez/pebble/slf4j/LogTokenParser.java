package com.hectorlopezfernandez.pebble.slf4j;

import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.BodyNode;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.parser.StoppingCondition;
import com.mitchellbosecke.pebble.tokenParser.AbstractTokenParser;

public class LogTokenParser extends AbstractTokenParser {
	
	private static final String TOKEN_START = "log";
	private static final String TOKEN_END = "endLog";

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
    	TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip the start token
        stream.next();
        // test and parse level
        Expression<?> level = null;
        if (stream.current().test(Token.Type.NAME, "level")) {
        	stream.next();
        	level = parser.getExpressionParser().parseExpression();
        }
        // test and parse logger name
        Expression<?> name = null;
        if (stream.current().test(Token.Type.NAME, "logger")) {
        	stream.next();
        	name = parser.getExpressionParser().parseExpression();
        }
        // end of start tag
        stream.expect(Token.Type.EXECUTE_END);
        
        // parse the body
        BodyNode body = parser.subparse(new StoppingCondition() {
            @Override
            public boolean evaluate(Token token) {
                return token.test(Token.Type.NAME, TOKEN_END);
            }
        });

        // skip the end token
        stream.next();
        // end of end tag
        stream.expect(Token.Type.EXECUTE_END);

        return new LogNode(lineNumber, level, name, body);
    }

    @Override
    public String getTag() {
        return TOKEN_START;
    }

}