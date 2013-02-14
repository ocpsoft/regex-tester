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
      RegexResult result = l.parse(new RegexRequest(text, ".+", "replacement"));

      Assert.assertNull(result.getError());
      Assert.assertEquals("replacement", result.getReplaced());

      Assert.assertEquals(0, result.getGroups().size());
   }

   @Test
   public void testRegexRequestOptional() throws Exception
   {
      String text = "x";
      String pattern = "(\\.)?";
      String replacement = "replacement";
      RegexResult result = l.parse(new RegexRequest(text, pattern, replacement));

      Assert.assertNull(result.getError());
      Assert.assertEquals(text.replaceAll(pattern, replacement), result.getReplaced());

      Assert.assertEquals(4, result.getGroups().size());
   }

   @Test
   public void testFindGroups() throws Exception
   {
      String text = "the quick brown fox jumped over the lazy dog";
      RegexResult result = l.parse(new RegexRequest(text, "\\w+ ?", "x"));

      Assert.assertNull(result.getError());
      Assert.assertEquals(9, result.getGroups().size());

      Assert.assertEquals("xxxxxxxxx", result.getReplaced());

      Group group = result.getGroups().get(0);
      Assert.assertEquals("\\w+ ?", group.getFragment());
      Assert.assertEquals(0, group.getStart());
      Assert.assertEquals(4, group.getEnd());
      Assert.assertEquals("the ", text.substring(group.getStart(), group.getEnd()));

      group = result.getGroups().get(4);
      Assert.assertEquals("\\w+ ?", group.getFragment());
      Assert.assertEquals(20, group.getStart());
      Assert.assertEquals(27, group.getEnd());
      Assert.assertEquals("jumped ", text.substring(group.getStart(), group.getEnd()));

   }

   @Test
   public void testImplicitNestedGroups() throws Exception
   {
      String text = "the quick brown fox";
      RegexResult result = l.parse(new RegexRequest(text, "(\\w+ (\\w+))", "$1 $2"));

      Assert.assertNull(result.getError());
      Assert.assertEquals("the quick quick brown fox fox", result.getReplaced());

      Group group = result.getGroups().get(0);
      Assert.assertEquals("(\\w+ (\\w+))", group.getFragment());
      Assert.assertEquals(0, group.getStart());
      Assert.assertEquals(9, group.getEnd());

      group = result.getGroups().get(1);
      Assert.assertEquals("\\w+", group.getFragment());
      Assert.assertEquals(4, group.getStart());
      Assert.assertEquals(9, group.getEnd());

      Assert.assertEquals(4, result.getGroups().size());
   }

   @Test
   public void testImplicitNestedGroupsNonDefault() throws Exception
   {
      String text = "the quick brown fox ";
      RegexResult result = l.parse(new RegexRequest(text, "(\\w+ (\\w+)) ", "$1 $2 "));

      Assert.assertNull(result.getError());
      Assert.assertEquals("the quick quick brown fox fox ", result.getReplaced());

      Group group = result.getGroups().get(0);
      Assert.assertEquals("(\\w+ (\\w+)) ", group.getFragment());
      Assert.assertEquals(0, group.getStart());
      Assert.assertEquals(10, group.getEnd());

      group = result.getGroups().get(1);
      Assert.assertEquals("\\w+ (\\w+)", group.getFragment());
      Assert.assertEquals(0, group.getStart());
      Assert.assertEquals(9, group.getEnd());

      group = result.getGroups().get(2);
      Assert.assertEquals("\\w+", group.getFragment());
      Assert.assertEquals(4, group.getStart());
      Assert.assertEquals(9, group.getEnd());

      Assert.assertEquals(6, result.getGroups().size());
   }

   @Test
   public void testExplicitNestedGroups() throws Exception
   {
      String text = "the quick brown fox jumped over the lazy dog";
      RegexResult result = l
               .parse(new RegexRequest(text, "\\w+ (\\w+ \\w+ \\w+ \\w+ (\\w+ \\w+ \\w+)) (\\w+)", "$1 $3"));

      Assert.assertNull(result.getError());
      Assert.assertEquals("quick brown fox jumped over the lazy dog", result.getReplaced());

      Group group = result.getGroups().get(0);
      Assert.assertEquals("\\w+ \\w+ \\w+ \\w+ (\\w+ \\w+ \\w+)", group.getFragment());
      Assert.assertEquals(4, group.getStart());
      Assert.assertEquals(text.length() - 4, group.getEnd());

      group = result.getGroups().get(1);
      Assert.assertEquals("\\w+ \\w+ \\w+", group.getFragment());
      Assert.assertEquals(27, group.getStart());
      Assert.assertEquals(text.length() - 4, group.getEnd());

      group = result.getGroups().get(2);
      Assert.assertEquals("\\w+", group.getFragment());
      Assert.assertEquals(text.length() - 3, group.getStart());
      Assert.assertEquals(text.length(), group.getEnd());

      Assert.assertEquals(3, result.getGroups().size());
   }

   @Test
   public void testResultsHighlightExplicitGroups() throws Exception
   {
      String text = "the quick brown fox";
      RegexResult result = l.parse(new RegexRequest(text, ".+(quick).+(fo(x))", "$1 $3"));

      String highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals(
               "the <span style=\"color: #ff1493\" class=\"highlight\">quick</span> brown " +
                        "<span style=\"color: #0099ff\" class=\"highlight\">fo" +
                        "<span style=\"color: #8b008b\" class=\"highlight\">x" +
                        "</span>" +
                        "</span>",
               highlighted);
   }

   @Test
   public void testResultsHighlightImplicitGroups() throws Exception
   {
      String text = "the quick brown fox ";
      RegexResult result = l.parse(new RegexRequest(text, "(\\w+ (\\w+)) ", "$1 $2"));

      String highlighted = new Highlighter().highlight(text, result);
      Assert.assertEquals("<span style=\"color: #ff8c00\" class=\"highlight\">" +
               "<span style=\"color: #daa520\" class=\"highlight\">the " +
               "<span style=\"color: #1e90ff\" class=\"highlight\">quick" +
               "</span></span> </span>" +
               "<span style=\"color: #b22222\" class=\"highlight\">" +
               "<span style=\"color: #0099ff\" class=\"highlight\">brown " +
               "<span style=\"color: #8b008b\" class=\"highlight\">fox" +
               "</span></span> </span>", highlighted);
   }

   @Test(expected = RegexException.class)
   public void testJavaModeSingleCharBackslash()
   {
      l.javaMode("\\");
   }

   @Test(expected = RegexException.class)
   public void testJavaModeSingleBackslashWithToken()
   {
      l.javaMode("\\w");
   }

   @Test(expected = RegexException.class)
   public void testJavaModeSingleCharBackslashBeforeValidEscape()
   {
      l.javaMode("\\asdf\\\\");
   }

   @Test(expected = RegexException.class)
   public void testJavaModeMiddleCharBackslash()
   {
      l.javaMode("something\\else");
   }

   @Test(expected = RegexException.class)
   public void testJavaModeMiddleCharBackslashAfterValidEscape()
   {
      l.javaMode("asdf\\\\something\\else");
   }

   @Test(expected = RegexException.class)
   public void testJavaModeLastCharBackslash()
   {
      l.javaMode("something\\");
   }

   @Test(expected = RegexException.class)
   public void testJavaModeLastCharBackslashAfterValidEscape()
   {
      l.javaMode("first\\\\something\\");
   }

   @Test
   public void testJavaModeCanonicalizationSingle() throws Exception
   {
      Assert.assertEquals("\\", l.javaMode("\\\\"));
   }

   @Test
   public void testJavaModeCanonicalizationFirst() throws Exception
   {
      Assert.assertEquals("\\something", l.javaMode("\\\\something"));
   }

   @Test
   public void testJavaModeCanonicalizationMiddle() throws Exception
   {
      Assert.assertEquals("first\\something", l.javaMode("first\\\\something"));
   }

   @Test
   public void testJavaModeCanonicalizationLast() throws Exception
   {
      Assert.assertEquals("something\\", l.javaMode("something\\\\"));
   }

   @Test
   public void testJavaModeCanonicalizationMultiple() throws Exception
   {
      Assert.assertEquals("something\\last\\", l.javaMode("something\\\\last\\\\"));
   }

}
