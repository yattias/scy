/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.Object;

/**
 *
 * @author pg
 */
public class GPSTypeParser {
    static float[] getCoords(byte[] data) {
        float[] gpsData = new float[2];
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            Object[] temp = (Object[]) inputStream.readObject();
            gpsData[0] = Float.parseFloat((String) temp[0]);
            gpsData[1] = Float.parseFloat((String) temp[1]);
        }
        catch(Exception e) {
            // System.out.println("whoops.. failure");
            e.printStackTrace();
            
        }
        return gpsData;
    }

}
