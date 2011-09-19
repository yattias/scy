package info.collide.android.scydatacollector;

import java.util.Hashtable;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DataCollectorFormsDataListAdapter extends BaseAdapter {

    private Hashtable<Integer, DataCollectorFormModel> _data = new Hashtable<Integer, DataCollectorFormModel>();

    private int _selectedIndex;

    private DataCollectorFormOverviewActivity _context;

    private DataCollectorContentProvider dccp;

    public DataCollectorFormsDataListAdapter(DataCollectorFormOverviewActivity context, ListView listview) {

        // save the activity/context ref
        _context = context;
        _selectedIndex = -1;
        // bind this model (and cell renderer) to the listview
        // listview.setAdapter(this);

        bindListViewListener(listview);

        // load some data into the model
        dccp = new DataCollectorContentProvider(context);
        getFormData();
    }

    private void bindListViewListener(ListView listview) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                setSelected(position);
            }
        });
    }

    private void getFormData() {
        _data.clear();
        Cursor c = dccp.getAllForms();
        if (c.moveToFirst()) {
            do {
                DataCollectorFormModel dcfm = new DataCollectorFormModel();
                int formid = c.getInt(c.getColumnIndex(DataCollectorContentProvider.KEY_FORMID));
                dccp.getDCFM(_context, formid, dcfm);
                _data.put(formid, dcfm);
            } while (c.moveToNext());
        }
        c.close();
    }

    public int getCount() {
        return _data.size();
    }

    public Object getItem(int position) {
        return _data.values().toArray()[position];
    }

    public long getItemId(int position) {
        Integer[] keys = _data.keySet().toArray(new Integer[_data.keySet().size()]);
        return keys[position];
    }

    public View getView(int index, View cellRenderer, ViewGroup parent) {
        CellRendererView cellRendererView = null;

        if (cellRenderer == null) {
            // create the cell renderer
            // Log.i(getClass().getSimpleName(),
            // "creating a CellRendererView object");
            cellRendererView = new CellRendererView();
        } else {
            cellRendererView = (CellRendererView) cellRenderer;
        }

        // update the cell renderer, and handle selection state
        cellRendererView.display(index, _selectedIndex == index);

        cellRendererView.setBackgroundResource(R.drawable.gradient);

        return cellRendererView;

    }

    public void setSelected(int index) {

        if (index == -1) {
            // unselected
        } else {
            // selected index...
        }

        _selectedIndex = index;

        // notify the model that the data has changed, need to update the view
        notifyDataSetChanged();

        // Log.i(getClass().getSimpleName(),
        // "updating _selectionIndex with index and firing model-change-event: index="
        // + index);
    }

    /**
     * this class is responsible for rendering the data in the model, given the selection state
     */
    private class CellRendererView extends TableLayout {

        // ui stuff
        private TextView _lblFormTitle;

        // private ImageView _lblIcon;
        private TextView _lblFormDescription;

        private Button _btnOpenForm;

        private Button _btnDeleteForm;

        // Button zum testen
        private Button _btnSaveElo;

        public CellRendererView() {

            super(_context);

            _createUI();

        }

        /** create the ui components */
        private void _createUI() {

            // make the 2nd col growable/wrappable
            setColumnShrinkable(1, true);
            setColumnStretchable(1, true);

            // set the padding
            setPadding(10, 10, 10, 10);

            // single row that holds icon/flag & name
            TableRow row = new TableRow(_context);
            {
                _lblFormTitle = new TextView(_context);

                // zum testen
                _btnSaveElo = new Button(_context);
                _btnSaveElo.setText(getResources().getString(R.string.btnSaveElo));

                _btnSaveElo.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        new AsyncTask<Void, Void, Boolean>() {

                            @Override
                            protected void onPreExecute() {
                                _context.showDialog(DataCollectorFormOverviewActivity.PROGRESS_DIALOG);
                            }

                            @Override
                            protected Boolean doInBackground(Void... params) {
                                WebServicesController dcwc = new WebServicesController(_context);
                                try {
                                    dcwc.postFormJSON((DataCollectorFormModel) getItem(_selectedIndex));
                                    return true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            @Override
                            protected void onPostExecute(Boolean result) {
                                _context.dismissDialog(DataCollectorFormOverviewActivity.PROGRESS_DIALOG);
                                if (result) {
                                    Toast.makeText(_context, R.string.storeFormResultMessagePositive, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(_context, R.string.storeFormResultMessageNegative, Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    }
                });
                _btnOpenForm = new Button(_context);
                _btnOpenForm.setText(getResources().getString(R.string.btnOpenForm));

                _btnOpenForm.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub

                        Intent tki = new Intent();
                        tki.setClass(_context, DataCollectorFormActivity.class);
                        long dataform = getItemId(_selectedIndex);

                        tki.putExtra("dataform", dataform);
                        _context.startActivityForResult(tki, _context.REFRESH_VIEW);

                    }
                });

                _btnDeleteForm = new Button(_context);
                _btnDeleteForm.setText(getResources().getString(R.string.btnDeleteForm));

                _btnDeleteForm.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        MessageDialog mdDeleteForm = new MessageDialog(_context);
                        android.content.DialogInterface.OnClickListener oclYes = new android.content.DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dccp = new DataCollectorContentProvider(_context);
                                dccp.deleteForm(getItemId(_selectedIndex));

                                refreshView();
                                notifyDataSetChanged();
                            }
                        };
                        mdDeleteForm.createYesNoDialog(getResources().getString(R.string.msgDeleteForm), oclYes, getResources().getString(R.string.YES), null, getResources().getString(R.string.NO));
                    }
                });

                // row.addView(_lblIcon);
                row.addView(_lblFormTitle);
                // row.addView(_btnOpenForm);
            }

            // create the 2nd row with: description
            {
                _lblFormDescription = new TextView(_context);
                // LayoutUtils.Layout.WidthFill_HeightWrap.applyTableLayoutParams(_lblDescription);
                _lblFormDescription.setPadding(10, 10, 10, 10);
            }

            // add the rows to the table
            {
                addView(row);
                addView(_lblFormDescription);
                addView(_btnOpenForm);
                addView(_btnDeleteForm);
                addView(_btnSaveElo);
            }

            // Log.i(getClass().getSimpleName(), "CellRendererView created");

        }

        /** update the views with the data corresponding to selection index */
        public void display(int index, boolean selected) {

            // String zip = getItem(index).toString();
            // Hashtable<String, String> weatherForZip = _data.get(zip);

            // Log.i(getClass().getSimpleName(), "row[" + index + "] = " +
            // weatherForZip.toString());

            // String temp = weatherForZip.get("temperature");

            // String icon = weatherForZip.get("icon");
            // int iconId = ResourceUtils.getResourceIdForDrawable(_context,
            // "com.developerlife", "w" + icon);

            // String humidity = weatherForZip.get("humidity");

            // _lblFormTitle.setText("bla");

            _lblFormTitle.setText(((DataCollectorFormModel) getItem(index)).getTitle());
            // _lblIcon.setImageResource(iconId);
            _lblFormDescription.setText(((DataCollectorFormModel) getItem(index)).getDescription());

            // Log.i(getClass().getSimpleName(), "rendering index:" + index);

            if (selected) {
                // _lblFormTitle.setText(((DataCollectorFormModel)
                // getItem(index)).getTitle() + " SELECTED");
                _lblFormDescription.setVisibility(View.VISIBLE);
                _btnOpenForm.setVisibility(View.VISIBLE);
                // Log.i(getClass().getSimpleName(),
                _btnDeleteForm.setVisibility(View.VISIBLE);
                _btnSaveElo.setVisibility(View.VISIBLE);
                // "hiding descripton for index:" + index);
            } else {
                _lblFormDescription.setVisibility(View.GONE);
                _btnOpenForm.setVisibility(View.GONE);
                _btnDeleteForm.setVisibility(View.GONE);
                _btnSaveElo.setVisibility(View.GONE);
                // Log.i(getClass().getSimpleName(),
                // "showing description for index:" + index);
            }

        }

    }

    public void refreshView() {
        getFormData();
    }

}

// }
