package org.ocpsoft.tutorial.regex.server;

import org.junit.Assert;
import org.junit.Test;
import org.ocpsoft.tutorial.regex.client.shared.Group;
import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;
import org.ocpsoft.tutorial.regex.client.shared.Highlighter;

public class RegexParserImplTest
{
   private RegexParserImpl l = new RegexParserImpl();

   @Test
   public void testRegexRequest() throws Exception
   {
      String text = "the quick brown fox jumped over the lazy dog";
      RegexResult result = l
               .parse(new RegexRequest(text, ".+", "replacement"));

      Assert.assertNull(result.getError());
      Assert.assertEquals("replacement", result.getReplaced());

      Group group = result.getFindGroups().get(0);
      Assert.assertEquals(".+", group.getFragment());
      Assert.assertEquals(0, group.getStart());
      Assert.assertEquals(text.length(), group.getEnd());

      Assert.assertEquals(0, result.getPatternGroups().size());
   }

   @Test
   public void testFindGroups() throws Exception
   {
      String text = "the quick brown fox jumped over the lazy dog";
      RegexResult result = l
               .parse(new RegexRequest(text, "\\\\w+ ?", "x"));

      Assert.assertNull(result.getError());
      Assert.assertEquals(9, result.getFindGroups().size());
      Assert.assertEquals(0, result.getPatternGroups().size());

      Assert.assertEquals("xxxxxxxxx", result.getReplaced());

      Group group = result.getFindGroups().get(0);
      Assert.assertEquals("\\w+ ?", group.getFragment());
      Assert.assertEquals(0, group.getStart());
      Assert.assertEquals(4, group.getEnd());
      Assert.assertEquals("the ", text.substring(group.getStart(), group.getEnd()));

      group = result.getFindGroups().get(4);
      Assert.assertEquals("\\w+ ?", group.getFragment());
      Assert.assertEquals(20, group.getStart());
      Assert.assertEquals(27, group.getEnd());
      Assert.assertEquals("jumped ", text.substring(group.getStart(), group.getEnd()));

   }

   @Test
   public void testExplicitNestedGroups() throws Exception
   {
      String text = "the quick brown fox jumped over the lazy dog";
      RegexResult result = l
               .parse(new RegexRequest(text, "\\\\w+ (\\\\w+ \\\\w+ \\\\w+ \\\\w+ (\\\\w+ \\\\w+ \\\\w+)) (\\\\w+)",
                        "$1 $3"));

      Assert.assertNull(result.getError());
      Assert.assertEquals("quick brown fox jumped over the lazy dog", result.getReplaced());

      Group group = result.getPatternGroups().get(0);
      Assert.assertEquals("\\w+ \\w+ \\w+ \\w+ (\\w+ \\w+ \\w+)", group.getFragment());
      Assert.assertEquals(4, group.getStart());
      Assert.assertEquals(text.length() - 4, group.getEnd());

      group = result.getPatternGroups().get(1);
      Assert.assertEquals("\\w+ \\w+ \\w+", group.getFragment());
      Assert.assertEquals(27, group.getStart());
      Assert.assertEquals(text.length() - 4, group.getEnd());

      group = result.getPatternGroups().get(2);
      Assert.assertEquals("\\w+", group.getFragment());
      Assert.assertEquals(text.length() - 3, group.getStart());
      Assert.assertEquals(text.length(), group.getEnd());

      Assert.assertEquals(3, result.getPatternGroups().size());
   }

   @Test
   public void testResultsHighlight() throws Exception
   {
      String text = "the quick brown fox";
      RegexResult result = l.parse(new RegexRequest(text, ".+(quick).+(fo(x))", "$1 $3"));

      String highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals(
               "the <span style=\"color: #b22222\" class=\"highlight\">quick</span> brown " +
                        "<span style=\"color: #ff1493\" class=\"highlight\">fo" +
                        "<span style=\"color: #8b008b\" class=\"highlight\">x" +
                        "</span>" +
                        "</span>",
               highlighted);
   }

   @Test(expected = RegexException.class)
   public void testSingleCharBackslash()
   {
      l.javaMode("\\");
   }

   @Test(expected = RegexException.class)
   public void testSingleBackslashWithToken()
   {
      l.javaMode("\\w");
   }

   @Test(expected = RegexException.class)
   public void testSingleCharBackslashBeforeValidEscape()
   {
      l.javaMode("\\asdf\\\\");
   }

   @Test(expected = RegexException.class)
   public void testMiddleCharBackslash()
   {
      l.javaMode("something\\else");
   }

   @Test(expected = RegexException.class)
   public void testMiddleCharBackslashAfterValidEscape()
   {
      l.javaMode("asdf\\\\something\\else");
   }

   @Test(expected = RegexException.class)
   public void testLastCharBackslash()
   {
      l.javaMode("something\\");
   }

   @Test(expected = RegexException.class)
   public void testLastCharBackslashAfterValidEscape()
   {
      l.javaMode("first\\\\something\\");
   }

   @Test
   public void testCanonicalizationSingle() throws Exception
   {
      Assert.assertEquals("\\", l.javaMode("\\\\"));
   }

   @Test
   public void testCanonicalizationFirst() throws Exception
   {
      Assert.assertEquals("\\something", l.javaMode("\\\\something"));
   }

   @Test
   public void testCanonicalizationMiddle() throws Exception
   {
      Assert.assertEquals("first\\something", l.javaMode("first\\\\something"));
   }

   @Test
   public void testCanonicalizationLast() throws Exception
   {
      Assert.assertEquals("something\\", l.javaMode("something\\\\"));
   }

   @Test
   public void testCanonicalizationMultiple() throws Exception
   {
      Assert.assertEquals("something\\last\\", l.javaMode("something\\\\last\\\\"));
   }

}
