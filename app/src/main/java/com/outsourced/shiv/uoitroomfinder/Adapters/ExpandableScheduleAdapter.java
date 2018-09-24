package com.outsourced.shiv.uoitroomfinder.Adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.outsourced.shiv.uoitroomfinder.Models.Room;
import com.outsourced.shiv.uoitroomfinder.Models.RoomSchedule;
import com.outsourced.shiv.uoitroomfinder.R;
import com.outsourced.shiv.uoitroomfinder.Models.Class;

public class ExpandableScheduleAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<RoomSchedule>> _listDataChild;

    public ExpandableScheduleAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<RoomSchedule>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final RoomSchedule child = (RoomSchedule) getChild(groupPosition, childPosition);

        final String start_time_text = child.getStart_time();
        final String end_time_text = child.getEnd_time();

        final Date start_date_text = child.getStart_date();
        final Date end_date_text = child.getEnd_date();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_row_room, null);
        }

        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss");
        String time_text = "";
        try {
            String start_time_format = displayFormat.format(inputFormat.parse(start_time_text));
            String end_time_format = displayFormat.format(inputFormat.parse(end_time_text));
            time_text = start_time_format + " - " + end_time_format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String type_text = "";
        String start_date_format = "";

        if (start_date_text.equals(end_date_text)) { // Only on certain days
            String date_displayFormat = "MMMM dd yyyy";

            SimpleDateFormat sdf = new SimpleDateFormat(date_displayFormat, Locale.CANADA);
            start_date_format = sdf.format(start_date_text);

            type_text = "Only on " + start_date_format;
            Log.d("Only on", start_date_text + " - " + end_date_text);
        } else { // Every Week
            type_text = "Every Week";
            Log.d("Every Week", start_date_text + " - " + end_date_text);
        }

        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(time_text);

        TextView type = (TextView) convertView.findViewById(R.id.type);
        type.setText(type_text);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_row, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.parent_title);

        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}