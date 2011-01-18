package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;

public class DataFormElementViewFactory {

    public static DataFormElementView createView(DataFormElementModel elementModel, DataCollectorFormActivity application, int i) {

        switch (elementModel.getType()) {
            case TEXT:
                return new DataFormElementTextView(elementModel, application, i);
            case COUNTER:
                return new DataFormElementCounterView(elementModel, application, i);
            case GPS:
                return new DataFormElementGPSView(elementModel, application, i);
            case IMAGE:
                return new DataFormElementImageView(elementModel, application, i);
            case NUMBER:
                return new DataFormElementNumberView(elementModel, application, i);
            case DATE:
                return new DataFormElementDateView(elementModel, application, i);
            case TIME:
                return new DataFormElementTimeView(elementModel, application, i);
            case VOICE:
                return new DataFormElementVoiceView(elementModel, application, i);
        }
        return null;
    }
}
