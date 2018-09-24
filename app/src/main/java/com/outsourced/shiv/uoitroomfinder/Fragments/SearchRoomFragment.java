package com.outsourced.shiv.uoitroomfinder.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.outsourced.shiv.uoitroomfinder.Adapters.ExpandableScheduleAdapter;
import com.outsourced.shiv.uoitroomfinder.Models.Class;
import com.outsourced.shiv.uoitroomfinder.Models.Class.ClassResult;
import com.outsourced.shiv.uoitroomfinder.Models.Room;
import com.outsourced.shiv.uoitroomfinder.Models.Room.RoomResult;
import com.outsourced.shiv.uoitroomfinder.Models.RoomSchedule;
import com.outsourced.shiv.uoitroomfinder.Models.RoomSchedule.RoomScheduleResult;
import com.outsourced.shiv.uoitroomfinder.Network.DataService;
import com.outsourced.shiv.uoitroomfinder.Network.RetrofitClient;
import com.outsourced.shiv.uoitroomfinder.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRoomFragment extends Fragment {

    ExpandableScheduleAdapter listAdapter;
    List<Room> rooms = new ArrayList<>();
    List<String> roomNames = new ArrayList<>();
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<RoomSchedule>> listDataChild = new HashMap<>();
    String roomName;
    ExpandableListView expListView;
    TextView ribbonTitle;
    boolean canSearch = false;
    FloatingActionButton searchFab;

    DisplayMetrics metrics;
    int width;

    private AdView mAdView;

    public static SearchRoomFragment newInstance() {
        SearchRoomFragment fragment = new SearchRoomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchroom, container, false);

        setUpAds(view);

        searchFab = (FloatingActionButton) view.findViewById(R.id.search_time);
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Create handle for the RetrofitInstance interface */
                DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
                Call<RoomScheduleResult> call = service.getRoomSchedule(roomName);
                call.enqueue(new Callback<RoomScheduleResult>() {
                    @Override
                    public void onResponse(Call<RoomScheduleResult> call, Response<RoomScheduleResult> response) {
                        Log.d("Search Time: HTTP CODE", Integer.toString(response.code()));
                        generateDataList(response.body());
                    }

                    @Override
                    public void onFailure(Call<RoomScheduleResult> call, Throwable t) {
                        Log.d("Error", t.toString());
                        Toast.makeText(getActivity(), "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        getRooms();

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), R.layout.autocomplete_text, roomNames);
        final AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.room);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);
        actv.setTextColor(getResources().getColor(R.color.colorAccent));
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    String errorString = "This field cannot be empty";  // Your custom error message.
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.White));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
                    actv.setError(spannableStringBuilder);
                    canSearch = false;
                } else {
                    actv.setError(null);
                    roomName = actv.getText().toString();
                    canSearch = true;
                }
                searchFab.setEnabled(canSearch);
            }
        });

        expListView = (ExpandableListView) view.findViewById(R.id.expLV);
        expListView.setFocusable(false);
        expListView.setNestedScrollingEnabled(true);
        ribbonTitle = (TextView) view.findViewById(R.id.ribbon_title);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        expListView.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));

        return view;
    }

    private void generateDataList(RoomScheduleResult rooms) {
        listDataChild.clear();
        listDataHeader.clear();
        if (rooms.getRoomSchedule().size() > 0) {
            List<RoomSchedule> dataSet = rooms.getRoomSchedule();
            for (RoomSchedule rs : dataSet) {
                if (!listDataHeader.contains(getDay(rs.getDay()))) {
                    listDataHeader.add(getDay(rs.getDay()));
                }
                if (!listDataChild.containsKey(getDay(rs.getDay()))) {
                    List<RoomSchedule> rsList = new ArrayList<>();
                    rsList.add(rs);
                    listDataChild.put(getDay(rs.getDay()), rsList);
                } else {
                    listDataChild.get(getDay(rs.getDay())).add(rs);
                }
            }
            listAdapter = new ExpandableScheduleAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            ribbonTitle.setText("Room Schedule for " + roomName);
        }
        else {
            ribbonTitle.setText("Room Schedule for " + roomName);
            listAdapter = new ExpandableScheduleAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
        }
    }

    private void getRooms() {
        /* Create handle for the RetrofitInstance interface */
        DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
        Call<RoomResult> call = service.getAllRooms();
        call.enqueue(new Callback<RoomResult>() {
            @Override
            public void onResponse(Call<RoomResult> call, Response<RoomResult> response) {
                Log.d("RoomList: HTTP CODE", Integer.toString(response.code()));
                getRoomList(response.body());
            }

            @Override
            public void onFailure(Call<RoomResult> call, Throwable t) {
                Log.d("Error", t.toString());
                Toast.makeText(getActivity(), "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDay(String d) {
        String day = "";
        switch(d) {
            case "M":
                day = "Monday";
                break;
            case "T":
                day = "Tuesday";
                break;
            case "W":
                day = "Wednesday";
                break;
            case "R":
                day = "Thursday";
                break;
            case "F":
                day = "Friday";
                break;
        }
        return day;
    }

    private void getRoomList(RoomResult rooms) {
        if (rooms.getRooms().size() > 0) {
            this.rooms = rooms.getRooms();
            for (Room r : this.rooms) {
                roomNames.add(r.getRoom());
            }
        }
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