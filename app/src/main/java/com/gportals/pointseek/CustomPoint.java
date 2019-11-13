package com.gportals.pointseek;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomPoint {
    public String name;
    public String description;
    public LatLng location;

    public CustomPoint(String name, String description, LatLng location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    public MarkerOptions getMarker() {
        MarkerOptions marker = new MarkerOptions();
        marker.title(name);
        marker.snippet(description);
        marker.position(location);

        return marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
