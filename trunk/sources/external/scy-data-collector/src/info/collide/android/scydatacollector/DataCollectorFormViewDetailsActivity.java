package info.collide.android.scydatacollector;

import info.collide.android.scydatacollector.formelements.DataFormElementGPSView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class DataCollectorFormViewDetailsActivity extends Activity implements Observer {

    private static ImageButton _btnFertig;

    private static ImageButton _btnPrevious;

    private static ImageButton _btnNext;

    private static ImageButton _btnDelete;

    private static int _viewPos = 0;

    // private static Activity _application;
    private static DataFormElementModel _dfem;

    private static int _elementPos;

    private static String _formTitle;

    private static DataCollectorFormViewDetailsActivity _thisView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details);

        Bundle extras = getIntent().getExtras();

        DataFormElementModel dfem = (DataFormElementModel) extras.getSerializable("dfem");
        _elementPos = extras.getInt("elementpos");
        _formTitle = extras.getString("formtitle");
        init(dfem);
    }

    public void init(DataFormElementModel dfem) {

        // _application = this.getApplication();
        _dfem = dfem;
        // _dfem.addObserver(this);
        _thisView = this;
        _viewPos = dfem.getDataList().size() - 1;

        update(null, null);
    }

    public static LinearLayout createLastRows(String caption, final Context activity) {
        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);

        Display display = _thisView.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        _btnPrevious = new ImageButton(activity);
        _btnNext = new ImageButton(activity);
        _btnFertig = new ImageButton(activity);
        _btnDelete = new ImageButton(activity);

        // _btnDelete.setText("Lšschen");
        _btnDelete.setImageResource(R.drawable.remove);
        _btnDelete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                MessageDialog mdDelete = new MessageDialog(_thisView);
                android.content.DialogInterface.OnClickListener oclYes = new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<byte[]> datalist = _dfem.getDataList();
                        datalist.remove(_viewPos);
                        for (DataFormElementEventModel dfeem : _dfem.getEvents()) {
                            ArrayList<byte[]> eventdatalist = dfeem.getDataList();
                            if (_viewPos < eventdatalist.size()) {
                                eventdatalist.remove(_viewPos);
                                dfeem.setDataList(eventdatalist);
                            }
                        }
                        _dfem.setDataList(datalist);
                        _thisView.update(null, null);
                    }
                };
                mdDelete.createYesNoDialog(activity.getResources().getString(R.string.msgDeleteDataSet), oclYes, activity.getResources().getString(R.string.YES), null, activity.getResources().getString(R.string.NO));
            }
        });

        // _btnPrevious.setText("<");
        _btnPrevious.setImageResource(R.drawable.arrow_state_grey_left);
        _btnPrevious.setMinimumWidth(width / 2);
        _btnNext.setMinimumWidth(width / 2);
        // _btnNext.setText(">");
        _btnNext.setImageResource(R.drawable.arrow_state_grey_right);

        // _btnFertig.setText(caption);
        _btnFertig.setImageResource(R.drawable.check);

        if (_viewPos > 0) {
            _btnPrevious.setEnabled(true);
        } else
            _btnPrevious.setEnabled(false);

        if (_viewPos < _dfem.getDataList().size() - 1) {
            _btnNext.setEnabled(true);
        } else
            _btnNext.setEnabled(false);

        _btnPrevious.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (_viewPos > 0) {
                    _viewPos -= 1;
                    _thisView.update(null, null);

                }
            }
        });

        _btnNext.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (_viewPos < _dfem.getDataList().size() - 1) {
                    _viewPos += 1;
                    _thisView.update(null, null);
                }
            }
        });

        _btnFertig.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                finishWithResult();
            }

            /**
			 * 
			 */
        });

        LinearLayout ll2 = new LinearLayout(activity);
        ll2.setOrientation(LinearLayout.HORIZONTAL);

        ll2.addView(_btnPrevious);
        ll2.addView(_btnNext);

        ll.setGravity(Gravity.CENTER);

        ll.addView(ll2);
        ll.addView(_btnDelete);
        ll.addView(_btnFertig);

        android.view.ViewGroup.LayoutParams params = _btnFertig.getLayoutParams();
        params.width = LayoutParams.FILL_PARENT;

        return ll;
    }

    private static void finishWithResult() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("dfem", _dfem);
        bundle.putBoolean("fromdetail", true);
        bundle.putInt("elementpos", _elementPos);
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        _thisView.setResult(RESULT_OK, mIntent);
        _thisView.finish();
    }

    public void addbtnFertigListener(OnClickListener ocl) {
        _btnFertig.setOnClickListener(ocl);
    }

    public void update(Observable observable, Object data) {
        TableLayout table = (TableLayout) _thisView.findViewById(R.id.TableLayoutDetails01);

        table.removeAllViews();

        if (_dfem.getDataList().size() > 0) {
            if (_viewPos > _dfem.getDataList().size() - 1)
                _viewPos = _dfem.getDataList().size() - 1;

            TextView tvLabelData = new TextView(_thisView);
            tvLabelData.setText(_dfem.getTitle() + ":");
            table.addView(tvLabelData);
            switch (_dfem.getType()) {
                case IMAGE:

                    ImageView iv = new ImageView(_thisView);

                    byte[] buf = _dfem.getStoredData(_viewPos);
                    Bitmap bm = BitmapFactory.decodeByteArray(buf, 0, buf.length);

                    Bitmap icon = Bitmap.createBitmap(bm);

                    iv.setImageBitmap(icon);

                    table.addView(iv);

                    break;
                case DATE:
                    TextView tvDate = new TextView(_thisView);
                    String tmpDataString = new String(_dfem.getStoredData(_viewPos));
                    tvDate.setText(tmpDataString);
                    table.addView(tvDate);

                    break;
                case TEXT:
                    final EditText etText = new EditText(_thisView);
                    String tmpTextString = new String(_dfem.getStoredData(_viewPos));
                    etText.setText(tmpTextString);
                    // tvText.set
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();

                    etText.setWidth(width);
                    etText.setHeight(300);
                    etText.setGravity(Gravity.TOP & Gravity.LEFT);
                    etText.setOnKeyListener(new OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            _dfem.setStoredData(etText.getText().toString().getBytes(), _viewPos);
                            return false;
                        }
                    });

                    table.addView(etText);
                    break;
                case COUNTER:
                    final TextView tvCounter = new TextView(_thisView);
                    tvCounter.setText(new String(_dfem.getStoredData(_viewPos)));
                    table.addView(tvCounter);
                    break;
                case VOICE:
                    final AudioPlayer ap = new AudioPlayer();
                    final AudioRecorder ar = new AudioRecorder(_dfem);

                    final ImageButton btnRec = new ImageButton(getApplication());
                    final ImageButton btnStp = new ImageButton(getApplication());
                    final ImageButton btnPly = new ImageButton(getApplication());

                    // TextView label.setText(dfem.getTitle());

                    btnRec.setImageResource(R.drawable.ic_btn_speak_now);
                    btnStp.setImageResource(R.drawable.media_controls_dark_stop);
                    btnPly.setImageResource(R.drawable.media_controls_dark_play);
                    // btnRec.setText("o");
                    // btnStp.setText("#");
                    // btnPly.setText(">");

                    btnStp.setEnabled(false);

                    if (_dfem.getStoredData(_dfem.getDataList().size() - 1) != null) {

                    } else {
                        btnPly.setEnabled(false);
                    }
                    // btnRec.setWidth(super.Column3width - 20);
                    // btnStp.setWidth(super.Column3width - 20);
                    // btnPly.setWidth(super.Column3width - 20);

                    btnRec.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {

                            try {
                                if (ar.start()) {
                                    Toast.makeText(getApplication(), getResources().getString(R.string.tstRecordStart), 100).show();

                                    btnStp.setEnabled(true);
                                    btnRec.setEnabled(false);
                                } else
                                    Toast.makeText(getApplication(), getResources().getString(R.string.tstCantStartRecording), 100).show();

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    });
                    btnStp.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            try {
                                if (ar.isRecording()) {
                                    ar.stop();
                                    _dfem.setStoredData(ar.getBytesFromFile(), _viewPos);

                                    btnRec.setEnabled(true);
                                    btnPly.setEnabled(true);
                                    btnStp.setEnabled(false);
                                    // ap.refresh();
                                    Toast.makeText(getApplication(), getResources().getString(R.string.tstRecordStop), 10).show();

                                } else if (ap != null && ap.isPlaying()) {
                                    Toast.makeText(getApplication(), getResources().getString(R.string.tstPlaybackStop), 10).show();
                                    ap.stop();
                                    btnPly.setEnabled(true);

                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                    btnPly.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            try {
                                if (_dfem.getStoredData(_viewPos) != null)
                                    ar.writeBytesToFile(_dfem.getStoredData(_viewPos));
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            final AudioPlayer ap = new AudioPlayer();
                            ap.play();
                            btnPly.setEnabled(false);
                            btnRec.setEnabled(false);
                            btnStp.setEnabled(true);
                            Toast.makeText(getApplication(), getResources().getString(R.string.tstPlay), 10).show();

                            ap.setOnCompletitionListener(new OnCompletionListener() {

                                public void onCompletion(MediaPlayer mp) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(getApplication(), getResources().getString(R.string.tstPlayDONE), 10).show();
                                    btnPly.setEnabled(true);
                                    btnRec.setEnabled(true);
                                    btnStp.setEnabled(false);
                                    ap.release();
                                }
                            });

                        }
                    });
                    // addView(label);

                    android.widget.LinearLayout.LayoutParams lp = new LayoutParams();
                    lp.weight = 0.33f;

                    btnRec.setLayoutParams(lp);
                    btnStp.setLayoutParams(lp);
                    btnPly.setLayoutParams(lp);
                    LinearLayout ll = new LinearLayout(_thisView);
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    // TableRow tr = new TableRow(_thisView);
                    ll.addView(btnRec);
                    ll.addView(btnStp);
                    ll.addView(btnPly);

                    table.addView(ll);

                    break;
                default:
                    break;
            }

            TextView tvEvents = new TextView(_thisView);
            tvEvents.setText(getResources().getString(R.string.Events));
            table.addView(tvEvents);
            for (final DataFormElementEventModel dfeem : _dfem.getEvents()) {
                LinearLayout llExtraData = new LinearLayout(_thisView);
                llExtraData.setOrientation(LinearLayout.HORIZONTAL);
                TextView tvType = new TextView(_thisView);
                tvType.setText(dfeem.getEventDataType().toString() + ":");
                TextView tvExtraValue = new TextView(_thisView);
                String tmp;

                switch (dfeem.getEventDataType()) {
                    case TIME:
                        tmp = new String(dfeem.getStoredData(_viewPos));
                        tvExtraValue.setText(tmp);

                        llExtraData.addView(tvType);
                        llExtraData.addView(tvExtraValue);
                        break;
                    case DATE:
                        tmp = new String(dfeem.getStoredData(_viewPos));
                        tvExtraValue.setText(tmp);

                        llExtraData.addView(tvType);
                        llExtraData.addView(tvExtraValue);

                        break;
                    case GPS:
                        TextView tvLabelGPS = new TextView(_thisView);
                        tvLabelGPS.setText(getResources().getString(R.string.GPS));
                        llExtraData.addView(tvLabelGPS);
                        if (dfeem.getStoredData(_viewPos) != null) {
                            ImageButton btnGPS = new ImageButton(_thisView);
                            // btnGPS.setText("Google Maps");
                            btnGPS.setImageResource(R.drawable.ic_dialog_map);
                            llExtraData.addView(btnGPS);

                            btnGPS.setOnClickListener(new OnClickListener() {

                                public void onClick(View arg0) {
                                    Intent tki = new Intent();
                                    tki.setClass(getApplication(), DataCollectorMapViewActivity.class);
                                    tki.putExtra("datagps", DataFormElementGPSView.getGPSCoordinates(dfeem, _viewPos));
                                    _thisView.startActivityForResult(tki, _viewPos);

                                }
                            });
                        } else {
                            TextView tvNoGPS = new TextView(_thisView);
                            tvNoGPS.setText(getResources().getString(R.string.noGPS));
                            llExtraData.addView(tvNoGPS);
                        }
                        break;
                    default:
                        break;
                }
                table.addView(llExtraData);
            }

            // for (DataFormElementModel el : _dfem.get) {
            //
            // // table.removeViewAt(i);
            // table.addView(new DataFormElement(el, _application, table
            // .getChildCount()).getView());
            // }
            TextView tvPos = new TextView(_thisView);
            tvPos.setText(getResources().getString(R.string.Position) + " " + (Integer.valueOf(_viewPos + 1)) + " " + getResources().getString(R.string.of) + " " + _dfem.getDataList().size());

            table.addView(tvPos);
        } else
            finishWithResult();
        table.addView(createLastRows(getResources().getString(R.string.btnDone), _thisView));
    }
}
