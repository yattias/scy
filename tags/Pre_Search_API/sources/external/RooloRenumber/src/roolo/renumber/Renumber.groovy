package roolo.renumber


class Renumber {
	File inEloStoreDir
	File outEloStoreDir
	int startNumber
	List<File> otherFiles

	private FileChecker fileChecker = new FileChecker()

	void checkFiles(){
		fileChecker.checkForExistingDirectory inEloStoreDir, false, true, "inEloStore"
		fileChecker.checkForExistingDirectory outEloStoreDir, true, false, "outEloStore"
		otherFiles.each { fileChecker.checkForExistingFile(it, "other")}
	}

	EloStore renumber(){
		def eloStoreDAO = new EloStoreFileDAO();
		def eloStore = eloStoreDAO.readEloStore(inEloStoreDir)
		println("EloStore read from $inEloStoreDir.absolutePath,\nWith $eloStore.numberOfElos elos in $eloStore.versionLists.size version lists")
		def renumberer = new Renumberer();
		renumberer.renumber(eloStore,startNumber, otherFiles)
		eloStoreDAO.writeEloStore eloStore, outEloStoreDir
		println("Renumbered eloStore written to $outEloStoreDir.absolutePath")
		eloStore
	}
}
