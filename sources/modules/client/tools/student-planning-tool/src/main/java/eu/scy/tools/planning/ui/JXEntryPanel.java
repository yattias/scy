package eu.scy.tools.planning.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;

import eu.scy.tools.planning.controller.StudentPlanningController;
import eu.scy.tools.planning.ui.images.Images;

public class JXEntryPanel extends JXPanel {

	private static final String DELETE_BUTTON = "DELETE_BUTTON";
	private JXButton deleteButton;
	private JXTaskPane taskpane;
	private JXLabel dateLabel;
	private StudentPlanningController studentPlanningController;
	
	Font dataFont = new Font("Segoe UI", Font.PLAIN, 9);
	
	public JXEntryPanel(StudentPlanningController studentPlanningController) {
		super();
		this.studentPlanningController = studentPlanningController;
	}

	/**
	 * @param isDoubleBuffered
	 */
	public JXEntryPanel(boolean isDoubleBuffered,StudentPlanningController studentPlanningController) {
		super(isDoubleBuffered);
		this.studentPlanningController = studentPlanningController;
	}

	/**
	 * @param layout
	 */
	public JXEntryPanel(LayoutManager layout,StudentPlanningController studentPlanningController) {
		super(layout);
		this.studentPlanningController = studentPlanningController;
	}

	public void addEntry(JComponent taskpane, String param) {
		if (param != null) {
			this.taskpane = (JXTaskPane) taskpane;
			this.createDateLabel();
			this.add(taskpane, param);
			this.createDeleteButton();
			this.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					JXEntryPanel bp = (JXEntryPanel) e.getSource();

					if( bp.getTaskpane().isCollapsed() ) {
						bp.setBackgroundOff();
					JXButton db = (JXButton) bp
							.getClientProperty(DELETE_BUTTON);
					db.setEnabled(false);
					} else {
						bp.setBackgroundOn();
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					JXEntryPanel bp = (JXEntryPanel) e.getSource();

					bp.setBackgroundOn();

					JXButton db = (JXButton) bp
							.getClientProperty(DELETE_BUTTON);
					db.setEnabled(true);

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});

		}
	}

	private void createDateLabel() {
		
		
		JXLabel jxLabel = new JXLabel("");
		
		setDateLabel(jxLabel);
		getDateLabel().setFont(dataFont);
		
		//getDateLabel().setForeground(Colors.Yellow.color());
		getDateLabel().setHorizontalAlignment(JXLabel.CENTER);
		this.add(getDateLabel(),"top");
		this.getDateLabel().setPreferredSize(new Dimension(90, this.getDateLabel().getPreferredSize().height));
		
	}

	private void createDeleteButton() {
		deleteButton = new JXButton(Images.TinyDelete.getIcon());
		deleteButton.setOpaque(false);
		deleteButton.setBorder(BorderFactory.createEmptyBorder());
		deleteButton.setEnabled(false);
		this.putClientProperty(DELETE_BUTTON, deleteButton);
		this.add(deleteButton, "wrap,top");

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JXButton b = (JXButton) e.getSource();
				Object[] options = {"Yes, please",
	                    "No, thanks"};
				int n = JOptionPane
						.showOptionDialog(null,
								"Are you sure you want to delete this entry?", "What do you want to do?",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);
				
				if( n == 0) {
					
					JXEntryPanel p = (JXEntryPanel) b.getParent();
					p.removeMe();
					
				}
			}
		});
		// deleteButton.setPaintBorderInsets(false);
		deleteButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				JXButton jb = (JXButton) e.getSource();
				jb.setEnabled(false);
				JXEntryPanel parent = (JXEntryPanel) jb.getParent();
				
			
				parent.setBackgroundOff();
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JXButton jb = (JXButton) e.getSource();
				jb.setEnabled(true);
				JXPanel parent = (JXPanel) jb.getParent();
				parent.setBackgroundPainter(Colors.getHighlightOnPainter());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void setBackgroundOn() {
		this.setBackgroundPainter(Colors.getHighlightOnPainter());
		deleteButton.setEnabled(true);
	}
	
	public void setBackgroundOff() {
		this.setBackgroundPainter(Colors.getHighlightOffPainter());
		deleteButton.setEnabled(false);
	}

	public void setTaskpane(JXTaskPane taskpane) {
		this.taskpane = taskpane;
	}

	public JXTaskPane getTaskpane() {
		return taskpane;
	}

	public void setStudentPlanningController(StudentPlanningController studentPlanningController) {
		this.studentPlanningController = studentPlanningController;
	}

	public StudentPlanningController getStudentPlanningController() {
		return studentPlanningController;
	}
	
	public void removeMe() {
		this.studentPlanningController.removeEntry(this);
	}

	public void setDateLabel(JXLabel dateLabel) {
		this.dateLabel = dateLabel;
	}

	public JXLabel getDateLabel() {
		return dateLabel;
	}
	
}
