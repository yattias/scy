package info.collide.portlet.customAsset.classes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;

import info.collide.portlet.customAsset.classes.DataStorage;

/**
 * @author berezowski
 *
 * This class is for getting all search results to a given query.
 */
public class queryDatabase {
	
	public enum TYPE{
		DOCUMENT, IMAGE;  
	} 
	
	// User Informations
	private String userId = "";
	private List<Long> userGroups = new ArrayList<Long>();
	
	// Portal Informations
	private String companyId = "";
	private String root = "";
	private String pathMain = "";
	private String plid = "";
	private String imagePath = "";
	
	// Community the Portlet is used
	private String community = "";
	private long communityId = 0;
	private String communityName = "";
	private boolean getCommunity = true;
	private int countCommunityItems = 0;
	
	// String to determine the choosen Language.
	private String local = "";
	
	// Language specific Strings for Html Output.
	private String description = "Description";
	private String type = "Type";	
	private String modifiedDate = "Modification Date";
	private String image = "image";
	private String resultsFound = "Results";
	private String sortBy = "Sort by ";
	private String of = "of";
	private String edit = "edit";
	private String resultSize = "Result size:&nbsp;";
	private String all = "All";
	private String from = "From:";
	
	// The final Result of the query.
	private String finalResult = "";
	
	// Strings for Paging
	String pagingScript = "<div id='customAssetPagingPosition'></div> <script type='text/javascript'>var customAssetPager = new CustomAssetPager('resultsOfCustomAsset', 5 , '', SPACER); customAssetPager.init(); customAssetPager.showPageNav('customAssetPager', 'customAssetPagingPosition', 'customAssetUpperPagingPosition'); customAssetPager.showPage(1);customAssetPager.changeCommunity();</script>";
	String upperPaging = "<div id='customAssetUpperPagingPosition'></div>";
	
	// String for Sorting
	String sortingScript = "<script type='text/javascript'>var sort = new Sorting('resultsOfCustomAsset'); sort.init();</script>";
	
	
	// Specifying the format for the Modified-Date.
	SimpleDateFormat simpleDate = new SimpleDateFormat( "dd-MM-yyyy" );
	
	public queryDatabase(){
		userId = "";
		companyId = "";
		root = "";
		pathMain = "";
		plid = "";
		imagePath = "";
		finalResult = "";
	}	
			
	/**
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	/**
	 * @param companyId
	 */
	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	/**
	 * @param root
	 */
	public void setRoot(String root){
		this.root = root;
	}
	
	/**
	 * @param pathMain
	 */
	public void setPathMain(String pathMain){
		this.pathMain = pathMain;
	}
	
	/**
	 * @param plid
	 */
	public void setPlid(String plid){
		this.plid = plid;
	}
	
