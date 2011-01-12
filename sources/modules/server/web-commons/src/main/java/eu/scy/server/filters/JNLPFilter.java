package eu.scy.server.filters;

import eu.scy.common.configuration.Configuration;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2011
 * Time: 06:05:38
 * To change this template use File | Settings | File Templates.
 */
public class JNLPFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //System.out.println("==== DO LOCAL FILE FILTER");
        if (request.getRequestURL().indexOf(".jnlp") >= 0) {

            String userName = servletRequest.getParameter("username");
            String mission = servletRequest.getParameter("mission");
            if(mission != null) {
                mission = URLDecoder.decode(mission, "UTF-8");
            }
            String password = "";
            User user = null;

            String serverName = Configuration.getInstance().getRooloServer();


            ServletContext servletContext = ((HttpServletRequest) servletRequest).getSession().getServletContext();

            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            UserService userService = (UserService) wac.getBean("userService");
            if(userService != null) {
                user = userService.getUser(userName);
                if(user != null) {
                    password = user.getUserDetails().getPassword();    
                }


            } else {
                System.out.println("SHIT - USER SERVICE IS NULL!!");
            }


            System.out.println("LOADING JNLP FILE! " + request.getRequestURL());
            response.setContentType("application/x-java-jnlp-file");

            String jnlpUrl = "";

            if(serverName != null) {
                jnlpUrl = "http://" + serverName + ":8080/extcomp/scy-lab.jnlp";
                System.out.println("USING SERVER NAME FROM STARTUP PARAMETER ROOLO.SERVER.NAME " + jnlpUrl);
            } else {
                jnlpUrl = "http://scy.collide.info:8080/extcomp/scy-lab.jnlp";
                System.out.println("DEFAULTING TO SCY.COLLIDE.INFO FOR SCY LAB!!");
                System.out.println("USING " + jnlpUrl);

            }
            URL url = new URL(jnlpUrl);
            URLConnection connection = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            String jnlpContent = "";

            Boolean extraArgumentsAdded = false;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("/application") && !extraArgumentsAdded) {
                    System.out.println("ADDDING EXTRA ARGUMENTS!");
                    extraArgumentsAdded = true;
                } else {
                    System.out.println("NOT APPLICATION");
                }
                jnlpContent += inputLine;
            }

            int index = jnlpContent.indexOf("</application");
            String start = jnlpContent.substring(0, index);
            String end = jnlpContent.substring(index, jnlpContent.length());
            String middle = "<argument>-defaultUsername</argument>\n";
            middle += "<argument>" + userName + "</argument>\n";
            middle += "<argument> -defaultpassword</argument>\n";
            middle += "<argument>" + password + "</argument>\n";
            if(mission != null) {
                middle += "<argument> -mission</argument>\n";
                middle += "<argument>" + mission.trim() + "</argument>\n";
            }
            middle +="<argument> -autologin</argument>\n";
            middle +="<argument>true</argument>\n";
            jnlpContent = start + middle + end;
            in.close();
            response.getOutputStream().print(jnlpContent);
        } else {
            //System.out.println("NOT JNLP");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
