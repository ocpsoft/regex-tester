package org.ocpsoft.tutorial.regex.client.local;

import org.ocpsoft.tutorial.regex.client.shared.HighlightedGroup;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTMLPanel;

public class HighlightedResult extends HTMLPanel
{
   private HighlightedGroup group;
   private ColorCycle colorCycle;
   private MouseOverEvent mouseOver;

   public HighlightedResult(ResultMouseOverHandler handler, HighlightedGroup group)
   {
      this(handler, group, null);
   }

   private HighlightedResult(ResultMouseOverHandler handler, HighlightedGroup group, ColorCycle colorCycle)
   {
      super("span", "");

      if (colorCycle == null)
      {
         colorCycle = new ColorCycle();
      }
      this.colorCycle = colorCycle;

      this.group = group;
      for (HighlightedGroup child : group.getChildren()) {
         this.add(new HighlightedResult(handler, child, colorCycle));
      }

      if (this.group.isLeaf())
      {
         getElement().setInnerSafeHtml(new SafeHtmlBuilder().appendEscaped(group.getText()).toSafeHtml());
      }

      if (this.group.isMatchingGroup()) {
         this.addStyleName("highlight");
      }

      addMouseOutHandler(handler);
      addMouseOverHandler(handler);
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

   public HighlightedGroup getHighlightedGroup()
   {
      return group;
   }

   public HandlerRegistration addMouseOverHandler(MouseOverHandler handler)
   {
      return addDomHandler(handler, MouseOverEvent.getType());
   }

   public HandlerRegistration addMouseOutHandler(MouseOutHandler handler)
   {
      return addDomHandler(handler, MouseOutEvent.getType());
   }

   public void setMouseOverEvent(MouseOverEvent event)
   {
      this.mouseOver = event;
   }

   public MouseOverEvent getMouseOverEvent()
   {
      return mouseOver;
   }
}
