package com.outsourced.shiv.uoitroomfinder.Fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.outsourced.shiv.uoitroomfinder.Activities.FutureClassActivity;
import com.outsourced.shiv.uoitroomfinder.Adapters.ExpandableListAdapter;
import com.outsourced.shiv.uoitroomfinder.Models.Class;
import com.outsourced.shiv.uoitroomfinder.Models.Class.ClassResult;
import com.outsourced.shiv.uoitroomfinder.Network.DataService;
import com.outsourced.shiv.uoitroomfinder.Network.RetrofitClient;
import com.outsourced.shiv.uoitroomfinder.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTimeFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<Class>> listDataChild = new HashMap<>();
    TextView ribbonTitle;

    DisplayMetrics metrics;
    int width;

    EditText edit_date, edit_start_time, edit_end_time;
    Calendar date_cal, start_time_cal, end_time_cal;
    String date, start_time, end_time;

    boolean canSearch = false;
    FloatingActionButton searchFab;

    private AdView mAdView;

    public static SearchTimeFragment newInstance() {
        SearchTimeFragment fragment = new SearchTimeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchtime, container, false);

        setUpAds(view);

        expListView = (ExpandableListView) view.findViewById(R.id.expLV);
        expListView.setFocusable(false);
        expListView.setNestedScrollingEnabled(true);
        ribbonTitle = (TextView) view.findViewById(R.id.ribbon_title);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ExpandableListAdapter eListAdapter = (ExpandableListAdapter) parent.getExpandableListAdapter();
                Class aClass = (Class) eListAdapter.getChild(groupPosition, childPosition);

                Bundle bundle = new Bundle();
                bundle.putSerializable("class", aClass);
                bundle.putString("start_time", start_time);
                bundle.putString("date", date);
                Intent i = new Intent(getActivity(), FutureClassActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                return true;
            }
        });

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        expListView.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));

        date_cal = Calendar.getInstance();
        start_time_cal = Calendar.getInstance();
        end_time_cal = Calendar.getInstance();

        edit_date = (EditText) view.findViewById(R.id.date);
        edit_start_time = (EditText) view.findViewById(R.id.start_time);
        edit_end_time = (EditText) view.findViewById(R.id.end_time);

        searchFab = (FloatingActionButton) view.findViewById(R.id.search_time);
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Create handle for the RetrofitInstance interface */
                Log.d("Params", end_time);
                DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
                Call<ClassResult> call = service.getClassesByParam(date, start_time, end_time);
                call.enqueue(new Callback<ClassResult>() {
                    @Override
                    public void onResponse(Call<ClassResult> call, Response<ClassResult> response) {

                        Log.d("Search Time: HTTP CODE", Integer.toString(response.code()));
                        generateDataList(response.body());
                        Log.d("onResponse", response.toString());
                    }

                    @Override
                    public void onFailure(Call<ClassResult> call, Throwable t) {
                        Log.d("Error", t.toString());
                        Toast.makeText(getActivity(), "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        checkParams();

        edit_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkParams();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                date_cal.set(Calendar.YEAR, year);
                date_cal.set(Calendar.MONTH, monthOfYear);
                date_cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };
        updateDateLabel();
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, date_cal
                        .get(Calendar.YEAR), date_cal.get(Calendar.MONTH),
                        date_cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edit_start_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkParams();
            }
        });

        edit_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog sTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        updateStartTimeLabel(selectedHour, selectedMinute);
                    }
                }, start_time_cal.get(Calendar.HOUR_OF_DAY), start_time_cal.get(Calendar.MINUTE), false);
                sTimePicker.setTitle("Select Start Time");
                sTimePicker.show();

            }
        });

        edit_end_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkParams();
            }
        });
        edit_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog sTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        updateEndTimeLabel(selectedHour, selectedMinute);
                    }
                }, end_time_cal.get(Calendar.HOUR_OF_DAY), end_time_cal.get(Calendar.MINUTE), false);
                sTimePicker.setTitle("Select End Time");
                sTimePicker.show();

            }
        });
        updateStartTimeLabel(start_time_cal.get(Calendar.HOUR_OF_DAY), start_time_cal.get(Calendar.MINUTE));
        end_time_cal.add(Calendar.HOUR_OF_DAY, 1);
        updateEndTimeLabel(end_time_cal.get(Calendar.HOUR_OF_DAY), end_time_cal.get(Calendar.MINUTE));

        return view;
    }

    private void generateDataList(ClassResult classes) {
        listDataChild.clear();
        listDataHeader.clear();
        if (classes.getClasses().size() > 0) {
            List<Class> dataSet = classes.getClasses();
            for (Class c : dataSet) {
                if (!listDataHeader.contains(c.getBuilding())) {
                    listDataHeader.add(c.getBuilding());
                }
                if (!listDataChild.containsKey(c.getBuilding())) {
                    List<Class> cList = new ArrayList<>();
                    cList.add(c);
                    listDataChild.put(c.getBuilding(), cList);
                } else {
                    listDataChild.get(c.getBuilding()).add(c);
                }
            }
            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            ribbonTitle.setText("Rooms open from " + edit_start_time.getText().toString()
                                                   + " - " + edit_end_time.getText().toString());
        }
        else {
            ribbonTitle.setText(R.string.no_rooms);
            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
        }
    }

    private void checkParams() {
        // Time
        if (end_time_cal.getTimeInMillis() <= start_time_cal.getTimeInMillis()) {
            String errorString = "End Time must be after Start Time";
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.White));
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edit_end_time.requestFocus();
            edit_end_time.setError(spannableStringBuilder);
            canSearch = false;
        } else {
            canSearch = true;
            edit_end_time.setError(null);
        }
        searchFab.setEnabled(canSearch);
    }

    private void updateDateLabel() {
        String paramFormat = "YYYY-MM-dd";
        String displayFormat = "MMMM dd yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(paramFormat, Locale.CANADA);
        date = sdf.format((date_cal.getTime()));

        sdf = new SimpleDateFormat(displayFormat, Locale.CANADA);
        edit_date.setText(sdf.format(date_cal.getTime()));
    }

    private void updateStartTimeLabel(int hour, int minute) {
        start_time_cal.set(Calendar.HOUR_OF_DAY, hour);
        start_time_cal.set(Calendar.MINUTE, minute);

        SimpleDateFormat paramFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm aa");
        start_time = paramFormat.format(start_time_cal.getTime());
        edit_start_time.setText(displayFormat.format(start_time_cal.getTime()));
    }

    private void updateEndTimeLabel(int hour, int minute) {
        end_time_cal.set(Calendar.HOUR_OF_DAY,hour);
        end_time_cal.set(Calendar.MINUTE,minute);

        SimpleDateFormat paramFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm aa");
        end_time = paramFormat.format(end_time_cal.getTime());

        edit_end_time.setText(displayFormat.format(end_time_cal.getTime()));
    }

    public int GetDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void setUpAds(View view) {

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getActivity(), "ca-app-pub-2173238213882820~7350740510");

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}