package info.collide.portlet.customAsset.classes;


public class DropdownBoxGenerator {
	
	private String addNew = "Add new";
	private String document = "Document";
	private String image = "Image";
	private String local = "";
	private String server = "";
	private String community = "";
	
	/**
	 * @param local
	 */
	public void setLocal(String local){
		this.local = local;
	}
	
	/**
	 * @param server
	 */
	public void setServer(String server){
		this.server = server;
	}
	
	/**
	 * @param community
	 */
	public void setCommunity(String community){
		this.community = community;
	}	
	
	/**
	 * @return
	 */
	public String getDropdownBox(){
		
		if(local.toLowerCase().contains("de")){
			addNew = "Neues hinzuf&uuml;gen";
			document = "Dokument";
			image = "Bild";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<form name=\"formName\" action=\"\"><div><select id=\"selector\" name=\"customAssetURL\" onchange=\"addNewAsset();\"><option value=\"\"><liferay-ui:message key=\"add-new\" />");
		sb.append(addNew);
		sb.append("...</option><option value=\"");
		sb.append(server);
		sb.append(community);
		sb.append("?p_p_id=20");
		sb.append("&p_p_lifecycle=0");
		sb.append("&p_p_state=maximized");
		sb.append("&p_p_mode=view");
		sb.append("&p_p_col_id=column-2");
	        sb.append("&p_p_col_count=1");
	        sb.append("&_20_struts_action=%2Fdocument_library%2Fedit_file_entry");
		sb.append("&_20_folderId=15262"); // folderId 15262 is the "Educational materials" folder
		sb.append("&_20_uploader=classic");
		sb.append("&_20_redirect="); 
		sb.append(community);
		sb.append("&_20_backURL=");
		sb.append(community);
		sb.append("&_20_tagsEntries=\"><liferay-ui:message key='<%= \"model.resource.com.liferay.portlet.documentlibrary.model.DLFileEntry\" %>' />");
		sb.append(document);
		sb.append("</option><option value=\"");
		sb.append(server);
		sb.append(community);
		sb.append("?p_p_id=31");
		sb.append("&p_p_lifecycle=0");
		sb.append("&p_p_state=maximized");
		sb.append("&p_p_mode=view");
		sb.append("&p_p_col_id=column-2");
		sb.append("&p_p_col_count=1");
		sb.append("&_31_struts_action=%2Fimage_gallery%2Fedit_image");
		sb.append("&_31_folderId=10844"); // folderId 10844 is the "SCYCOM picture" folder
		sb.append("&_31_uploader=classic");
		sb.append("&_31_redirect=");
		sb.append(community);
		sb.append("&_31_backURL="); 
		sb.append(community);
		sb.append("&_31_tagsEntries=\"><liferay-ui:message key='<%= \"model.resource.com.liferay.portlet.imagegallery\" %>' />");
		sb.append(image);
		sb.append("</option></select></div></form>");
		sb.append("<script language=\"JavaScript\">function addNewAsset() {var val = document.getElementById(\"selector\").selectedIndex;var url = document.getElementById(\"selector\").options[val].value;if (url != '') {location = url;}	}</script>");

		return sb.toString();
	}

}
