package com.iotkali.karport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity {
    private Button sign_in_button;
    private ImageView login_logo;
    private EditText email_input,password_input;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();

        user = new User(getSharedPreferences("User",Context.MODE_PRIVATE));
        login_logo = (ImageView)findViewById(R.id.login_logo_colors);
        sign_in_button = (Button)findViewById(R.id.sign_in_button_login);
        email_input = (EditText)findViewById(R.id.login_email_input);
        password_input = (EditText)findViewById(R.id.login_password_input);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_input.getText().toString();
                String password = password_input.getText().toString();
                if (!email.equals("") && !password.equals("")) {
                    //Función de envío de datos
                    JSONTask userTask = new JSONTask(email, password);
                    try {
                        userTask.execute(getString(R.string.URL_for_login));
                    }catch(Exception e){
                        Log.v("ERROR","FAIL");
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No hay datos suficientes", Toast.LENGTH_SHORT).show();
                }
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
        private String email,password;
        public JSONTask(String email,String password){
            this.email = email;
            this.password = password;
        }
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("Email", this.email);
                    jsonParam.put("Password", this.password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wr.writeBytes(jsonParam.toString());

                wr.flush();
                wr.close();

                BufferedReader br;

                if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                }
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                return sb.toString();

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
            Log.v("RESULT",result);
            if (!result.equals("Not Found")){
                Gson gson = new Gson();
                JsonObject jObject = gson.fromJson(result, JsonElement.class).getAsJsonObject();

                String name = jObject.get("Name").getAsString();
                String birthDate = jObject.get("Birthdate").getAsString();
                String email = jObject.get("Email").getAsString();
                String password = jObject.get("Password").getAsString();
                String gender  = jObject.get("Gender").getAsString();
                String condition = jObject.get("Condition").getAsString();

                user = new User(name, birthDate, email, password, gender, condition,getSharedPreferences("User",Context.MODE_PRIVATE));
                //Toast.makeText(getParent(),user.toString(),Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(mainIntent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }else{
                Toast.makeText(getBaseContext(),"ERROR",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
