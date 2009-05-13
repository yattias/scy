package eu.scy.modules.useradmin;

import org.apache.tapestry5.test.PageTester;
import org.apache.tapestry5.test.TapestryTestCase;
import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.apache.tapestry5.dom.Document;
import org.testng.annotations.Test;



/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2008
 * Time: 13:23:50
 * To change this template use File | Settings | File Templates.
 */
@Test
public class UserAdminTest {


   @Test
    public void test1() {
        /*String appPackage = "eu.scy.modules.useradmin";
        String appName = "scyadmin";
        PageTester tester = new PageTester(appPackage, "", "src/main/webapp");
        Document doc = tester.renderPage("Home");
        */
        
        //assertEquals(doc.getElementById("id1").getChildText(), "hello");
    }

    /*@Test
        public void integration_test() throws Exception
        {
            open(BASE_URL);

            type("input", "paris in the springtime");
            clickAndWait("//input[@value='Convert']");

            assertFieldValue("input", "PARIS IN THE SPRINGTIME");
        }

      */


}
