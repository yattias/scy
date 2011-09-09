package eu.scy.agents.groupformation;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;
import eu.scy.agents.session.SessionAgent;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** @author fschulz */
public class GroupformationAgent2Test extends AbstractTestFixture {

    private static final String REFERENCE_MAP = "roolo://memory/13/0/eco_reference_map.mapping";

    @BeforeClass
    public static void startTS() {
        startTupleSpaceServer();
    }

    @AfterClass
    public static void stopTS() {
        stopTupleSpaceServer();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
        params.put(AgentProtocol.TS_HOST, TSHOST);
        params.put(AgentProtocol.TS_PORT, TSPORT);
        this.agentMap.put(RooloAccessorAgent.NAME, params);
        this.agentMap.put(SessionAgent.NAME, params);
        this.agentMap.put(GroupFormationAgent2.NAME, params);
        this.startAgentFramework(this.agentMap);
    }

    @Override
    @After
    public void tearDown() throws AgentLifecycleException {
        this.stopAgentFrameWork();
        super.tearDown();
    }

    @Test
    public void testRun() throws TupleSpaceException, InterruptedException {
        this.login("user1", MISSION1, Mission.MISSION1.getName(), "en", "co2_2");
        this.login("user2", MISSION1, Mission.MISSION1.getName(), "en", "co2_2");
        this.login("user3", MISSION1, Mission.MISSION1.getName(), "en", "co2_2");

        Thread.sleep(200);

        this.getActionSpace().write(
                this.lasChangeTuple("user2", MISSION1, "conceptualisatsionConceptMap", "some", REFERENCE_MAP));
        Tuple response = this.getCommandSpace().waitToTake(
                new Tuple(AgentProtocol.NOTIFICATION, String.class, "user2", String.class,
                        "eu.scy.agents.groupformation.GroupFormationAgent2", String.class, String.class, String.class,
                        Field.createWildCardField()));
        assertNotNull("no response received", response);
        assertEquals("text=please wait for other users to be available", response.getField(7).getValue());

        this.getActionSpace().write(
                this.lasChangeTuple("user1", MISSION1, "conceptualisatsionConceptMap", "some", REFERENCE_MAP));
        Thread.sleep(1000);
        Tuple[] response2 = this.getAllResponses("user1");
        assertEquals("not enough responses received", 4, response2.length);
        assertEquals("text=Please consider collaboration with these students:\nuser2", response2[3].getField(7)
                .getValue());

        this.getActionSpace().write(
                this.lasChangeTuple("user3", MISSION1, "conceptualisatsionConceptMap", "some", REFERENCE_MAP));

        this.getActionSpace().write(
                this.lasChangeTuple("user3", MISSION1, "some", "conceptualisatsionConceptMap", REFERENCE_MAP));
        this.getActionSpace().write(this.logout("user2", MISSION1));
        Tuple response3 = this.getCommandSpace().waitToTake(
                new Tuple(AgentProtocol.NOTIFICATION, String.class, String.class, String.class, String.class,
                        String.class, String.class, String.class, Field.createWildCardField()),
                AgentProtocol.ALIVE_INTERVAL * 16);
        assertNotNull("no response received", response3);
        String message = (String) response3.getField(7).getValue();
        System.out.println(message);
    }

    private Tuple[] getAllResponses(String user) throws TupleSpaceException {
        return this.getCommandSpace().takeAll(
                new Tuple(AgentProtocol.NOTIFICATION, String.class, user, String.class,
                        String.class, String.class, String.class,
                        Field.createWildCardField()));
    }

    @Test
    public void testcreateUserListString() {
        GroupFormationAgent groupFormationAgent = new GroupFormationAgent(new HashMap<String, Object>());

        Group group = new Group();
        group.add("user1@scy.collide.info");
        group.add("user2@scy.collide.info");
        group.add("user3@scy.collide.info");
        String createUserListString = groupFormationAgent.createUserListString("user1@scy.collide.info", group);
        assertEquals("user2; user3", createUserListString);

        Group group2 = new Group();
        group2.add("user1@scy.collide.info");
        group2.add("user2@scy.collide.info");
        String createUserListString2 = groupFormationAgent.createUserListString("user1@scy.collide.info", group2);
        assertEquals("user2", createUserListString2);
    }

}
