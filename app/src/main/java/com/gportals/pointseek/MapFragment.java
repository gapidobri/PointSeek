package com.gportals.pointseek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private GoogleMap mMap;
    private CollectionReference mLocations = FirebaseFirestore.getInstance().collection("locations");

    final int DEFAULT_ZOOM = 17;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SupportMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.clear(); //clear old markers

                mMap.setMyLocationEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);

                mMap.getUiSettings().setMapToolbarEnabled(false);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.252979, 14.351269), DEFAULT_ZOOM));

                fetchMarkersFromDB();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        BottomSheetFragment bottomSheet = new BottomSheetFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("name", marker.getTitle());
                        bundle.putString("description", marker.getSnippet());
                        bottomSheet.setArguments(bundle);

                        bottomSheet.show(getActivity().getSupportFragmentManager(), "bottomSheet");

                        return false;
                    }
                });
            }
        });


        return rootView;
    }

    private void fetchMarkersFromDB(){

        mLocations.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> locations = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot doc : locations){
                    Log.i("Locations", doc.getString("name") + " - " + doc.getString("description"));

                    CustomPoint customPoint = new CustomPoint(doc.getString("name"), doc.getString("description"),
                            new LatLng(doc.getGeoPoint("location").getLatitude(),
                                    doc.getGeoPoint("location").getLongitude()));

                    mMap.addMarker(customPoint.getMarker());

                }

            }
        });

    }



}