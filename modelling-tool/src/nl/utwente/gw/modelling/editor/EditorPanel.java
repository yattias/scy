package nl.utwente.gw.modelling.editor;
import colab.um.draw.JdObject;
import colab.um.tools.JTools;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

import nl.utwente.gw.modelling.model.Model;

public class EditorPanel extends JPanel {
  
	private static final long serialVersionUID = -1203178692128313488L;
	private Model dModel;
  private Rectangle rDrag = null;
  private int startX, startY;   // The starting position of the mouse.
  public javax.swing.Timer rtimer; // timer
  private String tip = null;
  private int tip_x,tip_y;
  private Font tip_font = new Font("Arial", Font.PLAIN, 10); // label font  

public EditorPanel() {
    super();
    setLayout(null);
    setModel(null);
    // tooltip messages timer
    class JDTAP implements java.awt.event.ActionListener {
      public void actionPerformed(ActionEvent evt) {
        jmsTimerActionPerformed(evt);
      }
    }    
    rtimer = new javax.swing.Timer(2000, new JDTAP());
  }
  //---------------------------------------------------------------------------
  public void setModel(Model m) {
    dModel = m;
    if (dModel==null) {
      setBackground(Color.gray);
      setForeground(Color.gray);
    } else {
      setBackground(m.getBackground());
      setForeground(m.getForeground());
      }
    repaint();
  }
  //-------------------------------------------------------------------------
  @Override
public Dimension getPreferredSize() {
    return JTools.getSysResourceSize("EditorCanvas");
  }
  //-------------------------------------------------------------------------
  // paint
  //-------------------------------------------------------------------------
  @Override
public void paintComponent(Graphics g) {
    clear(g);
    if (dModel==null) return;
    Graphics2D g2d = (Graphics2D) g;
    //AffineTransform atb = g2d.getTransform();
    //g2d.scale(2,2);
    for (JdObject obj: dModel.getObjects().values()) {
      obj.draw(g2d);
    }
    //g2d.setTransform(atb);
    if (tip!=null) {
      g2d.setFont(tip_font);
      FontMetrics fm = g2d.getFontMetrics();;
      int w = fm.stringWidth(tip) + 8;
      int h = fm.getHeight() + 4;
      //System.out.println("drawTip " + tip + "x=" + String.valueOf(tip_x) + ", y=" + String.valueOf(tip_y));
      g2d.setColor(Color.black);
      g2d.setBackground(new Color(255, 255, 224));  // light yellow
      g2d.clearRect(tip_x - w/2, tip_y - h/2, w, h);
      g2d.drawRect(tip_x - w/2, tip_y - h/2, w, h);
      g2d.drawString(tip, tip_x - w/2 + 4, tip_y + h/2 - 3); // must be -2, but it looks nice with -3
    }
  }
  //-------------------------------------------------------------------------
  public void repaintObject(JdObject o) {
    Graphics2D g = (Graphics2D) this.getGraphics();
    o.draw(g);
  }
  //-------------------------------------------------------------------------
  protected void clear(Graphics g) {
    super.paintComponent(g);
  }
  //-------------------------------------------------------------------------
  // paint rect
  //-------------------------------------------------------------------------
  public void startRect(int x, int y) {
    startX = x;
    startY = y;
    rDrag = new Rectangle(x,y,0,0);
  }
  //-------------------------------------------------------------------------
  public void extendRect(int x2, int y2) {
    if (rDrag!=null) {
      Graphics g = this.getGraphics();
      g.setXORMode(this.getBackground());
      g.drawRoundRect(rDrag.x, rDrag.y, rDrag.width, rDrag.height,2,2);
      int x,y,w,h;
      if (startX>x2) {
        x = x2;
        w = startX - x2;
      } else {
        x = startX;
        w = x2 - startX;
        }
      if (startY>y2) {
        y = y2;
        h = startY - y2;
      } else {
        y = startY;
        h = y2 - startY;
        }
      g.drawRoundRect(x,y,w,h,2,2);
      rDrag.setBounds(x,y,w,h);
      }
  }
  //-------------------------------------------------------------------------
  public Rectangle stopRect() {
    Rectangle r = null;
    if (rDrag!=null) {
      Graphics g = this.getGraphics();
      g.setXORMode(this.getBackground());
      g.drawRoundRect(rDrag.x, rDrag.y, rDrag.width, rDrag.height,2,2);
      g.setPaintMode();
      r = new Rectangle(rDrag);
      rDrag=null;
      }
    return r;
  }
  //-------------------------------------------------------------------------
  // Tooltip
  //-------------------------------------------------------------------------
  public void setTip(String s, int x, int y) {
    if (s==null || s.length()<1) return;
    if (!s.equals(tip) && rDrag==null) {
      tip = s;
      tip_x = x;
      tip_y = y;
      //System.out.println("setTip " + tip + "x=" + String.valueOf(tip_x) + ", y=" + String.valueOf(tip_y));
      this.repaint();
      rtimer.start();
    }
  }
  //-------------------------------------------------------------------------
  public void clearTip() {
    if (tip==null) return;
    //System.out.println("clearTip " + tip + "x=" + String.valueOf(tip_x) + ", y=" + String.valueOf(tip_y));
    tip = null;
    tip_x = -1;
    tip_y = -1;
    if (rDrag==null) {
      this.repaint();
    }
  }
  //-------------------------------------------------------------------------
  /**
   *  Timer action to clear status message.
   *
   *@param  ActionEvent  the <code>ActionEvent</code> timer parameter
   */
  public void jmsTimerActionPerformed(ActionEvent e) {
    clearTip();
    rtimer.stop();
  }  
}
