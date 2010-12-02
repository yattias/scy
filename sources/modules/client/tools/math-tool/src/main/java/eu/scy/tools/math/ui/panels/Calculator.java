package eu.scy.tools.math.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.error.ErrorInfo;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.paint.RoundedBorder;

public class Calculator extends JXPanel {

	private static Logger log = Logger.getLogger("Calculator.class"); //$NON-NLS-1$

	
	private ArrayList<JXButton> symbolicButtons;
	private ArrayList<JXButton> adderButtons;
	private ArrayList<JXButton> numberButtons;
	private JXTextField sumTextField;


	private JXButton equalsButton;


	private JXButton radiusButton;

	private JXButton widthButton;


	private JXButton heightButton;


	private JXButton addButton;


	private JXButton subtractButton;


	private JXLabel resultLabel;


	private String type;


	private JXPanel calcButtonPanel;


	private Font buttonFont;
	
	
	public Calculator(String type) {
		setLayout(new MigLayout("fill, inset 5 5 5 5"));
		this.type = type;
		init();
		initButtonPanel();
	}
	
	
	private void initButtonPanel() {
		  setCalcButtonPanel(new JXPanel(new BorderLayout(5,5)));
		    getCalcButtonPanel().setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
		    
		    setResultLabel(new JXLabel("0.00"));
		    this.modResultLabel(getResultLabel());
		    
		    JXPanel l = new JXPanel(new FlowLayout());
		    l.add(getResultLabel());
		    l.setOpaque(false);
		    getCalcButtonPanel().add(l,BorderLayout.WEST);
		    
		    
		    
		    setAddButton(new JXButton());
		    getAddButton().putClientProperty(UIUtils.TYPE, this.type);
		    getAddButton().setToolTipText("Adds the calculation to the table.");

		    setSubtractButton(new JXButton());
		    getSubtractButton().putClientProperty(UIUtils.TYPE, this.type);
		    getSubtractButton().setToolTipText("Subtracts the calculation to the table.");
		    
		    JXPanel b = new JXPanel(new FlowLayout());
		    
		    if( type.equals(UIUtils._2D)) {
		    b.add(getSubtractButton());
		    b.add(getAddButton());
		    b.setOpaque(false);
		    }
		    getCalcButtonPanel().add(b, BorderLayout.EAST);
	}


	public void resetLabel() {
		this.resultLabel.setText("0.00");
	}
	private void modResultLabel(JXLabel label) {
		Font font = label.getFont();
		Font bigFont = new FontUIResource(font.deriveFont(Font.BOLD, font.getSize2D() * 1.3f)); 
		label.setFont(bigFont);
		label.setForeground(Color.blue);
	}
	
