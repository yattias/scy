package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Callback.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import roolo.elo.api.IMetadataKey;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import eu.scy.agents.impl.AbstractProcessingAgent;

public class CheckMisspellingAgent<K extends IMetadataKey> extends
		AbstractProcessingAgent<K> {

	private SpellChecker spellChecker;

	public CheckMisspellingAgent() {
		super("CheckMisspellingAgent");
		// First parse dictinary
		InputStream inputStream = CheckMisspellingAgent.class
				.getResourceAsStream("/eng_com.dic");// eng_com.dic
		SpellDictionary dictionary;
		try {
			dictionary = new SpellDictionaryHashMap(new InputStreamReader(
					inputStream));
			spellChecker = new SpellChecker(dictionary);

			// Register for notifications
			getTupleSpace().eventRegister(
					Command.WRITE,
					new Tuple("misspellings", String.class, Long.class,
							String.class, String.class), new Callback() {

						@Override
						public void call(Command command, int seq, Tuple after,
								Tuple before) {
							System.out.println(after);
							String content = (String) after.getField(3)
									.getValue();
							content = content.replaceAll("<[^>]*>", "");
							String uri = (String) after.getField(1).getValue();
							String user = (String) after.getField(4).getValue();
							checkMispelling(uri, content, user);
						}
					}, true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() {
		// FIXME
		done = true;
	}

	private void checkMispelling(String uri, String content, String user) {
		int numOfErrors = spellChecker.checkSpelling(new StringWordTokenizer(
				content));
		if (numOfErrors > 0) {
			try {
				getTupleSpace().write(
						new Tuple("misspellings", uri, System
								.currentTimeMillis(), numOfErrors, user));
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

}
