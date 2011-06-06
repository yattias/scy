package eu.scy.agents.api.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.api.parameter.AgentConfiguration;

public class AgentParameterTest {

	private static final String MISSION1 = "mission1";
	private static final String TEST_VALUE2 = "TestValue2";
	private static final String TEST_PARAMETER = "TestParameter";
	private static final String TEST_PARAMETER2 = "TestParameter2";
	private static final String USER1 = "user1";
	private AgentConfiguration parameter;

	@Before
	public void setupParameters() {
		parameter = new AgentConfiguration();
		parameter.setParameter(TEST_PARAMETER, 1);
		parameter.setParameter(TEST_PARAMETER2, TEST_VALUE2);
	}

	@Test
	public void getAgentDefaultParameters() {
		assertEquals(1, parameter.getParameter(TEST_PARAMETER));
		assertEquals(TEST_VALUE2, parameter.getParameter(TEST_PARAMETER2));
	}

	@Test
	public void missionSpecificParametersOverwriteDefaultParameters() {
		parameter.setParameter(MISSION1, TEST_PARAMETER, 2);
		parameter.setParameter(MISSION1, TEST_PARAMETER2, "Test");
		assertEquals(2, parameter.getParameter(MISSION1, TEST_PARAMETER));
		assertEquals("Test", parameter.getParameter(MISSION1, TEST_PARAMETER2));
	}

	@Test
	public void missingMissionSpecificParametersReturnDefaultParameters() {
		parameter.setParameter(MISSION1, TEST_PARAMETER, 2);
		assertEquals(2, parameter.getParameter(MISSION1, TEST_PARAMETER));
		assertEquals(TEST_VALUE2, parameter.getParameter(MISSION1,
				TEST_PARAMETER2));
	}

	@Test
	public void userSpecificParametersOverwriteMissionParameters() {
		parameter.setParameter(MISSION1, USER1, TEST_PARAMETER, 3);
		parameter.setParameter(MISSION1, USER1, TEST_PARAMETER2, "TestUser");
		assertEquals(3, parameter.getParameter(MISSION1, USER1, TEST_PARAMETER));
		assertEquals("TestUser", parameter.getParameter(MISSION1, USER1,
				TEST_PARAMETER2));
	}

	@Test
	public void missingUserSpecificParametersReturnMissionParameters() {
		parameter.setParameter(MISSION1, USER1, TEST_PARAMETER, 34);
		assertEquals(34, parameter
				.getParameter(MISSION1, USER1, TEST_PARAMETER));
		assertEquals(TEST_VALUE2, parameter.getParameter(MISSION1, USER1,
				TEST_PARAMETER2));
	}

	@Test
	public void missingUserAndMissionSpecificParametersReturnDefaultParameters() {
		assertEquals(1, parameter.getParameter(MISSION1, USER1, TEST_PARAMETER));
		assertEquals(TEST_VALUE2, parameter.getParameter(MISSION1, USER1,
				TEST_PARAMETER2));
	}

	@Test
	public void useAgentParameterObject() {
		AgentParameter agentParameter = new AgentParameter();
		agentParameter.setMission(MISSION1);
		agentParameter.setUser(USER1);
		agentParameter.setParameterName(TEST_PARAMETER);
		agentParameter.setParameterValue(4);

		parameter.setParameter(agentParameter);
		assertEquals(4, parameter.getParameter(agentParameter));
	}

	@Test
	public void useAgentParameterObjectNoUser() {
		AgentParameter agentParameter = new AgentParameter();
		agentParameter.setMission(MISSION1);
		agentParameter.setParameterName(TEST_PARAMETER);
		agentParameter.setParameterValue(6);

		parameter.setParameter(agentParameter);
		assertEquals(6, parameter.getParameter(agentParameter));
	}

	@Test
	public void useAgentParameterObjectNoUserAndMission() {
		AgentParameter agentParameter = new AgentParameter();
		agentParameter.setParameterName(TEST_PARAMETER);
		agentParameter.setParameterValue(5);

		parameter.setParameter(agentParameter);
		assertEquals(5, parameter.getParameter(agentParameter));
	}

}
