package eu.scy.mobile.toolbroker.demo.ui;
/* License
 *
 * Copyright 1994-2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *  * Redistribution in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */


import javax.microedition.lcdui.*;
import javax.microedition.media.MediaException;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import java.io.IOException;

public class CameraCanvas extends Canvas {
	private VideoControl videoControl;
	public static final Command CMD_CAPTURE = new Command("Capture", Command.OK, 0);
	private Player mediaPlayer;

	public CameraCanvas() {

		try {
			mediaPlayer = Manager.createPlayer("capture://video");
			mediaPlayer.realize();
			mediaPlayer.prefetch();

			videoControl = (VideoControl) mediaPlayer.getControl("VideoControl");
			int width = getWidth();
			int height = getHeight();

			videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
			videoControl.setDisplayLocation(2, 2);
			videoControl.setDisplaySize(width - 4, height - 4);
			mediaPlayer.start();
			videoControl.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (MediaException me) {
			try {
				videoControl.setDisplayFullScreen(true);
			}
			catch (MediaException me2) {
				me2.printStackTrace();
			}
		}
	}

	public byte[] capture() {
		DoCapture capturer = new DoCapture();
		Thread t = new Thread(capturer);
		t.start();
		try {
			t.join();
			return capturer.getCaptured();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		// Draw a green border around the VideoControl.
		g.setColor(0x00ff00);
		g.drawRect(0, 0, width - 1, height - 1);
		g.drawRect(1, 1, width - 3, height - 3);
	}
	private class DoCapture implements Runnable {

		private byte[] captured = null;

		public void run() {
			try {
				// Get the image.
				//byte[] raw = videoControl.getSnapshot(null);
				captured = videoControl.getSnapshot("encoding=jpeg");
			}
			catch (MediaException me) {
				me.printStackTrace();
			}
			finally {
				try {
					mediaPlayer.stop();
				} catch (MediaException e) {
					e.printStackTrace();
				}
				mediaPlayer.deallocate();
				mediaPlayer.close();
			}
		}

		public byte[] getCaptured() {
			return captured;
		}
	}
}
