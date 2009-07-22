package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import roolo.elo.api.IMetadataKey;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractProcessingAgent;

/**
 * Checks the received content for misspellings.
 * 
 * ("misspellings", <EloURI>:String(), <TS>:Long, <EloContent>:String,
 * <User>:String) -> ("misspellings":String, <URI>:String, <TS>:Long,
 * <NumberOfErrors>:Integer, <User>:String)
 * 
 * +<EloURI>: the uri of the elo that has been saved. <br />
 * +<TS>: timestamp <br />
 * +<EloContent>: the textual content of the elo. <br />
 * +<User>:the user that saved this elo. <br />
 * -<NumberOfErrors>: Number of found errors.<br />
 * 
 * @author fschulz_2
 * 
 * @param <K>
 */
public class CheckMisspellingAgent<K extends IMetadataKey> extends
		AbstractProcessingAgent<K> {

	private static final String CHECK_MISSPELLING_AGENT_NAME = "CheckMisspellingAgent";
	private SpellChecker spellChecker;
	private boolean stopped = false;

	public CheckMisspellingAgent(Map<String, Object> map) {
		super(CHECK_MISSPELLING_AGENT_NAME, (String) map.get("id"));
		InputStream inputStream = CheckMisspellingAgent.class
				.getResourceAsStream("/eng_com.dic");
		SpellDictionary dictionary;
		try {
			dictionary = new SpellDictionaryHashMap(new InputStreamReader(
					inputStream));
			spellChecker = new SpellChecker(dictionary);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkMispelling(String uri, String content, String user)
			throws TupleSpaceException {
		int numOfErrors = spellChecker.checkSpelling(new StringWordTokenizer(
				content));
		sendAliveUpdate();
		if (numOfErrors > 0) {
			getTupleSpace().write(
					new Tuple("misspellings", uri, System.currentTimeMillis(),
							numOfErrors, user));
		}
	}

	@Override
	protected void doRun() throws AgentLifecycleException {
		while (status == Status.Running) {
			try {
				sendAliveUpdate();
				Tuple trigger = getTupleSpace().waitToTake(getTemplateTuple());
				String content = (String) trigger.getField(3).getValue();
				content = content.replaceAll("<[^>]*>", "");
				String uri = (String) trigger.getField(1).getValue();
				String user = (String) trigger.getField(4).getValue();
				checkMispelling(uri, content, user);
			} catch (TupleSpaceException e) {
				stop();
			}
		}
		stopped = true;
	}

	private Tuple getTemplateTuple() {
		return new Tuple("misspellings", String.class, Long.class,
				String.class, String.class);
	}

	@Override
	protected void doStop() {
		// nothing to do
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

    @Override
    protected Tuple getIdentifyTuple() {
        // TODO Auto-generated method stub
        return null;
    }

}
