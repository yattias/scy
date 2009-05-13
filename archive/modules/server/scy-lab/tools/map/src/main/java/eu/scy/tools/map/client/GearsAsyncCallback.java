package eu.scy.tools.map.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Callback to be used to first save the data locally using gears and
 * afterwards continue with the normal callback
 */
public abstract class GearsAsyncCallback<T> implements AsyncCallback<T> {

    private AsyncCallback<T> callback;

    public GearsAsyncCallback(AsyncCallback<T> callback) {
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
