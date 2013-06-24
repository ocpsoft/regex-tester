package org.ocpsoft.tutorial.regex.client.shared;

import java.util.ArrayList;
import java.util.List;

public class HighlightedGroup
{
   private String text;
   private List<HighlightedGroup> groups;
   private Group group;
   private int start = -1;
   private int end = -1;

   public HighlightedGroup(String text, int start, int end)
   {
      this.groups = new ArrayList<HighlightedGroup>();
      this.text = text;
      this.start = start;
      this.end = end;
   }

   public HighlightedGroup(String text, Group group)
   {
      this(text, group.getStart(), group.getEnd());
      this.group = group;
   }

   public void add(HighlightedGroup group)
   {
      this.groups.add(group);
   }

   public Group getGroup()
   {
      return group;
   }

   public List<HighlightedGroup> getChildren()
   {
      return groups;
   }

   public int getStart()
   {
      return start;
   }

   public int getEnd()
   {
      return end;
   }

   public String getText()
   {
      return text;
   }

   public boolean isLeaf()
   {
      return getChildren() == null || getChildren().isEmpty();
   }

   @Override
   public String toString()
   {
      if (groups.isEmpty())
         return text;
      else
      {
         String result = "";
         for (HighlightedGroup group : groups) {
            result += group.toString();
         }
         return result;
      }
   }

   public boolean isMatchingGroup()
   {
      return group != null;
   }
}
