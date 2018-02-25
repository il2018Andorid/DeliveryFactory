package com.delivery.factory.deliveryfactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.delivery.factory.deliveryfactory.utilities.Const;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.delivery.factory.deliveryfactory.utilities.APICall.post;
import static com.delivery.factory.deliveryfactory.utilities.Const.CONST_SHAREDPREFERENCES;
import static com.delivery.factory.deliveryfactory.utilities.Const.MyPREFERENCES;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_EMAIL;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_FULL_NAME;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_ID;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_ROLE_ID;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_TOKEN;

public class LoginLocum extends AppCompatActivity {

    TextInputLayout TIL_emailid ,TIL_password;
    EditText et_email ,et_password;
    TextView tv_register,tv_forgotPassword;
    Button btn_sigin;
    String str_email,str_password,str_email_Fpwd;

    final Context context = this;


    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_locum);

        Const.CONST_SHAREDPREFERENCES  = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);



        TIL_emailid = (TextInputLayout)this.findViewById(R.id.TIL_emailid);
        TIL_password = (TextInputLayout)this.findViewById(R.id.TIL_password);

        et_email = (EditText) this.findViewById(R.id.et_email);
        final String email = et_email.getEditableText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        et_password = (EditText) this.findViewById(R.id.et_password);

        tv_register = (TextView) this.findViewById(R.id.tv_register);
        tv_register.setText(Html.fromHtml("<font color=\"#004670\">" + "Register   as   " + "</font>" + "<u><font color=\"#00A75B\" >Locum" + "</font></u>"));
        tv_forgotPassword = (TextView)this.findViewById(R.id.tv_forgotPassword);

        btn_sigin = (Button) this.findViewById(R.id.btn_sigin);
        btn_sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_email.getText().length()==0 || et_password.getText().length()==0){
                    if(et_email.getText().length()==0){
                        et_email.setError("Enter Email !");
                    }else{
                        et_password.setError("Enter Password !");
                    }
                }else if(!isEmailValid(et_email.getText().toString())){
                    et_email.setError("Enter Valid Email !");
                }/*else if(!isPasswordValid(et_password.getText().toString())){
                    et_password.setError("Password must be 8-15 character\nhas 1 cap&small alphabet\nhas number\nhas special character.");
                }*/else{
                    str_email= et_email.getText().toString();
                    str_password=et_password.getText().toString();
                    new LoginLocumAPI().execute();
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginLocum.this,SignupLocum.class));
                //finish();
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog_forgot_password, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInputEmal = (EditText) promptsView
                        .findViewById(R.id.et_forgot_password_email);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        if(!isEmailValid(userInputEmal.getText().toString())){
                                            userInputEmal.setError("Enter Valid Email !");
                                        }else{
                                            str_email_Fpwd = userInputEmal.getText().toString();
                                            new ForgotPasswordAPI().execute();
                                            dialog.dismiss();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



            }
        });


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

    String resUserDetails;
    protected ProgressDialog progressDialog;
    private class LoginLocumAPI extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(LoginLocum.this, "Loading", "Please Wait...", true, false);
        }
        @Override
        protected String doInBackground(Object... parametros) {
            try {
                RequestBody loginbody = new FormBody.Builder()
                        .add("email",str_email)
                        .add("password",str_password)
                        .add("device_id","deviceID")
                        .add("device_type","android")
                        .add("device_token","token")
                        .build();


                String responseUSerTitles = post(Const.SERVER_URL_API +"login", loginbody,"post");
                // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);
                resUserDetails=responseUSerTitles;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resUserDetails;
        }
        @Override
        protected void onPostExecute(String result) {
            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
            try{
                Log.i("RES UserDetails---", resUserDetails);
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resUserDetails).getAsJsonObject();

                if(rootObj.get("status_code").getAsString().equalsIgnoreCase("1")){

                    String user_id = rootObj.get("data").getAsJsonObject().get("user_id").getAsString();
                    String display_name = rootObj.get("data").getAsJsonObject().get("display_name").getAsString();
                    String email = rootObj.get("data").getAsJsonObject().get("email").getAsString();
                    String role_id = rootObj.get("data").getAsJsonObject().get("role_id").getAsString();
                    String auth_token = rootObj.get("data").getAsJsonObject().get("auth_token").getAsString();

                    CONST_SHAREDPREFERENCES.edit().putString(PREF_USER_TOKEN,auth_token).apply();
                    CONST_SHAREDPREFERENCES.edit().putString(PREF_USER_ID,user_id).apply();
                    CONST_SHAREDPREFERENCES.edit().putString(PREF_USER_FULL_NAME,display_name).apply();
                    CONST_SHAREDPREFERENCES.edit().putString(PREF_USER_EMAIL,email).apply();
                    CONST_SHAREDPREFERENCES.edit().putString(PREF_USER_ROLE_ID,role_id).apply();

                    startActivity(new Intent(LoginLocum.this,VendorProfile.class));
                    finish();
                }else{
                    et_email.setError(rootObj.get("message").getAsString());
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }


    String resForgotpwd;
    private class ForgotPasswordAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(LoginLocum.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                RequestBody loginbody = new FormBody.Builder()
                        .add("email",str_email_Fpwd)
                        .add("send","send")
                        .add("webservice","webservice")
                        .build();


                String resForgotpwd = post(Const.SERVER_URL_API +"forgot_password", loginbody,"post");
                // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);



            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resForgotpwd;
        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                Log.i("RES resForgotpwd---", resForgotpwd);
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resForgotpwd).getAsJsonObject();

                String message = rootObj.get("message").getAsString();

                Toast.makeText(LoginLocum.this,message,Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }


}