	private void init() {
		this.setBorder(new RoundedBorder(5));
		
		setSumTextField(new JXTextField("Formula"));
		getSumTextField().setBackground(Color.WHITE);
		getSumTextField().setOpaque(true);
		getSumTextField().putClientProperty(UIUtils.TYPE, type);
		this.add(getSumTextField(),"growx, wrap");
		
		JXPanel buttonPanel = new JXPanel(new GridLayout(5,5,6,6));
		
		//symbolic
		symbolicButtons = new ArrayList<JXButton>();
		adderButtons = new ArrayList<JXButton>();
		numberButtons = new ArrayList<JXButton>();
		
		JXButton symButton = new JXButton("¹");
		
		buttonFont = symButton.getFont().deriveFont(Font.BOLD, 12f);
		
		symButton.setName("3.14");
		symButton.addActionListener(buttonAction);
		symbolicButtons.add(symButton);
		
		symButton = new JXButton("<html>x<sup>2</sup></html>");
		symButton.setName("^2");
		symButton.addActionListener(buttonAction);
		symbolicButtons.add(symButton);
		
		symButton = new JXButton("<html>x<sup>3</sup></html>");
		symButton.setName("^3");
		symButton.addActionListener(buttonAction);
		symbolicButtons.add(symButton);
		
		
		symButton = new JXButton(".");
		symButton.setName(".");
		symButton.addActionListener(buttonAction);
		symbolicButtons.add(symButton);

		symButton = new JXButton("c");
		symButton.setName("c");
		symButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sumTextField.setText("");
			}
		});
		symbolicButtons.add(symButton);	
		
		symButton = new JXButton("(");
		symButton.setName("(");
		symButton.addActionListener(buttonAction);
		symbolicButtons.add(symButton);	
		
		symButton = new JXButton(")");
		symButton.setName(")");
		symButton.addActionListener(buttonAction);
		symbolicButtons.add(symButton);

	
		
		radiusButton = new JXButton("R");
		radiusButton.setName("r");
		radiusButton.addActionListener(buttonAction);
		
		symbolicButtons.add(radiusButton);
		widthButton = new JXButton("W");
		widthButton.addActionListener(buttonAction);
		widthButton.setName("w");
		symbolicButtons.add(widthButton);
		
		heightButton = new JXButton("H");
		heightButton.setName("h");
		heightButton.addActionListener(buttonAction);
		
		symbolicButtons.add(heightButton);
		
		JXButton adderButton = new JXButton("*");
		adderButton.setName("*");
		adderButton.addActionListener(buttonAction);
		adderButtons.add(adderButton);
		
		adderButton = new JXButton("-");
		adderButton.setName("-");
		adderButton.addActionListener(buttonAction);
		adderButtons.add(adderButton);

		adderButton = new JXButton("/");
		adderButton.setName("/");
		adderButton.addActionListener(buttonAction);
		adderButtons.add(adderButton);

		adderButton = new JXButton("+");
		adderButton.setName("+");
		adderButton.addActionListener(buttonAction);
		adderButtons.add(adderButton);

		setEqualsButton(new JXButton("="));
		adderButtons.add(getEqualsButton());
		
		JXButton numButton = new JXButton("4");
		numButton.setName("4");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);
		
		numButton = new JXButton("5");
		numButton.setName("5");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("3");
		numButton.setName("3");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("9");
		numButton.setName("9");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("1");
		numButton.setName("1");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("2");
		numButton.setName("2");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("8");
		numButton.setName("8");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("6");
		numButton.setName("6");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);
		
		numButton = new JXButton("0");
		numButton.setName("0");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);

		numButton = new JXButton("7");
		numButton.setName("7");
		numButton.addActionListener(buttonAction);
		numberButtons.add(numButton);
		
		for (JXButton addButton : adderButtons) {
			this.modButton(addButton);

			addButton.setOpaque(true);
			if( addButton.getText().equals("=")) {
				addButton.setBackgroundPainter(UIUtils.getEqualButtonPainter());
			} else {
				addButton.setBackgroundPainter(UIUtils.getAdderButtonPainter());
			}
			
			addButton.setForeground(Color.BLACK);
			addButton.setBorderPainted(true);
			addButton.setBorder(new LineBorder(Color.WHITE, 1));
		}
		
		for (JXButton sButton : symbolicButtons) {
			this.modButton(sButton);
			sButton.setOpaque(true);
			sButton.setBackgroundPainter(UIUtils.getSymbolButtonPainter());
			sButton.setForeground(Color.WHITE);
			sButton.setBorderPainted(true);
			sButton.setBorder(new LineBorder(Color.WHITE, 1));
			sButton.setRolloverEnabled(false);
			buttonPanel.add(sButton);
		}

		for (JXButton nButton : numberButtons) {
			this.modButton(nButton);
			nButton.setBackgroundPainter(UIUtils.getNumButtonPainter());
			nButton.setForeground(Color.WHITE);
			nButton.setBorderPainted(true);
			nButton.setBorder(new LineBorder(Color.WHITE, 1));
			buttonPanel.add(nButton);
			if( nButton.getText().equals("9"))
				buttonPanel.add(adderButtons.get(0));
			
			if( nButton.getText().equals("6"))
				buttonPanel.add(adderButtons.get(1));
			
			if( nButton.getText().equals("7")) {
				buttonPanel.add(adderButtons.get(2));
				buttonPanel.add(adderButtons.get(3));
				buttonPanel.add(adderButtons.get(4));
			}
		}
		
		buttonPanel.setOpaque(false);
		this.add(buttonPanel,"grow");
		this.setBackgroundPainter(UIUtils.getCalcBackgroundPainter());
	}


	private void modButton(JXButton button) {
		button.setFont(buttonFont);
		
	}


	public void setSumTextField(JXTextField sumTextField) {
		this.sumTextField = sumTextField;
	}


	public JXTextField getSumTextField() {
		return sumTextField;
	}


	public void setEqualsButton(JXButton equalsButton) {
		this.equalsButton = equalsButton;
		this.equalsButton.putClientProperty(UIUtils.TYPE, UIUtils._2D);
	}


	public JXButton getEqualsButton() {
		return equalsButton;
	}


	public void setCalcButtonPanel(JXPanel calcButtonPanel) {
		this.calcButtonPanel = calcButtonPanel;
	}


	public JXPanel getCalcButtonPanel() {
		return calcButtonPanel;
	}


	public void setAddButton(JXButton addButton) {
		this.addButton = addButton;
	}


	public JXButton getAddButton() {
		return addButton;
	}


	public void setSubtractButton(JXButton subtractButton) {
		this.subtractButton = subtractButton;
	}


	public JXButton getSubtractButton() {
		return subtractButton;
	}


	public void setResultLabel(JXLabel resultLabel) {
		this.resultLabel = resultLabel;
	}


	public JXLabel getResultLabel() {
		return resultLabel;
	}

	ActionListener buttonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JXButton button = (JXButton) e.getSource();
			sumTextField.setText(sumTextField.getText() + button.getName());
			
		}
	};
	

	
	
}
