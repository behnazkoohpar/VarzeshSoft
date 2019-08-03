package com.koohpar.eram.recycleViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.koohpar.eram.R;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> ParentItem;
    private TreeMap<String, List<String>> ChildItem;


    public ExpandableListAdapter(Context context, List<String> ParentItem,
                                 TreeMap<String, List<String>> ChildItem) {
        this.context = context;
        this.ParentItem = ParentItem;
        this.ChildItem = ChildItem;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {

        return this.ChildItem.get(this.ParentItem.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_item, null);
        }
        TextView text1 = (TextView) convertView.findViewById(R.id.item1);
        TextView text2 = (TextView) convertView.findViewById(R.id.item2);
        TextView text3 = (TextView) convertView.findViewById(R.id.item3);
        String name[] = expandedListText.split("/");
        text1.setText(name[0]);
        text2.setText(name[1]);
        text3.setText(name[2]);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        try {
            return this.ChildItem.get(this.ParentItem.get(listPosition))
                    .size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPosition;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.ParentItem.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.ParentItem.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_item, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        if (listTitle.equalsIgnoreCase("0"))
            listTitleTextView.setText("شنبه ها");
        else if (listTitle.equalsIgnoreCase("1"))
            listTitleTextView.setText("یک شنبه ها");
        else if (listTitle.equalsIgnoreCase("2"))
            listTitleTextView.setText("دو شنبه ها");
        else if (listTitle.equalsIgnoreCase("3"))
            listTitleTextView.setText("سه شنبه ها");
        else if (listTitle.equalsIgnoreCase("4"))
            listTitleTextView.setText("چهار شنبه ها");
        else if (listTitle.equalsIgnoreCase("5"))
            listTitleTextView.setText("پنج شنبه ها");
        else if (listTitle.equalsIgnoreCase("6"))
            listTitleTextView.setText("جمعه ها");

//        View v = super.getGroupView(listPosition, isExpanded, convertView, parent);
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(listPosition);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}