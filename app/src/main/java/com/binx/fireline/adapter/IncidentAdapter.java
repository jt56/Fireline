package com.binx.fireline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binx.fireline.R;
import com.binx.fireline.model.Incident;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by james on 1/5/17.
 */

public class IncidentAdapter extends ArrayAdapter<Incident> {
    List<Incident> incidentList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public IncidentAdapter(Context context, List<Incident> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        incidentList = objects;
    }

    @Override
    public Incident getItem(int position) {
        return incidentList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Incident item = getItem(position);

        vh.textViewIncidentType.setText(item.getIncidentType());
        vh.textViewResponseDate.setText(item.getResponseDate());
        vh.textViewBlockAddrressCity.setText(item.getBlock() + " " + item.getAddress() + ", " + item.getCity());
//        Picasso.with(context).load(item.getProfilePic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
//        public final ImageView imageView;
        public final TextView textViewIncidentType;
        public final TextView textViewResponseDate;
        public final TextView textViewBlockAddrressCity;

        private ViewHolder(RelativeLayout rootView, TextView textViewIncidentType, TextView textViewResponseDate, TextView textViewBlockAddrressCity) {
            this.rootView = rootView;
//            this.imageView = imageView;
            this.textViewIncidentType = textViewIncidentType;
            this.textViewResponseDate = textViewResponseDate;
            this.textViewBlockAddrressCity = textViewBlockAddrressCity;
        }

        public static ViewHolder create(RelativeLayout rootView) {
//            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewIncidentNumber = (TextView) rootView.findViewById(R.id.textViewIncidentType);
            TextView textViewResponseDate = (TextView) rootView.findViewById(R.id.textViewResponseDate);
            TextView textViewBlockAddrressCity = (TextView) rootView.findViewById(R.id.textViewBlockAddrressCity);
            return new ViewHolder(rootView, textViewIncidentNumber, textViewResponseDate, textViewBlockAddrressCity);
        }
    }
}
