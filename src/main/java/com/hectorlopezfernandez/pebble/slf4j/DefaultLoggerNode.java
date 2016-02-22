package com.hectorlopezfernandez.pebble.slf4j;

import java.io.Writer;

import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.template.ScopeChain;

public class DefaultLoggerNode extends AbstractRenderableNode {

    private final Expression<?> value;

    public DefaultLoggerNode(int lineNumber, Expression<?> value) {
        super(lineNumber);
        this.value = value;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException {
    	// evaluate and parse name
    	Object evaluatedName = value.evaluate(self, context);
    	if (!(evaluatedName instanceof String)) {
    		throw new IllegalArgumentException("DefaultLoggerName only supports String values. Actual argument was: " + (evaluatedName == null ? "null" : evaluatedName.getClass().getName()));
    	}
    	ScopeChain values = context.getScopeChain();
    	values.put(Slf4jExtension.DEFAULT_LOGGER, LoggerFactory.getLogger(evaluatedName.toString()));
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}