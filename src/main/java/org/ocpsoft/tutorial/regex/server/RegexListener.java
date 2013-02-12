package org.ocpsoft.tutorial.regex.server;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;

@RequestScoped
public class RegexListener
{
   @Inject
   private Event<RegexResult> resultEvent;

   public void handleRequest(@Observes RegexRequest request)
   {
      RegexResult result = new RegexResult();
      if (request.getText() != null && !request.getText().isEmpty())
      {
         result.setText(request.getText());
         try {
            String regex = javaMode(request.getRegex());
            if (regex != null)
               result.setMatches(request.getText().matches(regex));
         }
         catch (Exception e) {
            result.setText(e.getMessage());
         }
      }

      resultEvent.fire(result);
   }

   public String javaMode(String regex)
   {
      StringBuilder result = new StringBuilder();

      int count = 0;
      for (int i = 0; i < regex.length(); i++) {
         char c = regex.charAt(i);

         if (c == '\\')
         {
            count++;
         }

         if (count % 2 == 1)
         {
            if (c != '\\')
            {
               if (i + 1 < regex.length())
               {
                  throw new RegexException("Unterminated escape sequence at character " + i + ": " + regex + " <--");
               }
            }
            else if (i + 1 == regex.length())
            {
               throw new RegexException("Unterminated escape sequence at character " + i + ": " + regex + " <--");
            }
         }
         result.append(c);
      }
      return result.toString().replaceAll("\\\\\\\\", "\\\\");
   }
}