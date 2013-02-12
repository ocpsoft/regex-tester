package org.ocpsoft.tutorial.regex.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

@Portable
public class Group
{
   private String fragment;
   private int start;
   private int end;

   public Group(
            @MapsTo("fragment") String fragment,
            @MapsTo("start") int start,
            @MapsTo("end") int end)
   {
      this.fragment = fragment;
      this.start = start;
      this.end = end;
   }

   public String getFragment()
   {
      return fragment;
   }

   public int getStart()
   {
      return start;
   }

   public int getEnd()
   {
      return end;
   }

   @Override
   public String toString()
   {
      return "Group [fragment=" + fragment + ", start=" + start + ", end=" + end + "]";
   }

}
