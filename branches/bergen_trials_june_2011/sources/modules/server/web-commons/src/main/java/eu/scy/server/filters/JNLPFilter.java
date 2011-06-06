package eu.scy.server.filters;

import eu.scy.common.configuration.Configuration;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.server.jnlp.JNLPBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2011
 * Time: 06:05:38
 * To change this template use File | Settings | File Templates.
 */
public class JNLPFilter implements Filter, ApplicationContextAware {


    private JNLPBuilder jnlpBuilder;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        //// System.out.println("==== DO LOCAL FILE FILTER");
        if (request.getRequestURL().indexOf(".jnlp") >= 0) {

            String userName = servletRequest.getParameter("username");
            String mission = servletRequest.getParameter("mission");


            if(userName == null) {
                //userName = ((HttpServletRequest) servletRequest).getRemoteUser();
                userName = SecurityContextHolder.getContext().getAuthentication().getName();
            }

            if(mission != null) {
                mission = URLDecoder.decode(mission, "UTF-8");
            }
            String password = null;
            User user = null;

            String serverName = servletRequest.getServerName();
            String serverPort = String.valueOf(servletRequest.getLocalPort());
            
            ServletContext servletContext = ((HttpServletRequest) servletRequest).getSession().getServletContext();

            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            UserService userService = (UserService) wac.getBean("userService");
            if(userService != null) {
                user = userService.getUser(userName);
                if(user != null) {
                    password = user.getUserDetails().getPassword();    
                }
            } else {
                // System.out.println("SHIT - USER SERVICE IS NULL!!");
            }

            // System.out.println("LOADING JNLP FILE! " + request.getRequestURL());


            getJnlpBuilder().streamJnlpString(userName, mission, password, serverName, serverPort, request, response);

        } else {
            //// System.out.println("NOT JNLP");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JNLPBuilder getJnlpBuilder() {
        return jnlpBuilder;
    }

    public void setJnlpBuilder(JNLPBuilder jnlpBuilder) {
        this.jnlpBuilder = jnlpBuilder;
    }
}
