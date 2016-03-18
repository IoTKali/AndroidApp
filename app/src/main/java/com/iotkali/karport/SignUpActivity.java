package com.iotkali.karport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends Activity {
    private Spinner sex_spinner,special_condition_spinner;
    private Button sign_up_button,birthdate_select_button;
    private EditText sign_up_name_input,sign_up_email_input,sign_up_password_input;
    private static String sendDate,sendName,sendEmail,sendGender,sendCondition,sendPassword;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getActionBar().hide();
        sign_up_name_input = (EditText)findViewById(R.id.sign_up_name_input);
        sign_up_email_input = (EditText)findViewById(R.id.sign_up_email_input);
        sign_up_password_input = (EditText)findViewById(R.id.sign_up_password_input);

        sign_up_button = (Button)findViewById(R.id.sign_up_button);
        birthdate_select_button = (Button)findViewById(R.id.birthdate_select_button);

        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        special_condition_spinner = (Spinner) findViewById(R.id.special_condition_spinner);
        ArrayAdapter<CharSequence> sex_select_adapter = ArrayAdapter.createFromResource(this,R.array.sex_options, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> special_condition_select_adapter = ArrayAdapter.createFromResource(this,R.array.special_condition_options, android.R.layout.simple_spinner_item);
        sex_select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        special_condition_select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setAdapter(sex_select_adapter);
        special_condition_spinner.setAdapter(special_condition_select_adapter);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register Function

                sendName = sign_up_name_input.getText().toString();
                sendEmail = sign_up_email_input.getText().toString();
                sendGender = sex_spinner.getSelectedItem().toString();
                sendCondition = special_condition_spinner.getSelectedItem().toString();
                sendPassword = sign_up_password_input.getText().toString();
                JSONTask userTask = new JSONTask();
                userTask.execute(getString(R.string.URL_for_register_people));
            }
        });
        birthdate_select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(birthdate_select_button);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private Button birthdate_select_button;
        public DatePickerFragment(Button birthdate_select_button){
            this.birthdate_select_button = birthdate_select_button;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //Toast.makeText(getContext(),date.toString(),Toast.LENGTH_SHORT).show();
            month++;
            sendDate = "" + year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + "T00:00:00.000Z";
            this.birthdate_select_button.setText(String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year);
        }
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
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches (false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("Name", sendName);
                    jsonParam.put("Birthdate", sendDate);
                    jsonParam.put("Email", sendEmail);
                    jsonParam.put("Password", sendPassword);
                    jsonParam.put("Gender", sendGender);
                    jsonParam.put("Condition", sendCondition);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wr.writeBytes(jsonParam.toString());

                wr.flush();
                wr.close();
                Log.v("GSDF:", "" + connection.getResponseCode());

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
            user = new User(sendName,sendDate,sendEmail,sendPassword,sendGender,sendCondition,getSharedPreferences("User", Context.MODE_PRIVATE));
            Intent mainIntent = new Intent(SignUpActivity.this,AddNewCarActivity.class);
            SignUpActivity.this.startActivity(mainIntent);
            SignUpActivity.this.finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }


    }
}
