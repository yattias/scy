package info.collide.portlet.customAsset.classes;

import java.util.Date;

import info.collide.portlet.customAsset.classes.queryDatabase.TYPE;

/**
 * @author berezowski
 *
 * This class is for storing the Informations of an Result and sorting all Results.
 */
public class DataStorage  implements Comparable<DataStorage>{
	private String title = "";
	private TYPE type;
	private String mimeTyp = "";
	private String description = "";
	private String name = "";
	private String folderId = ""; 
	private String smallImageId = "";
	private String largeImageId = "";
	private Date modifiedDate = null;
	private String imageId = "";
	//TODO
	private boolean isFromCommunity = false;
	//TODO
	
	public DataStorage(){
		title = "";
		description = "";
		name = "";
		folderId = "";
		smallImageId = "";
		largeImageId = "";
	}
	
	/**
	 * @param title
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * @param type
	 */
	public void setType(TYPE type){
		this.type = type;
	}
	
	/**
	 * @param mimeTyp
	 */
	public void setMimeType(String mimeTyp){
		this.mimeTyp = mimeTyp.toLowerCase();
	}
	
	/**
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}	
	
	/**
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}	
	
	/**
	 * @param folderId
	 */
	public void setFolderId(String folderId){
		this.folderId = folderId;
	}			
	
	/**
	 * @param smallImageId
	 */
	public void setSmallImageId(String smallImageId){
		this.smallImageId = smallImageId;
	}		
	
	/**
	 * @param bigImageId
	 */
	public void setLargeImageId(String bigImageId){
		this.largeImageId = bigImageId;
	}		
	
	/**
	 * @param modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate){
		this.modifiedDate = modifiedDate;
	}		
	
	/**
	 * @param imageId
	 */
	public void setImageId (String imageId){
		this.imageId = imageId;
	}
	
	/**
	 * @param isFromCommunity
	 */
	public void setIsFromCommunity(boolean isFromCommunity){
		this.isFromCommunity = isFromCommunity;
	}
	
	/**
	 * @return
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * @return
	 */
	public TYPE getType(){
		return this.type;
	}
	
	/**
	 * @return
	 */
	public String getMimeTyp(){
		return this.mimeTyp;
	}
	
	/**
	 * @return
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * @return
	 */
	public String getFolderId(){
		return this.folderId;
	}

	/**
	 * @return
	 */
	public String getLargeImageId(){
		return this.largeImageId;
	}
	
	/**
	 * @return
	 */
	public String getSmallImageId(){
		return this.smallImageId;
	}
	
	/**
	 * @return
	 */
	public String getImageId(){
		return this.imageId;
	}
	
	/**
	 * @return
	 */
	public Date getModifiedDate(){
		return this.modifiedDate;
	}
	
	/**
	 * @return
	 */
	public boolean getIsFromCommunity(){
		return this.isFromCommunity;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	//@Override
	public int compareTo(DataStorage objectOther) {
		
		// An example:
		// http://www.java-blog-buch.de/d-objekte-sortieren-comparator-und-comparable/

		return this.getTitle().toLowerCase().compareTo(objectOther.getTitle().toLowerCase());			
	}
}
