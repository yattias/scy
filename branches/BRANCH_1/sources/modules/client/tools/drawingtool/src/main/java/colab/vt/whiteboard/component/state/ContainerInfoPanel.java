package colab.vt.whiteboard.component.state;

import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ContainerInfoPanel extends javax.swing.JPanel implements ChangeListener
{
	private static final long serialVersionUID = 932890035330093654L;
	private static final Logger logger = Logger.getLogger(ContainerInfoPanel.class.getName());

	private WhiteboardContainer whiteboardContainer = null;
	private boolean externalSettingValues = false;
	private boolean userSettingValues = false;
	private JLabel offsetLabel;
	private JSpinner rotateSpinner;
	private JSpinner yScaleSpinner;
	private JLabel yScaleLabel;
	private JSpinner xScaleSpinner;
	private JLabel xScaleLabel;
	private JSpinner yOffsetSpinner;
	private JLabel yOffsetLabel;
	private JSpinner xOffsetSpinner;
	private JLabel xOffsetLabel;
	private JCheckBox selectedCheckBox;
	private JLabel selectedLabel;
	private JLabel rotaleLabel;
	private JLabel scaleLabel;

	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ContainerInfoPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public ContainerInfoPanel()
	{
		super();
		initGUI();
		xOffsetSpinner.addChangeListener(this);
		yOffsetSpinner.addChangeListener(this);
		xScaleSpinner.addChangeListener(this);
		yScaleSpinner.addChangeListener(this);
		rotateSpinner.addChangeListener(this);
		selectedCheckBox.addChangeListener(this);
	}

	private void initGUI()
	{
		try
		{
			GroupLayout thisLayout = new GroupLayout(this);
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(291, 121));
			this.setBorder(BorderFactory.createTitledBorder("Container info"));
			{
				offsetLabel = new JLabel();
				offsetLabel.setText("Offset");
			}
			{
				scaleLabel = new JLabel();
				scaleLabel.setText("Scale");
			}
			{
				rotaleLabel = new JLabel();
				rotaleLabel.setText("Rotate");
			}
			{
				xScaleLabel = new JLabel();
				xScaleLabel.setText("X");
			}
			{
				SpinnerModel xScaleSpinnerModel = createScaleSpinnerModel();
				xScaleSpinner = new JSpinner();
				xScaleSpinner.setModel(xScaleSpinnerModel);
			}
			{
				yScaleLabel = new JLabel();
				yScaleLabel.setText("Y");
			}
			{
				SpinnerModel yScaleSpinnerModel = createScaleSpinnerModel();
				yScaleSpinner = new JSpinner();
				yScaleSpinner.setModel(yScaleSpinnerModel);
			}
			{
				SpinnerModel rotateSpinnerModel = createRotateSpinnerModel();
				rotateSpinner = new JSpinner();
				rotateSpinner.setModel(rotateSpinnerModel);
			}
			{
				xOffsetLabel = new JLabel();
				xOffsetLabel.setText("X");
				xOffsetLabel.setSize(20, 14);
			}
			{
				SpinnerModel xOffsetSpinnerModel = createOffsetSpinnerModel();
				xOffsetSpinner = new JSpinner();
				xOffsetSpinner.setModel(xOffsetSpinnerModel);
			}
			{
				yOffsetLabel = new JLabel();
				yOffsetLabel.setText("Y");
				yOffsetLabel.setSize(20, 14);
			}
			{
				SpinnerModel yOffsetSpinnerModel = createOffsetSpinnerModel();
				yOffsetSpinner = new JSpinner();
				yOffsetSpinner.setModel(yOffsetSpinnerModel);
				yOffsetSpinner.getEditor().setSize(55, 17);
				yOffsetSpinner.getEditor().setPreferredSize(new java.awt.Dimension(55, 17));
			}
			{
				selectedLabel = new JLabel();
				selectedLabel.setText("Selected");
			}
			{
				selectedCheckBox = new JCheckBox();
			}
			thisLayout
						.setVerticalGroup(thisLayout
									.createSequentialGroup()
									.addContainerGap()
									.addGroup(
												thisLayout
															.createParallelGroup()
															.addGroup(
																		thisLayout
																					.createSequentialGroup()
																					.addGroup(
																								thisLayout
																											.createParallelGroup(
																														GroupLayout.Alignment.BASELINE)
																											.addComponent(
																														xOffsetSpinner,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														offsetLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														xOffsetLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														yOffsetLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														yOffsetSpinner,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE))
																					.addGap(19)
																					.addGroup(
																								thisLayout
																											.createParallelGroup(
																														GroupLayout.Alignment.BASELINE)
																											.addComponent(
																														rotateSpinner,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														rotaleLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)))
															.addGroup(
																		thisLayout
																					.createSequentialGroup()
																					.addGap(20)
																					.addGroup(
																								thisLayout
																											.createParallelGroup(
																														GroupLayout.Alignment.BASELINE)
																											.addComponent(
																														xScaleSpinner,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														scaleLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														xScaleLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														yScaleLabel,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																											.addComponent(
																														yScaleSpinner,
																														GroupLayout.Alignment.BASELINE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.PREFERRED_SIZE))
																					.addGap(20))).addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED).addGroup(
												thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
															.addComponent(selectedLabel,
																		GroupLayout.Alignment.BASELINE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.PREFERRED_SIZE).addComponent(
																		selectedCheckBox,
																		GroupLayout.Alignment.BASELINE,
																		GroupLayout.PREFERRED_SIZE, 18,
																		GroupLayout.PREFERRED_SIZE)).addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap()
						.addGroup(
									thisLayout.createParallelGroup().addGroup(
												thisLayout.createSequentialGroup().addGroup(
															thisLayout.createParallelGroup().addComponent(
																		scaleLabel, GroupLayout.Alignment.LEADING,
																		GroupLayout.PREFERRED_SIZE, 34,
																		GroupLayout.PREFERRED_SIZE).addComponent(
																		rotaleLabel, GroupLayout.Alignment.LEADING,
																		GroupLayout.PREFERRED_SIZE, 34,
																		GroupLayout.PREFERRED_SIZE).addComponent(
																		offsetLabel, GroupLayout.Alignment.LEADING,
																		GroupLayout.PREFERRED_SIZE, 34,
																		GroupLayout.PREFERRED_SIZE)).addPreferredGap(
															LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
															thisLayout.createParallelGroup().addComponent(
																		xScaleLabel, GroupLayout.Alignment.LEADING,
																		GroupLayout.PREFERRED_SIZE, 21,
																		GroupLayout.PREFERRED_SIZE).addComponent(
																		xOffsetLabel, GroupLayout.Alignment.LEADING,
																		GroupLayout.PREFERRED_SIZE, 21,
																		GroupLayout.PREFERRED_SIZE)).addGap(9))
												.addComponent(selectedLabel, GroupLayout.Alignment.LEADING, 0,
															76, Short.MAX_VALUE)).addGroup(
									thisLayout.createParallelGroup().addComponent(xScaleSpinner,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 74,
												GroupLayout.PREFERRED_SIZE).addComponent(rotateSpinner,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 74,
												GroupLayout.PREFERRED_SIZE).addComponent(xOffsetSpinner,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 74,
												GroupLayout.PREFERRED_SIZE).addComponent(selectedCheckBox,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 74,
												GroupLayout.PREFERRED_SIZE)).addGap(24).addGroup(
									thisLayout.createParallelGroup().addComponent(yScaleLabel,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 19,
												GroupLayout.PREFERRED_SIZE).addComponent(yOffsetLabel,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 19,
												GroupLayout.PREFERRED_SIZE)).addGroup(
									thisLayout.createParallelGroup().addComponent(yScaleSpinner,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 76,
												GroupLayout.PREFERRED_SIZE).addComponent(yOffsetSpinner,
												GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
												GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private SpinnerModel createOffsetSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.0, -1000, 1000, 1);
		return spinnerNumberModel;
	}

	private SpinnerModel createScaleSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1.0,
					WhiteboardPanel.minimumScale, WhiteboardPanel.maximumScale, .1);
		return spinnerNumberModel;
	}

	private SpinnerModel createRotateSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.0, -360, 360, 1);
		return spinnerNumberModel;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (whiteboardContainer != null)
		{
			if (externalSettingValues)
			{
				// System.out.println("User value ignored because externalSettingValues");
				return;
			}
			// System.out.println("new user value in info display");
			try
			{
				userSettingValues = true;
				if (selectedCheckBox == e.getSource())
					whiteboardContainer.setSelected(selectedCheckBox.isSelected());
				else
				{
					if (xOffsetSpinner == e.getSource())
						whiteboardContainer.setXOffset((Double) xOffsetSpinner.getValue());
					else if (yOffsetSpinner == e.getSource())
						whiteboardContainer.setYOffset((Double) yOffsetSpinner.getValue());
					else if (xScaleSpinner == e.getSource())
						whiteboardContainer.setXScale((Double) xScaleSpinner.getValue());
					else if (yScaleSpinner == e.getSource())
						whiteboardContainer.setYScale((Double) yScaleSpinner.getValue());
					else if (rotateSpinner == e.getSource())
						whiteboardContainer.setRotationDegrees((Double) rotateSpinner.getValue());
					else
					{
						logger.warning("Unknown source " + e.getSource());
					}
				}
				whiteboardContainer.repaint(false);
			}
			finally
			{
				userSettingValues = false;
			}
		}
	}

	public void setWhiteboardContainer(WhiteboardContainer whiteboardContainer)
	{
		this.whiteboardContainer = whiteboardContainer;
		valuesChanged();
	}

	public void valuesChanged()
	{
		if (userSettingValues)
		{
			// System.out.println("external value ignored because userSettingValues");
			return;
		}
		try
		{
			externalSettingValues = true;
			xOffsetSpinner.setValue(whiteboardContainer.getXOffset());
			yOffsetSpinner.setValue(whiteboardContainer.getYOffset());
			xScaleSpinner.setValue(whiteboardContainer.getXScale());
			yScaleSpinner.setValue(whiteboardContainer.getYScale());
			rotateSpinner.setValue(whiteboardContainer.getRotationDegrees());
			selectedCheckBox.setSelected(whiteboardContainer.isSelected());
		}
		finally
		{
			externalSettingValues = false;
		}
	}

}
