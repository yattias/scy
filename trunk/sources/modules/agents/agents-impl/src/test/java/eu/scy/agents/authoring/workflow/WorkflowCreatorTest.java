package eu.scy.agents.authoring.workflow;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.TestRooloServiceImpl;
import eu.scy.agents.api.AgentLifecycleException;

public class WorkflowCreatorTest extends AbstractTestFixture {

	private WorkflowCreator workflowCreator;

	@Override
    @Before
	public void setUp() throws Exception {
		super.setUp();

		TestRooloServiceImpl rooloServices = new TestRooloServiceImpl(
				repository);
		rooloServices.setMetadataTypeManager(typeManager);
		workflowCreator = new WorkflowCreator(rooloServices);
	}

	@Override
    @After
	public void tearDown() throws AgentLifecycleException {
		super.tearDown();
	}

	@Test
	public void testCreateWorkflow() throws URISyntaxException {
		Workflow workflow = workflowCreator.createWorkflow(new URI(MISSION1));
	}

}
