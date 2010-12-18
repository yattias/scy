package eu.scy.tools.math.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;

import org.apache.commons.lang.StringUtils;
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


	private ExpressionModel expressionModel;


	private JXLabel textField;
	
	
	public Calculator(String type) {
		setLayout(new MigLayout("fill, inset 5 5 5 5"));
		this.type = type;
		init();
		initButtonPanel();
	}
	
	
	private void initButtonPanel() {
		  setCalcButtonPanel(new JXPanel(new BorderLayout(5,5)));
		    getCalcButtonPanel().setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
		    
		    setResultLabel(new JXLabel("3.14"));
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
		    
		    
		    
		    if( type.equals(UIUtils._2D)) {
		    	JXPanel b = new JXPanel(new FlowLayout());
				b.add(getSubtractButton());
				b.add(getAddButton());
				b.setOpaque(false);
				getCalcButtonPanel().add(b, BorderLayout.EAST);
		    }
		    
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
		
//		setSumTextField(new JXTextField("Select a shape and create a Formula"));
//		getSumTextField().setBackground(Color.WHITE);
//		getSumTextField().setOpaque(true);
//		getSumTextField().putClientProperty(UIUtils.TYPE, type);
		
		JXPanel eqPanel = new JXPanel(new MigLayout("insets 2 2 2 2"));
		eqPanel.setOpaque(false);
		textField = new JXLabel();
		textField.putClientProperty(UIUtils.TYPE, type);
		textField.setPreferredSize(new Dimension(20,30));
		textField.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
		textField.setBackground(Color.white);
		textField.setBorder(new LineBorder(Color.BLACK, 1));
		textField.setText("Formula");
		textField.setOpaque(true);
		
		 expressionModel = new ExpressionModel();
		
		
		
		 eqPanel.add(textField,"growx, wrap");
		
		JXButton piButton = new JXButton("<html>&#960</html>");
		
		buttonFont = piButton.getFont().deriveFont(Font.BOLD, 12f);
		
		piButton.setName(ExpressionModel.PI);
		piButton.addActionListener(buttonAction);
		this.modSymbolButton(piButton);
		
		JXButton sqButton = new JXButton("<html>x<sup>2</sup></html>");
		sqButton.setName(ExpressionModel.cubedsym);
		sqButton.addActionListener(buttonAction);
		this.modSymbolButton(sqButton);
		
		JXButton sqrtButton = new JXButton("<html>&#8730</html>");
		sqrtButton.setName(ExpressionModel.SQRT);
		sqrtButton.addActionListener(buttonAction);
		this.modSymbolButton(sqrtButton);
		
		JXButton cbrtButton = new JXButton("<html>&#179 &#8730;</html>");
		cbrtButton.setName(ExpressionModel.CBRT);
		cbrtButton.addActionListener(buttonAction);
		this.modSymbolButton(cbrtButton);
		
		JXButton clearButton = new JXButton("<html>C</html>");
		clearButton.setName("c");
		this.modSymbolButton(clearButton);
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sumTextField.setText("");
			}
		});
		
		
		JXButton clearNumButton = new JXButton("<html>C</html>");
		clearNumButton.setName("c");
		this.modSymbolButton(clearNumButton);
		clearNumButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sumTextField.setText("");
			}
		});
		
		
		JXButton deleteButton = new JXButton("<html>DEL</html>");
		deleteButton.setName("del");
		modSymbolButton(deleteButton);
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = sumTextField.getText();
				
				if( StringUtils.stripToNull(text) != null)
					sumTextField.setText(StringUtils.chomp(text));
			}
		});
		
		JXButton deleteNumButton = new JXButton("<html>DEL</html>");
		deleteNumButton.setName("del");
		modSymbolButton(deleteNumButton);
		deleteNumButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = sumTextField.getText();
				
				if( StringUtils.stripToNull(text) != null)
					sumTextField.setText(StringUtils.chomp(text));
			}
		});
		
		
		
		JXButton leftButton = new JXButton("<html>(</html>");
		leftButton.setName("(");
		leftButton.addActionListener(buttonAction);
		this.modRegularButton(leftButton);
		
		JXButton rightButton = new JXButton("<html>)</html>");
		rightButton.setName(")");
		rightButton.addActionListener(buttonAction);
		this.modRegularButton(rightButton);
		
		radiusButton = new JXButton("<html>r</html>");
		radiusButton.setName("r");
		radiusButton.addActionListener(buttonAction);
		this.modRegularButton(radiusButton);
		
		widthButton = new JXButton("<html>w</html>");
		widthButton.addActionListener(buttonAction);
		widthButton.setName("w");
		this.modRegularButton(widthButton);
		
		heightButton = new JXButton("<html>h</html>");
		heightButton.setName("h");
		heightButton.addActionListener(buttonAction);
		this.modRegularButton(heightButton);
		
		JXButton mulButton = new JXButton("<html>x</html>");
		mulButton.setName("*");
		mulButton.addActionListener(buttonAction);
		this.modSymbolButton(mulButton);
		
		JXButton pointButton = new JXButton("<html>.</html>");
		pointButton.setName(".");
		pointButton.addActionListener(buttonAction);
		this.modSymbolButton(pointButton);
		
		
		JXButton minusButton = new JXButton("<html>-</html>");
		minusButton.setName("-");
		minusButton.addActionListener(buttonAction);
		this.modSymbolButton(minusButton);
		
		JXButton divButton = new JXButton("<html>&#247</html>");
		divButton.setName("/");
		divButton.addActionListener(buttonAction);
		this.modSymbolButton(divButton);
		
		JXButton plusButton = new JXButton("<html>+</html>");
		plusButton.setName("+");
		plusButton.addActionListener(buttonAction);
		this.modSymbolButton(plusButton);
		
		JXButton eb = new JXButton("<html>=</html>");
		eb.setName("=");
		eb.putClientProperty(UIUtils.TYPE, type);
		setEqualsButton(eb);
		eb.setBackgroundPainter(UIUtils.getEqualButtonPainter());
		modSymbolButton(eb);
		
		JXButton enterButton = new JXButton("<html>&#8629</html>");
		enterButton.setName("=");
		enterButton.putClientProperty(UIUtils.TYPE, type);
		enterButton.setBackgroundPainter(UIUtils.getEqualButtonPainter());
		modSymbolButton(enterButton);
		
		
		
		
		JXButton fourButton = new JXButton("<html>4</html>");
		fourButton.setName("4");
		fourButton.addActionListener(buttonAction);
		this.modNumberButton(fourButton);
		
		JXButton fiveButton = new JXButton("<html>5</html>");
		fiveButton.setName("5");
		fiveButton.addActionListener(buttonAction);
		this.modNumberButton(fiveButton);
		
		JXButton threeButton = new JXButton("<html>3</html>");
		threeButton.setName("3");
		threeButton.addActionListener(buttonAction);
		this.modNumberButton(threeButton);
		
		JXButton nineButton = new JXButton("<html>9</html>");
		nineButton.setName("9");
		nineButton.addActionListener(buttonAction);
		this.modNumberButton(nineButton);
		
		JXButton oneButton = new JXButton("<html>1</html>");
		oneButton.setName("1");
		oneButton.addActionListener(buttonAction);
		this.modNumberButton(oneButton);
		
		JXButton twoButton = new JXButton("<html>2</html>");
		twoButton.setName("2");
		twoButton.addActionListener(buttonAction);
		this.modNumberButton(twoButton);
		
		JXButton eightButton = new JXButton("<html>8</html>");
		eightButton.setName("8");
		eightButton.addActionListener(buttonAction);
		this.modNumberButton(eightButton);
		
		JXButton sixButton = new JXButton("<html>6</html>");
		sixButton.setName("6");
		sixButton.addActionListener(buttonAction);
		this.modNumberButton(sixButton);
		
		JXButton zeroButton = new JXButton("<html>0</html>");
		zeroButton.setName("0");
		zeroButton.addActionListener(buttonAction);
		this.modNumberButton(zeroButton);
		
		JXButton sevenButton = new JXButton("<html>7</html>");
		sevenButton.setName("7");
		sevenButton.addActionListener(buttonAction);
		this.modNumberButton(sevenButton);
		
		
		JXPanel buttonPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		//top row
		
		
		
		buttonPanel.add(piButton);
		buttonPanel.add(sqButton);
		buttonPanel.add(sqrtButton);
		buttonPanel.add(clearButton,"wrap");
		
		
		buttonPanel.add(cbrtButton);
		buttonPanel.add(minusButton);
		buttonPanel.add(plusButton);
		buttonPanel.add(deleteButton ,"wrap");
		
		
		
		buttonPanel.add(mulButton);
		buttonPanel.add(divButton);
		buttonPanel.add(leftButton);
		buttonPanel.add(rightButton, "wrap");
		
		buttonPanel.add(radiusButton);
		buttonPanel.add(widthButton);
		buttonPanel.add(heightButton);
		buttonPanel.add(eb,"wrap");
		
		buttonPanel.setOpaque(false);
		eqPanel.add(buttonPanel);
		
		
		JXPanel numPanel = new JXPanel(new MigLayout("insets 2 2 2 2"));
		numPanel.setOpaque(false);
		JXTextField numField = new JXTextField("type/enter number");
		numField.putClientProperty(UIUtils.TYPE, type);
		numField.setPreferredSize(new Dimension(20,30));
		numField.setBackground(Color.white);
		numField.setBorder(new LineBorder(Color.BLACK, 1));
		numField.setText("Formula");
		numField.setOpaque(true);
		
		
		
		
		 numPanel.add(numField,"growx, wrap");
		 JXPanel numButtonPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		 
		 
		 
		 numButtonPanel.add(nineButton);
		 numButtonPanel.add(eightButton);
		 numButtonPanel.add(sevenButton);
		
		numButtonPanel.add(clearNumButton,"wrap");
		
		 numButtonPanel.add(sixButton);
		numButtonPanel.add(fiveButton);
		numButtonPanel.add(fourButton);
		numButtonPanel.add(deleteNumButton,"wrap");
		
		numButtonPanel.add(threeButton);
		numButtonPanel.add(twoButton);
		numButtonPanel.add(oneButton);
		numButtonPanel.add(zeroButton, "wrap");
		
		numButtonPanel.add(pointButton);
		numButtonPanel.add(enterButton,"growx, span 3 1, wrap");
		numButtonPanel.setOpaque(false);
