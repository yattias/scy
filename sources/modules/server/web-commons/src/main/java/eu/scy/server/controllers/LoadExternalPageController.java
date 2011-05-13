package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.mai.2011
 * Time: 12:53:45
 * To change this template use File | Settings | File Templates.
 */
public class LoadExternalPageController extends BaseController{
    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        String content = "";
        try {
            String url = request.getParameter("url");
            URL u = new URL(url);
            URLConnection connection = u.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                content = content + inputLine;
            }

            //content = URLEncoder.encode(content,  "utf-8");
            content = content.replaceAll("'", "");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        
        logger.info("CONTENT IS: " + content);


        modelAndView.addObject("content", content);
    }
}
