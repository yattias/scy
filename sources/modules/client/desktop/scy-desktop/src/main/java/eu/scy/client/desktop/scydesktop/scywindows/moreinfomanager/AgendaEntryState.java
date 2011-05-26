package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.paint.Color;

public enum AgendaEntryState {

    ENABLED, ACTIVATED, COMPLETED, NEED2CHECK;

    public Color getColor() {
        switch (this) {
            case ACTIVATED:
                return Color.web("#009d12");
            case ENABLED:
                return Color.web("#7b7b7b");
            case COMPLETED:
                return Color.web("#415bdb");
            case NEED2CHECK:
                return Color.web("#c10000");
        }
        return null;
    }

}
