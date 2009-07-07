package eu.scy.agents.roolo.elo.conceptawareness;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class TestHelper {

	@Before
	public void setUp() {
		// do nothing
	}

	@Test
	public void keepMavenQuiet() {

	}

	public static void deleteDirectory(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				file.delete();
			}
		}
		dir.delete();
	}

}
