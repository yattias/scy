package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.List;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 17:30:53
 */
public class BuddyListModel extends AbstractListModel implements IAwarenessPresenceListener {
	private IAwarenessService service;
	private List<IAwarenessUser> buddies;
	private final static Logger logger = Logger.getLogger(BuddyListModel.class);

	public BuddyListModel(List<IAwarenessUser> buddies) {
		this.buddies = buddies;
	}

	private void setBuddies(List<IAwarenessUser> buddies) {
		this.buddies = buddies;
	}

	@Override
	public int getSize() {
		return buddies.size();
	}

	@Override
	public Object getElementAt(int index) {
		return buddies.get(index);
	}

	@Override
	public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
		try {
			setBuddies(service.getBuddies());
		} catch (AwarenessServiceException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
