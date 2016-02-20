package com.hectorlopezfernandez.pebble.slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.BodyNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.template.ScopeChain;

public class LogNode extends AbstractRenderableNode {

    private final Expression<?> level;
    private final Expression<?> name;
    private final BodyNode body;

    public LogNode(int lineNumber, Expression<?> level, Expression<?> name, BodyNode body) {
        super(lineNumber);
        this.level = level;
        this.name = name;
        this.body = body;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException, IOException {
    	ScopeChain values = context.getScopeChain();
    	// resolve logger
    	Logger logger = null;
    	if (name != null) {
    		Object evaluatedName = name.evaluate(self, context);
    		logger = LoggerFactory.getLogger(evaluatedName.toString());
    	} else {
    		Object defaultName = values.get(Slf4jExtension.DEFAULT_LOGGER_NAME);
    		if (defaultName != null) {
    			logger = LoggerFactory.getLogger(defaultName.toString());
    		} else {
    			logger = LoggerFactory.getLogger(self.getName());
    		}
    	}
    	// resolve level
    	String activeLevel = null;
    	if (level == null) {
    		Object defaultLevel = values.get(Slf4jExtension.DEFAULT_LOG_LEVEL);
    		if (defaultLevel == null) {
    			// no established level means DEBUG, try to find as fast as possible if we should log
    			if (!logger.isDebugEnabled()) return;
    			activeLevel = "debug";
    		} else {
    			activeLevel = defaultLevel.toString();
    		}
    	} else {
    		Object evaluatedLevel = level.evaluate(self, context);
    		activeLevel = evaluatedLevel.toString().trim().toLowerCase();
    	}
    	// at this point we are going to log anyway, so parse the tag's body
    	StringWriter sw = new StringWriter();
    	body.render(self, sw, context);
    	// do the logging thing
    	switch (activeLevel) {
    	case "error":
    		logger.error(sw.toString());
    		break;
    	case "warn":
    		logger.warn(sw.toString());
    		break;
    	case "info":
    		logger.info(sw.toString());
    		break;
    	case "debug":
    		logger.debug(sw.toString());
    		break;
    	case "trace":
    		logger.trace(sw.toString());
    		break;
    	default:
    		throw new IllegalArgumentException("Level argument is not a valid Slf4j log level: " + activeLevel);
    	}
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}