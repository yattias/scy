package eu.scy.agents.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Corpus;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.MemoryCorpus;
import de.fhg.iais.kd.tm.obwious.type.Container;

/**
 * Reads a corpus from a file where each document is one line.
 * 
 * @author Florian Schulz
 * 
 */
public class OneDocPerLineReader extends AbstractCorpusReader {

	private static final long serialVersionUID = 1L;
	private int docIndex = 0;

	@Override
	protected Corpus createCorpus() {
		return new MemoryCorpus();
	}

	@Override
	protected void readCorpus(String inputPath, Container parameters) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(inputPath));
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#")) {
				continue;
			}
			String id = "";
			if (docIndex < 10) {
				id = "000" + docIndex;
			} else if (docIndex < 100) {
				id = "00" + docIndex;
			} else if (docIndex < 1000) {
				id = "0" + docIndex;
			}

			Document doc = new Document(id);
			doc.setFeature(Features.TEXT, line);
			docIndex++;
			corpus.addDocument(doc);
		}
	}
}
