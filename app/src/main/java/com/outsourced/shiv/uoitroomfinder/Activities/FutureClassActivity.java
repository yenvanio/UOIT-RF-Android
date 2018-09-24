package com.outsourced.shiv.uoitroomfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.outsourced.shiv.uoitroomfinder.Adapters.ExpandableListAdapter;
import com.outsourced.shiv.uoitroomfinder.Adapters.FutureClassAdapter;
import com.outsourced.shiv.uoitroomfinder.Models.Class;
import com.outsourced.shiv.uoitroomfinder.Models.FutureClass;
import com.outsourced.shiv.uoitroomfinder.Network.DataService;
import com.outsourced.shiv.uoitroomfinder.Network.RetrofitClient;
import com.outsourced.shiv.uoitroomfinder.R;
import com.outsourced.shiv.uoitroomfinder.Models.FutureClass.FutureClassResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FutureClassActivity extends AppCompatActivity {

    Toolbar toolbar;
    Class aClass;
    String room, start_time, date;
    private List<Class> classList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FutureClassAdapter mAdapter;
    private TextView ribbonTitle;
    ProgressDialog mProgressDialog;
    private List<Location> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_class);

        mProgressDialog = new ProgressDialog(this);
        ribbonTitle = (TextView) findViewById(R.id.ribbon_title);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat paramTimeFormat = new SimpleDateFormat("hh:mm:ss");
        String paramDateFormat = "YYYY-MM-dd";

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        aClass = (Class) bundle.getSerializable("class");
        room = aClass.getRoom();

        if (bundle.containsKey("start_time")) {
            start_time = bundle.getString("start_time");
        } else {
            start_time = paramTimeFormat.format(cal.getTime());
        }
        if (bundle.containsKey("date")) {
            date = bundle.getString("date");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(paramDateFormat, Locale.CANADA);
            date = sdf.format((cal.getTime()));
        }
        gatherLocations();

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        startService();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (Location loc : locations) {
                    LatLng marker = new LatLng(loc.getLatitude(), loc.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(marker)
                            .title(aClass.getBuilding()));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 18.0f));

                }
            }
        });
    }

    private void gatherLocations() {
        String[] arr = aClass.getLocation().split(";");
        for (int i = 0; i < arr.length; i++) {
            String[] loc_arr = arr[i].split(",");
            double lat = Double.parseDouble(loc_arr[0]);
            double lng = Double.parseDouble(loc_arr[1]);
            Location loc = new Location("");
            loc.setLatitude(lat);
            loc.setLongitude(lng);
            locations.add(loc);
        }
        aClass.setLocations(locations);
    }

    public void startService() {
        mProgressDialog.show();
        /* Create handle for the RetrofitInstance interface */
        DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
        Call<FutureClassResult> call = service.getFutureClassesByParam(room, date, start_time);
        call.enqueue(new Callback<FutureClassResult>() {
            @Override
            public void onResponse(Call<FutureClassResult> call, Response<FutureClassResult> response) {
                mProgressDialog.dismiss();
                Log.d("Future Class: HTTP CODE", Integer.toString(response.code()));
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<FutureClassResult> call, Throwable t) {
                mProgressDialog.dismiss();
                Log.d("Error", t.toString());
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(FutureClassResult classes) {
        classList.clear();
        if (classes.getClasses().size() > 0) {
            List<Class> dataSet = classes.getClasses();
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
            for (Class c : dataSet) {
                try {
                    String start_time_format = displayFormat.format(inputFormat.parse(c.getStart_time()));
                    String end_time_format = displayFormat.format(inputFormat.parse(c.getEnd_time()));
                    c.setDuration(start_time_format + " - " + end_time_format);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            classList = dataSet;
            mAdapter = new FutureClassAdapter(classList);
            recyclerView.setAdapter(mAdapter);
            ribbonTitle.setText(getString(R.string.more_classes) + " " + room);
        } else {
            ribbonTitle.setText(getString(R.string.no_classes) + " " + room);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}
