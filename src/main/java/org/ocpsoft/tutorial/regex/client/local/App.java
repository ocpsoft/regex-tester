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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.ocpsoft.tutorial.regex.client.shared.RegexParser;
import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Main application entry point.
 */
@EntryPoint
@Templated("Mockup.html#app-template")
public class App extends Composite
{
   @Inject
   @DataField
   private TextBox text;

   @Inject
   @DataField
   private TextBox regex;

   @Inject
   @DataField
   private TextBox replacement;

   @Inject
   @DataField
   private Label result;

   @Inject
   @DataField
   private Label replaced;

   @Inject
   @DataField
   private Label error;

   @PostConstruct
   private final void init()
   {
      error.setVisible(false);
      replaced.setVisible(false);
   }

   @Inject
   private Caller<RegexParser> parser;
   private Timer timer;

   @EventHandler({ "text", "regex", "replacement" })
   void handleUpdate(KeyUpEvent event)
   {
      result.setText(text.getText());

      if (timer != null)
         timer.cancel();

      timer = new Timer() {

         @Override
         public void run()
         {
            timer = null;
            parser.call(callback, errorCallback)
                     .parse(new RegexRequest(text.getText(), regex.getText(), replacement.getText()));

         }
      };

      timer.schedule(200);

   }

   public void handleResult(RegexResult event)
   {
      if (event.getError() != null && !event.getError().isEmpty())
      {
         error.setText(event.getError());
         error.setVisible(true);
         result.removeStyleName("matches");
      }
      else
      {
         error.setVisible(false);
         if (event.isMatches())
         {
            result.addStyleName("matches");
         }
         else
            result.removeStyleName("matches");
      }

      if (event.getReplaced() != null && !event.getReplaced().isEmpty())
      {
         replaced.setText(event.getReplaced());
         replaced.setVisible(true);
      }
      else
         replaced.setVisible(false);

   }

   @PostConstruct
   public void setup()
   {
      RootPanel.get().add(this);
   }

   RemoteCallback<RegexResult> callback = new RemoteCallback<RegexResult>() {
      public void callback(RegexResult value)
      {
         handleResult(value);
      }
   };

   ErrorCallback errorCallback = new ErrorCallback() {
      @Override
      public boolean error(Message message, Throwable throwable)
      {
         return false;
      }
   };

}
