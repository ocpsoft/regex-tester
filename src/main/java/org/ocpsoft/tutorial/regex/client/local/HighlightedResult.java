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

      if (colorCycle == null)
      {
         colorCycle = new ColorCycle();
      }
      this.colorCycle = colorCycle;

      this.group = group;
      for (HighlightedGroup child : group.getChildren()) {
         this.add(new HighlightedResult(child, colorCycle));
      }

      if (this.group.isLeaf())
      {
         getElement().setInnerSafeHtml(new SafeHtmlBuilder().appendEscaped(group.getText()).toSafeHtml());
      }

      if (this.group.isMatchingGroup()) {
         this.addStyleName("highlight");
      }
   }

   @Override
   protected void onLoad()
   {
      super.onLoad();

      if (this.group.isMatchingGroup())
      {
         this.getElement().setAttribute("style", "color: #" + colorCycle.getColor() + ";");
      }
   }
}
