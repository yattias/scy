package eu.scy.common.scyelo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.scy.common.scyelo.EloLogicalRole;

public class EloLogicalRoleTest
{

   @Before
   public void setUp() throws Exception
   {
   }

   @Test
   public void testMyValueOf()
   {
      assertNull(EloLogicalRole.myValueOf(null));
      assertNull(EloLogicalRole.myValueOf(""));
      assertNull(EloLogicalRole.myValueOf("not existing"));
      testMyValueOf(EloLogicalRole.CONCEPT_MAP, "conceptMap");
      testMyValueOf(EloLogicalRole.CONCEPT_MAP, "concept_Map");
   }

   private void testMyValueOf(EloLogicalRole eloLogicalRole, String value)
   {
      assertEquals(eloLogicalRole, EloLogicalRole.myValueOf(value));
      assertEquals(eloLogicalRole, EloLogicalRole.myValueOf(value.toLowerCase()));
      assertEquals(eloLogicalRole, EloLogicalRole.myValueOf(value.toUpperCase()));
   }

   @Test
   public void testEquals()
   {
      assertFalse(EloLogicalRole.CONCEPT_MAP.equals(null));
      testEquals(false, EloLogicalRole.CONCEPT_MAP, "");
      testEquals(false, EloLogicalRole.CONCEPT_MAP, "not existing");
      testEquals(true, EloLogicalRole.CONCEPT_MAP, "conceptMap");
      testEquals(true, EloLogicalRole.CONCEPT_MAP, "concept_Map");
   }

   private void testEquals(boolean equal, EloLogicalRole eloLogicalRole, String value)
   {
      assertEquals(equal, eloLogicalRole.equals(value));
      assertEquals(equal, eloLogicalRole.equals(value.toLowerCase()));
      assertEquals(equal, eloLogicalRole.equals(value.toUpperCase()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvertToEloLogicalRoles1()
   {
      testConvertToEloLogicalRoles("not existing");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvertToEloLogicalRoles2()
   {
      testConvertToEloLogicalRoles("conceptMap", "not existing", "table");
   }

   @Test
   public void testConvertToEloLogicalRoles3()
   {
      assertNull(EloLogicalRole.convertToEloLogicalRoles(null));
      testConvertToEloLogicalRoles();
      testConvertToEloLogicalRoles("conceptMap");
      testConvertToEloLogicalRoles("conceptMap", "concept_Map", "table");
   }

   private void testConvertToEloLogicalRoles(String... strings)
   {
      List<String> values = new ArrayList<String>();
      for (String value : strings)
      {
         values.add(value);
      }
      List<EloLogicalRole> eloLogicalRoles = EloLogicalRole
               .convertToEloLogicalRoles(values);
      assertEquals(strings.length, eloLogicalRoles.size());
      for (int i = 0; i < strings.length; i++)
      {
         assertEquals(EloLogicalRole.myValueOf(strings[i]), eloLogicalRoles.get(i));
      }
   }


}
