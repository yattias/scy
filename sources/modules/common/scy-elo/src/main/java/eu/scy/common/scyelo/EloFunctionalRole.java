/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SikkenJ
 */
public enum EloFunctionalRole
{

   MAP_CONCEPT("mapConcept"), MAP_INITIAL_IDEAS("mapInitialIdeas"), MAP_KEY_IDEAS("mapKeyIdeas"),
   DATASET("dataset","datasetManual","dataset_manual","datasetAutomatic","dataset_automatic"),
   /*DATASET_MANUAL("datasetManual"), DATASET_AUTOMATIC("datasetAutomatic"),*/ 
   DATASET_PROCESSED("datasetProcessed"),
   EXPERIMENTAL_DESIGN("experimentalDesign"), EXPERIMENTAL_CONCLUSION("experimentalConclusion"),
   DRAWING_DESIGN("drawingDesign"),
   ARTEFACT_DESIGNED("artefactDesigned"),
   STATEMENT_PROBLEM("statementProblem"),
   WEB_SUMMARY("webSummary"),
   HYPOTHESIS("hypothesis"),
   DATA_ANALYSIS("dataAnalysis"),
   RESEARCH_QUESTION("researchQuestion"),
   ARGUMENT("argument"),
   INTERVIEW("interview"),
   DOCUMENT_DESIGN("documentDesign"),DOCUMENT_REFLECTION("documentReflection"),
   REPORT_EVALUATION("reportEvaluation"), REPORT_FINAL("reportFinal"),
   PRESENTATION_DESIGN("presentationDesign"), PRESENTATION_FINAL("presentationFinal"), PRESENTATION_EXPERT("presentationExpert"),
   INFORMATION_RESOURCE("informationResource"), INFORMATION_ASSIGNMENT("informationAssignment"), INFORMATION_HELP("informationHelp"),
   SOURCE_DATA("sourceData");
   
   private final String string;
   private final String[] alternativeStrings;
   private static final Map<String,EloFunctionalRole> stringMap = new HashMap<String,EloFunctionalRole>();
   
   static{
      for (EloFunctionalRole value : EloFunctionalRole.values()){
         stringMap.put(value.string.toUpperCase(), value);
         stringMap.put(value.name().toUpperCase(), value);
         for (String s: value.alternativeStrings){
            stringMap.put(s.toUpperCase(), value);
         }
      }
   }
   
   private EloFunctionalRole(String string, String...alternativeStrings)
   {
      this.string = string;
      this.alternativeStrings = alternativeStrings;
   }

   protected static EloFunctionalRole myValueOf(String aString)
   {
      if (aString == null || aString.length() == 0)
      {
         return null;
      }
      return stringMap.get(aString.toUpperCase());
   }

   @Override
   public String toString()
   {
      return string;
   }

   /**
    * does a bit smart equals, it is case insensitive and you may use the name or the string representation
    * 
    * @param aString
    * @return
    */
   public boolean equals(String aString)
   {
      if (aString==null || aString.length()==0){
         return false;
      }
      String upperCaseString = aString.toUpperCase();
      EloFunctionalRole eloFunctionalRole = stringMap.get(upperCaseString);
      return equals(eloFunctionalRole);
   }

   public static List<EloFunctionalRole> convertToEloFunctionalRoles(List<String> values)
   {
      if (values == null)
      {
         return null;
      }
      List<EloFunctionalRole> eloFunctionalRoles = new ArrayList<EloFunctionalRole>();
      for (String value : values)
      {
         EloFunctionalRole eloFunctionalRole = myValueOf(value);
         if (eloFunctionalRole != null)
         {
            eloFunctionalRoles.add(eloFunctionalRole);
         }
         else
         {
            throw new IllegalArgumentException(
                     "unknown string representation for EloFunctionalRole, " + value);
         }
      }
      return eloFunctionalRoles;
   }

}
