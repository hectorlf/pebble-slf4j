package com.hectorlopezfernandez.pebble.slf4j;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

@RunWith(JUnit4.class)
public class Slf4jExtensionTests {

	private static PebbleEngine engine;

	@BeforeClass
	public static void setup() {
		Slf4jExtension e = new Slf4jExtension();
		Loader<String> l = new ClasspathLoader();
		engine = new PebbleEngine.Builder().loader(l).extension(e).build();
	}

	@Test
	public void testDefaultTags() throws Exception {
		PebbleTemplate compiledTemplate = engine.getTemplate("default-tags.pebble");
		Map<String, Object> context = new HashMap<>();
		Writer writer = new StringWriter();
		compiledTemplate.evaluate(writer, context);
		String output = writer.toString();
		Assert.assertTrue(output.contains("ERROR randomClassName"));
	}

	@Test
	public void testBlockTags() throws Exception {
		PebbleTemplate compiledTemplate = engine.getTemplate("block-tags.pebble");
		Map<String, Object> context = new HashMap<>();
		Writer writer = new StringWriter();
		compiledTemplate.evaluate(writer, context);
		String output = writer.toString();
		Assert.assertTrue(output.contains("WARN test"));
	}

	@Test
	public void testLogTag() throws Exception {
		PebbleTemplate compiledTemplate = engine.getTemplate("log-tag.pebble");
		Map<String, Object> context = new HashMap<>();
		Writer writer = new StringWriter();
		compiledTemplate.evaluate(writer, context);
		String output = writer.toString();
		Assert.assertTrue(output.isEmpty());
	}

}