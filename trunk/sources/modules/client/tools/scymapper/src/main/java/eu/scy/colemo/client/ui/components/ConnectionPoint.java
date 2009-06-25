package eu.scy.colemo.client.ui.components;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 18.jun.2009
 * Time: 14:14:58
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionPoint extends Component {
    private BufferedImage image;

    public ConnectionPoint(BufferedImage i) {
        image = i;
    }
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
}
