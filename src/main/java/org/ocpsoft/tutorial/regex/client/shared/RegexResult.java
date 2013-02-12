package org.ocpsoft.tutorial.regex.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegexResult
{
   private String error;
   private String text;
   private boolean matches;

   public String getText()
   {
      return text;
   }

   public void setText(String text)
   {
      this.text = text;
   }

   public boolean isMatches()
   {
      return matches;
   }

   public void setMatches(boolean matches)
   {
      this.matches = matches;
   }

   public String getError()
   {
      return error;
   }

   public void setError(String error)
   {
      this.error = error;
   }

}
