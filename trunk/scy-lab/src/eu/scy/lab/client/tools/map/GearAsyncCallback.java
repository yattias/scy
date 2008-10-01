package eu.scy.lab.client.tools.map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Callback to be used to first do the normal callback to get/save data via rpc 
 * and afterwards save data locally via gears 
 */
public abstract class GearAsyncCallback<T> implements AsyncCallback<T> {

	private AsyncCallback<T> callback;

	public GearAsyncCallback(AsyncCallback<T> callback) {
		this.callback = callback;
	}

	public void onFailure(Throwable caught) {
		callback.onFailure(caught);
	}

	public void onSuccess(T result) {
		saveData(result);
		callback.onSuccess(result);
	}

	protected abstract void saveData(T result);

}
