package org.ocpsoft.tutorial.regex.client.local;

import java.util.Arrays;
import java.util.List;

public class ColorCycle
{
   private int colorIndex = 0;

   private List<String> colors = Arrays.asList(
            "8b008b",
            "ff8c00",
            "0099ff",
            "ff1493",
            "b22222",
            "1e90ff",
            "ff4500",
            "daa520"
            );

   public String getColor()
   {
      if (colorIndex == colors.size())
         colorIndex = 0;
      return colors.get(colorIndex++);
   }
}
