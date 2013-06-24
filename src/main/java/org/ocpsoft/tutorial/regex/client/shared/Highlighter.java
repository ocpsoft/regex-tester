package org.ocpsoft.tutorial.regex.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Highlighter
{

   public HighlightedGroup highlight(String text, RegexResult event)
   {
      HighlightedGroup result = new HighlightedGroup(text, 0, text.length());

      if (text != null && !text.isEmpty() && !event.getGroups().isEmpty())
      {
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

}
