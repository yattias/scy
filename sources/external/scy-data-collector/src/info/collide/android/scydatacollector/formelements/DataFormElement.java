package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import android.view.View;

public class DataFormElement {

    private DataFormElementModel dfem;

    private DataFormElementView dfev;

    private DataFormElementController dfec;

    public DataFormElement(DataFormElementModel dfem, DataCollectorFormActivity application, int i) {
        this.dfem = dfem;

        this.dfec = new DataFormElementController(dfem, application);

        switch (dfem.getType()) {
            case TEXT:
                this.dfev = new DataFormElementTextView(dfec, dfem, application, i);
                break;
            case COUNTER:
                this.dfev = new DataFormElementCounterView(dfec, dfem, application, i);
                break;
            case GPS:
                this.dfev = new DataFormElementGPSView(dfec, dfem, application, i);
                break;
            case IMAGE:
                this.dfev = new DataFormElementImageView(dfec, dfem, application, i);
                break;
            case NUMBER:
                this.dfev = new DataFormElementNumberView(dfec, dfem, application, i);
                break;
            case DATE:
                this.dfev = new DataFormElementDateView(dfec, dfem, application, i);
                break;
            case TIME:
                this.dfev = new DataFormElementTimeView(dfec, dfem, application, i);
                break;
            case VOICE:
                this.dfev = new DataFormElementVoiceView(dfec, dfem, application, i);
                break;
            default:
                this.dfev = new DataFormElementView(dfem, application, dfec);
                break;
        }
    }

    public View getView() {
        return this.dfev;
    }

}
