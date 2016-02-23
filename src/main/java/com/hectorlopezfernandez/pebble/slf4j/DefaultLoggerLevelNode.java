package com.hectorlopezfernandez.pebble.slf4j;

import java.io.Writer;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.template.ScopeChain;

public class DefaultLoggerLevelNode extends AbstractRenderableNode {

    private final Expression<?> value;

    public DefaultLoggerLevelNode(int lineNumber, Expression<?> value) {
        super(lineNumber);
        this.value = value;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException {
    	// evaluate and parse level
    	Object evaluatedLevel = value.evaluate(self, context);
    	if (!(evaluatedLevel instanceof String)) {
    		throw new IllegalArgumentException("DefaultLoggerLevel only supports String values. Actual argument was: " + (evaluatedLevel == null ? "null" : evaluatedLevel.getClass().getName()));
    	}
    	LogLevel level = LogLevel.valueOf(evaluatedLevel.toString().trim().toUpperCase());
    	ScopeChain values = context.getScopeChain();
    	values.put(Slf4jExtension.DEFAULT_LOG_LEVEL, level);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}