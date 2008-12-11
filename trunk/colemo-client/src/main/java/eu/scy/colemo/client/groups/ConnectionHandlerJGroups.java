package eu.scy.colemo.client.groups;

import org.jgroups.*;
import org.jgroups.util.Util;
import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.MainFrame;
import eu.scy.colemo.contributions.*;
import eu.scy.colemo.contributions.cmap.ConceptNode;
import eu.scy.colemo.server.uml.*;
import eu.scy.colemo.network.Person;
import eu.scy.colemo.network.NetworkMessage;
import eu.scy.colemo.network.LogOn;
import eu.scy.colemo.network.ObjectList;
import eu.scy.colemo.agent.AgentMessage;
import eu.scy.colemo.agent.StartVote;
import eu.scy.colemo.agent.VoteResult;
import eu.scy.colemo.agent.EndVote;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.des.2008
 * Time: 10:20:00
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandlerJGroups  extends ReceiverAdapter implements ConnectionHandler {

    protected Logger log = Logger.getLogger("ConnectionHandlerJGroups");

    JChannel channel;
    String user_name=System.getProperty("user.name", "n/a");
    final List<Object> state=new LinkedList<Object>();

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        String line=msg.getSrc() + ": " + msg.getObject();
        log.info(String.valueOf(msg.getObject()));
        System.out.println(line);



        Object obj = msg.getObject();
        processObject(obj);

        synchronized(state) {
            //state.add(line);
            state.add(obj);
        }
    }

    public byte[] getState() {
        synchronized(state) {
            try {
                return Util.objectToByteBuffer(state);
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void setState(byte[] new_state) {
        try {
            List<String> list=(List<String>)Util.objectFromByteBuffer(new_state);
            synchronized(state) {
                state.clear();
                state.addAll(list);
            }
            System.out.println("received state (" + list.size() + " messages in chat history):");
            for(String str: list) {
                System.out.println(str);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws Exception {
        channel=new JChannel();
        channel.setReceiver(this);
        channel.connect("ColemoCluster");
        channel.getState(null, 10000);
        //eventLoop();
        //channel.close();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                line="[" + user_name + "] " + line;
                sendMessage(line);
                //Message msg=new Message(null, null, line);
                //channel.send(msg);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void sendMessage(String message) {
        try {
            Message msg=new Message(null, null, message);
            channel.send(msg);
        } catch (ChannelNotConnectedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ChannelClosedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendObject(Object object) {
        try {
            Message msg = new Message(null, null, (Serializable) object);
            log.info("Sending: "+ object);
            channel.send(msg);
        } catch (ChannelNotConnectedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ChannelClosedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

        public void processObject(Object o) {
            MainFrame frame = ApplicationController.getDefaultInstance().getMainFrame();

        DefaultStyledDocument logDoc = frame.getChatPane().getLogDoc();
        if (o instanceof AddClass) {
            AddClass add = (AddClass) o;
            UmlClass umlClass = new UmlClass(add.getName(), add.getType(), add.getAuthor());
            frame.getGraphicsDiagram().getUmlDiagram().addDiagramData(umlClass);
            frame.getGraphicsDiagram().addClass(umlClass);
        }
        if (o instanceof ConceptNode) {
            log.info("CONCEPT NODE!!");
            ConceptNode add = (ConceptNode) o;
            ConceptMapNodeData conceptMapNodeData = new ConceptMapNodeData(add.getName());
            frame.getGraphicsDiagram().getUmlDiagram().addDiagramData(conceptMapNodeData);
            frame.getGraphicsDiagram().addConceptMapNodeData(conceptMapNodeData);
        }
        if (o instanceof AddLink) {
            AddLink add = (AddLink) o;
            UmlLink umlLink = new UmlLink(add.getFrom(), add.getTo(), add.getUser());
            frame.getGraphicsDiagram().getUmlDiagram().addLink(umlLink);
            frame.getGraphicsDiagram().addLink(umlLink);
        }
        if (o instanceof AssociateClass) {
            AssociateClass add = (AssociateClass) o;
            UmlAssociation umlAssociation = new UmlAssociation(add.getFrom(), add.getTo(), add.getUser());
            frame.getGraphicsDiagram().getUmlDiagram().addAssociation(umlAssociation);
            frame.getGraphicsDiagram().addAssociation(umlAssociation);
        }
        if (o instanceof AddField) {
            AddField add = (AddField) o;
            frame.getGraphicsDiagram().getUmlDiagram().addField(add.getUmlClass(), add.getField());
            frame.getGraphicsDiagram().updateClass(add.getUmlClass());
        }
        if (o instanceof AddMethod) {
            AddMethod add = (AddMethod) o;
            frame.getGraphicsDiagram().getUmlDiagram().addMethod(add.getUmlClass(), add.getMethod());
            frame.getGraphicsDiagram().updateClass(add.getUmlClass());
        }
        if (o instanceof DeleteField) {
            DeleteField delete = (DeleteField) o;
            frame.getGraphicsDiagram().getUmlDiagram().deleteField(delete.getUmlClass(), delete.getField());
            frame.getGraphicsDiagram().updateClass(delete.getUmlClass());
        }
        if (o instanceof DeleteMethod) {
            DeleteMethod delete = (DeleteMethod) o;
            frame.getGraphicsDiagram().getUmlDiagram().deleteMethod(delete.getUmlClass(), delete.getMethod());
            frame.getGraphicsDiagram().updateClass(delete.getUmlClass());
        }
        if (o instanceof MoveClass) {
            MoveClass move = (MoveClass) o;
            frame.getGraphicsDiagram().getUmlDiagram().updateClass(move.getUmlClass());
            frame.getGraphicsDiagram().updateClass(move.getUmlClass());

        }
        if (o instanceof DeleteLink) {
            DeleteLink delete = (DeleteLink) o;
            frame.getGraphicsDiagram().getUmlDiagram().deleteLink(delete.getUmlLink());
            frame.getGraphicsDiagram().deleteLink(delete.getUmlLink());
        }
        if (o instanceof DeleteAssociation) {
            DeleteAssociation delete = (DeleteAssociation) o;
            frame.getGraphicsDiagram().getUmlDiagram().deleteAssociation(delete.getUmlAssociation());
            frame.getGraphicsDiagram().deleteAssociation(delete.getUmlAssociation());
        }
        if (o instanceof DeleteClass) {
            DeleteClass delete = (DeleteClass) o;
            frame.getGraphicsDiagram().getUmlDiagram().deleteClass(delete.getUmlClass());
            frame.getGraphicsDiagram().deleteClass(delete.getUmlClass());
        }
        if (o instanceof Rename) {
            Rename rename = (Rename) o;
            if (rename.getType().equals("class")) {
                frame.getGraphicsDiagram().getUmlDiagram().renameClass(rename.getUmlClass(), rename.getNewName());
                frame.getGraphicsDiagram().renameClass(rename.getUmlClass());
            } else if (rename.getType().equals("field")) {
                frame.getGraphicsDiagram().getUmlDiagram().renameField(rename.getUmlClass(), rename.getNewName(), rename.getOldName());
                frame.getGraphicsDiagram().updateClass(rename.getUmlClass());
            } else if (rename.getType().equals("method")) {
                frame.getGraphicsDiagram().getUmlDiagram().renameMethod(rename.getUmlClass(), rename.getNewName(), rename.getOldName());
                frame.getGraphicsDiagram().updateClass(rename.getUmlClass());
            }
        }
        if (o instanceof ClassMoving) {
            ClassMoving classMoving = (ClassMoving) o;
            frame.getGraphicsDiagram().getUmlDiagram().updateClass(classMoving.getUmlClass());
            frame.getGraphicsDiagram().updateClass(classMoving.getUmlClass());
        }

        if (o instanceof Chat) {
            Chat chat = (Chat) o;
            String input = (String) chat.getInput();
            Person person = (Person) chat.getPerson();
            try {
                //frame.getChatPane().addChatText(person.getUserName() + " > " + input + newline);
            }
            catch (Exception e) {
                System.out.println("FEIL: " + e);
            }
        }


        if (o instanceof NetworkMessage) {
            NetworkMessage message = (NetworkMessage) o;
            System.out.println("NETWORK MESSAGE SENT!!!! ");
            if (message.getObject() instanceof UmlDiagram) {
                UmlDiagram umlDiagram = (UmlDiagram) message.getObject();
                frame.getGraphicsDiagram().updateUmlDiagram(umlDiagram);
            }
        }

        if (o instanceof String) {
            String string = (String) o;
            frame.agentMessage(string);
        }
        if (o instanceof LogOn) {
            LogOn logon = (LogOn) o;
            Person person = logon.getPerson();

            try {
                frame.getChatPane().addServerText("System > " + person.getUserName() + " has logged on\n");
            }
            catch (Exception e) {

            }
        }

        if (o instanceof Vector) {
            Vector v = (Vector) o;
            frame.updateUsers(v);
        }
        if (o instanceof ObjectList) {
            ObjectList list = (ObjectList) o;
            frame.updateUsers(list.getVector());
        }

        if (o instanceof AgentMessage) {
            AgentMessage message = (AgentMessage) o;
            String string = (String) message.getMessage();
            if (message.isDialogMode()) {
                frame.agentMessage(string);
            } else {
            }
        }
        if (o instanceof StartVote) {
            StartVote startVote = (StartVote) o;
            frame.voteMessage(startVote);
        }
        if (o instanceof VoteResult) {
            VoteResult result = (VoteResult) o;

/*            frame.getChatPane().addVoteText("Results for voting of deletion of: " + result.getClas() + newline);
            frame.getChatPane().addVoteText("Suggested by: " + result.getUser() + newline);
            frame.getChatPane().addVoteText("Delete: " + result.getYes() + ", Not delete: " + result.getNo() + newline);
            if (result.getYes() > result.getNo()) {
                frame.getChatPane().addVoteText("The class will be deleted.." + newline);
            } else {
                frame.getChatPane().addVoteText("The class will NOT be deleted.." + newline);
            }
            */
        }
        if (o instanceof EndVote) {
            EndVote endVote = (EndVote) o;
            if (endVote.isDelete()) {
                frame.getGraphicsDiagram().removeClass(frame.getGraphicsDiagram().getClass(endVote.getClas()));
            } else {
                //JOptionPane.showMessageDialog(frame, "The majority of the users decided not to delete" + newline +
                //        endVote.getClas() + newline + "Please consider discussing this with the others!");
            }
        }
    }
}
