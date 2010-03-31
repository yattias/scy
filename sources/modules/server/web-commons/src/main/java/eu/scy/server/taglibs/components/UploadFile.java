package eu.scy.server.taglibs.components;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mar.2010
 * Time: 05:39:50
 * To change this template use File | Settings | File Templates.
 */
public class UploadFile extends TagSupport {

    private String listener;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();



            /*pageContext.getOut().write("<form method=\"post\" action=\"/webapp/components/fileupload/fileupload.html\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">");
            pageContext.getOut().write("<input type=\"file\" name=\"file\"/>");
            pageContext.getOut().write("<input type=\"text\" name=\"listener\" value=\"" + getListener() + "\"/>");
            pageContext.getOut().write("<div id=\"fFiles\" class=\"field\"></div>");

            pageContext.getOut().write("<div class=\"uploadBtn\" dojoType=\"dojox.form.FileUploader\" hoverClass=\"uploadHover\" pressClas=\"uploadPress\"\n" +
                    "     activeClass=\"uploadBtn\" disabledClass=\"uploadDisable\" uploadUrl=\"../serverpage.php\">Select Files</div>");
            pageContext.getOut().write("<input type=\"submit\" tabIndex=\"6\" id=\"fSubmit\" class=\"btn\" dojoType=\"dijit.form.Button\" value=\"Submit\" />\n" +
                    "\t\t\t\t<div indeterminate=\"false\" id=\"progressBar\" class=\"progBar\" dojoType=\"dijit.ProgressBar\"></div>");
            pageContext.getOut().write("</form>");*/
            pageContext.getOut().write("<script>" +
                    "dojo.require(\"dojox.form.FileUploader\");\n" +
                    "dojo.require(\"dijit.form.Button\");\n" +
                    "\t\tdojo.require(\"dijit.ProgressBar\");\n" +
                    "\t\taddThumb = function(d, id){\n" +
                    "\t\t\tconsole.log(\"THUMB:\", d);\n" +
                    "\t\t\tvar fileRoot = dojo.moduleUrl(\"dojox.form\", \"tests\").toString();\n" +
                    "\t\t\tvar img = '<img src='+fileRoot+\"/\"+escape(d.file)+\n" +
                    "\t\t\t(d.width>d.height ?\n" +
                    "\t\t\t' width=\"50\"/>' :\n" +
                    "\t\t\t' height=\"50\"/>');\n" +
                    "\t\t\tconsole.log(\"IMG:\", img)\n" +
                    "\t\t\tvar str = '<div id=\"file_'+d.name+'\" class=\"thumb\"><div class=\"thumbPic\">'+img+'</div>';\n" +
                    "\t\t\tstr += '<div class=\"thumbText\">';\n" +
                    "\t\t\tif(d.fGroup || d.hGroup){\n" +
                    "\t\t\t\tstr += 'Group: '+(d.fGroup || d.hGroup)+'<br/>';\n" +
                    "\t\t\t}\n" +
                    "\t\t\tstr += 'Title: '+d.name+'<br/>';\n" +
                    "\t\t\tif(d.author){\n" +
                    "\t\t\t\tstr += 'Author: '+ d.author+'<br/>';\n" +
                    "\t\t\t}\n" +
                    "\t\t\tif(d.date){\n" +
                    "\t\t\t\tstr += d.date+' ';\n" +
                    "\t\t\t}\n" +
                    "\t\t\tstr += Math.ceil(d.size*.001)+'kb</div></div>';\n" +
                    "\t\t\tdojo.byId(id).innerHTML += str;\n" +
                    "\t\t}\n" +
                    "        dojo.addOnLoad(function(){\n" +
                    "\n" +
                    "\t\t\tvar props = {\n" +
                    "\t\t\t\tisDebug:false,\n" +
                    "\t\t\t\thtmlFieldName:'file',\n" +
                    "\t\t\t\tflashFieldName:'file',\n" +
                    "\t\t\t\thoverClass:\"uploadHover\",\n" +
                    "\t\t\t\tactiveClass:\"uploadPress\",\n" +
                    "\t\t\t\tdisabledClass:\"uploadDisabled\",\n" +
                    //"\t\t\t\tuploadUrl:dojo.moduleUrl(\"dojox.form\", \"webapp/components/fileupload/fileupload.html\"),\n" +
                    "\t\t\t\tuploadUrl:\"/webapp/components/fileupload/fileupload.html\",\n" +
                    "\t\t\t\tfileMask:[\n" +
                    "\t\t\t\t\t[\"Jpeg File\", \t\"*.jpg;*.jpeg\"],\n" +
                    "\t\t\t\t\t[\"GIF File\", \t\"*.gif\"],\n" +
                    "\t\t\t\t\t[\"PNG File\", \t\"*.png\"],\n" +
                    "\t\t\t\t\t[\"All Images\", \t\"*.jpg;*.jpeg;*.gif;*.png\"]\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t}\n" +
                    "\t\t\t\n" +
                    "\t\t\tif(dojo.byId(\"btnF\")){\n" +
                    "\t\t\t\tdojo.byId(\"fFiles\").value = \"\"; \n" +
                    "\t\t\t\tvar f = new dojox.form.FileUploader(dojo.mixin({\n" +
                    "\t\t\t\t\tprogressWidgetId:\"progressBar\",\n" +
                    "\t\t\t\t\tshowProgress:true,\n" +
                    "\t\t\t\t\tfileListId:\"fFiles\",\n" +
                    "\t\t\t\t\ttabIndex:5,\n" +
                    "\t\t\t\t\tselectMultipleFiles:true,\n" +
                    "\t\t\t\t\tdeferredUploading:false\n" +
                    "\t\t\t\t},props), \"btnF\");\n" +
                    //"\t\t\t\tf.attr(\"disabled\", dojo.byId(\"fGroup\").value==\"\");\n" +
                    //"\t\t\t\tdojo.connect(dojo.byId(\"fGroup\"), \"keyup\", function(){\n" +
                    //"\t\t\t\t\tf.attr(\"disabled\", dojo.byId(\"fGroup\").value==\"\");\n" +
                    //"\t\t\t\t});\n" +

                    "\t\t\t\tdojo.connect(dijit.byId(\"fSubmit\"), \"onClick\", function(dataArray){\n" +
                    "\t\t\t\t\tf.submit(dojo.byId(\"formF\"));\n" +
                    
                    //"\t\t\t\t\tpostForm(dojo.byId(\"formF\"));\n" +

                    "\t\t\t\t});\n" +
                    "\t\t\t\tdojo.connect(f, \"onChange\", function(dataArray){\n" +
                    "\t\t\t\t\tconsole.log(\"onChange.data:\", dataArray);\n" +
                    "\t\t\t\t});\n" +
                    "\t\t\t\tdojo.connect(f, \"onComplete\", function(dataArray){\n" +
                    "\t\t\t\t\tdojo.forEach(dataArray, function(d){\n" +
                    "\t\t\t\t\t\taddThumb(d, \"fThumbs\");\n" +
                    "\t\t\t\t\t});\n" +
                    "\t\t\t\t});\n" +
                    "\t\t\t}\n" +
                    "\n" +
                    "\n" +
                    "\t\t\tif(dojo.byId(\"btnH\")){\n" +
                    "\t\t\t\tdojo.byId(\"hFiles\").value = \"\";\n" +
                    "\t\t\t\tvar h = new dojox.form.FileUploader(dojo.mixin({\n" +
                    "\t\t\t\t\tforce:\"html\",\n" +
                    "\t\t\t\t\tshowProgress:true,\n" +
                    "\t\t\t\t\tprogressWidgetId:\"progressBarHtml\",\n" +
                    "\t\t\t\t\tselectMultipleFiles:true,\n" +
                    "\t\t\t\t\tfileListId:\"hFiles\",\n" +
                    "\t\t\t\t\ttabIndex:11\n" +
                    "\t\t\t\t}, props), \"btnH\");\n" +
                    "\t\t\t\t\n" +
                    //"\t\t\t\th.attr(\"disabled\", dojo.byId(\"hGroup\").value==\"\");\n" +
                    //"\t\t\t\tdojo.connect(dojo.byId(\"hGroup\"), \"keyup\", function(){\n" +
                    //"\t\t\t\t\th.attr(\"disabled\", dojo.byId(\"hGroup\").value==\"\");\n" +
                    //"\t\t\t\t});\n" +
                    "\t\t\t\tdojo.connect(dijit.byId(\"hSubmit\"), \"onClick\", function(){\n" +
                    "\t\t\t\t\th.submit(dojo.byId(\"formH\"));\n" +
                    "\t\t\t\t});\n" +
                    "\t\t\t\tdojo.connect(h, \"onComplete\", function(dataArray){\n" +
                    "\t\t\t\t\tconsole.warn(\"html onComplete\", dataArray)\n" +
                    "\t\t\t\t\tdojo.forEach(dataArray, function(d){\n" +
                    "\t\t\t\t\t\taddThumb(d, \"hThumbs\");\n" +
                    "\t\t\t\t\t});\n" +
                    "\t\t\t\t});\n" +
                    "\t\t\t}\n" +
                    "\t\t\tif(dijit.byId(\"btnD\")){\n" +
                    "\t\t\t\tvar d = new FlashHTML.widget(dojo.mixin({button:dijit.byId(\"btnD\")}, props));\n" +
                    //"\t\t\t\td.attr(\"disabled\", dojo.byId(\"dTitle\").value==\"\");\n" +
                    //"\t\t\t\tdojo.connect(dojo.byId(\"dTitle\"), \"keyup\", function(){\n" +
                    //"\t\t\t\t\td.attr(\"disabled\", dojo.byId(\"dTitle\").value==\"\");\n" +
                    //"\t\t\t\t});\n" +
                    "\t\t\t\tdojo.connect(dijit.byId(\"fSubmit\"), \"onClick\", function(){\n" +
                    "\t\t\t\t\tf.submit(dojo.byId(\"formF\"));\n" +
                    "\t\t\t\t});\n" +
                    "\t\t\t}\n" +
                    "\t\t\t\n" +
                    "\t\t\t\n" +
                    "\t\t});\n" +
                    "        \n" +
                    "        \n" +
                    "    </script>");
            pageContext.getOut().write("\t\t\t<form id=\"formF\" class=\"form\" action=\"/webapp/components/fileupload/fileupload.html\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">\n");
            pageContext.getOut().write("<input id=\"uploadFilesField\" type=\"file\" name=\"file\"/>");
            pageContext.getOut().write("<input type=\"text\" name=\"listener\" value=\"" + getListener() + "\"/>");

            //pageContext.getOut().write("\t\t\t\t<label>Group Name:</label>\n" +
                    pageContext.getOut().write("\n" +
                    //"\t\t\t\t<input class=\"field\" tabIndex=\"1\" type=\"text\" value=\"\" id=\"fGroup\" name='fGroup' /><br/>\n" +
                    //"\t\t\t\t<label>Date:</label>\n" +
                    //"\t\t\t\t<input class=\"field\" tabIndex=\"2\" type=\"text\" value=\"\" id=\"fDate\" name='date' /><br/>\n" +
                    //"\t\t\t\t<label>Author:</label>\n" +
                    //"\t\t\t\t<input class=\"field\" tabIndex=\"3\" type=\"text\" value=\"\" id=\"fAuthor\" name='author' /><br/>\n" +
                    "\n" +
                    "\t\t\t\t<label>Files:</label>\n" +
                    "\t\t\t\t<div id=\"fFiles\" class=\"field\"></div>\n" +
                    "\t\t\t\t<div tabIndex=\"5\" id=\"btnF\" class=\"uploadBtn btn\">Flash Select Files</div>\n" +
                    "\t\t\t\t<button tabIndex=\"6\" id=\"fSubmit\" class=\"btn\" dojoType=\"dijit.form.Button\">Submit</button>\n" +
                    "\t\t\t\t<div indeterminate=\"false\" id=\"progressBar\" class=\"progBar\" dojoType=\"dijit.ProgressBar\"></div>\n" +
                    "\t\t\t</form>\n" +
                    "\t\t\t<div id=\"fThumbs\" class=\"thumbList\"></div>");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