	/**
	 * @param imagePath
	 */
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}	
	
	/**
	 * @param community
	 */
	public void SetCommunity(String community){
		this.community = community;
	}
	
	/**
	 * @param local
	 */
	public void setLanguage(String local){
		this.local = local;
	}	
	
	/**
	 * @return
	 */
	public String getAllResults(){
		
		// Choose the fitting Language.
		if(local.toLowerCase().contains("de")){
			description = "Beschreibung";
			type = "Typ";	
			modifiedDate = "Änderungsdatum";
			image = "bild";
			resultsFound = "Ergebnisse";
			sortBy = "Sortieren nach ";
			of = "von";
			edit = "bearbeiten";
			resultSize = "Ergebnisse:&nbsp;";
			pagingScript = "<div id='customAssetPagingPosition'></div> <script type='text/javascript'>var customAssetPager = new CustomAssetPager('resultsOfCustomAsset', 5 , 'de'); customAssetPager.init(); customAssetPager.showPageNav('pager', 'customAssetPagingPosition', 'customAssetUpperPagingPosition'); customAssetPager.showPage(1); customAssetPager.changeCommunity();</script>";
			all = "Allen";
			from = "Von:";
		}
		
		// First get all Groups the User is member of.
		getUserGroups();
		
		List<DataStorage> results = new ArrayList<DataStorage>();
		
		// Get all Documents 
		results.addAll(getFiles());
		
		
		// Get all Images
		results.addAll(getImages());
		
		Collections.sort(results);
		
		if(results.size()>0){
			
			finalResult = finalResult
				+"<table width='100%'>"
				+"<tr>"
				+"<th width='68%'>"
				+"<span id='customAssetActualPosition' >" + resultsFound + ": 1 - 1 " + of + " " + results.size() + ".</span>"
				+"</th>"
				+"<td>"
				+resultSize
				+"</td>"
				+"<td>"
				+ "<select id='setPagingSizeForCustomAsset' onchange='customAssetPager.changeSize()'>"
				+ "<option value='5'>5</option>"
				+ "<option value='10'>10</option>"
				+ "<option value='15'>15</option>"
				+ "<option value='20'>20</option>"
				+ "</select>"
				+"</td>"
				+"</tr>"			
				+"<tr>"
				+"<th>"
				+"</th>"
				+"<td>"
				+ from
				+"</td>"
				+"<td>"
				+ "<select id='setCommunityForCustomAsset' onchange='customAssetPager.changeCommunity()'>"
				+ "<option value='all'>"
				+ all
				+ "</option>"
				+ "<option value='community' selected>" 
				+ communityName
				+ "</option>"
				+ "</select>"
				+"</td>"
				+"</tr>"
				+"</table>";
			
			finalResult = finalResult + "<br>" + upperPaging;

			finalResult = finalResult + "<table border='0' cellpadding='10em' id='resultsOfCustomAsset' class='customassettable'>"; 
			
			finalResult = finalResult + "<colgroup><col width='30%'><col width='40%'><col width='10%'><col width='20%'></colgroup>";

			finalResult = finalResult + "<tr><th onclick='sort.sortTable(0)'><a href='#' title='" + sortBy + 
				"Name'>Name</a></th><th onclick='sort.sortTable(1)'><a href='#' title='" + sortBy + description +"'>" + description + 
				"</a></th><th onclick='sort.sortTable(2)'><a href='#' title='" + sortBy + type +"'>" + type + 
				"</a></th><th onclick='sort.sortTable(3)'><a href='#' title='" + sortBy + modifiedDate +"'>" + modifiedDate + 
				"</a></th></tr>";

			for(int i =0; i<results.size(); i++){
				
				String isFromComm = "";
				if(results.get(i).getIsFromCommunity()){
					isFromComm = "id='isFromCommunity'";
					countCommunityItems = countCommunityItems + 1;
				}
								
				if(results.get(i).getType()==TYPE.DOCUMENT){
					
					String temp = "<tr "+isFromComm+" >";				
					
					// The Name and Link to the Document.
					temp = 
						temp + 
						"<td><a href="
						+'"'+
						root+
						pathMain+
						"/document_library/get_file?p_l_id="+
						plid+
						"&folderId="+
						results.get(i).getFolderId()+
						"&name="+
						results.get(i).getName()
						+'"'+
						"title="+'"'+
						results.get(i).getTitle()+
						'"'+
						"alt="+'"'+
						results.get(i).getTitle()+
						'"'+
						">"+
						results.get(i).getTitle()+
						"</a></td>";
					
					// The Description.
					temp = temp + "<td>" + results.get(i).getDescription() +"</td>";
					
					// If an Icon exists add it for Type.
					if("doc,dot,ods,odt,pdf,ppt,txt,vsd,xls".contains(results.get(i).getMimeTyp())){
						temp = temp + 
							"<td>"+ 
							"<img src="+ 
							'"'+
							"/scy-theme/images/document_library/"+results.get(i).getMimeTyp()+".png"+
							'"'+
							" title="+'"'+
							results.get(i).getMimeTyp()+
							'"'+
							"alt="+'"'+
							results.get(i).getMimeTyp()+
							'"'+
							" /></td>";
					}
					else{
						temp = temp + "<td>" + results.get(i).getMimeTyp() + "</td>";
					}					
					
					// Date of Modification and Icon with link to Modification-Site.
					temp = temp + "<td>" + simpleDate.format( results.get(i).getModifiedDate() )
						+ "&nbsp"
						+ "<a href='" 
						+ root
						+ "/web/guest/content?p_p_id=20&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_pos=1&p_p_col_count=2&_20_struts_action=%2Fdocument_library%2Fedit_file_entry&_20_folderId="
						+ results.get(i).getFolderId()
						+ "&_20_name="
						+ results.get(i).getName()
						+ "&_20_redirect=%2Fweb%2Fguest%2Fcontent"
						+ "' >"
						+ "<img style='background-image: url(&quot;/scy-theme/images/common/.sprite.png&quot;); background-position: 50% -593px; background-repeat: no-repeat; height: 16px; width: 16px;' title='"
						+ edit 
						+ "' alt='"
						+ edit 
						+ "' src='/scy-theme/images/spacer.png' class='icon'> </a>"
						+ "</td>";				
					
					temp = temp +"</tr>";
					
					finalResult = finalResult + temp;
				}
				else{
					if(results.get(i).getType()==TYPE.IMAGE){
						
						String temp = "<tr "+isFromComm+" >";		
						
						// Link to original sized Picture.
						temp = temp + "<td><a href="+'"'+
							imagePath+
							"/image_gallery?img_id="+
							results.get(i).getLargeImageId()+
							"&t="+
							results.get(i).getLargeImageId()+
							'"'+" target="+'"'+"_blank"+'"'+
							"title="+'"'+
							results.get(i).getTitle()+
							'"'+
							"alt="+'"'+
							results.get(i).getTitle()+
							'"'+
							">"+results.get(i).getTitle()+"</a></td>";
						
						// Small Preview Picture
						temp = 
							temp + "<td>" +
							"<img src="+ 
							'"'+imagePath+
							"/image_gallery?img_id="+
							results.get(i).getSmallImageId()+
							"&t="+
							results.get(i).getSmallImageId()+
							'"'+" " +
							"title='" + results.get(i).getDescription() + "'" +
							"alt='" + results.get(i).getDescription() + "'"
							+
							"/></td>";
						
						temp = temp + 
							"<td>"+
							"<img src="+ 
							'"'+
							"/scy-theme/images/portlet/portlet_css.png"+
							'"'+
							" title="+'"'+
							image+
							'"'+
							"alt="+'"'+
							image+
							'"'+
							" /></td>";
						
						// Date of Modification and Icon with link to Modification-Site.
						temp = temp + "<td>" + simpleDate.format( results.get(i).getModifiedDate() )
							+ "&nbsp"
							+ "<a href='" 
							+ root
							+ "/web/guest/content?p_p_id=31&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_pos=1&p_p_col_count=2&_31_struts_action=%2Fimage_gallery%2Fedit_image&_31_folderId="
							+ results.get(i).getFolderId()
							+ "&_31_imageId="
							+ results.get(i).getImageId()
							+ "&_31_redirect=%2Fweb%2Fguest%2Fcontent"
							+ "' ><img style='background-image: url(&quot;/scy-theme/images/common/.sprite.png&quot;); background-position: 50% -593px; background-repeat: no-repeat; height: 16px; width: 16px;' title='"
							+ edit
							+ "' alt='"
							+ edit
							+ "' src='/scy-theme/images/spacer.png' class='icon'></a>"
							+ "</td>";	

						temp = temp +"</tr>";
						
						finalResult = finalResult + temp;
					}	
				}
			}
			pagingScript = pagingScript.replace("SPACER", " "+countCommunityItems+" ");

			finalResult = finalResult + "</table>" + pagingScript + sortingScript;
		}
		return finalResult;
	}

	/**
	 * @return
	 */
	private void getUserGroups(){
		
		// Get all Groups.
		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<String, Object>();
		groupParams.put("active", Boolean.TRUE);
		
		try{
			List<Group> allGroups = GroupLocalServiceUtil.search(Long.valueOf(companyId).longValue(), null, null, groupParams, 0, 20);
			Collections.sort(allGroups, new Comparator<Group>(){
				public int compare(Group g1, Group g2){
					return g1.getDescriptiveName().compareTo(g2.getDescriptiveName());
				}
			});
	
			// Get all Groups for the User.
			for(int j = 0; j < allGroups.size(); j++){
				if(GroupLocalServiceUtil.hasUserGroup(Long.valueOf(userId).longValue(), allGroups.get(j).getGroupId())){
					

					userGroups.add(allGroups.get(j).getGroupId());
					
					if(getCommunity){
						if(community.contains(allGroups.get(j).getFriendlyURL())){
							communityName = allGroups.get(j).getName();
							if(communityName.equalsIgnoreCase("Guest")){
								communityName = "SCYCOM";
							}
							communityId = allGroups.get(j).getGroupId();
							getCommunity = false;
						}
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("An error occured in 'getUserGroups': "+e);
		}
	}

	/**
	 * @return
	 */
	private List<DataStorage> getFiles(){
		// Return Value.
		List<DataStorage> fileResults = new ArrayList<DataStorage>();	
		
		// auxiliary Variables.
		int docsize = 0;
		List<DLFileEntry> allDocuments = new ArrayList<DLFileEntry>();
		
		// Get all Documents for all Groups the user is a member of.
		for(int i =0; i<userGroups.size(); i++){
			
			try {
				docsize = DLFileEntryLocalServiceUtil.getGroupFileEntriesCount(userGroups.get(i));
			} catch (SystemException e) {
				System.out.println("An error occured in 'Get all Documents'-> getGroupFileEntriesCount: "+e);
			}
			
			try {
				allDocuments.addAll(DLFileEntryLocalServiceUtil.getGroupFileEntries(userGroups.get(i),0,docsize));
			} catch (SystemException e) {
				System.out.println("An error occured in 'Get all Documents'-> getGroupFileEntries: "+e);
			}
		}
		
		// Add allDocuments to fileResults.
		for(int i =0; i<allDocuments.size(); i++){
			DataStorage d = new DataStorage();
			d.setTitle(allDocuments.get(i).getTitle());
			d.setType(TYPE.DOCUMENT);
			d.setMimeType(allDocuments.get(i).getName().substring(allDocuments.get(i).getName().length()-3,allDocuments.get(i).getName().length()));
			d.setDescription(allDocuments.get(i).getDescription());
			d.setName(allDocuments.get(i).getName());
			d.setFolderId(String.valueOf(allDocuments.get(i).getFolderId()));
			d.setModifiedDate(allDocuments.get(i).getModifiedDate());
			
			if(allDocuments.get(i).getGroupId() == communityId){
				d.setIsFromCommunity(true);
			}
			
			fileResults.add(d);
		}
		
		return fileResults;
	}
	
	/**
	 * @return
	 */
	private List<DataStorage> getImages(){
		List<DataStorage> imageResults = new ArrayList<DataStorage>();		
		
		// auxiliary Variables.
		int imagesize = 0;
		List<IGImage> allImages = new ArrayList<IGImage>();
		
		// Get all Images for all Groups the user is a member of.
		for(int i =0; i<userGroups.size(); i++){
			try {
				imagesize = IGImageLocalServiceUtil.getGroupImagesCount(userGroups.get(i));
			} catch (SystemException e) {
				System.out.println("An error occured in 'Get all Images'-> getGroupImagesCount: "+e);
			}
			
			try {
				allImages.addAll(IGImageLocalServiceUtil.getGroupImages(userGroups.get(i),0,imagesize));
			} catch (SystemException e) {
				System.out.println("An error occured in 'Get all Documents'-> getGroupImages: "+e);
			}
		}
	
		// Add allImages to imageResults.
		for(int i =0; i<allImages.size(); i++){
			DataStorage d = new DataStorage();
			d.setType(TYPE.IMAGE);
			d.setTitle(allImages.get(i).getName());// For Sorting.
			d.setDescription(allImages.get(i).getDescription());
			d.setName(allImages.get(i).getName());
			d.setSmallImageId(String.valueOf(allImages.get(i).getSmallImageId()));
			d.setLargeImageId(String.valueOf(allImages.get(i).getLargeImageId()));
			d.setModifiedDate(allImages.get(i).getModifiedDate());
			d.setFolderId(String.valueOf(allImages.get(i).getFolderId()));
			d.setImageId(String.valueOf(allImages.get(i).getImageId()));
			
			if(allImages.get(i).getGroupId() == communityId){
				d.setIsFromCommunity(true);
			}
		
			imageResults.add(d);
		}					
		return imageResults;
	}
}
