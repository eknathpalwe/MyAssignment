package com.ek.myassignment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by User on 6/29/2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setTitle(R.string.btn_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
         gmap.setMinZoomPreference(8);
        mapUiSettings();
        LatLng ny = new LatLng(18.48026029, 73.90537262);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        readGeoJson();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initiaze map ettings
     */
    private void mapUiSettings() {
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
    }

    /**
     * Read geo json template from raw folder
     */
    private void readGeoJson() {
        try {
            String geoJson = readTextFile(getResources().openRawResource(R.raw.geojson));
            JSONObject jsonObjMain = new JSONObject(geoJson);

            if (jsonObjMain != null) {

                //farms
                JSONArray farmsJsonArr = jsonObjMain.getJSONArray("farms");
                if (farmsJsonArr != null) {
                    for (int i = 0; i < farmsJsonArr.length(); i++) {
                        GeoJsonLayer geoJsonLayer = new GeoJsonLayer(gmap, farmsJsonArr.getJSONObject(i));
                        geoJsonLayer.addLayerToMap();

                        for (GeoJsonFeature feature : geoJsonLayer.getFeatures()) {  //loop through features
                            GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();

                            pointStyle.setTitle(feature.getProperty("farm_name"));
                            pointStyle.setSnippet(feature.getProperty("farm_location"));
                            feature.setPointStyle(pointStyle);
                        }
                    }
                }

                //fields
                JSONArray fieldsJsonArr = jsonObjMain.getJSONArray("fields");
                if (fieldsJsonArr != null) {
                    for (int i = 0; i < fieldsJsonArr.length(); i++) {
                        GeoJsonLayer geoJsonLayer = new GeoJsonLayer(gmap, fieldsJsonArr.getJSONObject(i));
                        geoJsonLayer.addLayerToMap();
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Read whole text file to string
     * @param inputStream
     * @return
     */
    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
