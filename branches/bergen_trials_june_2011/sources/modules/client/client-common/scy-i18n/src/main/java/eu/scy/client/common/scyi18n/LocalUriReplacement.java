/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.common.scyi18n;

/**
 * 
 * @author SikkenJ
 */
public class LocalUriReplacement
{
   private static final String sourceReplacementSeparator = "->";
   private String source;
   private String replacement;

   public LocalUriReplacement(String source, String replacement)
   {
      this.source = source;
      this.replacement = makeUniversalPath(replacement);
   }

   public LocalUriReplacement(String specification)
   {
      int separatorIndex = specification.indexOf(sourceReplacementSeparator);
      if (separatorIndex < 0)
      {
         throw new IllegalArgumentException("cannot find separator '" + sourceReplacementSeparator
                  + "' in LocalUriReplacement specification: " + specification);
      }
      if (separatorIndex == 0)
      {
         throw new IllegalArgumentException(
                  "source may not be empty in LocalUriReplacement specification: " + specification);
      }
      this.source = specification.substring(0, separatorIndex);
      this.replacement = makeUniversalPath(specification.substring(separatorIndex
               + sourceReplacementSeparator.length()));
   }

   private String makeUniversalPath(String path)
   {
      return path.replace("\\", "/");
   }

   public String getReplacement()
   {
      return replacement;
   }

   public String getSource()
   {
      return source;
   }
}
