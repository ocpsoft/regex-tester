package org.ocpsoft.tutorial.regex.client.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Highlighter
{
   int colorIndex = 0;
   private List<String> colors = Arrays.asList(
            "8b008b",
            "ff8c00",
            "0099ff",
            "ff1493",
            "b22222",
            "1e90ff",
            "ff4500",
            "daa520"
            );

   public HighlightedGroup highlight(String text, RegexResult event)
   {
      HighlightedGroup result = new HighlightedGroup(text, 0, text.length());

      if (text != null && !text.isEmpty() && !event.getGroups().isEmpty())
      {
         colorIndex = 0;
         List<Group> groups = new ArrayList<Group>(event.getGroups());

         Collections.sort(groups);
         Collections.reverse(groups);

         highlightGroup(groups, result);
      }

      return result;
   }

   private void highlightGroup(List<Group> groups, HighlightedGroup parent)
   {
      while (!groups.isEmpty()) {
         Group group = groups.get(0);
         if (group.getStart() >= parent.getStart() && group.getEnd() <= parent.getEnd())
         {
            groups.remove(0);
            String text = parent.getText();
            int start = group.getStart() - parent.getStart();
            int end = group.getEnd() - parent.getStart();

            HighlightedGroup prefix = new HighlightedGroup(text.substring(0, start), 0, start);
            HighlightedGroup content = new HighlightedGroup(text.substring(start, end), group);
            HighlightedGroup suffix = new HighlightedGroup(text.substring(end), group.getEnd(), parent.getEnd());

            if (!prefix.getText().isEmpty())
               parent.add(prefix);

            if (!content.getText().isEmpty())
            {
               parent.add(content);
               highlightGroup(groups, content);
            }

            if (!suffix.getText().isEmpty())
            {
               parent.add(suffix);
               highlightGroup(groups, suffix);
            }
         }
         else
            break;
      }
   }

   private String selectColor()
   {
      if (colorIndex == colors.size())
         colorIndex = 0;
      return colors.get(colorIndex++);
   }

}
