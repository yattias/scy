package colab.vt.whiteboard.component.state;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
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
public class ContainerInfoPanel2 extends javax.swing.JPanel implements ChangeListener
{
	private static final long serialVersionUID = 8992637222037780134L;
	private static final Logger logger = Logger.getLogger(ContainerInfoPanel2.class.getName());

	private WhiteboardContainer whiteboardContainer = null;
	private boolean externalSettingValues = false;
	private boolean userSettingValues = false;

	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel transformMatrixLabel;
	private JTextArea transformMatrix;
	private JLabel colorLabel;
	private JButton fillColorButton;
	private JButton lineColorButton;
	private JLabel penSizeLabel;
	private JCheckBox fixedPenSizeCheckBox;
	private JSpinner penSizeSpinner;
	private JCheckBox selectedCheckBox;
	private JSpinner yScaleCompensationSpinner;
	private JSpinner xScaleCompensationSpinner;
	private JSpinner yShearSpinner;
	private JSpinner xShearSpinner;
	private JLabel offsetLabel;
	private JSpinner yScaleSpinner;
	private JSpinner xScaleSpinner;
	private JSpinner rotateSpinner;
	private JLabel selectedLabel;
	private JLabel scaleCompensationLabel;
	private JLabel shearLabel;
	private JLabel scaleLabel;
	private JLabel rotateLabel;
	private JSpinner yOffsetSpinner;
	private JSpinner xOffsetSpinner;

	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ContainerInfoPanel2());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public ContainerInfoPanel2()
	{
		super();
		initGUI();
		xOffsetSpinner.addChangeListener(this);
		yOffsetSpinner.addChangeListener(this);
		rotateSpinner.addChangeListener(this);
		xScaleSpinner.addChangeListener(this);
		yScaleSpinner.addChangeListener(this);
		xShearSpinner.addChangeListener(this);
		yShearSpinner.addChangeListener(this);
		xScaleCompensationSpinner.addChangeListener(this);
		yScaleCompensationSpinner.addChangeListener(this);
		penSizeSpinner.addChangeListener(this);
		fixedPenSizeCheckBox.addChangeListener(this);
		selectedCheckBox.addChangeListener(this);
	}

	private void initGUI()
	{
		try
		{
			GroupLayout thisLayout = new GroupLayout(this);
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(291, 280));
			{
				xLabel = new JLabel();
				xLabel.setText("X");
			}
			{
				yLabel = new JLabel();
				yLabel.setText("Y");
			}
			{
				offsetLabel = new JLabel();
				offsetLabel.setText("Offset");
			}
			{
				xOffsetSpinner = new JSpinner();
				xOffsetSpinner.setModel(createOffsetSpinnerModel());
			}
			{
				yOffsetSpinner = new JSpinner();
				yOffsetSpinner.setModel(createOffsetSpinnerModel());
			}
			{
				rotateLabel = new JLabel();
				rotateLabel.setText("Rotate");
			}
			{
				rotateSpinner = new JSpinner();
				rotateSpinner.setModel(createRotateSpinnerModel());
			}
			{
				scaleLabel = new JLabel();
				scaleLabel.setText("Scale");
			}
			{
				xScaleSpinner = new JSpinner();
				xScaleSpinner.setModel(createScaleSpinnerModel());
			}
			{
				yScaleSpinner = new JSpinner();
				yScaleSpinner.setModel(createScaleSpinnerModel());
			}
			{
				shearLabel = new JLabel();
				shearLabel.setText("Shear");
			}
			{
				xShearSpinner = new JSpinner();
				xShearSpinner.setModel(createShearSpinnerModel());
			}
			{
				yShearSpinner = new JSpinner();
				yShearSpinner.setModel(createShearSpinnerModel());
			}
			{
				scaleCompensationLabel = new JLabel();
				scaleCompensationLabel.setText("Comp. offset");
			}
			{
				xScaleCompensationSpinner = new JSpinner();
				xScaleCompensationSpinner.setModel(createScaleCompensationSpinnerModel());
			}
			{
				yScaleCompensationSpinner = new JSpinner();
				yScaleCompensationSpinner.setModel(createScaleCompensationSpinnerModel());
			}
			{
				penSizeLabel = new JLabel();
				penSizeLabel.setText("Pen size");
			}
			{
				penSizeSpinner = new JSpinner();
				penSizeSpinner.setModel(createPenSizeSpinnerModel());
			}
			{
				fixedPenSizeCheckBox = new JCheckBox("fixed");
			}
			{
				colorLabel = new JLabel();
				colorLabel.setText("Color");
			}
			{
				lineColorButton = new JButton();
				lineColorButton.setText("line");
				lineColorButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
//						System.out.println("lineColorButton.actionPerformed, event=" + evt);
						chooseLineColor();
					}
				});
			}
			{
				fillColorButton = new JButton();
				fillColorButton.setText("fill");
				fillColorButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
//						System.out.println("fillColorButton.actionPerformed, event=" + evt);
						chooseFillColor();
					}
				});
			}
			{
				selectedLabel = new JLabel();
				selectedLabel.setText("Selected");
			}
			{
				selectedCheckBox = new JCheckBox();
			}
			{
				transformMatrixLabel = new JLabel();
				transformMatrixLabel.setText("Transform");
			}
			{
				transformMatrix = new JTextArea();
				transformMatrix.setText("transform\n matrix");
				transformMatrix.setEditable(false);
				transformMatrix.setFont(new java.awt.Font("Courier New", 0, 11));
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(xLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(yLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(xOffsetSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(offsetLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(yOffsetSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(rotateSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(rotateLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(xScaleSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(scaleLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(yScaleSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(xShearSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(shearLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(yShearSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(xScaleCompensationSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(scaleCompensationLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(yScaleCompensationSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(penSizeSpinner, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(fixedPenSizeCheckBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(penSizeLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(lineColorButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(fillColorButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(colorLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(selectedLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(selectedCheckBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(transformMatrixLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				        .addGap(15))
				    .addComponent(transformMatrix, GroupLayout.Alignment.LEADING, 0, 29, Short.MAX_VALUE))
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(transformMatrixLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(selectedLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(colorLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(penSizeLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(scaleCompensationLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(shearLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(scaleLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(rotateLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				    .addComponent(offsetLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(transformMatrix, GroupLayout.Alignment.LEADING, 0, 183, Short.MAX_VALUE)
				    .addGroup(thisLayout.createSequentialGroup()
				        .addGroup(thisLayout.createParallelGroup()
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(lineColorButton, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(selectedCheckBox, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
				                .addGap(11))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(penSizeSpinner, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(xScaleCompensationSpinner, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(xShearSpinner, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(xScaleSpinner, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(rotateSpinner, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(xOffsetSpinner, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addGap(0, 16, Short.MAX_VALUE)
				                .addComponent(xLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				                .addGap(35)))
				        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				        .addGroup(thisLayout.createParallelGroup()
				            .addComponent(fillColorButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(fixedPenSizeCheckBox, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
				                .addGap(12))
				            .addComponent(yScaleCompensationSpinner, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				            .addComponent(yShearSpinner, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				            .addComponent(yScaleSpinner, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				            .addComponent(yOffsetSpinner, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(yLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				                .addGap(52)))))
				.addContainerGap());
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

	private SpinnerModel createRotateSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.0, -360, 360, 1);
		return spinnerNumberModel;
	}

	private SpinnerModel createScaleSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1.0,
					WhiteboardPanel.minimumScale, WhiteboardPanel.maximumScale, .1);
		return spinnerNumberModel;
	}

	private SpinnerModel createShearSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.0, -10, 10, 0.1);
		return spinnerNumberModel;
	}

	private SpinnerModel createScaleCompensationSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.0, -1000, 1000, 1);
		return spinnerNumberModel;
	}

	private SpinnerModel createPenSizeSpinnerModel()
	{
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1.0,
					WhiteboardPanel.minimumPenSize, WhiteboardPanel.maximumPenSize, .1);
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
				whiteboardContainer.repaint();
				if (xOffsetSpinner == e.getSource())
					whiteboardContainer.setXOffset((Double) xOffsetSpinner.getValue());
				else if (yOffsetSpinner == e.getSource())
					whiteboardContainer.setYOffset((Double) yOffsetSpinner.getValue());
				else if (rotateSpinner == e.getSource())
					whiteboardContainer.setRotationDegrees((Double) rotateSpinner.getValue());
				else if (xScaleSpinner == e.getSource())
					whiteboardContainer.setXScale((Double) xScaleSpinner.getValue());
				else if (yScaleSpinner == e.getSource())
					whiteboardContainer.setYScale((Double) yScaleSpinner.getValue());
				else if (xShearSpinner == e.getSource())
					whiteboardContainer.setXShear((Double) xShearSpinner.getValue());
				else if (yShearSpinner == e.getSource())
					whiteboardContainer.setYShear((Double) yShearSpinner.getValue());
				else if (xScaleCompensationSpinner == e.getSource())
					whiteboardContainer.setXScaleOffsetCompensation((Double) xScaleCompensationSpinner
								.getValue());
				else if (yScaleCompensationSpinner == e.getSource())
					whiteboardContainer.setYScaleOffsetCompensation((Double) yScaleCompensationSpinner
								.getValue());
				else if (penSizeSpinner == e.getSource())
					whiteboardContainer.setPenSize((Double) penSizeSpinner.getValue());
				else if (fixedPenSizeCheckBox == e.getSource())
					whiteboardContainer.setScaleIndependentPenSize(fixedPenSizeCheckBox.isSelected());
				else if (selectedCheckBox == e.getSource())
					whiteboardContainer.setSelected(selectedCheckBox.isSelected());
				else
				{
					logger.warning("Unknown source " + e.getSource());
				}
				xScaleCompensationSpinner.setValue(whiteboardContainer.getXScaleOffsetCompensation());
				yScaleCompensationSpinner.setValue(whiteboardContainer.getYScaleOffsetCompensation());
				transformMatrix.setText(transformationMatrixToString(whiteboardContainer
							.getTransformMatrix()));
				whiteboardContainer.repaint();
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
			rotateSpinner.setValue(whiteboardContainer.getRotationDegrees());
			xScaleSpinner.setValue(whiteboardContainer.getXScale());
			yScaleSpinner.setValue(whiteboardContainer.getYScale());
			xShearSpinner.setValue(whiteboardContainer.getXShear());
			yShearSpinner.setValue(whiteboardContainer.getYShear());
			xScaleCompensationSpinner.setValue(whiteboardContainer.getXScaleOffsetCompensation());
			yScaleCompensationSpinner.setValue(whiteboardContainer.getYScaleOffsetCompensation());
			penSizeSpinner.setValue(whiteboardContainer.getPenSize());
			fixedPenSizeCheckBox.setSelected(whiteboardContainer.isScaleIndependentPenSize());
			selectedCheckBox.setSelected(whiteboardContainer.isSelected());
			transformMatrix.setText(transformationMatrixToString(whiteboardContainer
						.getTransformMatrix()));
		}
		finally
		{
			externalSettingValues = false;
		}
	}

	protected void chooseLineColor()
	{
		Color selectedColor = JColorChooser.showDialog(this, "Select line color", whiteboardContainer.getLineColor());
		if (selectedColor!=null)
		{
			whiteboardContainer.setLineColor(selectedColor);
			whiteboardContainer.repaint();
		}
	}

	protected void chooseFillColor()
	{
		Color selectedColor = JColorChooser.showDialog(this, "Select fill color", whiteboardContainer.getFillColor());
		if (selectedColor!=null)
		{
			whiteboardContainer.setFillColor(selectedColor);
			whiteboardContainer.repaint();
		}
	}

	private String transformationMatrixToString(double[] matrix)
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < matrix.length; i += 3)
		{
			builder.append(String.format("%8.2f %8.2f %8.2f\n", matrix[i], matrix[i + 1], matrix[i + 2]));
		}
		return builder.toString();
	}

}
