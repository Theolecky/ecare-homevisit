package com.edarpecare.ecare.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edarpecare.ecare.R;
import com.edarpecare.ecare.app.AppConfig;
import com.edarpecare.ecare.app.AppController;
import com.edarpecare.ecare.helper.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import android.widget.Toolbar;


public class CHWReportFormActivity extends AppCompatActivity {
    private static final String TAG = CHWReportFormActivity.class.getSimpleName();
    SharedPreferences prf;
    private Button btnFindCHW;
    private Button btnClose;
    private Button btnSave;
    DatePickerDialog picker;
    private ProgressDialog pDialog;
    private EditText input_date,input_chw_number,input_chw_name,input_progress,input_challenges,input_way_forward;
    private SessionManager session;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chwreportform);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        input_date = findViewById(R.id.txt_date);
        input_chw_number = findViewById(R.id.chw_number);
        input_chw_name = findViewById(R.id.chw_name);
        input_progress = findViewById(R.id.chw_progress);
        input_challenges = findViewById(R.id.chw_challenges);
        input_way_forward = findViewById(R.id.chw_way_forward);
        btnClose = findViewById(R.id.cancel_button);
        btnFindCHW = findViewById(R.id.btnFindCHW);
        btnSave = findViewById(R.id.btn_save_record);
        // SqLite database handler
        //db = new SQLHandler(getApplicationContext());

        // session manager
        //session = new SessionManager(getApplicationContext());

        input_date.setInputType(InputType.TYPE_NULL);
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CHWReportFormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                input_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(new Date().getTime());
                picker.show();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnFindCHW.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String chw_number = input_chw_number.getText().toString().trim();

                // Check for empty data in the form
                if (chw_number.isEmpty()) {
                    // Prompt for service number
                    Toast.makeText(getApplicationContext(),
                            "Please enter the CHW Number to search", Toast.LENGTH_LONG)
                            .show();
                } else {
                    // login user
                    FindCHW(chw_number);
                }
            }

        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String chw_number = input_chw_number.getText().toString().trim();
                String date = input_date.getText().toString().trim();
                String chw_progress = input_progress.getText().toString().trim();
                String challenges = input_challenges.getText().toString().trim();
                String way_forward = input_way_forward.getText().toString().trim();
                //
                prf = getSharedPreferences("EcareMobileLogin", MODE_PRIVATE);
                String created_by = prf.getString("full_name", null);

                // Check for empty data in the form
                if (date.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the date", Toast.LENGTH_LONG)
                            .show();return;
                } if (chw_number.isEmpty()) {
                    // Prompt for service number
                    Toast.makeText(getApplicationContext(),
                            "Please enter the CHW Number to save", Toast.LENGTH_LONG)
                            .show();return;
                } if (chw_progress.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter CHW Progress", Toast.LENGTH_LONG)
                            .show();return;
                } if (challenges.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the Challenges being experienced by CHW", Toast.LENGTH_LONG)
                            .show();return;
                } if (way_forward.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the Way Forward.", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                SaveRecord(chw_number, date, chw_progress, challenges, way_forward,created_by);
            }

        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }

        });
    }
    /**
     * function to search client in the db
     * */
    private void FindCHW(final String chw_number) {
        // Tag used to cancel the request
        String tag_string_req = "req_search";

        pDialog.setMessage("Fetching CHW details...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_CHW_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Search Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    input_chw_name.setText("");
                    // Check for error node in json
                    if (!error) {

                        JSONObject client = jObj.getJSONObject("client");
                        String name = client.getString("name");
                        input_chw_name.setText(name);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Data Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("chw_number", chw_number);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void clearScreen()
    {
        input_chw_number.setText("");
        input_date.setText("");
        input_progress.setText("");
        input_challenges.setText("");
        input_chw_name.setText("");
        input_way_forward.setText("");
    }
    /**
     * function to save report form in the db
     * */
    private void SaveRecord(final String chw_number,
                            final String date,
                            final String chw_progress,
                            final String chw_challenges,
                            final String chw_way_forward,
                            final String created_by
    ) {
        // Tag used to cancel the request
        String tag_string_req = "req_save";

        pDialog.setMessage("Saving Data...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_CHW_REPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Save Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error)
                    {
                        JSONObject data = jObj.getJSONObject("data");
                        String msg = data.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_LONG).show();

                        clearScreen();
                    } else {
                        // Error in saving. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Data error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Saving Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to saving url

                Map<String, String> params = new HashMap<String, String>();
                params.put("chw_number", chw_number);
                params.put("date", date);
                params.put("progress", chw_progress);
                params.put("challenges", chw_challenges);
                params.put("way_forward", chw_way_forward);
                params.put("created_by", created_by);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
