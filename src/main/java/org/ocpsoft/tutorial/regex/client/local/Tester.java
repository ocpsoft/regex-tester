/*
 * Copyright 2009 JBoss, a division of Red Hat Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.tutorial.regex.client.local;

import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.HistoryToken;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.ocpsoft.tutorial.regex.client.shared.HighlightedGroup;
import org.ocpsoft.tutorial.regex.client.shared.Highlighter;
import org.ocpsoft.tutorial.regex.client.shared.RegexParser;
import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Main application entry point.
 */
@Page(path = "!", role = DefaultPage.class)
@Templated("Tester.html#app-template")
public class Tester extends Composite
{
   private static final int DELAY = 200;

   @PageState
   private String t;
   @PageState
   private String r;
   @PageState
   private String x;

   @Inject
   private TransitionTo<Tester> shareButtonTransition;

   @Inject
   @DataField
   private Button shareButton;

   @EventHandler("shareButton")
   private void handleCreateLinkButtonClicked(ClickEvent event)
   {
      HashMap<String, String> map = new HashMap<String, String>();
      map.put("t", text.getValue());
      map.put("r", regex.getValue());
      map.put("x", replacement.getValue());
      Multimap<String, String> multimap = Multimaps.forMap(map);
      HistoryToken token = HistoryToken.of("!", multimap);
      assignTop(token.toString());
      shareButtonTransition.go(multimap);
   }

   @PageShowing
   private void pageShowing(HistoryToken token)
   {
      boolean update = false;

      Collection<String> texts = token.getState().get("t");
      if (!texts.isEmpty())
      {
         text.setValue(texts.iterator().next());
         update = true;
      }

      Collection<String> regexes = token.getState().get("r");
      if (!regexes.isEmpty())
      {
         regex.setValue(regexes.iterator().next());
         update = true;
      }

      Collection<String> replacements = token.getState().get("x");
      if (!replacements.isEmpty())
      {
         replacement.setValue(replacements.iterator().next());
         update = true;
      }

      if (update)
      {
         new Timer() {
            @Override
            public void run()
            {
               requestUpdate();
            }
         }.schedule(DELAY);
      }
   }

   private native void assignTop(String hash)
   /*-{
      $wnd.top.location.hash = hash;
   }-*/;

   @Inject
   @DataField
   private TextArea text;

   @Inject
   @DataField
   private TextArea regex;

   @Inject
   @DataField
   private TextArea replacement;

   @DataField
   private HTMLPanel result = new HTMLPanel("div", "");

   @Inject
   @DataField
   private Label replaced;

   @Inject
   @DataField
   private Label error;

   @Inject
   private Navigation navigation;

   @PostConstruct
   private final void init()
   {
      error.setVisible(false);
      replaced.setVisible(false);
   }

   @Override
   protected void onAttach()
   {
      super.onAttach();
      initAutogrow();
   }

   private native void initAutogrow()
   /*-{
       var $opts = {within: 0, by: 1, interval: 100, duration: 'fast'};
       $wnd.$('#text').css('overflow', 'hidden').expandable($opts)
       $wnd.$('#regex').css('overflow', 'hidden').expandable($opts)
       $wnd.$('#replacement').css('overflow', 'hidden').expandable($opts)
    }-*/;

   @Inject
   private Caller<RegexParser> parser;
   private Timer timer;
   private RegexRequest request;

   @EventHandler({ "text", "regex", "replacement" })
   void onInputsKeyUp(KeyUpEvent event)
   {
      requestUpdate();
   }

   private void requestUpdate()
   {
      final RegexRequest update = new RegexRequest(
               text.getText(),
               regex.getText(),
               replacement.getText()
               );

      if (!update.equals(request))
      {
         request = update;
         if (timer != null)
            timer.cancel();

         timer = new Timer() {

            @Override
            public void run()
            {
               timer = null;
               parser.call(callback, errorCallback).parse(request);
            }
         };

         timer.schedule(DELAY);
         updateToggles();
      }
   }

   @Inject
   private @DataField
   Button ignoreCase;
   @Inject
   private @DataField
   Button dotAll;
   @Inject
   private @DataField
   Button multiLine;
   @Inject
   private @DataField
   Button unixLines;
   @Inject
   private @DataField
   Button commentsMode;
   @Inject
   private @DataField
   Button unicodeCase;

   FlagToggle ignoreCaseHandler;
   FlagToggle unixLinesHandler;
   FlagToggle multiLineHandler;
   FlagToggle dotAllHandler;
   FlagToggle unicodeCaseHandler;
   FlagToggle commentsModeHandler;

   public void updateToggles()
   {
      ignoreCaseHandler.refresh();
      unixLinesHandler.refresh();
      multiLineHandler.refresh();
      dotAllHandler.refresh();
      unicodeCaseHandler.refresh();
      commentsModeHandler.refresh();
   }

   @PostConstruct
   public void initFlags()
   {
      ignoreCaseHandler = new FlagToggle(ignoreCase, regex, "i");
      unixLinesHandler = new FlagToggle(unixLines, regex, "d");
      multiLineHandler = new FlagToggle(multiLine, regex, "m");
      dotAllHandler = new FlagToggle(dotAll, regex, "s");
      unicodeCaseHandler = new FlagToggle(unicodeCase, regex, "u");
      commentsModeHandler = new FlagToggle(commentsMode, regex, "x");

      ignoreCase.addClickHandler(ignoreCaseHandler);
      ClickHandler toggleClickHandler = new ClickHandler() {

         @Override
         public void onClick(ClickEvent event)
         {
            requestUpdate();
         }
      };

      ignoreCase.addClickHandler(toggleClickHandler);
      dotAll.addClickHandler(dotAllHandler);
      dotAll.addClickHandler(toggleClickHandler);
      multiLine.addClickHandler(multiLineHandler);
      multiLine.addClickHandler(toggleClickHandler);
      unixLines.addClickHandler(unixLinesHandler);
      unixLines.addClickHandler(toggleClickHandler);
      unicodeCase.addClickHandler(unicodeCaseHandler);
      unicodeCase.addClickHandler(toggleClickHandler);
      commentsMode.addClickHandler(commentsModeHandler);
      commentsMode.addClickHandler(toggleClickHandler);
   }

   RemoteCallback<RegexResult> callback = new RemoteCallback<RegexResult>() {
      public void callback(RegexResult event)
      {
         if (event.getError() != null && !event.getError().isEmpty())
         {
            error.setText(event.getError());
            error.setVisible(true);
            result.removeStyleName("matches");
         }
         else
            error.setVisible(false);

         if (event.getText() != null && !event.getText().isEmpty())
         {
            Highlighter highlighter = new Highlighter();
            result.clear();
            HighlightedGroup highlightedGroups = highlighter.highlight(event.getText(), event);
            result.add(new HighlightedResult(new ResultMouseOverHandler(regex), highlightedGroups));
            if (event.isMatches())
            {
               result.addStyleName("matches");
            }
            else
               result.removeStyleName("matches");
            result.setVisible(true);
         }
         else
            result.setVisible(false);

         if (event.getReplaced() != null && !event.getReplaced().isEmpty())
         {
            replaced.setText(event.getReplaced());
            replaced.setVisible(true);
         }
         else
            replaced.setVisible(false);
      }
   };

   ErrorCallback<Object> errorCallback = new ErrorCallback<Object>() {
      @Override
      public boolean error(Object message, Throwable throwable)
      {
         return false;
      }
   };

}
