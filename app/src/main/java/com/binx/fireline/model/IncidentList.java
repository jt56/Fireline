package com.binx.fireline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by james on 1/5/17.
 * This does not do anything because fireline returns an array not an object
 */

public class IncidentList {

    @SerializedName("incidents")
    @Expose
    private ArrayList<Incident> incidents = new ArrayList<>();

    /**
     * @return The incidents
     */
    public ArrayList<Incident> getIncidents() { return incidents; }

    /**
     * @param incidents The contacts
     */
    public void setIncidents(ArrayList<Incident> incidents) {
        this.incidents = incidents;
    }
}

