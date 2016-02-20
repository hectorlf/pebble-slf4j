package com.hectorlopezfernandez.pebble.slf4j;

import java.util.ArrayList;
import java.util.List;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

public class Slf4jExtension extends AbstractExtension {
	
	public static final String DEFAULT_LOG_LEVEL = "slf4j_default_level";
	public static final String DEFAULT_LOGGER_NAME = "slf4j_default_logger";

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> parsers = new ArrayList<>();
        parsers.add(new DefaultLoggerLevelTokenParser());
        parsers.add(new DefaultLoggerNameTokenParser());
        parsers.add(new LoggerLevelTokenParser());
        parsers.add(new LoggerNameTokenParser());
        parsers.add(new LogTokenParser());
        return parsers;
    }

}