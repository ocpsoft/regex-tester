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
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;

import com.google.gwt.event.dom.client.KeyUpEvent;
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
   private Event<RegexRequest> requestEvent;

   @EventHandler({ "text", "regex", "replacement" })
   void handleUpdate(KeyUpEvent event)
   {
      requestEvent.fire(new RegexRequest(text.getText(), regex.getText(), replacement.getText()));
   }

   public void handleResult(@Observes RegexResult event)
   {
      if (event.isMatches())
         result.addStyleName("matches");
      else
         result.removeStyleName("matches");

      result.setText(event.getText());
   }

   @PostConstruct
   public void setup()
   {
      RootPanel.get().add(this);
   }

}
