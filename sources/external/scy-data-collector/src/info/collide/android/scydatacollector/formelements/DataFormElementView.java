package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.DataFormElementModel.DataFormElementTypes;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class DataFormElementView extends LinearLayout {

    private DataFormElementModel dfem;

    public final static int IMAGE_TAKEN = 1;

    private static DataCollectorFormActivity application;

    private DataFormElementController _dfec;

    public int Column1width = 80;

    public int Column2width = 55;

    public int Column3width = 55;

    public int Column4width = 55;

    public int Column5width = 70;

    public int RowMaxHeight = 50;

    private String _formTitle;

    public DataFormElementView(DataFormElementModel dfem, DataCollectorFormActivity application, DataFormElementController dfec) {
        super(application);

        Display display = ((WindowManager) application.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        if (width <= 320) {
            Column1width = 80;
            Column2width = 55;
            Column3width = 55;
            Column4width = 55;
            Column5width = 70;
            // RowMaxHeight = 50;
        }
        if (width > 320) {
            Column1width = (int) (80 * 1.5);
            Column2width = (int) (55 * 1.5);
            Column3width = (int) (55 * 1.5);
            Column4width = (int) (55 * 1.5);
            Column5width = (int) (70 * 1.5);
            // RowMaxHeight = (int) (50 * 1.5);
        }
        DataFormElementView.setApplication(application);
        _dfec = dfec;
        this.setDfem(dfem);
        // _formTitle = formTitle;
    }

    public static void setApplication(DataCollectorFormActivity application) {
        DataFormElementView.application = application;
    }

    public static DataCollectorFormActivity getApplication() {
        return application;
    }

    public void setDfem(DataFormElementModel dfem) {
        this.dfem = dfem;
    }

    public DataFormElementModel getDfem() {
        return dfem;
    }

    public DataFormElementTypes getDataFormElementViewType() {
        return getDfem().getType();
    }

}