//		buttonPanel.add(oneButton);
//		buttonPanel.add(eb, "growy, span 1 2, wrap");
//		//fourth
//		buttonPanel.add(zeroButton, "growx, span 3");
//		buttonPanel.add(pointButton);
////		buttonPanel.add(eb, "span 1 2");
//		
//		eb.setPreferredSize(new Dimension(40, eb.getPreferredSize().height));
		
	
		numPanel.add(numButtonPanel);
		this.setBackgroundPainter(UIUtils.getCalcBackgroundPainter());
		this.add(eqPanel, "top");
		this.add(numPanel, "top");
	}

	public void modSymbolButton(JXButton sButton) {
		sButton.setOpaque(true);
		
		if( !sButton.getName().equals("="))
			sButton.setBackgroundPainter(UIUtils.getSymbolButtonPainter());
		
		sButton.setForeground(Color.WHITE);
		sButton.setBorderPainted(true);
		sButton.setBorder(new LineBorder(Color.WHITE, 1));
		sButton.setRolloverEnabled(false);
		this.modButton(sButton);
	}
	
	public void modRegularButton(JXButton regularButton) {
		regularButton.setForeground(Color.RED);
		regularButton.setBackgroundPainter(UIUtils.getSymbolButtonPainter());
		regularButton.setBorderPainted(true);
		regularButton.setBorder(new LineBorder(Color.WHITE, 1));
		this.modButton(regularButton);
	}
	
	public void modNumberButton(JXButton numberButton ){
		numberButton.setBackgroundPainter(UIUtils.getNumButtonPainter());
		numberButton.setForeground(Color.WHITE);
		numberButton.setBorderPainted(true);
		numberButton.setBorder(new LineBorder(Color.WHITE, 1));
		
		if( !numberButton.getName().equals("0"));
			this.modButton(numberButton);
	}

	private void modButton(JXButton button) {
		button.setFont(buttonFont);
		button.setPreferredSize(new Dimension(40, 30));
	}


	public void setSumTextField(JXTextField sumTextField) {
		this.sumTextField = sumTextField;
	}


	public JXLabel getSumTextField() {
		return textField;
	}


	public void setEqualsButton(JXButton equalsButton) {
		this.equalsButton = equalsButton;
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
			
			expressionModel.addExpression(button.getName());
			
			log.info("new exp to display " + expressionModel.getExpressionHTML());
			log.info("new exp to EVAL " + expressionModel.getExpressionEval());
			
			
			Evaluator evaluator = new Evaluator();
			try {
				String result = evaluator.evaluate(expressionModel.getExpressionEval());
				log.info("result" +result );
			} catch (EvaluationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			textField.setText(expressionModel.getExpressionHTML());
//			sumTextField.setText(sumTextField.getText() + button.getName());
			
		}
	};
	

	
	
}
