
package roolo.renumber


import java.io.File;
import java.util.Collections;

/**
 *
 * @author sikken
 */
class EloStoreFileDAO implements EloStoreDAO {

	private def uriPattern = ~/<lom>[\S\s]*?<general>[\S\s]*?<identifier>[\s]*?<catalog>memory<\/catalog>[\s]*<entry>(.+)<\/entry>/
	private def String eloFileExtension = ".xml"

	EloStore readEloStore(File dir){
		def dirPath = dir.&getAbsolutePath()
//		println("readEloStore(${dir.getAbsolutePath()})")
		if (!dir.exists()){
			throw new IllegalArgumentException("store directory $dir.absolutePath does not exists")
		}
		if (!dir.isDirectory()){
			throw new IllegalArgumentException("store directory $dir.absolutePath is not a directory")
		}
		def eloStore = new EloStore();
		def eloDirectories = dir.listFiles().findAll { !it.isHidden()}
		for (eloDir in eloDirectories){
			//			println("eloDir: $eloDir")
			eloStore.versionLists.add(readVersionList(eloDir))
		}
		Collections.sort(eloStore.versionLists)
		eloStore
	}

	private VersionList readVersionList(File eloDir){
		int versionId = Integer.parseInt(eloDir.getName())
		def versionList = new VersionList()
		versionList.id = versionId
		def eloFiles = eloDir.listFiles().findAll { !it.isHidden() && it.isFile()}
		for (eloFile in eloFiles){
			versionList.elos.add(readElo(eloFile))
		}
		return versionList
	}

	private Elo readElo(File file){
		def elo = new Elo();
		String fileName = file.name
		if (fileName.endsWith(eloFileExtension)){
			fileName = fileName.substring( 0, fileName.length()-eloFileExtension.length())
		}
		elo.id = Integer.parseInt(fileName)
		elo.xml = file.getText()
		def uriMatcher = elo.xml =~ uriPattern
		if (uriMatcher.find()){
			elo.uri = new URI(uriMatcher.group(1))
		}
		else{
			println("failed to find uri")
		}
		return elo
	}

	void writeEloStore(EloStore eloStore, File dir){
		if (!dir.exists()){
			throw new IllegalArgumentException("store directory $dir.absolutePath does not exists")
		}
		if (!dir.isDirectory()){
			throw new IllegalArgumentException("store directory $dir.absolutePath is not a directory")
		}
		if (dir.listFiles().length>0){
			throw new IllegalArgumentException("store directory $dir.absolutePath is not empty")
		}
//		println("storing eloStore at $dir")
		for (versionList in eloStore.versionLists){
			File versionDir = new File(dir,"$versionList.id")
			if (versionDir.mkdir()){
				for (elo in versionList.elos){
					File eloFile = new File(versionDir,"$elo.id$eloFileExtension")
					eloFile.setText elo.xml
				}
			}
		}
	}
}

