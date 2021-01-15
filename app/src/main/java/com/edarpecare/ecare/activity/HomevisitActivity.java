package com.edarpecare.ecare.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edarpecare.ecare.DBHelper;
import com.edarpecare.ecare.R;
import com.edarpecare.ecare.app.AppConfig;
import com.edarpecare.ecare.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomevisitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SpinnerAdapter {

    private static final String TAG = HomevisitActivity.class.getSimpleName();
    SharedPreferences prf;


    // My declarations
    public LinearLayout newlinearlayout, defaulterlinearlayout, heilinearlayout, virallinearlayout, shiftedlinearlayout, selftransferredlinearlayout, reasonlinearlayout, yesadrlinearlayout, testresultslinearlayout
            , selftestkitlinearlayout, pcrlinearlayout, antibodylinearlayout, prophylaxislinearlayout, viralsexualpartnerlinearlayout, drugstimelinearlayout, viralmdtlinearlayout;
    public CheckBox newclientcheckBox, defaultercheckBox, heicheckBox, viralcheckBox, shiftedCheckbox, selftransferredCheckBox, diedCheckbox, defaultercallclinician, defaulteraccompanyclient, defaultercollectdrugs,
            heicallclinician, heiaccompanyclient, heicollectprophylaxis, heiantibodytest;

    public Button btnSave, btnClose;
    public EditText txtDate, pillcountTXT, next_tcaTXT, yes_adr_specifyTXT, reasonfordefaulting, defaulterselftransferred, defaultershifted, pcrEdittext, antibodyEdittext, prophylaxisEdittext, nodrugstimeEdittext,
            nomdtEdittext;
    public Spinner matching_tcaTXT, have_adrTXT, stressfulTXT,selfkit_resultsTXT,partner_testedTXT,selfkit_issuedTXT, adr_spinner, sexual_spinner, selftest_spinner, testresult_spinner, clientfound_spinner,
            pcr_spinner, antibodytest_spinner, prophylaxis_spinner, viralsexualpartner_spinner, viralselftestkit_spinner,drugstime_spinner, viralmdt_spinner;
    public int selected, noselected;

    private ProgressDialog pDialog;

    private DBHelper mydb ;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homevisit);
        final TextView textView = findViewById(R.id.text_home);

        mydb = new DBHelper(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(HomevisitActivity.this);
        pDialog.setCancelable(false);

        // My assignments
        newlinearlayout = (LinearLayout) findViewById(R.id.newclientlinearLayout);
        defaulterlinearlayout = (LinearLayout) findViewById(R.id.defaulterlinearLayout);
        heilinearlayout = (LinearLayout) findViewById(R.id.heilinearLayout);
        virallinearlayout = (LinearLayout) findViewById(R.id.virallinearLayout);
        shiftedlinearlayout = (LinearLayout) findViewById(R.id.shiftedLinearLayout);
        selftransferredlinearlayout = (LinearLayout) findViewById(R.id.selftransferredLinearLayout);
        reasonlinearlayout = (LinearLayout) findViewById(R.id.reasonlinearlayout);
        yesadrlinearlayout = (LinearLayout) findViewById(R.id.yesadrlinearlayout);
        testresultslinearlayout = (LinearLayout) findViewById(R.id.testresultslinearlayout);
        selftestkitlinearlayout = (LinearLayout) findViewById(R.id.selftestkitlinearlayout);
        //HEI CLIENT
        pcrlinearlayout = (LinearLayout) findViewById(R.id.pcrlinearlayout);
        antibodylinearlayout = (LinearLayout) findViewById(R.id.antibodylinearlayout);
        prophylaxislinearlayout = (LinearLayout) findViewById(R.id.prophylaxislinearlayout);

        // HIGH VIRAL LOAD CLIENT
        viralsexualpartnerlinearlayout = (LinearLayout) findViewById(R.id.viralsexualpartnerlinearlayout);
        drugstimelinearlayout = (LinearLayout) findViewById(R.id.drugstimelinearlayout);
        viralmdtlinearlayout = (LinearLayout) findViewById(R.id.viralmdtlinearlayout);

        newclientcheckBox = (CheckBox) findViewById(R.id.newclientCheckBox);
        defaultercheckBox = (CheckBox) findViewById(R.id.defaulterCheckBox);
        heicheckBox = (CheckBox) findViewById(R.id.heiCheckBox);
        viralcheckBox = (CheckBox) findViewById(R.id.viralCheckBox);



        btnSave = (Button) findViewById(R.id.buttonsave);
        btnClose = findViewById(R.id.cancel_button);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtDate.setInputType(InputType.TYPE_NULL);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HomevisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(new Date().getTime());
                picker.show();
            }
        });

        //NEW CLIENT
        pillcountTXT = (EditText) findViewById(R.id.pillcountEditText);
        next_tcaTXT = (EditText) findViewById(R.id.nexttcaEditText);
        matching_tcaTXT = (Spinner) findViewById(R.id.matchtca_spinner);
        have_adrTXT = (Spinner) findViewById(R.id.adr_spinner);
        yes_adr_specifyTXT = (EditText) findViewById(R.id.yesadrEditText);
        partner_testedTXT = (Spinner) findViewById(R.id.sexualpartner_spinner);
        selfkit_issuedTXT = (Spinner) findViewById(R.id.issuedselftestkit_spinner);
        selfkit_resultsTXT = (Spinner) findViewById(R.id.testresults_spinner);
        stressfulTXT = (Spinner) findViewById(R.id.stressful_spinner);

        //populating the dropdown menus with yes/no
        Spinner tca_spinner = (Spinner) findViewById(R.id.matchtca_spinner);
        adr_spinner = (Spinner) findViewById(R.id.adr_spinner);
        adr_spinner.setOnItemSelectedListener(this);
        sexual_spinner = (Spinner) findViewById(R.id.sexualpartner_spinner);
        sexual_spinner.setOnItemSelectedListener(this);
        selftest_spinner = (Spinner) findViewById(R.id.issuedselftestkit_spinner);
        selftest_spinner.setOnItemSelectedListener(this);
        testresult_spinner = (Spinner) findViewById(R.id.testresults_spinner);
        testresult_spinner.setOnItemSelectedListener(this);
        Spinner stressful_spinner = (Spinner) findViewById(R.id.stressful_spinner);

        // Defaulter client spinner(s)
        clientfound_spinner = (Spinner) findViewById(R.id.clientfound_spinner);
        clientfound_spinner.setOnItemSelectedListener(this);

        reasonfordefaulting = (EditText) findViewById(R.id.reasonfordefaultingEditText);
        defaulterselftransferred = (EditText) findViewById(R.id.selftransferredEditText);
        defaultershifted = (EditText) findViewById(R.id.shiftedtoEditText);
        defaultercallclinician = (CheckBox) findViewById(R.id.callclinicianCheckBox);
        defaulteraccompanyclient = (CheckBox) findViewById(R.id.accompanyclientCheckBox);
        defaultercollectdrugs = (CheckBox) findViewById(R.id.collectdrugsCheckBox);

        shiftedCheckbox = (CheckBox) findViewById(R.id.shiftedCheckBox);
        selftransferredCheckBox = (CheckBox) findViewById(R.id.selftransferredCheckBox);
        diedCheckbox = (CheckBox) findViewById(R.id.clientdiedCheckBox);


        //HEI Client spinner (s)
        pcr_spinner = (Spinner) findViewById(R.id.pcr_spinner);
        pcr_spinner.setOnItemSelectedListener(this);
        antibodytest_spinner = (Spinner) findViewById(R.id.antibodytest_spinner);
        antibodytest_spinner.setOnItemSelectedListener(this);
        prophylaxis_spinner = (Spinner) findViewById(R.id.prophylaxis_spinner);
        prophylaxis_spinner.setOnItemSelectedListener(this);

        pcrEdittext = (EditText) findViewById(R.id.pcrreasonEditText);
        antibodyEdittext = (EditText) findViewById(R.id.noantibodyreasonEditText);
        prophylaxisEdittext = (EditText) findViewById(R.id.noprophylaxisEditText);
        heicallclinician = (CheckBox) findViewById(R.id.heicallclinicianCheckBox);
        heiaccompanyclient = (CheckBox) findViewById(R.id.heiaccompanyclientCheckBox);
        heicollectprophylaxis = (CheckBox) findViewById(R.id.heicollectprophylaxisCheckBox);
        heiantibodytest = (CheckBox) findViewById(R.id.heiantibodytestCheckBox);

        // Viral Clients spinner (s)
        viralsexualpartner_spinner = (Spinner) findViewById(R.id.viralsexualpartner_spinner);
        viralsexualpartner_spinner.setOnItemSelectedListener(this);
        viralselftestkit_spinner = (Spinner) findViewById(R.id.viralselftestkit_spinner);
        viralselftestkit_spinner.setOnItemSelectedListener(this);
        drugstime_spinner = (Spinner) findViewById(R.id.drugstime_spinner);
        drugstime_spinner.setOnItemSelectedListener(this);
        viralmdt_spinner = (Spinner) findViewById(R.id.viralmdt_spinner);
        viralmdt_spinner.setOnItemSelectedListener(this);

        nodrugstimeEdittext = (EditText) findViewById(R.id.nodrugstimeEditText);
        nomdtEdittext = (EditText) findViewById(R.id.nomdtEditText);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.yes_no, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> testadapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.testresults, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        testadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tca_spinner.setAdapter(adapter);
        adr_spinner.setAdapter(adapter);
        sexual_spinner.setAdapter(adapter);
        selftest_spinner.setAdapter(adapter);
        testresult_spinner.setAdapter(testadapter);
        stressful_spinner.setAdapter(adapter);

        clientfound_spinner.setAdapter(adapter);

        pcr_spinner.setAdapter(adapter);
        antibodytest_spinner.setAdapter(adapter);
        prophylaxis_spinner.setAdapter(adapter);

        viralsexualpartner_spinner.setAdapter(adapter);
        viralselftestkit_spinner.setAdapter(adapter);
        drugstime_spinner.setAdapter(adapter);
        viralmdt_spinner.setAdapter(adapter);

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        onCheckboxClicked();
        onBtnClick();
