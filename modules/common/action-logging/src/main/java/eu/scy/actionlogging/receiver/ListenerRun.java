package eu.scy.actionlogging.receiver;

public class ListenerRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			ActionReceiver ar = new ActionReceiver();
			SQLSpacesActionListener al = new SQLSpacesActionListener(ar);
	}

}
