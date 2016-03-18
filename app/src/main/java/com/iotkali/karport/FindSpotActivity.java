package com.iotkali.karport;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FindSpotActivity extends FragmentActivity implements OnMapReadyCallback {

    private Zone[] zones;
    private GoogleApiClient client;
    private GoogleMap gMap;
    private Zone myZone;
    private Boolean zoneBool;
    private JSONTask userTask;
    private ArrayList<Polygon> areas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_spot);
        getActionBar().hide();
        zoneBool = true;
        userTask = new JSONTask();
        userTask.execute(getString(R.string.yoab_ip_address));



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        this.gMap = gMap;
        LatLng tec = new LatLng(Double.parseDouble(getString(R.string.latitude_value)),Double.parseDouble(getString(R.string.longitude_value)));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(tec));
        //gMap.setMyLocationEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.iotkali.karport/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.iotkali.karport/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader br = null;
            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));

                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }

                return sb.toString();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                try {
                    if (br != null){
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(zoneBool) {
                Gson gson = new Gson();
                JsonArray jArray = gson.fromJson(result, JsonElement.class).getAsJsonArray();

                zones = new Zone[jArray.size()];

                for (int i = 0; i < zones.length; i++) {
                    String zoneName = jArray.get(i).getAsJsonObject().get("Name").getAsString();
                    int numPoints = jArray.get(i).getAsJsonObject().get("Points").getAsJsonArray().size();
                    int totalSpaces = jArray.get(i).getAsJsonObject().get("Spots").getAsInt();
                    int cars = jArray.get(i).getAsJsonObject().get("Cars").getAsInt();
                    int availableSpaces = totalSpaces - cars;
                    double[] center = {jArray.get(i).getAsJsonObject().get("Center").getAsJsonObject().get("latitude").getAsDouble(), jArray.get(i).getAsJsonObject().get("Center").getAsJsonObject().get("longitude").getAsDouble()};

                    int green, red;
                    double spacePorcentage = Math.rint(((double) (availableSpaces) / totalSpaces) * 10) / 10;
                    if (spacePorcentage <= 0.5) {
                        red = 255;
                        green = (int) (255 * 2 * spacePorcentage);
                    } else {
                        green = 255;
                        red = (int) (255 - (255 * spacePorcentage));
                    }

                    int[] argbColor = {30, red, green, 0};
                    String strokeColor = String.format("#%02X%02X%02X", red, green, 0);

                    List<double[]> points = new ArrayList<>();
                    for (int j = 0; j < numPoints; j++) {
                        double[] point = {jArray.get(i).getAsJsonObject().get("Points").getAsJsonArray().get(j).getAsJsonObject().get("latitude").getAsDouble(), jArray.get(i).getAsJsonObject().get("Points").getAsJsonArray().get(j).getAsJsonObject().get("longitude").getAsDouble()};
                        points.add(new double[]{point[0], point[1]});
                    }

                    zones[i] = new Zone(zoneName, numPoints, totalSpaces, cars,center, points, availableSpaces, strokeColor, argbColor, true);

                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }

                areas = new ArrayList<>();

                for (int j = 0; j < zones.length; j++) {
                    ArrayList<LatLng> points = new ArrayList<>();
                    for (int i = 0; i < zones[j].getNumPoints(); i++) {
                        points.add(new LatLng(zones[j].getCoordinates().get(i)[0], zones[j].getCoordinates().get(i)[1]));
                    }
                    areas.add(gMap.addPolygon(new PolygonOptions()
                                    .addAll(points)
                                    .strokeColor(Color.parseColor(zones[j].getStrokeColor()))
                                    .fillColor(Color.argb(zones[j].getFillColor()[0], zones[j].getFillColor()[1], zones[j].getFillColor()[2], zones[j].getFillColor()[3]))
                                    .clickable(zones[j].isClickable()))
                    );
                }

                gMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                    @Override
                    public void onPolygonClick(Polygon polygon) {

                        Zone zone = null;

                        for (int i = 0; i < zones.length; i++) {
                            if (polygon.getId().equals("pg" + i)) {
                                zone = zones[i];
                                break;
                            }
                        }

                        myZone = zone;

                        String url = "http://10.43.28.194:9000/api/zones/priority/" + myZone.getName();
                        zoneBool = false;
                        userTask = new JSONTask();
                        userTask.execute(url);

                    }
                });
            }else{
                int[] newRgb = {30, 153, 153, 153};
                for (int i=0; i<zones.length; i++){
                    if(i<3){
                        Marker marker = gMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(zones[i].getCenter()[0], zones[i].getCenter()[1]))
                                        .title("Recomended: " + zones[i].getName())
                        );
                        if (i==1)
                            marker.showInfoWindow();
                    }else{
                        zones[i].setFillColor(newRgb);
                        areas.get(i).setFillColor(Color.argb(newRgb[0], newRgb[1], newRgb[2], newRgb[3]));
                        zones[i].setStrokeColor("#5C5C5C");
                        areas.get(i).setStrokeColor(Color.parseColor("#5C5C5C"));
                    }

                }
            }

        }

    }
}
