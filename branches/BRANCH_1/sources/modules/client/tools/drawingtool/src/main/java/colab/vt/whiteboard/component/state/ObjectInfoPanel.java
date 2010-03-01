package colab.vt.whiteboard.component.state;

import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import colab.vt.whiteboard.component.WhiteboardObject;
import colab.vt.whiteboard.component.WhiteboardObjectContainer;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ObjectInfoPanel extends javax.swing.JPanel
{
	private static final long serialVersionUID = -6379889889499060572L;
	private WhiteboardObjectContainer whiteboardObjectContainer = null;
	private WhiteboardObject whiteboardObject = null;
	private JLabel typeLabel;
	private JLabel boundsLabel;
	private JLabel bounds;
	private JScrollPane descriptionScrollPane;
	private JTextArea description;
	private JLabel descriptionLabel;
	private JLabel objectType;

	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ObjectInfoPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public ObjectInfoPanel()
	{
		super();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(290, 171));
			GroupLayout thisLayout = new GroupLayout(this);
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createTitledBorder("Object Info"));
			{
				typeLabel = new JLabel();
				typeLabel.setText("Type");
			}
			{
				descriptionScrollPane = new JScrollPane();
				{
					description = new JTextArea();
					descriptionScrollPane.setViewportView(description);
					description.setText("The description");
					description.setEditable(false);
					// description.setPreferredSize(new java.awt.Dimension(237, 106));
				}
			}
			{
				objectType = new JLabel();
				objectType.setText("Unknown");
			}
			{
				boundsLabel = new JLabel();
				boundsLabel.setText("Bounds");
			}
			{
				bounds = new JLabel();
				bounds.setText("Unknown");
				bounds.setSize(50, 14);
			}
			{
				descriptionLabel = new JLabel();
				descriptionLabel.setText("Description");
			}
			thisLayout
						.setVerticalGroup(thisLayout.createSequentialGroup()
									.addGroup(
												thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
															.addComponent(objectType,
																		GroupLayout.Alignment.BASELINE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE).addComponent(
																		typeLabel, GroupLayout.Alignment.BASELINE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE)).addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED).addGroup(
												thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
															.addComponent(bounds, GroupLayout.Alignment.BASELINE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE).addComponent(
																		boundsLabel, GroupLayout.Alignment.BASELINE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE)).addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED).addGroup(
												thisLayout.createParallelGroup().addGroup(
															GroupLayout.Alignment.LEADING,
															thisLayout.createSequentialGroup().addComponent(
																		descriptionLabel, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE).addGap(81))
															.addComponent(descriptionScrollPane,
																		GroupLayout.Alignment.LEADING, 0, 95,
																		Short.MAX_VALUE)).addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap()
						.addGroup(
									thisLayout.createParallelGroup().addComponent(descriptionLabel,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGroup(
															GroupLayout.Alignment.LEADING,
															thisLayout.createSequentialGroup().addComponent(
																		boundsLabel, GroupLayout.PREFERRED_SIZE, 46,
																		GroupLayout.PREFERRED_SIZE).addGap(7))
												.addGroup(
															GroupLayout.Alignment.LEADING,
															thisLayout.createSequentialGroup().addComponent(
																		typeLabel, GroupLayout.PREFERRED_SIZE, 34,
																		GroupLayout.PREFERRED_SIZE).addGap(19)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
									thisLayout.createParallelGroup().addGroup(
												GroupLayout.Alignment.LEADING,
												thisLayout.createSequentialGroup().addComponent(
															descriptionScrollPane, 0, 185, Short.MAX_VALUE)
															.addGap(55)).addComponent(bounds,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 240,
												GroupLayout.PREFERRED_SIZE).addGroup(
												GroupLayout.Alignment.LEADING,
												thisLayout.createSequentialGroup().addComponent(objectType,
															GroupLayout.PREFERRED_SIZE, 155,
															GroupLayout.PREFERRED_SIZE).addGap(85))));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setWhiteboardObjectContainer(WhiteboardObjectContainer whiteboardObjectContainer)
	{
		this.whiteboardObjectContainer = whiteboardObjectContainer;
		whiteboardObject = whiteboardObjectContainer.getWhiteboardObject();
		valuesChanged();
	}

	public void valuesChanged()
	{
		objectType.setText(whiteboardObject.getType());
		Rectangle boundsRect = whiteboardObject.getBounds();
		String boundsString = "x:" + boundsRect.x + ", y:" + boundsRect.y + ", width:"
					+ boundsRect.width + ", height:" + boundsRect.height;
		bounds.setText(boundsString);
		description.setText(whiteboardObjectContainer.getWhiteboardObject().getDescription());
//		 Rectangle2D transformedBounds = whiteboardObjectContainer.transformRectangle(boundsRect);
//		 description.setText(transformedBounds.toString());
	}

}
