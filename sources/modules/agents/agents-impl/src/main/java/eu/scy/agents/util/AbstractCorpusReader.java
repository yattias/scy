  package eu.scy.agents.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Corpus;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.CorpusView;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;
import de.fhg.iais.kd.tm.obwious.type.Container;

/**
 * Abstract class to ease the programming of new Corpusreader.
 * 
 * @author Florian Schulz
 * 
 */
public abstract class AbstractCorpusReader extends OperatorSpecification {

	private static final long serialVersionUID = 1L;

	/**
	 * corpus to store the documents in. Needed by subclasses.
	 */
	protected Corpus corpus;

	/**
	 * Create a new AbstractCorpusReader.
	 */
	public AbstractCorpusReader() {
		this(null);
	}

	/**
	 * 
	 * @param properties
	 *            Properties for initializing stuff.
	 */
	public AbstractCorpusReader(Properties properties) {
		super(properties);
		this.addParameterType(TMParameters.INPUT, JavaClasses.STRING);
		this.addParameterType(TMParameters.RANDOM, JavaClasses.BOOLEAN, false, false);
		this.addParameterType(TMParameters.MAX_DOCUMENTS, JavaClasses.INTEGER, false, -1);
        this.addParameterType(TMParameters.START_DOCUMENT_NO, JavaClasses.INTEGER, false, -1);
        this.addParameterType(TMParameters.END_DOCUMENT_NO, JavaClasses.INTEGER, false, -1);
		this.addOutputType(ObjectIdentifiers.CORPUSVIEW, CorpusView.class);
	}

	@Override
	public Container run(Container inputParameters) {
		Container result = new Container(getOutputSignature());

		logger.info("Started reading corpus");

		String inputPath = (String)inputParameters.getObject(TMParameters.INPUT);
		Boolean random = (Boolean)inputParameters.getObject(TMParameters.RANDOM);
		int maxDocuments = (Integer)(inputParameters.getObject(TMParameters.MAX_DOCUMENTS));
		int startDoc = (Integer)inputParameters.getObject(TMParameters.START_DOCUMENT_NO);
        int endDoc = (Integer)inputParameters.getObject(TMParameters.END_DOCUMENT_NO);
		this.corpus = createCorpus();

		try {
			readCorpus(inputPath, inputParameters);
		} catch (IOException e) {
			e.printStackTrace();
		}

		long start = System.nanoTime();
		if (maxDocuments > corpus.size()) {
			random = false;
			maxDocuments = corpus.size();
		}
		if (maxDocuments == -1) {
			random = false;
			maxDocuments = corpus.size();
		}
		
		if(startDoc < 0){
		  startDoc = 0;
		}
		if(startDoc >= corpus.size()){
		  startDoc= corpus.size()-1;
		}
		if(endDoc >= corpus.size() | endDoc < 0){
		  endDoc = corpus.size()-1;
		}
		if(maxDocuments > (1 + endDoc - startDoc)){
		  maxDocuments = endDoc - startDoc;
		}
		if(maxDocuments <= 0){
		  logger.info("empty corpus");
		}
		if (!random) {
			Frame view = new Frame(this.corpus);
			Iterator<de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document> corpusIter = corpus.iterator();
			if(startDoc > 0){
			// iterate up to startDoc:
			  for (int i = 0; i < startDoc; i++) {
			    corpusIter.next();
			  }
			}
			for (int i = 0; i < maxDocuments; i++) {
				if (corpusIter.hasNext()) {
					view.addDocument(corpusIter.next().getId());
				}
			}
			result.setObject(ObjectIdentifiers.CORPUSVIEW, view);
			logger.info("create CorpusView containing " + view.size() + " documents.");
		} else {
			Frame view = new Frame(this.corpus);
			int corpusSize = corpus.size();
			Random rand = new Random(0);
			int[] randomDocuments = new int[maxDocuments];
			boolean[] notAlreadyRolled = new boolean[corpusSize];
			for (int i = 0; i < maxDocuments; i++) {
				randomDocuments[i] = rand.nextInt(corpusSize - 1);
				while (notAlreadyRolled[randomDocuments[i]]) {
					randomDocuments[i] = rand.nextInt(corpusSize - 1);
				}
				notAlreadyRolled[randomDocuments[i]] = true;
			}
			Arrays.sort(randomDocuments);
			Iterator<de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document> corpusIter = corpus.iterator();
			int i = -1;
			de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document document = null;
			for (int doc : randomDocuments) {
				for (; i < doc; i++) {
					if (corpusIter.hasNext()) {
						document = corpusIter.next();
					}
				}
				view.addDocument(document.getId());
			}
			result.setObject(ObjectIdentifiers.CORPUSVIEW, view);
			logger.info("create CorpusView containing " + view.size() + " documents.");
		}

		logger.info("CorpusView created in " + (System.nanoTime() - start));

		return result;
	}

	/**
	 * create a corpus. Either a MemoryCorpus or a CachedCorpus.
	 * 
	 * @return A new empty corpus.
	 */
	protected abstract Corpus createCorpus();

	/**
	 * Really read the corpus. Needs to be overridden due to special formats.
	 * 
	 * @param inputPath
	 *            the path to the corpus
	 * @param parameters TODO
	 * @throws IOException
	 *             If corpus couldn't be read.
	 */
	protected abstract void readCorpus(String inputPath, Container parameters) throws IOException;

	/**
	 * Collect the corpora files recursively from a directory.
	 * 
	 * @param directory
	 *            The directory to search for files.
	 * @param suffix
	 *            Suffix of files.
	 * @return A list of files containing a corpus.
	 */
	protected File[] collectCorpora(File directory, final String suffix) {
		ArrayList<File> allDPAFiles = new ArrayList<File>();

		File[] files = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory() || pathname.getName().endsWith(suffix)) {
					return true;
				}
				return false;
			}
		});
		for (File file : files) {
			if (file.isDirectory()) {
				File[] subFiles = collectCorpora(file, suffix);
				allDPAFiles.addAll(Arrays.asList(subFiles));
			} else {
				allDPAFiles.add(file);
			}
		}
		return allDPAFiles.toArray(new File[0]);
	}

	/**
	 * convenience method @see
	 * {@link AbstractCorpusReader#collectCorpora(File, String)}. Calls
	 * collectCorpora(directory,".xml").
	 * 
	 * @param directory The directory to search for
	 * @return All xml files in tis directory.
	 */
	protected File[] collectCorpora(File directory) {
		return collectCorpora(directory, ".xml");
	}


  protected String formatId(int index) {
    String integer = "";
    if (index < 10) {
      integer = "000" + index;
    } else if (index < 100) {
      integer = "00" + index;
    } else if (index < 1000) {
      integer = "0" + index;
    }
    return integer;
  }

}