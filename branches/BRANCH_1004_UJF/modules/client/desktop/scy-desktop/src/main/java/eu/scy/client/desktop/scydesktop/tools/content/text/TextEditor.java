/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TextEditor.java
 *
 * Created on 29-sep-2009, 19:36:18
 */
package eu.scy.client.desktop.scydesktop.tools.content.text;

import javax.swing.JScrollBar;

/**
 *
 * @author sikken
 */
public class TextEditor extends javax.swing.JPanel
{

   /** Creates new form TextEditor */
   public TextEditor()
   {
      initComponents();
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      textScrollPane = new javax.swing.JScrollPane();
      textPane = new javax.swing.JTextPane();

      setPreferredSize(new java.awt.Dimension(300, 200));

      textScrollPane.setViewportView(textPane);

      org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(textScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(textScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
      );
   }// </editor-fold>//GEN-END:initComponents
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JTextPane textPane;
   private javax.swing.JScrollPane textScrollPane;
   // End of variables declaration//GEN-END:variables

   public void setText(String text)
   {
      textPane.setText(text);
   }

   public String getText()
   {
      return textPane.getText();
   }

   public void setEditable(boolean editable)
   {
      textPane.setEditable(editable);
   }

   public boolean isEditable()
   {
      return textPane.isEditable();
   }

   public void resetScrollbars()
   {
      resetScrollbar(textScrollPane.getHorizontalScrollBar());
      resetScrollbar(textScrollPane.getVerticalScrollBar());
   }

   private void resetScrollbar(JScrollBar scrollbar)
   {
      if (scrollbar!=null)
      {
         scrollbar.setValue(scrollbar.getMinimum());
      }
   }
}
