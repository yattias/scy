package roolo.renumber


class FileChecker {
	void checkForExistingDirectory(File dir, boolean empty, boolean notEmpty, String label){
		if (!dir.exists()){
			throw new IllegalArgumentException("$label directory $dir.absolutePath does not exists")
		}
		if (!dir.isDirectory()){
			throw new IllegalArgumentException("$label directory $dir.absolutePath is not a directory")
		}
		def files = dir.listFiles().findAll { !it.isHidden()}
		if (empty){
			if (files.size()>0){
				throw new IllegalArgumentException("$label directory $dir.absolutePath is not empty")
			}
		}
		if (notEmpty){
			if (files.size()==0){
				throw new IllegalArgumentException("$label directory $dir.absolutePath is empty")
			}
		}
	}

	void checkForExistingFile(File file, String label){
		if (!file.exists()){
			throw new IllegalArgumentException("$label file $file.absolutePath does not exists")
		}
		if (!file.isFile()){
			throw new IllegalArgumentException("$label file $file.absolutePath is not a file")
		}
		String content = file.getText()
	}
}
