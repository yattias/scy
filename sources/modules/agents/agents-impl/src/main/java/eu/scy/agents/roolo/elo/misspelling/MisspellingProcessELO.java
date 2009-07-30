package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.impl.elo.AbstractELOAgent;

/**
 * Notifies the agent that checks for misspellings that something checkworthy
 * has been saved to the roolo.
 * 
 * ELO saved -> ("misspellings", <EloURI>:String(), <TS>:Long,
 * <EloContent>:String, <User>:String)
 * 
 * +<EloURI>: the uri of the elo that has been saved. <br />
 * +<TS>: timestamp <br />
 * +<EloContent>: the textual content of the elo. <br />
 * +<User>:the user that saved this elo. <br />
 * 
 * @author Florian Schulz
 * 
 */
public class MisspellingProcessELO extends AbstractELOAgent {

	/**
	 * Create a new MisspellingProcessELO filtering agent. The argument
	 * <code>map</code> is used to initialize special parameters. Never used
	 * here.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	protected MisspellingProcessELO(Map<String, Object> map) {
		super("MisspellingProcessELO", (String) map.get("id"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processElo(IELO elo) {
		// System.err
		// .println("*********************** Misspelling: Processing elo************************");
		if (elo == null) {
			return;
		}

		IMetadata metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer(metadataTypeManager
							.getMetadataKey("type"));
			if (!"scy/text".equals(type.getValue())) {
				return;
			}
		}

		IMetadataKey authorKey = metadataTypeManager.getMetadataKey("author");
		IMetadataValueContainer authorContainer = metadata
				.getMetadataValueContainer(authorKey);
		Contribute author = (Contribute) authorContainer.getValue();
		String user = author.getVCard();

		try {
			IContent content = elo.getContent();
			if (content != null && content.getXmlString() != null) {
				TupleSpace ts = getTupleSpace();
				ts.write(new Tuple("misspellings", elo.getUri().toString(),
						System.currentTimeMillis(), content.getXmlString(),
						user));
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
