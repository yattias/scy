package eu.scy.agents.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Corpus;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.CorpusView;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.util.Assert;

/**
 * A frame is a special corpus that sorts its documents by id.
 * 
 * @author Florian Schulz
 * 
 */
public class Frame extends CorpusView implements Iterator<String> {

	private static final long serialVersionUID = 1L;

	private int currentIndex;

	/**
	 * Create new frame with lastcorpus as backing corpus.
	 */
	public Frame() {
		super();
		currentIndex = 0;
	}

	/**
	 * Create new frame with <code>c</code> as backing corpus.
	 * @param c The backing corpus
	 */
	public Frame(Corpus c) {
		super(c);
		currentIndex = 0;
	}

	// public Frame(List<String> subList, Corpus c) {
	// myCorpus = c;
	// currentIndex = 0;
	// }

	@Override
	public Iterator<String> iterator() {
		return documentIds.iterator();
	}

	@Override
	public boolean hasNext() {
		return (currentIndex) < documentIds.size();
	}

	@Override
	public String next() {
		if (hasNext()) {
			String docId = documentIds.get(currentIndex);
			currentIndex++;
			return docId;
		}
		return null;
	}

	@Override
	public void remove() {
		System.err.println("operation not allowed");
	}

	@Override
	public String toString() {
		return documentIds.toString();
	}

	@Override
	public void addDocument(String documentId, double weight) {
		Assert.isNotNull(documentId);
		Assert.isTrue(this.myCorpus.hasDocument(documentId));

		int insertionPoint = Collections.binarySearch(documentIds, documentId);
		if (insertionPoint < 0) {
			this.documentIds.add(Math.abs(insertionPoint) - 1, documentId);
			this.weights.add(Math.abs(insertionPoint) - 1, weight);
		} else {
			this.documentIds.add(insertionPoint + 1, documentId);
			this.weights.add(insertionPoint + 1, weight);
		}
	}

	@Override
	public Document getDocument(int index) {
		return myCorpus.getDocument(documentIds.get(index));
	}

	@Override
	public String getDocumentId(int index) {
		return documentIds.get(index);
	}

	@Override
	public void clear() {
		// not allowed
		throw new RuntimeException("Removing in frames not allowed");
	}

	@Override
	public boolean contains(Object o) {
		return documentIds.contains(o);
	}

	@Override
	public Document getDocument(String documentId) {
		if (documentIds.contains(documentId)) {
			return myCorpus.getDocument(documentId);
		}
		return null;
	}

	@Override
	public List<Integer> getIndexes(String documentId) {
		// not allowed
		throw new RuntimeException("Never needed");
	}

	@Override
	public int getNumDocuments() {
		return documentIds.size();
	}

	@Override
	public boolean hasDocument(String documentId) {
		return documentIds.contains(documentId);
	}

	@Override
	public boolean isEmpty() {
		return documentIds.isEmpty();
	}

	@Override
	public void removeDocument(String documentId) {
		// not allowed
		throw new RuntimeException("Removing in frames not allowed");
	}

	@Override
	public int size() {
		return documentIds.size();
	}

	@Override
	public Object[] toArray() {
		return documentIds.toArray();
	}

	@Override
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return super.toArray(a);
	}

}
