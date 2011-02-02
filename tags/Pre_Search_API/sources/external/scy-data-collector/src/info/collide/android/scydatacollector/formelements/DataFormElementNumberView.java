package info.collide.android.scydatacollector.formelements;

import info.collide.android.scydatacollector.DataCollectorFormActivity;
import info.collide.android.scydatacollector.DataFormElementModel;
import info.collide.android.scydatacollector.R;
import info.collide.android.scydatacollector.DataFormElementEventModel.DataFormElementEventTypes;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataFormElementNumberView extends DataFormElementView {

    private EditText textField;

    public DataFormElementNumberView(final DataFormElementModel elementModel, DataCollectorFormActivity application, int id) {
        super(elementModel, application);

        inflate(getApplication(), R.layout.numberformelement, this);
        
        TextView label = (TextView) findViewById(R.id.numberformelement_label);
        label.setWidth(super.Column1width);
        label.setText(elementModel.getTitle());

        Button btnOK = (Button) findViewById(R.id.numberformelement_okay);
        btnOK.setText("Ok");
        btnOK.setWidth(super.Column3width);
        
        textField = (EditText) findViewById(R.id.numberformelement_number_field);
        textField.setInputType(InputType.TYPE_CLASS_NUMBER);
        textField.setWidth(super.Column2width + super.Column3width);

        btnOK.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                events(DataFormElementEventTypes.ONBEFORE);
                elementModel.addStoredData(textField.getEditableText().toString().getBytes());
                events(DataFormElementEventTypes.ONAFTER);
            }
        });
        updateView(elementModel);
    }

    @Override
    protected void updateView(DataFormElementModel elementModel) {
        if (elementModel.getStoredData(elementModel.getDataList().size() - 1) != null) {
            String tmp = new String(elementModel.getStoredData(elementModel.getDataList().size() - 1));
            textField.setText(tmp);
        } else {
            textField.setText("0");
        }
    }

}
