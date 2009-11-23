package eu.scy.agents.api;

/**
 * Request agents can be triggered by the client or other agents and just answer
 * a request. In opposition to other agents they are never triggered
 * automatically e.g. when an ELO is saved.
 * 
 * @author Florian Schulz
 * 
 */
public interface IRequestAgent extends IThreadedAgent {

}
