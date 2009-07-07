package eu.scy.agents.api;

public interface IThreadedAgent extends IAgent, Runnable {

	public void start() throws AgentLifecycleException;

	public void stop() throws AgentLifecycleException;

	public boolean isRunning();

	public boolean isStopped();

}
