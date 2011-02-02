/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package roolo.renumber


/**
 *
 * @author sikken
 */
class MainG {

	static void main(String[] args){
		println("Welcome to roolo renumber (written in Groovy!)")
		if (args.length>=3){
			def renumber = new Renumber();
			renumber.inEloStoreDir = new File(args[0])
			renumber.outEloStoreDir = new File(args[1])
			renumber.startNumber = Integer.parseInt(args[2])
			List<File> otherFiles = [];
			for (int i=3;i<args.length;i++){
				otherFiles << new File(args[i])
			}
			renumber.otherFiles = otherFiles
			try{
				renumber.checkFiles()
				renumber.renumber()
			}
			catch (IllegalArgumentException e){
				println("\nOptions: $renumber.inEloStoreDir $renumber.outEloStoreDir $renumber.startNumber ${renumber.otherFiles.join(' ')}\nError: $e.message")
			}
		}
		else{
			println("needed options: inEloStoreDir outEloStoreDir start number")
		}
	}
}

