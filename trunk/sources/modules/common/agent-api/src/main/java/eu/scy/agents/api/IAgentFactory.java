package eu.scy.agents.api;

public interface IAgentFactory {

	public IThreadedAgent create(IParameter params);

	public String getAgentName();

}
