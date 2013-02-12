package org.ocpsoft.tutorial.regex.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

@Portable
@Bindable
public class RegexRequest
{
   private String text;
   private String regex;
   private String replacement;

   public RegexRequest()
   {}

   public RegexRequest(String text, String pattern, String replacement)
   {
      this.text = text;
      this.regex = pattern;
      this.replacement = replacement;
   }

   public String getText()
   {
      return text;
   }

   public String getRegex()
   {
      return regex;
   }

   public String getReplacement()
   {
      return replacement;
   }

   public void setText(String text)
   {
      this.text = text;
   }

   public void setRegex(String regex)
   {
      this.regex = regex;
   }

   public void setReplacement(String replacement)
   {
      this.replacement = replacement;
   }

   @Override
   public String toString()
   {
      return "RegexRequest [text=" + text + ", pattern=" + regex + ", replacement=" + replacement + "]";
   }

}
