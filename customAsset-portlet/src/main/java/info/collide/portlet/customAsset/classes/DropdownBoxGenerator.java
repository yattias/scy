package info.collide.portlet.customAsset.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.tags.model.TagsAssetType;
import com.liferay.portlet.tags.service.TagsAssetServiceUtil;
import com.liferay.portlet.PortletRequestImpl;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.RenderRequestImpl;

public class DropdownBoxGenerator {
	
	private String addNew = "Add new";
	private String document = "Document";
	private String dropbox = "";
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
			addNew = "Neues hinzufügen";
			document = "Dokument";
			image = "Bild";
		}
				
		dropbox = "<form name=\"formName\" action=\"\"><div><select id=\"selector\" name=\"customAssetURL\" onchange=\"addNewAsset();\"><option value=\"\"><liferay-ui:message key=\"add-new\" />" + addNew + "...</option><option value=\""
			+ server
			+ community
			+ "?p_p_id=20&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_20_struts_action=%2Fdocument_library" 
			+ "%2Fedit_file_entry&_20_folderId=0&_20_uploader=classic&_20_redirect=%2Fweb%2Fguest%2F" 
			+ community
			+ "&_20_backURL=%2Fweb%2Fguest%2F"
			+ community
			+ "&_20_tagsEntries=\"><liferay-ui:message key='<%= \"model.resource.com.liferay.portlet.documentlibrary.model.DLFileEntry\" %>' />" + document + "</option><option value=\""
			+ server
			+ community
			+ "?p_p_id=31&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_31_struts_action=%2Fimage_gallery%2Fedit_image&_31_folderId=0&_31_uploader=classic&_31_redirect=%2Fweb%2Fguest%2F"
			+ community
			+ "&_31_backURL=%2Fweb%2Fguest%2F" 
			+ community
			+ "&_31_tagsEntries=\"><liferay-ui:message key='<%= \"model.resource.com.liferay.portlet.imagegallery\" %>' />" + image + "</option></select></div></form>"
			+ "<script language=\"JavaScript\">function addNewAsset() {var val = document.getElementById(\"selector\").selectedIndex;var url = document.getElementById(\"selector\").options[val].value;if (url != '') {location = url;}	}</script>";

		return dropbox;
	}

}
