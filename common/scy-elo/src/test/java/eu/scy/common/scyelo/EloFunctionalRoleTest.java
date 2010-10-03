package eu.scy.common.scyelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.scy.common.scyelo.EloFunctionalRole;

public class EloFunctionalRoleTest
{

   @Before
   public void setUp() throws Exception
   {
   }

   @Test
   public void testMyValueOf()
   {
      assertNull(EloFunctionalRole.myValueOf(null));
      assertNull(EloFunctionalRole.myValueOf(""));
      assertNull(EloFunctionalRole.myValueOf("not existing"));
      testMyValueOf(EloFunctionalRole.DRAWING_DESIGN, "drawingDesign");
      testMyValueOf(EloFunctionalRole.DRAWING_DESIGN, "drawing_Design");
   }

   private void testMyValueOf(EloFunctionalRole eloFunctionalRole, String value)
   {
      assertEquals(eloFunctionalRole, EloFunctionalRole.myValueOf(value));
      assertEquals(eloFunctionalRole, EloFunctionalRole.myValueOf(value.toLowerCase()));
      assertEquals(eloFunctionalRole, EloFunctionalRole.myValueOf(value.toUpperCase()));
   }

   @Test
   public void testEquals()
   {
      assertFalse(EloFunctionalRole.DRAWING_DESIGN.equals(null));
      testEquals(false, EloFunctionalRole.DRAWING_DESIGN, "");
      testEquals(false, EloFunctionalRole.DRAWING_DESIGN, "not existing");
      testEquals(true, EloFunctionalRole.DRAWING_DESIGN, "drawingDesign");
      testEquals(true, EloFunctionalRole.DRAWING_DESIGN, "drawing_Design");
   }

   private void testEquals(boolean equal, EloFunctionalRole eloFunctionalRole, String value)
   {
      assertEquals(equal, eloFunctionalRole.equals(value));
      assertEquals(equal, eloFunctionalRole.equals(value.toLowerCase()));
      assertEquals(equal, eloFunctionalRole.equals(value.toUpperCase()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvertToEloFunctionalRoles1()
   {
      testConvertToEloFunctionalRoles("not existing");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvertToEloFunctionalRoles2()
   {
      testConvertToEloFunctionalRoles("drawingDesign", "not existing", "interview");
   }

   @Test
   public void testConvertToEloFunctionalRoles3()
   {
      assertNull(EloFunctionalRole.convertToEloFunctionalRoles(null));
      testConvertToEloFunctionalRoles();
      testConvertToEloFunctionalRoles("drawingDesign");
      testConvertToEloFunctionalRoles("drawingDesign", "drawing_Design", "interview");
   }

   private void testConvertToEloFunctionalRoles(String... strings)
   {
      List<String> values = new ArrayList<String>();
      for (String value : strings)
      {
         values.add(value);
      }
      List<EloFunctionalRole> eloFunctionalRoles = EloFunctionalRole
               .convertToEloFunctionalRoles(values);
      assertEquals(strings.length, eloFunctionalRoles.size());
      for (int i = 0; i < strings.length; i++)
      {
         assertEquals(EloFunctionalRole.myValueOf(strings[i]), eloFunctionalRoles.get(i));
      }
   }

}
