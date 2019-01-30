package com.chunhuitech.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunhuitech.reader.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    List<HashMap<String, Object>> listData;
    public ListViewAdapter(Context context, int resource, List<HashMap<String, Object>> listData) {
        this.context = context;
        this.resource = resource;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        Map<String, Object> data = listData.get(position);
        if (data.containsKey("id")) {
            float fId = Float.parseFloat(data.get("id").toString());
            return (int)fId;
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null ) {
            convertView = inflater.inflate(resource, null);
            TextView textView = (TextView)convertView.findViewById(R.id.textItem);
            Map<String, Object> data = listData.get(position);
            boolean isFolder = (int)Float.parseFloat(data.get("leaf").toString()) == 0;
            if (data.containsKey("cnName")) {
                textView.setText(data.get("cnName").toString());
            }

            ImageView imageView = convertView.findViewById(R.id.iconItem);
            if (isFolder) {
                imageView.setImageResource(R.drawable.ic_list_folder);
            }
            else {
                imageView.setImageResource(R.drawable.ic_list_book);
            }
        }
        return convertView;
    }
}
