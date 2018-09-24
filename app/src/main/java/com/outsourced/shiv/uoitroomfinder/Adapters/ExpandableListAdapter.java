package com.outsourced.shiv.uoitroomfinder.Adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.outsourced.shiv.uoitroomfinder.R;
import com.outsourced.shiv.uoitroomfinder.Models.Class;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<Class>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Class>> listChildData) {
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

        final Class child = (Class) getChild(groupPosition, childPosition);
        final String childText = child.getRoom();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_row_class, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.child_title);

        ImageView lab_warning = (ImageView) convertView.findViewById(R.id.lab_icon);
        TextView lab_warning_text = (TextView) convertView.findViewById(R.id.lab_warning);
        if (child.getIsLab().equals("Laboratory")) {
            lab_warning.setVisibility(View.VISIBLE);
            lab_warning_text.setVisibility(View.VISIBLE);
        } else {
            lab_warning.setVisibility(View.INVISIBLE);
            lab_warning_text.setVisibility(View.GONE);
        }

        txtListChild.setText(childText);
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

        if (isExpanded)
            convertView.setPadding(0, 0, 0, 0);
        else
            convertView.setPadding(0, 0, 0, 100);


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