//        return root;
    }



    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    /////////////// MY FUNCTIONS ///////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////



    public void onCheckboxClicked(){

        newclientcheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Put new client form here
                    newlinearlayout.setVisibility(View.VISIBLE);

                }
                else {
                    // Remove the new client form and clear its fields
                    emptyNewClientFields();
                    newlinearlayout.setVisibility(View.GONE);
                }

            }
        });
        defaultercheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Put new client form here
                    defaulterlinearlayout.setVisibility(View.VISIBLE);

                }
                else {
                    // Remove the new client form and clear its fields
                    emptyDefaulterFields();
                    defaulterlinearlayout.setVisibility(View.GONE);
                }

            }
        });
        heicheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Put new client form here
                    heilinearlayout.setVisibility(View.VISIBLE);

                }
                else {
                    // Remove the new client form and clear its fields
                    emptyHeiFields();
                    heilinearlayout.setVisibility(View.GONE);
                }

            }
        });
        viralcheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Put new client form here
                    virallinearlayout.setVisibility(View.VISIBLE);

                }
                else {
                    // Remove the new client form and clear its fields
                    emptyViralFields();
                    virallinearlayout.setVisibility(View.GONE);
                }

            }
        });

        shiftedCheckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Put new client form here
                    shiftedlinearlayout.setVisibility(View.VISIBLE);

                }
                else {
                    // Remove the new client form
                    shiftedlinearlayout.setVisibility(View.GONE);
                }

            }
        });

        selftransferredCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Put new client form here
                    selftransferredlinearlayout.setVisibility(View.VISIBLE);

                }
                else {
                    // Remove the new client form
                    selftransferredlinearlayout.setVisibility(View.GONE);
                }

            }
        });

        defaulteraccompanyclient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is defaulter accompany client checked?
                if (((CheckBox) v).isChecked()) {
                    // Uncheck and make client died checkbox unclickable
                    if (diedCheckbox.isChecked()) {
                        diedCheckbox.setChecked(false);
                    }
                    diedCheckbox.setClickable(false);

                }
                else if (defaultercallclinician.isChecked() || defaultercollectdrugs.isChecked()){
                    diedCheckbox.setClickable(false);
                }
                else {
                    diedCheckbox.setClickable(true);
                }
            }
        });

        defaultercallclinician.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is defaulter call clinician checked?
                if (((CheckBox) v).isChecked()) {
                    // Uncheck and make client died checkbox unclickable
                    if (diedCheckbox.isChecked()) {
                        diedCheckbox.setChecked(false);
                    }
                    diedCheckbox.setClickable(false);

                }
                else if (defaulteraccompanyclient.isChecked() || defaultercollectdrugs.isChecked()){
                    diedCheckbox.setClickable(false);
                }
                else {
                    diedCheckbox.setClickable(true);
                }
            }
        });

        defaultercollectdrugs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    // Uncheck and make client died checkbox unclickable
                    if (diedCheckbox.isChecked()) {
                        diedCheckbox.setChecked(false);
                    }
                    diedCheckbox.setClickable(false);

                }
                else if (defaulteraccompanyclient.isChecked() || defaultercallclinician.isChecked()){
                    diedCheckbox.setClickable(false);
                }
                else {
                    diedCheckbox.setClickable(true);
                }
            }
        });

    }

    public void onBtnClick(){
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //
                prf = getApplicationContext().getSharedPreferences("EcareMobileLogin", Context.MODE_PRIVATE);
                String created_by = prf.getString("full_name", null);

                //Initialize parent checkboxes
                String newclient, defaulter, heiclient, viralclient, tbclient="0", pmtctclient = "0";

                //Initialize new client fields
                String pillcount = null, nexttca, matching_tca, have_adr, yes_adr_specify, partner_tested, selfkit_issued, selfkit_results, stressful = "";
                String date = "20200105";
                String serviceno = "10501398424";
                String facility = "Kariobangi";

                String clientfound,reasonfordefault = null, defaulteraction = null, tracingoutcome = null,
                        callclinician, accompanyclient, collectdrugs,
                        selftransferred, selftransferredto = null, clientdied, shifted,
                        shiftedto = null;

                String pcr = null, pcrreason = null, heiaction = null, antibody, antibodyreason = null, prophylaxis, prophylaxisreason = null, heicall, heiaccompany, heicollect, heiantibody;

                if (newclientcheckBox.isChecked()||defaultercheckBox.isChecked()||heicheckBox.isChecked()||viralcheckBox.isChecked())
                {
                    newclient = newclientcheckBox.getText().toString();
                    defaulter = defaultercheckBox.getText().toString();
                    heiclient = heicheckBox.getText().toString();
                    viralclient = viralcheckBox.getText().toString();
//                    pillcount = "";

                    if (pillcountTXT.getText().toString().isEmpty()){
                        pillcountTXT.setError("Enter pill count");
                    }
                    else {
                        pillcount = pillcountTXT.getText().toString();
                    }
                    nexttca = next_tcaTXT.getText().toString();
                    matching_tca = matching_tcaTXT.getSelectedItem().toString();
                    have_adr = have_adrTXT.getSelectedItem().toString();
                    yes_adr_specify = yes_adr_specifyTXT.getText().toString();
                    partner_tested = partner_testedTXT.getSelectedItem().toString();
                    selfkit_issued = selfkit_issuedTXT.getSelectedItem().toString();
                    selfkit_results = selfkit_resultsTXT.getSelectedItem().toString();
                    stressful = stressfulTXT.getSelectedItem().toString();


//                    Toast.makeText(getApplicationContext(), newclientcheckBox.getText().toString() + " Addeded " +
//                            pillcount + ',' + nexttca + ',' + matching_tca + ',' + have_adr + ',' + yes_adr_specify+ ','+ partner_tested+ ','+ selfkit_issued
//                            + ',' + selfkit_results + ',' + stressful + ',', Toast.LENGTH_SHORT).show();


                //}
                //else if (defaultercheckBox.isChecked()){
                    clientfound = clientfound_spinner.getSelectedItem().toString();
//                    String reasonfordefault = null, defaulteraction = null, tracingoutcome = null,
//                            callclinician = null, accompanyclient = null, collectdrugs = null,
//                            selftransferred = null, selftransferredto = null, clientdied = null, shifted = null,
//                            shiftedto = null;
                    if (clientfound_spinner.getSelectedItemPosition() == 1) {

                        reasonfordefault = reasonfordefaulting.getText().toString();
                        if (defaultercallclinician.isChecked()) {
                            defaulteraction = defaultercallclinician.getText().toString();
                        }
                        if (defaulteraccompanyclient.isChecked()) {
                            defaulteraction = defaulteraccompanyclient.getText().toString();
                        }
                        if (defaultercollectdrugs.isChecked()) {
                            defaulteraction = defaultercollectdrugs.getText().toString();
                        }
                    }

                    if (selftransferredCheckBox.isChecked()){
                        tracingoutcome = selftransferredCheckBox.getText().toString();
                        selftransferredto = defaulterselftransferred.getText().toString();
                    }
                    if (diedCheckbox.isChecked()){
                        tracingoutcome = diedCheckbox.getText().toString();
                    }
                    if (shiftedCheckbox.isChecked()){
                        tracingoutcome = shiftedCheckbox.getText().toString();
                        shiftedto = defaultershifted.getText().toString();
                    }

//                    Toast.makeText(getApplicationContext(), defaultercheckBox.getText().toString() + " Addeded " +
//                            clientfound + ',' + reasonfordefault + ',' + callclinician + ',' + accompanyclient + ',' + collectdrugs+ ','+ selftransferred+ ','+ selftransferredtxt
//                            + ',' + clientdied + ',' + shifted + ',' + shiftedtxt + ',', Toast.LENGTH_LONG).show();

                //}

                //else if (heicheckBox.isChecked()){
//                    String pcr = null, pcrreason = null, heiaction = null, antibody = null, antibodyreason = null, prophylaxis = null, prophylaxisreason = null, heicall = null, heiaccompany = null, heicollect = null, heiantibody = null;
                    if (pcr_spinner.getSelectedItemPosition() == 0){
                        pcr = null;
                    }
                    else {
                        pcr = pcr_spinner.getSelectedItem().toString();
                    }
                    if (antibodytest_spinner.getSelectedItemPosition() == 0){
                        antibody = null;
                    }
                    else {
                        antibody = antibodytest_spinner.getSelectedItem().toString();
                    }
                    if (prophylaxis_spinner.getSelectedItemPosition() == 0){
                        prophylaxis = null;
                    }
                    else {
                        prophylaxis = prophylaxis_spinner.getSelectedItem().toString();
                    }

                    if (pcr_spinner.getSelectedItemPosition() == 2){
                        pcrreason = pcrEdittext.getText().toString();
                    }
                    if (antibodytest_spinner.getSelectedItemPosition() == 2){
                        antibodyreason = antibodyEdittext.getText().toString();
                    }
                    if (prophylaxis_spinner.getSelectedItemPosition() == 2){
                        prophylaxisreason = prophylaxisEdittext.getText().toString();
                    }

                    if (heicallclinician.isChecked()){
                        heiaction = heicallclinician.getText().toString();
                    }
                    if (heiaccompanyclient.isChecked()){
                        heiaction = heiaccompanyclient.getText().toString();
                    }
                    if (heicollectprophylaxis.isChecked()){
                        heiaction = heicollectprophylaxis.getText().toString();
                    }
                    if (heiantibodytest.isChecked()){
                        heiaction = heiantibodytest.getText().toString();
                    }

                    //Toast.makeText(getApplicationContext(), heicheckBox.getText().toString() + " Addeded " +
                    //        pcr + ',' + pcrreason + ',' + antibody + ',' + antibodyreason + ',' + prophylaxis+ ','+ prophylaxisreason+ ','+ heicall
                    //        + ',' + heiaccompany + ',' + heicollect + ',' + heiantibody + ',', Toast.LENGTH_LONG).show();
                //}

                //else if (viralcheckBox.isChecked()){
                    String viralpartnertested, viralselftestkit = null, lastvl, drugstime, nodrugsreason = null, clientmdt, reasonnomdt = null;
                    lastvl = txtDate.getText().toString();
                    if (viralsexualpartner_spinner.getSelectedItemPosition() == 0){
                        viralpartnertested = null;
                    }
                    else {
                        viralpartnertested = viralsexualpartner_spinner.getSelectedItem().toString();
                        if (viralselftestkit_spinner.getSelectedItemPosition() == 2) {
                            viralselftestkit = viralselftestkit_spinner.getSelectedItem().toString();
                        }
                    }
                    if (drugstime_spinner.getSelectedItemPosition() == 0){
                        drugstime = null;
                    }
                    else {
                        drugstime = drugstime_spinner.getSelectedItem().toString();
                        if (drugstime_spinner.getSelectedItemPosition() == 2){
                            nodrugsreason = nodrugstimeEdittext.getText().toString();
                        }
                    }
                    if (viralmdt_spinner.getSelectedItemPosition() == 0){
                        clientmdt = null;
                    }
                    else {
                        clientmdt = viralmdt_spinner.getSelectedItem().toString();
                        if (viralmdt_spinner.getSelectedItemPosition() == 2){
                            reasonnomdt = nomdtEdittext.getText().toString();
                        }
                    }
                    SaveNewClient(date,serviceno,facility,newclient,defaulter,tbclient,pmtctclient,heiclient,viralclient,
                            pillcount, nexttca, matching_tca, have_adr, yes_adr_specify, partner_tested, selfkit_issued,
                            selfkit_results, stressful,clientfound,reasonfordefault,defaulteraction,tracingoutcome,
                            shiftedto,selftransferredto,pcr,pcrreason,antibody,antibodyreason,prophylaxis,
                            prophylaxisreason
                            ,heiaction,viralpartnertested,viralselftestkit,lastvl,drugstime,nodrugsreason,
                            clientmdt,reasonnomdt
                            ,created_by);
                    //Toast.makeText(getApplicationContext(), viralcheckBox.getText().toString() + " Addeded " +
                    //        viralpartnertested + ',' + viralselftestkit + ',' + lastvl + ',' + drugstime + ',' + nodrugsreason+ ','+ clientmdt+ ','+ reasonnomdt + ',', Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Data Not Addeded!!", Toast.LENGTH_SHORT).show();
                }
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void SaveNewClient(
            final String date,final String serviceno,final String facility,
            final String newclient,final String defaulter,final String tbclient,
            final String pmtctclient,final String heiclient,final String viralclient,
            final String pillcount,
            final String nexttca,
            final String matching_tca,
            final String have_adr,
            final String yes_adr_specify,
            final String partner_tested,
            final String selfkit_issued,
            final String selfkit_results,
            final String stressful,final String clientfound,final String reasonfordefault,
            final String defaulteraction,
            final String tracingoutcome,final String shiftedto,final String selftransferredto,final String pcr,
            final String pcrreason,final String antibody,final String antibodyreason,final String prophylaxis,
            final String prophylaxisreason,final String heiaction,final String viralpartnertested,final String viralselftestkit,
            final String lastvl,final String drugstime,final String nodrugsreason,final String clientmdt,
            final String reasonnomdt,
            final String created_by

    ) {
        // Tag used to cancel the request
        String tag_string_req = "req_save";

        pDialog.setMessage("Saving Data...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_HOME_VISIT, new Response.Listener<String>() {

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

                        emptyNewClientFields();
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
                params.put("date", date);
                params.put("serviceno", serviceno);
                params.put("facility", facility);
                params.put("newclient", newclient);
                params.put("defaulter", defaulter);
                params.put("tbclient", tbclient);
                params.put("pmtctclient", pmtctclient);
                params.put("heiclient", heiclient);
                params.put("viralclient", viralclient);
                params.put("pillcount", pillcount);
                params.put("nexttca", nexttca);
                params.put("matching_tca", matching_tca);
                params.put("have_adr", have_adr);
                params.put("yes_adr_specify", yes_adr_specify);
                params.put("partner_tested", partner_tested);
                params.put("selfkit_issued", selfkit_issued);
                params.put("selfkit_results", selfkit_results);
                params.put("stressful", stressful);
                params.put("clientfound", clientfound);
                params.put("reasonfordefault", reasonfordefault);
                params.put("defaulteraction", defaulteraction);
                params.put("tracingoutcome", tracingoutcome);
                params.put("shiftedto", shiftedto);
                params.put("selftransferredto", selftransferredto);
                params.put("pcr", pcr);
                params.put("pcrreason", pcrreason);
                params.put("antibody", antibody);
                params.put("antibodyreason", antibodyreason);
                params.put("prophylaxis", prophylaxis);
                params.put("prophylaxisreason", prophylaxisreason);
                params.put("heiaction", heiaction);
                params.put("viralpartnertested", viralpartnertested);
                params.put("viralselftestkit", viralselftestkit);
                params.put("lastvl", lastvl);
                params.put("drugstime", drugstime);
                params.put("nodrugsreason", nodrugsreason);
                params.put("clientmdt", clientmdt);
                params.put("reasonnomdt", reasonnomdt);
                params.put("created_by", created_by);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void emptyNewClientFields(){
        pillcountTXT.getText().clear();
        next_tcaTXT.getText().clear();
        matching_tcaTXT.setSelection(0);
        have_adrTXT.setSelection(0);
        yes_adr_specifyTXT.getText().clear();
        partner_testedTXT.setSelection(0);
        selfkit_issuedTXT.setSelection(0);
        selfkit_resultsTXT.setSelection(0);
        stressfulTXT.setSelection(0);
    }

    public void emptyDefaulterFields(){

        reasonfordefaulting.getText().clear();
        clientfound_spinner.setSelection(0);
        defaulterselftransferred.getText().clear();
        defaultershifted.getText().clear();
        defaultercallclinician.setChecked(false);
        defaulteraccompanyclient.setChecked(false);
        defaultercollectdrugs.setChecked(false);
        shiftedCheckbox.setChecked(false);
        selftransferredCheckBox.setChecked(false);
        diedCheckbox.setChecked(false);
    }

    public void emptyHeiFields(){

        pcrEdittext.getText().clear();
        antibodyEdittext.getText().clear();
        prophylaxisEdittext.getText().clear();
        pcr_spinner.setSelection(0);
        antibodytest_spinner.setSelection(0);
        prophylaxis_spinner.setSelection(0);
        heicallclinician.setChecked(false);
        heiaccompanyclient.setChecked(false);
        heicollectprophylaxis.setChecked(false);
        heiantibodytest.setChecked(false);
    }

    public void emptyViralFields(){

        txtDate.getText().clear();
        nomdtEdittext.getText().clear();
        nodrugstimeEdittext.getText().clear();

        viralsexualpartner_spinner.setSelection(0);
        viralselftestkit_spinner.setSelection(0);
        drugstime_spinner.setSelection(0);
        viralmdt_spinner.setSelection(0);

    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        selected  = parent.getSelectedItemPosition();
        noselected  = parent.getSelectedItemPosition();

        if(selftest_spinner.getSelectedItemPosition() == 1 ){
            testresultslinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            testresultslinearlayout.setVisibility(View.GONE);
            testresult_spinner.setSelection(0);
        }

        if (sexual_spinner.getSelectedItemPosition() == 2 ){
            selftestkitlinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            selftestkitlinearlayout.setVisibility(View.GONE);
            selftest_spinner.setSelection(0);
        }

        if (adr_spinner.getSelectedItemPosition() == 1){
            yesadrlinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            yesadrlinearlayout.setVisibility(View.GONE);
            yes_adr_specifyTXT.getText().clear();
        }

        if (clientfound_spinner.getSelectedItemPosition() == 1){
            reasonlinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            reasonlinearlayout.setVisibility(View.GONE);
        }

        if (pcr_spinner.getSelectedItemPosition() == 2){
            pcrlinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            pcrlinearlayout.setVisibility(View.GONE);
        }

        if (antibodytest_spinner.getSelectedItemPosition() == 2){
            antibodylinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            antibodylinearlayout.setVisibility(View.GONE);
        }
        if (prophylaxis_spinner.getSelectedItemPosition()  == 2){
            prophylaxislinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            prophylaxislinearlayout.setVisibility(View.GONE);
        }

        // Viral spinners
        if (viralsexualpartner_spinner.getSelectedItemPosition() == 2){
            viralsexualpartnerlinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            viralsexualpartnerlinearlayout.setVisibility(View.GONE);
        }

        if (drugstime_spinner.getSelectedItemPosition() == 2){
            drugstimelinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            drugstimelinearlayout.setVisibility(View.GONE);
        }

        if (viralmdt_spinner.getSelectedItemPosition() == 2){
            viralmdtlinearlayout.setVisibility(View.VISIBLE);
        }
        else {
            viralmdtlinearlayout.setVisibility(View.GONE);
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        reasonlinearlayout.setVisibility(View.GONE);
        yesadrlinearlayout.setVisibility(View.GONE);
        testresultslinearlayout.setVisibility(View.GONE);
        selftestkitlinearlayout.setVisibility(View.GONE);

        pcrlinearlayout.setVisibility(View.GONE);
        antibodylinearlayout.setVisibility(View.GONE);
        prophylaxislinearlayout.setVisibility(View.GONE);

        viralsexualpartnerlinearlayout.setVisibility(View.GONE);
        drugstimelinearlayout.setVisibility(View.GONE);
        viralmdtlinearlayout.setVisibility(View.GONE);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (position == 0) {
            TextView tv = new TextView(getApplicationContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = getDropDownView(position, null, parent);
        }
        return v;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}