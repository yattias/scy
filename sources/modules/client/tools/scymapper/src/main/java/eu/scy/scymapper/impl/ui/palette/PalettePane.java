package eu.scy.scymapper.impl.ui.palette;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 13:24:59
 */
public class PalettePane extends JPanel {

	public PalettePane() {
		setBorder(new TitledBorder("Palette"));
		setLayout(new BorderLayout());
	}
}
