/*
 * Created on 01.07.2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Corpus;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.MemoryCorpus;
import de.fhg.iais.kd.tm.obwious.type.Container;

/**
 * @author joerg
 * 
 *         reads all .txt files in the inputPath and generates one document per
 *         file
 */
public class OneDocPerFileReader extends AbstractCorpusReader {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.fhg.iais.kd.tm.centime.operators.corpusreader.AbstractCorpusReader
	 * #createCorpus()
	 */
	@Override
	protected Corpus createCorpus() {
		return new MemoryCorpus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.fhg.iais.kd.tm.centime.operators.corpusreader.AbstractCorpusReader
	 * #readCorpus(java.lang. String)
	 */
	@Override
	protected void readCorpus(String inputPath, Container parameters) throws IOException {
		File[] txtFiles;
		File inputFile = new File(inputPath);
		if (inputFile.isDirectory()) {
			txtFiles = collectCorpora(inputFile, ".txt");
		} else {
			txtFiles = new File[1];
			txtFiles[0] = inputFile;
		}

		for (File txtFile : txtFiles) {
			Document doc = readFile(txtFile);
			corpus.addDocument(doc);
		}
	}

	private Document readFile(File txtFile) throws IOException {
		// reads text from file and creates one document
		BufferedReader reader = new BufferedReader(new FileReader(txtFile));
		String text = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.matches("\\s")) {
				continue;
			}
			text = text + " " + line;
		}
		reader.close();
		Document doc = new Document(txtFile.getName());
		doc.setFeature(Features.TEXT, text);
		return doc;
	}

}
