package eu.scy.colemo.client;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import eu.scy.colemo.client.actions.BaseAction;
import eu.scy.colemo.client.actions.SCYMapperAction;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2009
 * Time: 06:44:51
 * To change this template use File | Settings | File Templates.
 */
public class SCYMapperToolbar extends JToolBar implements SelectionControllerListener{

	//XXX: REMOVE
	private TupleSpace ts;
	
    public SCYMapperToolbar() {
        setPreferredSize(new Dimension(200,40));
        List commonActions = ActionController.getDefaultInstance().getCommonActions();
        addCommonActions(commonActions);
        
        try {
			ts = new TupleSpace("scy.collide.info", 2525, "command");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
        
        fillOntologySearchMenu();
    }

	private void fillOntologySearchMenu() {
		final JTextField textField = new JTextField(6);
        add(textField);
        JButton lookup = new JButton("Find related");
        lookup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						String text = textField.getText();
						final List<String> relatedTopics = lookupRelated(text);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								if(!relatedTopics.isEmpty()) {
									String object = (String) JOptionPane.showInputDialog(ApplicationController.getDefaultInstance().getColemoPanel(), "Please choose a concept", "Related concepts",
										JOptionPane.QUESTION_MESSAGE, null, relatedTopics.toArray(), null);
									ApplicationController.getDefaultInstance().getColemoPanel().addNewConcept(ApplicationController.getDefaultInstance().getGraphicsDiagram().getUmlDiagram(), "c", object);
								} else {
									JOptionPane.showMessageDialog(ApplicationController.getDefaultInstance().getColemoPanel(), "No related concepts found.", "No concepts found", JOptionPane.WARNING_MESSAGE);
								}
							};
						});
					}	
				}.start();
			}
        	
        });
        add(lookup);
	}
	
	// Don't try this at home
	
	public List<String> lookupRelated(String lookupTerm) {
        List<String> result = new ArrayList<String>();
		String ontology = "house-ontology";
        
		try {
			String id = new UID().toString();
			ts.write(new Tuple(id, "onto", "lookup", ontology, lookupTerm));
			Tuple response = ts.waitToTake(new Tuple(id, String.class));
			String category = response.getField(1).getValue().toString();
			if(category.equals("notfound")) {
				return result;
			}
			
			if (category.equals("class")) {
				id = new UID().toString();
				ts.write(new Tuple(id, "onto", "class info", ontology, lookupTerm));
				response = ts.waitToTake(new Tuple(id, Field.createWildCardField()));
				String instances = response.getField(1).getValue().toString();
				String superclasses = response.getField(2).getValue().toString();
				String subclasses = response.getField(3).getValue().toString();
				result.addAll(Arrays.asList(instances.split(",")));
				result.addAll(Arrays.asList(superclasses.split(",")));
				result.addAll(Arrays.asList(subclasses.split(",")));
			} else if (category.equals("individual")) {
				id = new UID().toString();
				ts.write(new Tuple(id, "onto", "instance info", ontology, lookupTerm));
				response = ts.waitToTake(new Tuple(id, Field.createWildCardField()));
				String classes = response.getField(1).getValue().toString();
				result.addAll(Arrays.asList(classes.split(",")));
			}

			id = new UID().toString();
			ts.write(new Tuple(id, "onto", "siblings", ontology, lookupTerm));
			response = ts.waitToTake(new Tuple(id, Field.createWildCardField()));
			String siblings = response.getField(1).getValue().toString();
			result.addAll(Arrays.asList(siblings.split(",")));

			//Uber-hack
			while(result.remove("")) {};
			
			Collections.sort(result);

			return result;
		} catch (TupleSpaceException e) {
			e.printStackTrace();
			return new ArrayList<String>(0);
		}
    } 
	
	// the end

    public void selectionPerformed(Object selected) {
        removeAll();
        List commonActions = ActionController.getDefaultInstance().getCommonActions();
        List baseActions = ActionController.getDefaultInstance().getActions(selected);


        addCommonActions(commonActions);

        for (int i = 0; i < baseActions.size(); i++) {
            BaseAction baseAction = (BaseAction) baseActions.get(i);
            JButton button = new JButton(baseAction);
	        button.setIcon((Icon)baseAction.getValue(AbstractAction.SMALL_ICON));
            button.revalidate();
            add(button);
        }

        fillOntologySearchMenu();
        
        invalidate();
        validate();
        repaint();

    }

    private void addCommonActions(List commonActions) {
        for (int i = 0; i < commonActions.size(); i++) {
            SCYMapperAction scyMapperAction = (SCYMapperAction) commonActions.get(i);
            JButton button = new JButton(scyMapperAction);
	        button.setIcon((Icon) scyMapperAction.getValue(AbstractAction.SMALL_ICON));
            button.revalidate();
            add(button);
        }
    }


}
