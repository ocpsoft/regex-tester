package org.ocpsoft.tutorial.regex.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.ocpsoft.tutorial.regex.client.shared.RegexParser;
import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;

@Service
@RequestScoped
public class RegexParserImpl implements RegexParser
{
   @Override
   public RegexResult parse(RegexRequest request)
   {
      RegexResult result = new RegexResult();

      Matcher matcher = null;

      String regex = request.getRegex();
      if (request.getText() != null && !request.getText().isEmpty())
      {
         try {
            regex = javaMode(request.getRegex());
            if (regex != null)
            {
               matcher = Pattern.compile(regex).matcher(request.getText());
               result.setMatches(matcher.matches());
               if (request.getReplacement() != null && !request.getReplacement().isEmpty())
                  result.setText(matcher.replaceAll(javaMode(request.getReplacement())));
            }
         }
         catch (Exception e) {
            result.setError(e.getMessage());
            return result;
         }
      }

      matcher.reset();
      while (matcher.find())
      {
         matcher.group();
         matcher.start();
         matcher.end();
      }      
      
      matcher.reset();
      if(matcher.groupCount() > 1)
      while (matcher.find())
      {
         matcher.group();
         matcher.end();
      }

      return result;
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
                  throw new RegexException("Unterminated escape sequence at character " + i + ": " + result);
               }
            }
            else if (i + 1 == regex.length())
            {
               throw new RegexException("Unterminated escape sequence at character " + i + ": " + result);
            }
         }
         result.append(c);
      }
      return result.toString().replaceAll("\\\\\\\\", "\\\\");
   }
}