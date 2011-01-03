package roolo.renumber


import java.io.File;

class Renumberer
{
	void renumber(EloStore eloStore, int startNumber,List<File> otherFiles){
		def Map<Integer,Integer> renumberMap = createRenumberMap(eloStore, startNumber)
		def Map<String,String> uriReplacementMap = createUriReplaceMap(eloStore,renumberMap)
		doRenumberEloStore(eloStore,renumberMap,uriReplacementMap)
		renumberOtherFiles otherFiles, uriReplacementMap
	}
	
	private Map<Integer,Integer> createRenumberMap(EloStore eloStore, def startNumber){
		def Map<Integer,Integer> renumberMap = new HashMap<Integer,Integer>();
		int newNumber = startNumber
		for (versionList in eloStore.versionLists){
			renumberMap.put versionList.id, newNumber
			++newNumber
		}
		return renumberMap;
	}
	
	private Map<String,String> createUriReplaceMap(EloStore eloStore, Map<Integer,Integer> renumberMap){
		def Map<String,String> uriReplacementMap = new HashMap<String,String>()
		for (versionList in eloStore.versionLists){
			def newNumber = renumberMap.get(versionList.id)
			for (elo in versionList.elos){
				def currentUriString = elo.uri.toString()
				def newUriString = currentUriString.replaceFirst( "roolo://memory/$versionList.id/", "roolo://memory/$newNumber/")
//				println("change $currentUriString to $newUriString")
				uriReplacementMap.put( currentUriString, newUriString)
			}
			renumberMap.put versionList.id, newNumber
			++newNumber
		}

		return uriReplacementMap
	}
	
	private void doRenumberEloStore(EloStore eloStore, Map<Integer,Integer> renumberMap, Map<String,String> uriReplacementMap)
	{
		for (versionList in eloStore.versionLists){
			def newNumber = renumberMap.get(versionList.id)
			for (elo in versionList.elos){
				elo.xml = renumberEloLinks(elo.xml,uriReplacementMap)
				elo.uri = new URI(uriReplacementMap.get(elo.uri.toString()))
			}
			versionList.id = renumberMap.get( versionList.id)
		}
	}
	
	private String renumberEloLinks(String xml, Map<String,String> uriReplacementMap){
		String renumberedXml = xml;
		uriReplacementMap.each { key, value -> renumberedXml = renumberedXml.replace(key,value) }
		return renumberedXml
	}
	
	private void renumberOtherFiles(List<File> otherFiles, Map<String,String> uriReplacementMap){
		otherFiles.each{ File otherFile ->
				String xml = otherFile.getText()
				String renumberedXml = renumberEloLinks(xml, uriReplacementMap)
				File originalFile = otherFile
				String backupName = "${originalFile.name}.bak"
				File backupFile = new File(otherFile.parentFile,backupName)
				if (backupFile.exists()){
					if (!backupFile.delete()){
						println("failed to delete old backup file: $backupFile")
					}
				}
				if (!originalFile.renameTo(backupFile)){
					println("failed to rename other file to $backupName")
				}
				otherFile.setText(renumberedXml)
				println("Renumbered file: $otherFile.absolutePath")
			}
	}
}
