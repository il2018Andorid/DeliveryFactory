package com.delivery.factory.deliveryfactory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.delivery.factory.deliveryfactory.fragments.SignupStepOne;
import com.delivery.factory.deliveryfactory.fragments.SignupStepTwo;
import com.delivery.factory.deliveryfactory.utilities.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.arrayList_prefixName;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.et_email;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.et_firstName;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.et_lastName;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.et_mobileno;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.rg_gender;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.sp_prefixName;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepOne.spinnerArrayAdapter;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.TIL_catTypeNumber;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.et_catTypeNumber;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.et_confirmPassword;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.et_password;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.isTermsAccepted;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.rg_plan;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.rg_plantype;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.sp_category;
import static com.delivery.factory.deliveryfactory.fragments.SignupStepTwo.tv_termscondition;
import static com.delivery.factory.deliveryfactory.utilities.APICall.post;
import static com.delivery.factory.deliveryfactory.utilities.Const.CONST_SHAREDPREFERENCES;
import static com.delivery.factory.deliveryfactory.utilities.Const.MyPREFERENCES;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_TOKEN;

public class SignupLocum extends AppCompatActivity {

    JsonObject SignupJsonRequest = new JsonObject();
    JsonArray rootArrayCategory;

    protected ProgressDialog progressDialog;

    public static ArrayList<String> arrayList_category = new ArrayList<String>();
    public static ArrayAdapter<String> spinnerCategoryAdapter;


    Button firstFragment, secondFragment,btn_submit;
    LinearLayout ll_stepssignup;
    TextView tv_alreadyaccount;
    public static String str_prifixname,str_firstName,str_lastName,str_gender,str_email,str_mobile,
            str_plan,str_plan_type,str_category,str_categoryNumber,str_TitlecatTypeNumber,str_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_locum);

        Const.CONST_SHAREDPREFERENCES  = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        spinnerCategoryAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.onlytextview,arrayList_category);
        spinnerCategoryAdapter.setDropDownViewResource(R.layout.onlytextview);




        btn_submit = (Button) findViewById(R.id.btn_submit);

        ll_stepssignup = (LinearLayout)this.findViewById(R.id.ll_stepssignup);

        tv_alreadyaccount = (TextView) findViewById(R.id.tv_alreadyaccount);
        tv_alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupLocum.this,LoginLocum.class));
                finish();
            }
        });

        // get the reference of Button's
        firstFragment = (Button) findViewById(R.id.firstFragment);
        secondFragment = (Button) findViewById(R.id.secondFragment);

        loadFragment(new SignupStepOne());


        // perform setOnClickListener event on First Button
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // load First Fragment
                //loadFragment(new SignupStepOne());
            }
        });

        // perform setOnClickListener event on Second Button
        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // load Second Fragment
                //loadFragment(new SignupStepTwo());
            }
        });


        btn_submit.setText("Sign Up");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*et_firstName.getText().length()<=0 || et_lastName.getText().length()<=0 ||*/
                        et_email.getText().length() <= 0 || et_password.getText().length() <= 0 ||
                                et_confirmPassword.getText().length() <= 0
                           /*|| et_mobileno.getText().length()<=0*/) {
                       /* if(et_firstName.getText().length()<=0){
                            et_firstName.setError("Required !");
                        }else if(et_lastName.getText().length()<=0){
                            et_lastName.setError("Required ! ");
                        }else*/
                    if (et_email.getText().length() <= 0) {
                        et_email.setError("Required ! ");
                    }/*else if(et_cnfemail.getText().length()<=0){
                            et_cnfemail.setError("Required ! ");
                        }else if(et_mobileno.getText().length()<=0){
                            et_mobileno.setError("Required ! ");
                        }*/
                    if (et_password.getText().length() <= 0) {
                        et_password.setError("Required !");
                    }
                    if (et_confirmPassword.getText().length() <= 0) {
                        et_confirmPassword.setError("Required ! ");
                    }
                } else if (!isEmailValid(et_email.getText().toString())) {
                    et_email.setError("Enter Valid Email Id.");
                }else if(!et_password.getText().toString().equalsIgnoreCase(et_confirmPassword.getText().toString())) {
                    et_confirmPassword.setError("Password not matched !");
                }/*else if (et_mobileno.getText().length()<10){
                        et_mobileno.setError("Enter 10 Digit Mobile Number.");
                    }else if (rg_gender.getCheckedRadioButtonId() == -1)
                    {// no radio buttons are checked
                        Toast.makeText(getBaseContext(),"Please Select Gender .",Toast.LENGTH_LONG).show();
                    }else {
                        loadFragment(new SignupStepTwo());
                        step_no = 2;
                        ll_stepssignup.setBackgroundResource(R.drawable.steptwo);

                        int selectedId=rg_gender.getCheckedRadioButtonId();
                        RadioButton radioGenderButton=(RadioButton)findViewById(selectedId);
//                        Toast.makeText(getBaseContext(),radioGenderButton.getText(),Toast.LENGTH_SHORT).show();

                        str_prifixname = sp_prefixName.getSelectedItem().toString();
                        str_firstName = et_firstName.getText().toString();
                        str_lastName = et_lastName.getText().toString();

                        str_mobile = et_mobileno.getText().toString();
                        str_gender = radioGenderButton.getText().toString();

                    }*/

                else{
                    str_email = et_email.getText().toString();
                    str_password = et_password.getText().toString();
                    new SignUpLocumAPI().execute();
                }
            }});

    }


    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isPasswordValid(String password) {
        boolean isValid = false;
        String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,15})";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    String resUserSigup;
    private class SignUpLocumAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(SignupLocum.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

               // Log.d("data send--",""+SignupJsonRequest.toString());
                RequestBody signupbody = new FormBody.Builder()
                        .add("email",str_email)
                        .add("password",str_password)
                        .build();

                String responseUSerTitles = post(Const.SERVER_URL_API +"users", signupbody,"post");
                // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);
                resUserSigup=responseUSerTitles;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resUserSigup;
        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                Log.e("RES resUserSigup---", resUserSigup);
                JsonParser parser = new JsonParser();
                JsonObject rootObjsignup = parser.parse(resUserSigup).getAsJsonObject();

                if(rootObjsignup.has("errors")){

                }else{

                    String token = rootObjsignup.get("token").getAsString();
                    CONST_SHAREDPREFERENCES.edit().putString(PREF_USER_TOKEN,token).apply();
                    /*startActivity(new Intent(SignupLocum.this,DashboardLocum.class));
                    finish();*/
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

}
