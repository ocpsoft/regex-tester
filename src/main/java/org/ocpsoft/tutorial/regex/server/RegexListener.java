package org.ocpsoft.tutorial.regex.server;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.ocpsoft.tutorial.regex.client.shared.RegexRequest;
import org.ocpsoft.tutorial.regex.client.shared.RegexResult;

@RequestScoped
public class RegexListener
{
   @Inject
   private Event<RegexResult> resultEvent;

   public void handleRequest(@Observes RegexRequest request)
   {
      System.out.println(request);

      RegexResult result = new RegexResult();
      if (request.getText() != null)
      {
         result.setText(request.getText());
         if (request.getRegex() != null)
            result.setMatches(request.getText().matches(request.getRegex()));
      }

      resultEvent.fire(result);
   }
}