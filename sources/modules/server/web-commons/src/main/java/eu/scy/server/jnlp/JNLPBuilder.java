package eu.scy.server.jnlp;

import eu.scy.core.UserService;
import eu.scy.core.model.User;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mai.2011
 * Time: 13:42:20
 * To change this template use File | Settings | File Templates.
 */
public class JNLPBuilder {

    private UserService userService;


    public void streamJnlpString(String userName, String mission, String password, String serverName, String serverPort, ServletRequest request, HttpServletResponse response) {
        try {
            String jnlpContent = generateJnlpString(userName, mission, password, serverName, serverPort);
            response.setContentType("application/x-java-jnlp-file");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.getOutputStream().print(jnlpContent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String generateJnlpString(String userName, String mission, String password, String serverName, String serverPort) throws MalformedURLException, IOException {
        String jnlpUrl = "";

        if(serverName != null && serverPort != null) {
            jnlpUrl = "http://" + serverName + ":" + serverPort + "/extcomp/scy-lab.jnlp";
        } else {
            jnlpUrl = "http://scy.collide.info:8080/extcomp/scy-lab.jnlp";
        }
        URL url = new URL(jnlpUrl);
        URLConnection connection = url.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        String jnlpContent = "";

        Boolean extraArgumentsAdded = false;

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("/application") && !extraArgumentsAdded) {
                // System.out.println("ADDDING EXTRA ARGUMENTS!");
                extraArgumentsAdded = true;
            } else {
                // System.out.println("NOT APPLICATION");
            }
            jnlpContent += inputLine;
        }

        String locale = getLocale(userName);


        if (userName != null && password != null) {
            int index = jnlpContent.indexOf("</application-desc>");
            String start = jnlpContent.substring(0, index);
            String end = jnlpContent.substring(index, jnlpContent.length());
            String middle = "<argument>-defaultUsername</argument>";
            middle += "<argument>" + userName + "</argument>";
            middle += "<argument>-defaultpassword</argument>";
            middle += "<argument>" + password + "</argument>";
            middle += "<argument>-languageList</argument>";
            middle += "<argument>" + locale + "</argument>";
            if(mission != null) {
                middle += "<argument>-defaultMission</argument>";
                middle += "<argument>" + mission.trim() + "</argument>";
            }
            middle +="<argument>-autologin</argument>";
            middle +="<argument>true</argument>";
            jnlpContent = start + middle + end;
        } else {
            // seems like we are not logged in -> no autologin in SCY-Lab
        }
        in.close();
        return jnlpContent;
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getLocale(String userName) {
        User user = getUserService().getUser(userName);
        if(user != null) {
            return user.getUserDetails().getLocale();
        }
        return "en";
    }
}
