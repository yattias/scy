/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package roolo.renumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sikken
 */
public class Main
{

	/**
	 * @param args
	 *           the command line arguments
	 */
	public static void main(String[] args)
	{
		System.out.println("Welcome to roolo renumber (written in Groovy!, calling from Java)");
		if (args.length >= 3)
		{
			final Renumber renumber = new Renumber();
			renumber.setInEloStoreDir(new File(args[0]));
			renumber.setOutEloStoreDir(new File(args[1]));
			renumber.setStartNumber(Integer.parseInt(args[2]));
			final List<File> otherFiles = new ArrayList<File>();
			for (int i = 3; i < args.length; i++)
			{
				otherFiles.add(new File(args[i]));
			}
			renumber.setOtherFiles(otherFiles);
			try
			{
				renumber.checkFiles();
				EloStore eloStore = renumber.renumber();
				dumpEloStore(eloStore);
			}
			catch (IllegalArgumentException e)
			{
				System.out.print("\nOptions: " + renumber.getInEloStoreDir() + " " + renumber.getOutEloStoreDir() + " "
							+ renumber.getStartNumber());
				for (File otherFile : renumber.getOtherFiles())
				{
					System.out.print(" " + otherFile);
				}
				System.out.println("\nError: " + e.getMessage());
			}
		}
		else
		{
			System.out.println("Needed options: inEloStoreDir outEloStoreDir start number [other xml files]");
		}
	}

	@SuppressWarnings("unused")
	private static void dumpEloStore(EloStore eloStore)
	{
		for (VersionList versionList : eloStore.getVersionLists())
		{
			System.out.println("VersionList: " + versionList.getId());
			for (Elo elo : versionList.getElos())
			{
				System.out.println(" elo: id=" + elo.getId() + ", uri=" + elo.getUri());
			}
		}
	}

}
