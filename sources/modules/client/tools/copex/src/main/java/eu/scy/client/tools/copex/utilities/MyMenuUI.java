/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.basic.BasicMenuUI;





/**
 * look and feel menu
 *
 * @author Marjolaine
 */
public class MyMenuUI extends BasicMenuUI{

    Color bg;
    public MyMenuUI(Color bg) {
        this.bg = bg;
    }

    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        super.paintBackground(g, menuItem, bg);
    }

    @Override
    protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap) {
        super.paintMenuItem(g, c, checkIcon, arrowIcon, bg, foreground, defaultTextIconGap);
    }
    
}
