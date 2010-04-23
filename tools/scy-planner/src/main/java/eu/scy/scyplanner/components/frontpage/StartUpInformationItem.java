package eu.scy.scyplanner.components.frontpage;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.MutableAttributeSet;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 09:51:27
 */
public class StartUpInformationItem extends JPanel {
    private final JLabel heading = new JLabel();
    private JTextPane description = new JTextPane();
    private final static String EMPTY_STRING = "";

    public StartUpInformationItem(String title, String descritpion, ImageIcon icon) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(SCYPlannerApplicationManager.getDefaultBorderSize(), SCYPlannerApplicationManager.getDefaultBorderSize(), 22, SCYPlannerApplicationManager.getDefaultBorderSize()));
        heading.setFont(StartUpMenuItem.DEFAULT_STARTUP_PANEL_FONT);

        setTitle(title);

        setOpaque(false);

        JPanel nameAndDescriptionPanel = new JPanel();
        nameAndDescriptionPanel.setLayout(new BorderLayout());
        nameAndDescriptionPanel.setOpaque(false);

        description.setContentType("text/html");
        description.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        description.setEditable(false);
        description.setOpaque(false);
        description.setFocusable(false);
        setDescription(descritpion);

        nameAndDescriptionPanel.add(BorderLayout.NORTH, heading);
        nameAndDescriptionPanel.add(BorderLayout.CENTER, description);
        add(BorderLayout.CENTER, nameAndDescriptionPanel);
        add(BorderLayout.WEST, new JLabel(icon));
    }

    public void setTitle(String title) {
        if (title != null) {
            heading.setText("<html>" + title + "</html>");
            heading.setForeground(Color.BLACK);
        } else {
            heading.setText(EMPTY_STRING);
        }
    }

    public void setDescription(String descritpion) {
        if (descritpion != null) {
            description.setText(descritpion);
            setJTextPaneFont(description, new Font("Dialog", Font.PLAIN, 12));
        } else {
            description.setText(EMPTY_STRING);
        }
    }

    public static void setJTextPaneFont(JTextPane jtp, Font font) {
        MutableAttributeSet attrs = jtp.getInputAttributes();

        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        /*StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);*/

        StyledDocument doc = jtp.getStyledDocument();

        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }
}