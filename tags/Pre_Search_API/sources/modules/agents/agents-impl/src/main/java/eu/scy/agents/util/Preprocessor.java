package eu.scy.agents.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

public class Preprocessor {

	public Instance createInstanceFromTokens(String id, String[] documents) {
		List<Token> malletTokens = new ArrayList<Token>();
		for (String token : documents) {
			malletTokens.add(new Token(token));
		}
		Instance instance = new Instance(new TokenSequence(malletTokens), "",
				id, id);
		return instance;
	}

	public String removePunctuation(String document) {
		return document.replaceAll("[,;\\.:\\?\\!\\)\\(\"“„”\\[\\]\\*-]", " ");
	}

	public String[] tokenize(String document) {
		List<String> tokenizedDocument = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(document);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			tokenizedDocument.add(token);
		}
		return tokenizedDocument.toArray(new String[0]);
	}

	public String[] removeStopwords(String[] tokens, String language) {
		Set<String> stopWords = readStopWords(language);
		List<String> result = new ArrayList<String>();
		for (String token : tokens) {
			if (!stopWords.contains(token)) {
				result.add(token);
			}
		}
		return result.toArray(new String[0]);
	}

	private Set<String> readStopWords(String language) {
		Set<String> stopwords = new HashSet<String>();
		InputStream inStream = getClass().getResourceAsStream(
				"/" + language + "_stopwords.txt");
		Scanner scanner = new Scanner(inStream).useDelimiter("\n");

		while (scanner.hasNext()) {
			stopwords.add(scanner.next().trim());
		}
		return stopwords;
	}

	public String toLowerCase(String string) {
		return string.toLowerCase();
	}

}
