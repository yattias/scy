package eu.scy.agents.util;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.impl.EloTypes;
import org.junit.Before;
import org.junit.Test;
import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;

import static org.junit.Assert.assertEquals;

/** @author fschulz */
public class UtilitiesTest extends AbstractTestFixture {


    private IELO elo;

    @Before
    public void before() {
        elo = createNewElo("Test", EloTypes.SCY_XPROC);
        elo.setContent(new BasicContent(XML));
    }

    @Test
    public void testGetEloText() throws Exception {
        String text = Utilities.getEloText(elo, "//learner_proc/proc_hypothesis/hypothesis", null);
        assertEquals("Something that should be a hypothesis. Added something. Blabla.",text);
    }

    private static final String XML = "<experimental_procedure>  <mission>    <type>      <id>TYPE_DEFAULT</id>      <name " +
            "language=\"fr\">Type de matériel par défaut</name>      <name language=\"en\">Default material type</name>    " +
            "</type>    <type>      <id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name " +
            "language=\"en\">Liquid</name>    </type>    <type>      <id>TYPE_PROD_CHIM</id>      <name " +
            "language=\"fr\">Produit chimique</name>      <name language=\"en\">Chemical product</name>    </type>    <type> " +
            "     <id>TYPE_MESUREUR</id>      <name language=\"fr\">Mesureur</name>      <name " +
            "language=\"en\">Mesureur</name>    </type>    <type>      <id>TYPE_DELIVREUR</id>      <name " +
            "language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name>    </type>    <type>      " +
            "<id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name language=\"en\">Container</name> " +
            "   </type>    <type>      <id>TYPE_USTENSILE</id>      <name language=\"fr\">Ustensile</name>      <name " +
            "language=\"en\">Ustensil</name>    </type>    <type>      <id>TYPE_INGREDIENT</id>      <name " +
            "language=\"fr\">Ingrédient</name>      <name language=\"en\">Ingredient</name>    </type>    <type>      " +
            "<id>TYPE_CHAUFFANT</id>      <name language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name> " +
            "   </type>    <type>      <id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name" +
            " language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <type>      <id>TYPE_DEFAULT</id>      <name language=\"fr\">Type de" +
            " matériel par défaut</name>      <name language=\"en\">Default material type</name>    </type>    <type>      " +
            "<id>TYPE_LIQUIDE</id>      <name language=\"fr\">Liquide</name>      <name language=\"en\">Liquid</name>    " +
            "</type>    <type>      <id>TYPE_PROD_CHIM</id>      <name language=\"fr\">Produit chimique</name>      <name " +
            "language=\"en\">Chemical product</name>    </type>    <type>      <id>TYPE_MESUREUR</id>      <name " +
            "language=\"fr\">Mesureur</name>      <name language=\"en\">Mesureur</name>    </type>    <type>      " +
            "<id>TYPE_DELIVREUR</id>      <name language=\"fr\">Délivreur</name>      <name language=\"en\">Délivreur</name> " +
            "   </type>    <type>      <id>TYPE_CONTENEUR</id>      <name language=\"fr\">Conteneur</name>      <name " +
            "language=\"en\">Container</name>    </type>    <type>      <id>TYPE_USTENSILE</id>      <name " +
            "language=\"fr\">Ustensile</name>      <name language=\"en\">Ustensil</name>    </type>    <type>      " +
            "<id>TYPE_INGREDIENT</id>      <name language=\"fr\">Ingrédient</name>      <name " +
            "language=\"en\">Ingredient</name>    </type>    <type>      <id>TYPE_CHAUFFANT</id>      <name " +
            "language=\"fr\">Chauffant</name>      <name language=\"en\">Chauffant</name>    </type>    <type>      " +
            "<id>TYPE_HOMOGENEISATEUR</id>      <name language=\"fr\">Homogénéisateur</name>      <name " +
            "language=\"en\">Homogénéisateur</name>    </type>    <type>      <id>TYPE_SOLUTION</id>      <name " +
            "language=\"fr\">Solution</name>      <name language=\"en\">Solution</name>    </type>    <type>      " +
            "<id>TYPE_OBJET</id>      <name language=\"fr\">Objet</name>      <name language=\"en\">Object</name>    </type> " +
            "   <type>      <id>TYPE_SOLIDE</id>      <name language=\"fr\">Solide</name>      <name " +
            "language=\"en\">Solide</name>    </type>    <initial_proc>      <name language=\"en\">Procedure</name>      " +
            "<question>        <description language=\"en\" />        <task_right>          <edit_right>X</edit_right>          " +
            "<delete_right>N</delete_right>          <copy_right>N</copy_right>          <move_right>N</move_right>          " +
            "<parent_right>X</parent_right>          <draw_right>N</draw_right>          <repeat_right>N</repeat_right>        " +
            "</task_right>      </question>      <proc_hypothesis>        <hypothesis language=\"en\" />        <comment " +
            "language=\"en\" />        <hypothesis_hide>false</hypothesis_hide>      </proc_hypothesis>      " +
            "<proc_general_principle>        <principle language=\"en\" />        <principle_comment language=\"en\" />      " +
            "  <principle_hide>false</principle_hide>      </proc_general_principle>      <manipulation />      <code>TEST</code>    " +
            "  <is_free_action>true</is_free_action>      <is_task>true</is_task>      <is_task_repeat>false</is_task_repeat>      " +
            "<hypothesis_mode>M</hypothesis_mode>      <principle_mode>M</principle_mode>      <draw_principle>false</draw_principle>    " +
            "" +
            "  " +
            "<evaluation_mode>N</evaluation_mode>      <strategy_ref>        <code>S4</code>      </strategy_ref>      " +
            "<is_task_draw>false</is_task_draw>    </initial_proc>  </mission>  <learner_proc>    <name language=\"en\">Procedure</name> " +
            "" +
            "   <question>      <description language=\"en\" />      <task_right>        <edit_right>X</edit_right>        " +
            "<delete_right>N</delete_right>        <copy_right>N</copy_right>        <move_right>N</move_right>        " +
            "<parent_right>X</parent_right>        <draw_right>N</draw_right>        <repeat_right>N</repeat_right>      </task_right>   " +
            "" +
            " </question>    <proc_hypothesis>      <hypothesis language=\"en\">Something that should be a hypothesis. Added something. " +
            "Blabla.</hypothesis>      <comment language=\"en\" />      <hypothesis_hide>false</hypothesis_hide>    </proc_hypothesis>   " +
            "" +
            " <proc_general_principle>      <principle language=\"en\" />      <principle_comment language=\"en\" />      " +
            "<principle_hide>false</principle_hide>    </proc_general_principle>    <manipulation />    <material_used>      <material>  " +
            "" +
            "      <id>305</id>        <name language=\"en\">PC</name>        <info language=\"en\" />      </material>      " +
            "<used>false</used>      <comment language=\"en\" />    </material_used>    <material_used>      <material>        " +
            "<id>306</id>        <name language=\"en\">Vernier pH sensor</name>        <info language=\"en\" />      </material>      " +
            "<used>false</used>      <comment language=\"en\" />    </material_used>    <material_used>      <material>        " +
            "<id>307</id>        <name language=\"en\">Distilled water</name>        <info language=\"en\">3 litres</info>      " +
            "</material>      <used>false</used>      <comment language=\"en\" />    </material_used>    <material_used>      <material> " +
            "" +
            "       <id>308</id>        <name language=\"en\">Samples of ground</name>        <info language=\"en\">limestone, granite, " +
            "sand, peat</info>      </material>      <used>false</used>      <comment language=\"en\" />    </material_used>    " +
            "<material_used>      <material>        <id>309</id>        <name language=\"en\">Vessels</name>        <info " +
            "language=\"en\">250 ml</info>      </material>      <used>false</used>      <comment language=\"en\" />    </material_used> " +
            "" +
            "   <material_used>      <material>        <id>310</id>        <name language=\"en\">Scales</name>        <info " +
            "language=\"en\" />      </material>      <used>false</used>      <comment language=\"en\" />    </material_used>    " +
            "<material_used>      <material>        <id>311</id>        <name language=\"en\">Sulphuric acid solutions</name>        " +
            "<info language=\"en\">Solution A - 0,0001M&#xD;&#xD;Solution B - 0,01 M</info>      </material>      <used>false</used>     " +
            "" +
            " <comment language=\"en\" />    </material_used>    <material_used>      <material>        <id>312</id>        <name " +
            "language=\"en\">Syringes</name>        <info language=\"en\">2ml and 10ml</info>      </material>      <used>false</used>   " +
            "" +
            "   <comment language=\"en\" />    </material_used>    <material_used>      <material>        <id>313</id>        <name " +
            "language=\"en\">Paper towel</name>        <info language=\"en\" />      </material>      <used>false</used>      <comment " +
            "language=\"en\" />    </material_used>    <initial_proc_ref>      <code>TEST</code>    </initial_proc_ref>  " +
            "</learner_proc></experimental_procedure>";
}
