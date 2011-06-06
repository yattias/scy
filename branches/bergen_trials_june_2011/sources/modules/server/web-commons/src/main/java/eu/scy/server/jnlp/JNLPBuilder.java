package eu.scy.server.jnlp;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.PropertyTransfer;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.RuntimeELOService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mai.2011
 * Time: 13:42:20
 * To change this template use File | Settings | File Templates.
 */
public class JNLPBuilder {

    private UserService userService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private RuntimeELOService runtimeELOService;


    public void streamJnlpString(String userName, String mission, String password, String serverName, String serverPort, ServletRequest request, HttpServletResponse response) {
        try {
            String jnlpContent = generateJnlpString(userName, mission, password, serverName, serverPort, request);
            response.setContentType("application/x-java-jnlp-file");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.getOutputStream().print(jnlpContent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String generateJnlpString(String userName, String mission, String password, String serverName, String serverPort, ServletRequest request) throws MalformedURLException, IOException {
        String jnlpUrl = "";

        if(mission == null) {
            mission = loadMissionHack(userName);
        }


        if(serverName != null && serverPort != null) {
            jnlpUrl = "http://" + serverName + ":" + serverPort + "/extcomp/scy-lab.jnlp";
        } else {
            System.out.println("FREAK FUCKING SHIT HACK!");
            jnlpUrl = "http://scy.collide.info:8080/extcomp/scy-lab.jnlp";
        }

        //jnlpUrl = "http://scy.collide.info:8080/extcomp/scy-lab.jnlp";


        Enumeration parameters = request.getParameterNames();
        if(parameters.hasMoreElements()) jnlpUrl += "?";
        while(parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            String value = request.getParameter(parameter);
            jnlpUrl +=parameter;
            if(value != null) jnlpUrl+="=" + value;
            if(parameters.hasMoreElements()) jnlpUrl +="&";
        }

        System.out.println("JNLP URL: " + jnlpUrl);

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
            if (mission != null) {
                middle += "<argument>-defaultMission</argument>";
                middle += "<argument>" + mission.trim() + "</argument>";
            }
            middle += "<argument>-autologin</argument>";
            middle += "<argument>true</argument>";

            /*String additionalArguments = getAdditionalArgumentsFromMission(mission);
            if(additionalArguments.length() > 0) {
                middle += additionalArguments;
            } */

            String singleEloOptions = getSingleEloOptionsIfApplicable(request);
            if(singleEloOptions.length() > 0 ) {
                middle += singleEloOptions;
            }

            jnlpContent = start + middle + end;
        } else {
            // seems like we are not logged in -> no autologin in SCY-Lab
        }
        in.close();
        return jnlpContent;
    }

    private String loadMissionHack(String userName) {
        List runtimeElos = getRuntimeELOService().getRuntimeElosForUser(userName);
        if(runtimeElos.size() == 1) {
            MissionRuntimeElo missionRuntimeElo = (MissionRuntimeElo) runtimeElos.get(0);
            String uri = String.valueOf(missionRuntimeElo.getUri());
            try {
                return URLEncoder.encode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getSingleEloOptionsIfApplicable(ServletRequest request) {
        String singleEloUri = request.getParameter("singleEloUri");
        String returnString = "";
        if (singleEloUri != null) {
            returnString += "<argument>-singleEloUri</argument>";
            returnString += "<argument>" + singleEloUri + "</argument>";
            returnString += "<argument>-globalReadOnlyMode</argument>";
            returnString += "<argument>true</argument>";
        }
        return returnString;
    }

    private String getAdditionalArgumentsFromMission(String mission) {
        String returnString = "";
        if (mission != null) {
            PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMissionRuntimeElo(mission);

            for (int i = 0; i < pedagogicalPlanTransfer.getTechnicalInfo().getJnlpProperties().size(); i++) {
                PropertyTransfer propertyTransfer = (PropertyTransfer) pedagogicalPlanTransfer.getTechnicalInfo().getJnlpProperties().get(i);
                returnString += "<argument>-" + propertyTransfer.getName() + "</argument>";
                returnString += "<argument>" + propertyTransfer.getValue() + "</argument>";
            }

        }

        return returnString;
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getLocale(String userName) {
        User user = getUserService().getUser(userName);
        if (user != null) {
            return user.getUserDetails().getLocale();
        }
        return "en";
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }
}
