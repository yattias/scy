package info.collide;

import java.util.Comparator;
import java.util.Date;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

public class IEloDateComparator implements Comparator<IELO> {

	IMetadataTypeManager mtm;

	public IEloDateComparator(IMetadataTypeManager mtm) {
		this.mtm = mtm;
	}

	@Override
	public int compare(IELO elo0, IELO elo1) {

		IMetadata metadataA = elo0.getMetadata();
		IMetadata metadataB = elo1.getMetadata();

		Object objectDateModiA = (Object) metadataA.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED)).getValue();
		Object objectDateModiB = (Object) metadataB.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED)).getValue();

		Object objectDateA = (Object) metadataA.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED)).getValue();
		Object objectDateB = (Object) metadataB.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED)).getValue();

		Date dateA = null;
		Date dateB = null;
		
		Long longDateModiA = null;
		Long longDateModiB = null;		
		Long longDateA = null;
		Long longDateB = null;
		

		if (objectDateModiA != null) {
			if(objectDateModiA.getClass().getName().equals(Long.class.getName())){
				longDateModiA = (Long) objectDateModiA;
				dateA = new Date(longDateModiA);				
			}else if(objectDateModiA.getClass().getName().equals(Date.class.getName())){
				dateA = (Date) objectDateModiA;
			}else {
				dateA = new Date(0);
			}
		} else if (objectDateModiA == null && objectDateA != null) {
			if(objectDateA.getClass().getName().equals(Long.class.getName())){
				longDateA = (Long) objectDateA;
				dateA = new Date(longDateA);				
			}else if(objectDateA.getClass().getName().equals(Date.class.getName())){
				dateA = (Date) objectDateA;
			}else {
				dateA = new Date(0);
			}
		} else {
			dateA = new Date(0);
		}
		
		if (objectDateModiB != null) {
			if(objectDateModiB.getClass().getName().equals(Long.class.getName())){
				longDateModiB = (Long) objectDateModiB;
				dateB = new Date(longDateModiB);				
			}else if(objectDateModiB.getClass().getName().equals(Date.class.getName())){
				dateB = (Date) objectDateModiB;
			}else {
				dateB = new Date(0);
			}
		} else if (objectDateModiB == null && objectDateB != null) {
			if(objectDateB.getClass().getName().equals(Long.class.getName())){
				longDateB = (Long) objectDateB;
				dateB = new Date(longDateB);				
			}else if(objectDateB.getClass().getName().equals(Date.class.getName())){
				dateB = (Date) objectDateB;
			}else {
				dateB = new Date(0);
			}
		} else {
			dateB = new Date(0);
		}		
		
		if(dateA == null){
			dateA = new Date(0);
		}
		
		if(dateB == null){
			dateB = new Date(0);
		}
		
		return dateB.compareTo(dateA);
	}
}
