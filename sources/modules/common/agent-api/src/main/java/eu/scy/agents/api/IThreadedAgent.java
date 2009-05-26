package eu.scy.agents.api;

public interface IThreadedAgent extends Runnable, IAgent {

	public void start();

	public void stop();

	public void suspend();

	public void resume();

	public boolean isSuspended();

	public boolean isRunning();

}
