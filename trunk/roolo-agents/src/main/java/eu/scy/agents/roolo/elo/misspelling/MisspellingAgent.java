package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.File;
import java.net.URL;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import eu.scy.agents.impl.AbstractProcessingAgent;

public class MisspellingAgent extends AbstractProcessingAgent {

	public MisspellingAgent() {
		super();
	}

	@Override
	protected void doRun() {
		Tuple t;
		try {
			t = this.getTupleSpace().waitToTake(
					new Tuple("misspellings", String.class, Long.class, String.class));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}

		String content = (String) t.getField(3).getValue();
		content = content.replaceAll("<[^>]*>", "");

		URL url = MisspellingAgent.class.getResource("/de.dic");// eng_com.dic
		File dictFile;
		try {
			dictFile = new File(url.toURI());
			SpellDictionary dictionary = new SpellDictionaryHashMap(dictFile);
			SpellChecker spellChecker = new SpellChecker(dictionary);
			int numOfErrors = spellChecker.checkSpelling(new StringWordTokenizer(content));
			if (numOfErrors > 0) {
				this.getTupleSpace().write(
						new Tuple("misspellings", t.getField(1).getValue(), System
								.currentTimeMillis(), numOfErrors));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
