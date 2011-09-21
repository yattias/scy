package eu.scy.agents.groupformation;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.Context;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.rmi.dgc.VMID;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

/** @author fschulz */
class GroupFormationNotificationHelper {

    private static final Logger LOGGER = Logger.getLogger(GroupFormationNotificationHelper.class);
    private static final String FORM_GROUP = "form_group";


    private String sender;
    private TupleSpace notificationSpace;
    private TupleSpace actionSpace;

    public GroupFormationNotificationHelper(TupleSpace notificationSpace, TupleSpace actionSpace, String sender) {
        this.sender = sender;
        this.notificationSpace = notificationSpace;
        this.actionSpace = actionSpace;
    }

    public Tuple createMessageNotificationTuple(IAction action, String notificationId, String message, String user) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(this.sender);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("text=" + message.toString());
        notificationTuple.add("title=Groupformation");
        notificationTuple.add("type=message_dialog_show");
        notificationTuple.add("modal=false");
        notificationTuple.add("dialogType=OK_DIALOG");
        return notificationTuple;
    }

    public Tuple createBuddifyNotificationTuple(IAction action, String notificationId, String user, String userToBuddify) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(sender);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("type=add_buddy");
        notificationTuple.add("user=" + userToBuddify);
        return notificationTuple;
    }


    public String createId() {
        return new VMID().toString();
    }

    public void sendStudentAddedToGroupNotification(IAction action, String newUser, Collection<Group> newGroups,
                                                    String language, String las) {

        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));

        Group newGroup = null;
        // buddify group toNewUser
        for ( Group group : newGroups ) {
            if ( !group.contains(newUser) ) {
                continue;
            } else {
                newGroup = group;
                for ( String user : group ) {
                    String messageNotificationId = createId();
                    buddifyGroup(action, group, user, messageNotificationId);

                    if ( !user.equals(newUser) ) {
                        // buddify newUser to group
                        try {
                            String id = createId();
                            // inform other users that somebody entered their group
                            StringBuilder addUserToGroupMessage = new StringBuilder();
                            addUserToGroupMessage.append(messages.getString("GF_ADD_USER_TO_GROUP"));
                            addUserToGroupMessage.append(" ");
                            addUserToGroupMessage.append(newUser);

                            Tuple messageNotificationTuple = createMessageNotificationTuple(action, id, addUserToGroupMessage.toString(),
                                    user);
                            notificationSpace.write(messageNotificationTuple);

                            waitForNotificationProcessedAction(id, "Buddify " + user + " -> " + newUser + " notification was not " +
                                    "processed");
                        } catch ( TupleSpaceException e ) {
                            LOGGER.error("Could not write into Tuplespace", e);
                        }
                    } else {
                        // set the status for every user to the lasId
                        setStatus(action, las, user);

                        // enable filtering on the lasid
                        enableFiltering(action, las, user, true);

                        // send message about proposed collaboration to user
                        sendCollaborationMessage(action, messages, group, user, messageNotificationId);
                    }

                }
            }
        }
        // send special notification to newUser
        if ( newGroup != null ) {
            String userListString = createUserListString(newUser, newGroup);
            StringBuilder addUserToGroupMessage = new StringBuilder();
            addUserToGroupMessage.append(messages.getString("GF_USER_ADDED_TO_GROUP"));
            addUserToGroupMessage.append("\n");
            addUserToGroupMessage.append(userListString);
            createMessageNotificationTuple(action, createId(), addUserToGroupMessage.toString(),
                    newUser);
        } else {
            LOGGER.error("New user " + newUser + " not added to any group");
        }
    }

    public void sendWaitForExistingGroupNotification(IAction action, String language, Group group) {
        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));
        StringBuilder message = new StringBuilder();
        message.append(messages.getString("GF_WAIT_GROUP_MESSAGE"));
        message.append("\n");
        message.append(createUserListString(action.getUser(), group));
        Tuple notificationTuple = createMessageNotificationTuple(action, createId(),
                message.toString(),
                action.getUser());
        try {
            notificationSpace.write(notificationTuple);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    public void waitForNotificationProcessedAction(String notificationId,
                                                   String message) throws TupleSpaceException {
        Tuple notificationProcessedTuple = actionSpace.waitToRead(
                new Tuple(ActionConstants.ACTION, notificationId, Long.class,
                        String.class, Field.createWildCardField()),
                AgentProtocol.MILLI_SECOND * 10);
        if ( notificationProcessedTuple == null ) {
            LOGGER.warn(message);
        }
    }

    public void sendGroupNotification(IAction action, Collection<Group> formedGroups, String language,
                                      String las) {
        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));

        for ( Group group : formedGroups ) {
            for ( String user : group ) {
                // set the status for every user to the lasId
                setStatus(action, las, user);

                // enable filtering on the lasid
                enableFiltering(action, las, user, true);

                String messageNotificationId = createId();

                // buddify whole group
                buddifyGroup(action, group, user, messageNotificationId);

                // send message about proposed collaboration to user
                sendCollaborationMessage(action, messages, group, user, messageNotificationId);
            }
        }
    }

    private void sendCollaborationMessage(IAction action, ResourceBundle messages, Group group, String user,
                                          String messageNotificationId) {
        StringBuilder message = new StringBuilder();
        message.append(messages.getString("GF_COLLABORATE"));
        message.append("\n");

        String userListString = createUserListString(user, new Group(group));
        message.append(userListString);

        Tuple messageNotificationTuple = createMessageNotificationTuple(action, messageNotificationId,
                message.toString(), user);
        logGroupFormation(action, userListString, user);
        try {
            notificationSpace.write(messageNotificationTuple);
            waitForNotificationProcessedAction(messageNotificationId, "Message about group notification was not processed");
        } catch ( TupleSpaceException e ) {
            LOGGER.error("Could not write into Tuplespace", e);
        }
    }

    private void buddifyGroup(IAction action, Group group, String user, String messageNotificationId) {
        for ( String userToBuddify : group ) {
            if ( !user.equals(userToBuddify) ) {
                String buddifyNotificationId = messageNotificationId;
                Tuple buddifyNotification = createBuddifyNotificationTuple(action, buddifyNotificationId,
                        user, userToBuddify);

                try {
                    notificationSpace.write(buddifyNotification);
                    waitForNotificationProcessedAction(buddifyNotificationId, "Buddify " + user + " -> " + userToBuddify + " " +
                            "notification was not processed");
                } catch ( TupleSpaceException e ) {
                    LOGGER.error("Could not write into Tuplespace", e);
                }
            }
        }
    }

    public void enableFiltering(IAction action, String las, String user, boolean filterEnabled) {
        String notificationId = createId();
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(sender);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("type=filter_users");
        notificationTuple.add("filter=" + Boolean.toString(filterEnabled));
        notificationTuple.add("groupd-id=" + las);
        try {
            notificationSpace.write(notificationTuple);
        } catch ( TupleSpaceException e ) {
            LOGGER.error("Could not write into Tuplespace", e);
        }

    }

    public void setStatus(IAction action, String las, String user) {
        String notificationId = createId();
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(sender);
        notificationTuple.add(action.getContext(ContextConstants.mission));
        notificationTuple.add(action.getContext(ContextConstants.session));
        notificationTuple.add("type=set_status");
        notificationTuple.add("status=" + las);
        try {
            notificationSpace.write(notificationTuple);
            waitForNotificationProcessedAction(notificationId, "group all buddies for " + user + " notification " + "was not " +
                    "processed");
        } catch ( TupleSpaceException e ) {
            LOGGER.error("Could not write into Tuplespace", e);
        }
    }

    public void sendWaitNotification(IAction action, String language) {
        ResourceBundle messages = ResourceBundle.getBundle("agent_messages", new Locale(language));
        Tuple notificationTuple = createMessageNotificationTuple(action, createId(),
                messages.getString("GF_WAIT_MESSAGE"), action.getUser());
        try {
            notificationSpace.write(notificationTuple);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    private void logGroupFormation(IAction action, String userListString, String user) {
        Action groupFormationAction = new Action();
        groupFormationAction.setContext(new Context(sender, action
                .getContext(ContextConstants.mission), action
                .getContext(ContextConstants.session), action
                .getContext(ContextConstants.eloURI)));
        groupFormationAction.setId(createId());
        groupFormationAction.setTimeInMillis(System.currentTimeMillis());
        groupFormationAction.setUser(action.getUser());
        groupFormationAction.setType(FORM_GROUP);
        groupFormationAction.addAttribute("user", user);
        groupFormationAction.addAttribute("group", userListString);
        Tuple actionAsTuple = ActionTupleTransformer
                .getActionAsTuple(groupFormationAction);
        try {
            actionSpace.write(actionAsTuple);
        } catch ( TupleSpaceException e ) {
            LOGGER.error("Could not write action into Tuplespace", e);
        }
    }

    private String createUserListString(String userToNotify, Group group) {
        StringBuilder message = new StringBuilder();
        int i = 0;
        for ( String user : group ) {
            if ( user.equals(userToNotify) ) {
                i++;
                continue;
            }
            message.append(sanitizeName(user));
            if ( i != group.size() - 1 ) {
                message.append("; ");
            }
            i++;
        }
        return message.toString();
    }


    private String sanitizeName(String user) {
        int indexOf = user.indexOf("@");
        if ( indexOf != -1 ) {
            return user.substring(0, indexOf);
        }
        return user;
    }
}
