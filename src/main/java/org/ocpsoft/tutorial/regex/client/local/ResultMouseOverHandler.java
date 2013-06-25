package org.ocpsoft.tutorial.regex.client.local;

import java.util.Iterator;

import org.ocpsoft.tutorial.regex.client.shared.HighlightedGroup;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ResultMouseOverHandler implements MouseOverHandler, MouseOutHandler
{
   private TextArea area;

   public ResultMouseOverHandler(TextArea area)
   {
      this.area = area;
   }

   public void onMouseOver(final MouseOverEvent event)
   {
      HighlightedResult widget = (HighlightedResult) event.getSource();
      HighlightedGroup group = widget.getHighlightedGroup();

      if (group.getGroup() != null)
      {
         widget.setMouseOverEvent(event);
         if (!hasHighlightedChildren(widget))
         {
            area.setSelectionRange(0, 0);
            int start = group.getGroup().getRegexFragment().getStart();
            int length = group.getGroup().getRegexFragment().getCaptured().length + 2;

            if (start + length > area.getText().length())
               length = area.getText().length() - start;

            area.setSelectionRange(start, length);
         }
      }
   }

   private boolean hasHighlightedChildren(HighlightedResult widget)
   {
      Iterator<Widget> iterator = widget.iterator();
      while (iterator.hasNext())
      {
         Widget child = iterator.next();
         if (child instanceof HighlightedResult
                  && (((HighlightedResult) child).getMouseOverEvent() != null
                  || hasHighlightedChildren((HighlightedResult) child)))
         {
            return true;
         }
      }
      return false;
   }

   public void onMouseOut(final MouseOutEvent event)
   {
      HighlightedResult widget = (HighlightedResult) event.getSource();
      HighlightedGroup group = widget.getHighlightedGroup();

      widget.setMouseOverEvent(null);
      if (group.getGroup() != null)
      {
         area.setSelectionRange(0, 0);

         Widget parent = widget.getParent();
         while (parent instanceof HighlightedResult)
         {
            MouseOverEvent mouseOverEvent = ((HighlightedResult) parent).getMouseOverEvent();
            if (mouseOverEvent != null)
            {
               final Widget finalParent = parent;
               onMouseOver(new MouseOverEvent() {
                  @Override
                  public Object getSource()
                  {
                     return finalParent;
                  }
               });
               break;
            }
            else
               parent = parent.getParent();
         }
      }
   }
}