package com.binx.fireline.adapter;

import com.binx.fireline.R;

/**
 * Created by james on 1/16/17.
 * Incident Type string to a image resource
 */

public class IncidentImageAdapter {

    public int getIncidentTypeImageId(String incidentType){

        switch (incidentType){
            case "Medical":
                return R.drawable.circle_yellow;
            case "Traffic Collision":
                return R.drawable.circle_orange;
            case "Medical High":
            case "Medical High Plus":
                return R.drawable.circle_red;
            default:
                return R.drawable.circle_blue;
        }
    }
}
