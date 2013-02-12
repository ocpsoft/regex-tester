package org.ocpsoft.tutorial.regex.server;

import org.junit.Assert;
import org.junit.Test;

public class RegexParserImplTest
{
   private RegexParserImpl l = new RegexParserImpl();

   @Test(expected = RegexException.class)
   public void testSingleCharBackslash()
   {
      l.javaMode("\\");
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
