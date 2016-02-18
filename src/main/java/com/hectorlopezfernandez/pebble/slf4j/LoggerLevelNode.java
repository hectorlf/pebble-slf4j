package com.hectorlopezfernandez.pebble.slf4j;

import java.io.IOException;
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

public class LoggerLevelNode extends AbstractRenderableNode {

	private static final Logger logger = LoggerFactory.getLogger(LoggerLevelNode.class);

    private final Expression<?> value;
    private final BodyNode body;

    public LoggerLevelNode(int lineNumber, Expression<?> value, BodyNode body) {
        super(lineNumber);
        this.value = value;
        this.body = body;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException, IOException {
    	// evaluate level
    	Object evaluatedLevel = value.evaluate(self, context);
    	if (!(evaluatedLevel instanceof String)) {
    		throw new IllegalArgumentException("LoggerLevel only supports String values. Actual argument was: " + (evaluatedLevel == null ? "null" : evaluatedLevel.getClass().getName()));
    	}
    	// create a scope with the new level and process the body
    	ScopeChain values = context.getScopeChain();
    	values.pushScope();
    	values.put(Slf4jExtension.DEFAULT_LOG_LEVEL, evaluatedLevel);
    	body.render(self, writer, context);
    	// check if scope is the same and clean it, else warn (there's nothing more we can do about it)
    	if (values.currentScopeContainsVariable(Slf4jExtension.DEFAULT_LOG_LEVEL)) values.popScope();
    	else logger.warn("Could not clean scoped level {} because a child node opened a scope without closing it. The level will live for the rest of the current render.", evaluatedLevel);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}