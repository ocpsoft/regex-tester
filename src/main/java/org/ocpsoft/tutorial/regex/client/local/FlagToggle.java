package org.ocpsoft.tutorial.regex.client.local;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;

public class FlagToggle implements ClickHandler
{

   private static final String DISABLED_STYLE = "disabled";
   private static final String FLAGS_PATTERN = "^\\(\\?([idmsux]*)(-)?([idmsux]*)\\)";
   private Button target;
   private HasText text;
   private String flag;

   private boolean enabled = false;

   public FlagToggle()
   {}

   public FlagToggle(Button target, HasText text, String flag)
   {
      this.target = target;
      this.text = text;
      this.flag = flag;

      refresh();
   }

   public void refresh()
   {
      String value = text.getText();
      if (value != null && RegExp.compile(FLAGS_PATTERN).test(value))
      {
         MatchResult result;
         RegExp flagsPattern = RegExp.compile(FLAGS_PATTERN, "g");
         
         while ((result = flagsPattern.exec(value)) != null)
         {
            String on = result.getGroup(1);
            String off = result.getGroup(2);

            if (off != null && off.contains(flag))
               this.enabled = false;
            else if (on != null && on.contains(flag))
               this.enabled = true;
            else
               this.enabled = false;
         }
      }
      else
         enabled = false;

      if (enabled)
         target.addStyleName(DISABLED_STYLE);
      else
         target.removeStyleName(DISABLED_STYLE);
   }

   @Override
   public void onClick(ClickEvent event)
   {
      enabled = !enabled;
      text.setText(updateFlag(text.getText(), flag, enabled));

      if (enabled)
         target.addStyleName(DISABLED_STYLE);
      else
         target.removeStyleName(DISABLED_STYLE);
   }

   public String updateFlag(String value, String flag, boolean enabled)
   {
      if (RegExp.compile(FLAGS_PATTERN).test(value))
      {
         if (enabled && RegExp.compile("\\(\\?([idmsux]*" + flag + "[idmsux]*)(-)?([idmsux]+)?\\)").test(value))
         {
            // all good
         }
         else if (enabled && RegExp.compile("\\(\\?([idmsux]*)(-)?([idmsux]*)\\)").test(value))
         {
            value = RegExp.compile("\\(\\?([idmsux]*)(-)?([idmsux]+)?\\)").replace(value, "(?" + flag + "$1$2$3)");
         }
         else if (enabled
                  && RegExp.compile("\\(\\?([idmsux]*)(-)?(([idmsux]*)(" + flag + ")([idmsux]*))?\\)").test(value))
         {
            value = RegExp.compile(
                     "\\(\\?([idmsux]*)(-)?(([idmsux]*)(" + flag + ")([idmsux]*))?\\)", "g").replace(value,
                     "(?$1$2$4$6)");
            value = RegExp.compile(
                     "\\(\\?(([idmsux]*)" + flag + "?([idmsux]*)(-)?)([idmsux]*)\\)").replace(value,
                     "(?" + flag + "$2$3$4)");
         }
         else if (!enabled && RegExp.compile("\\(\\?([idmsux]?)(-)([idmsux]*" + flag + "[idmsux]*)?\\)").test(value))
         {
            // all good
         }
         else if (!enabled && RegExp.compile("\\(\\?([idmsux]*" + flag + "[idmsux]*)(-)?([idmsux]+)?\\)").test(value))
         {
            value = RegExp.compile("\\(\\?([idmsux]*?)" + flag + "([idmsux]*)(-?)([idmsux]*)\\)", "g").replace(value,
                     "(?$1$2$3)");
         }

         if (!enabled && RegExp.compile("\\(\\?-?\\)").test(value))
         {
            value = RegExp.compile("\\(\\?-?\\)", "g").replace(value, "");
         }
      }

      if (!RegExp.compile(FLAGS_PATTERN).test(value))
      {
         if (enabled)
            value = "(?" + flag + ")" + value;
      }

      return value;
   }
}
