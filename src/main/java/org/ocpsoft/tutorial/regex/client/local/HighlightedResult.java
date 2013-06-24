package org.ocpsoft.tutorial.regex.client.local;

import org.ocpsoft.tutorial.regex.client.shared.HighlightedGroup;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTMLPanel;

public class HighlightedResult extends HTMLPanel
{
   private HighlightedGroup group;
   private ColorCycle colorCycle;

   public HighlightedResult(HighlightedGroup group)
   {
      this(group, null);
   }

   private HighlightedResult(HighlightedGroup group, ColorCycle colorCycle)
   {
      super("span", "");

      this.group = group;
      if (!group.isMatchingGroup())
      {
         for (HighlightedGroup child : group.getChildren()) {
            this.add(new HighlightedResult(child, colorCycle));
         }
      }
      else
      {
         this.addStyleName("highlight");
      }

      if (this.group.isLeaf())
      {
         System.out.println("LEAF: " + group);
         getElement().setInnerSafeHtml(new SafeHtmlBuilder().appendEscaped(group.getText()).toSafeHtml());
      }
   }

   @Override
   protected void onLoad()
   {
      super.onLoad();

      if (this.group.isMatchingGroup())
      {
         if (colorCycle == null)
            this.colorCycle = new ColorCycle();
         this.getElement().setAttribute("style", "color: #" + colorCycle.getColor() + ";");
      }
   }
}
