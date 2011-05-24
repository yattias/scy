package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.paint.Color;

public enum AgendaEntryState {

    ENABLED, ACTIVE, COMPLETED, REVOKED;

    public Color getColor() {
        switch (this) {
            case ACTIVE:
                return Color.$GREEN;
            case ENABLED:
                return Color.$GRAY;
            case COMPLETED:
                return Color.$BLUE;
            case REVOKED:
                return Color.$RED;
        }
        return null;
    }

}
