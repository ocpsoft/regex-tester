package org.ocpsoft.tutorial.regex.client.shared;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.ocpsoft.tutorial.regex.server.RegexParserImpl;

public class HighlighterTest
{
   @Test
   public void testResultsHighlightExplicitGroups() throws Exception
   {
      RegexParserImpl l = new RegexParserImpl();
      String text = "the quick brown fox jumped";
      RegexResult result = l.parse(new RegexRequest(text, ".+((quick).+(fo(x))).*", "$2 $4"));

      HighlightedGroup highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals(text, highlighted.toString());
      Assert.assertEquals(text, highlighted.getText());
   }

   @Test
   public void testResultsHighlightExplicitGroups2() throws Exception
   {
      RegexParserImpl l = new RegexParserImpl();
      String text = "four";
      RegexResult result = l.parse(new RegexRequest(text, "\\w{2}", null));

      HighlightedGroup highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals(text, highlighted.toString());
      Assert.assertEquals(text, highlighted.getText());
   }

   @Test
   public void testResultsHighlightImplicitGroups() throws Exception
   {
      RegexParserImpl l = new RegexParserImpl();
      String text = "the quick brown fox ";
      RegexResult result = l.parse(new RegexRequest(text, "(\\w+ (\\w+)) ", "$1 $2"));

      HighlightedGroup highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals(text, highlighted.toString());
      Assert.assertEquals(text, highlighted.getText());
   }

   @Test
   public void testResultsHighlightOptionalToken() throws Exception
   {
      RegexParserImpl l = new RegexParserImpl();
      String text = "x";
      String pattern = "(\\.)?";
      String replacement = "replacement";
      RegexResult result = l.parse(new RegexRequest(text, pattern, replacement));

      HighlightedGroup highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals(text, highlighted.toString());
      Assert.assertEquals(text, highlighted.getText());
   }

   @Test
   public void testResultsHighlightOmitsNonCapturingGroups() throws Exception
   {
      RegexParserImpl l = new RegexParserImpl();
      String text = "12";
      RegexResult result = l.parse(new RegexRequest(text, "(?=(1))(12)", "$1"));

      Highlighter highlighter = new Highlighter();
      HighlightedGroup highlighted = highlighter.highlight(text, result);

      Assert.assertEquals(text, highlighted.toString());
      Assert.assertEquals(text, highlighted.getText());
   }

}
