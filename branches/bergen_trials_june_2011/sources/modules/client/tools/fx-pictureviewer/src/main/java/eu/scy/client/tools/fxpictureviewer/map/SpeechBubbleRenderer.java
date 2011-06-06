/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxpictureviewer.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.imageio.ImageIO;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

/**
 *
 * @author pg
 */
public class SpeechBubbleRenderer implements WaypointRenderer {
    BufferedImage img = null;
    String caption = "hello world";
    private int closeX;
    private int closeY;
    public SpeechBubbleRenderer(String caption) {
        this.caption = caption;
        //
        try {
            img = ImageIO.read(getClass().getResource("speechbubble.png"));
        } catch (Exception ex) {
            // System.out.println("couldn't read speechbubble.png");
            // System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint waypoint) {
        if(img != null) {
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D myrect = null;
            String[] captions = caption.split("\n");
            int max = 0;
            int height = 0;
            for(int i = 0; i < captions.length; i++) {
                if(captions[i].length() <= 0) {
                    captions[i] = " ";
                }
                myrect = fm.getStringBounds(captions[i], g);
                if((int)myrect.getWidth() > max) {
                    max = (int) myrect.getWidth();
                }
                height = height + (int)myrect.getHeight();
            }
            //g.drawImage(img,0,-img.getHeight(),null);
            g.setStroke(new BasicStroke(1));
            GeneralPath p = new GeneralPath();
          /*  p.moveTo(0, 0);
            p.lineTo(0, -100);
            p.lineTo(30, -100);
            p.lineTo(0,0); *
           */
            p.moveTo(0,0);
            p.lineTo(-10,-55);
            p.lineTo(-30,-55);
            p.quadTo(-50, -55, -50, -70);
            p.lineTo(-50, -height-70);
            p.quadTo(-50, -height-85, -30, -height-85);
            p.lineTo(max-35, -height-85); // top right begin
            this.closeX = max-35;
            this.closeY = -height-70;
            p.quadTo(max-20, -height-85, max-20, -height-70); //top right end
            p.lineTo(max-20, -70);
            p.quadTo(max-20, -55, max-35, -55);
            p.lineTo(20,-50);
            p.lineTo(0, 0);
            g.setColor(Color.WHITE);
            g.fill(p);
            g.setColor(Color.BLACK);
            g.draw(p);
            g.setColor(Color.WHITE);
           // g.fillRoundRect(-50, -height-70, max+15 , height+15, 20, 20);
            g.setColor(Color.BLACK);
           // g.drawRoundRect(-50, -height-70, max+15 , height+15, 20, 20);
            height = -height-70;
            g.drawString("[X]", closeX, closeY);
            for(int i = 0; i < captions.length; i++) {
                g.drawString(captions[i], -45, height);
                height = height + (int)myrect.getHeight()+2;
            }
       }
        return false;
    }

    private void prepareContent(String content) {
        content = content + "\n";
        String[] foo = content.split("\n");
        for(int i = 0; i < foo.length; i++) {
            // System.out.println(foo[i]);
        }

    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getCloseX() {
        return this.closeX;
    }

    public int getCloseY() {
        return this.closeY;
    }
}
