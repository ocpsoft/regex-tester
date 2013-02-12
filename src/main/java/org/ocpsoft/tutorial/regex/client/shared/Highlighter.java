package org.ocpsoft.tutorial.regex.client.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Highlighter
{
   private static final String COLOR_BEGIN = "<span style=\"color: #XXXXXX\" class=\"highlight\">";
   private static final String COLOR_END = "</span>";

   int colorIndex = 0;
   private List<String> colors = Arrays.asList("8b008b", "ff8c00", "ff1493", "b22222", "1e90ff", "ff4500", "daa520");

   public String highlight(String text, RegexResult event)
   {
      String result = text;

      if (!event.getGroups().isEmpty())
      {
         colorIndex = 0;
         List<Group> groups = new ArrayList<Group>(event.getGroups());
         Collections.reverse(groups);

         List<Group> seen = new ArrayList<Group>();

         for (Group group : groups) {
            int start = group.getStart();
            int end = group.getEnd();

            int level = level(seen, group);
            if (level > 0)
               end = end + (level * (selectColor().length() + COLOR_END.length()));

            String prefix = result.substring(0, start);
            String content = result.substring(start, end);
            String suffix = result.substring(end);
            result = prefix + selectColor() + content + COLOR_END + suffix;

            seen.add(group);
         }
      }

      return result;
   }

   private String selectColor()
   {
      if (colorIndex == colors.size())
         colorIndex = 0;
      return COLOR_BEGIN.replaceFirst("XXXXXX", colors.get(colorIndex++));
   }

   private int level(List<Group> seen, Group group)
   {
      List<Group> temp = new ArrayList<Group>(seen);
      Collections.reverse(seen);

      for (Group s : temp) {
         if (s.getStart() >= group.getStart() && s.getEnd() <= group.getEnd() && group != s)
            return 1 + level(seen, s);
      }
      return 0;
   }

}
