package eu.scy.client.common.datasync;

public interface CollaboratorStatusListener {

    public void joined(String name);

    public void left(String name);

    public void wentOnline(String name);

    public void wentOffline(String name);
}
