package com.openmtas.dispatcher;


import org.junit.Assert;
import org.junit.Test;

public class TestPatternParser {

	@Test
	public void test(){
		ExpressionResult result=PatternParser.match("#NP", "#34222");
		Assert.assertEquals("34222", result.getNp());
	}
}
