package com.iotkali.karport;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddNewCarActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private User user;
    private String sendPlates,sendBrand,sendModel;
    private EditText add_car_plates_input,add_car_brand_input,add_car_model_input,add_car_year_input;
    private int sendYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
        add_car_plates_input = (EditText)findViewById(R.id.add_car_plates_input);
        add_car_brand_input = (EditText)findViewById(R.id.add_car_brand_input);
        add_car_model_input = (EditText)findViewById(R.id.add_car_model_input);
        add_car_year_input = (EditText)findViewById(R.id.add_car_year_input);
        user = new User(getSharedPreferences("User", Context.MODE_PRIVATE));
        if (!user.isOnline()){
            Intent mainIntent = new Intent(AddNewCarActivity.this, PreLoginActivity.class);
            AddNewCarActivity.this.startActivity(mainIntent);
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        fab = (FloatingActionButton)findViewById(R.id.add_new_car_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new car function
                sendBrand = add_car_brand_input.getText().toString();
                sendModel = add_car_model_input.getText().toString();
                sendPlates = add_car_plates_input.getText().toString();
                sendYear = Integer.parseInt(add_car_year_input.getText().toString());
                JSONTask userTask = new JSONTask();
                userTask.execute(getString(R.string.URL_for_register_cars), getString(R.string.URL_for_register_people_add_plate) + "/" + user.getEmail());
            }
        });
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

            Log.v("PRUEBA: ",params[1]);
            HttpURLConnection connection = null;
            HttpURLConnection connection2 = null;
            try {
                URL url = new URL(params[0]);
                URL url2 = new URL(params[1]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);

                connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setDoOutput(true);
                connection2.setDoInput(true);
                connection2.setInstanceFollowRedirects(false);
                connection2.setRequestMethod("PATCH");
                connection2.setRequestProperty("Content-Type", "application/json");
                connection2.setRequestProperty("charset", "utf-8");
                connection2.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                DataOutputStream wr2 = new DataOutputStream(connection2.getOutputStream ());

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParam2 = new JSONObject();
                try {
                    jsonParam.put("Plates", sendPlates);
                    jsonParam.put("Brand", sendBrand);
                    jsonParam.put("Model", sendModel);
                    jsonParam.put("Year", sendYear);
                    jsonParam2.put("Plates", sendPlates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wr.writeBytes(jsonParam.toString());
                wr2.writeBytes(jsonParam2.toString());

                wr.flush();
                wr.close();
                wr2.flush();
                wr2.close();

                Log.v("A: ", "" + connection.getResponseCode());
                Log.v("B: ","" + connection2.getResponseCode());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Intent mainIntent = new Intent(AddNewCarActivity.this, MainActivity.class);
            AddNewCarActivity.this.startActivity(mainIntent);
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }


    }

}
