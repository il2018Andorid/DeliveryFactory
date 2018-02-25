package com.delivery.factory.deliveryfactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.delivery.factory.deliveryfactory.utilities.Const;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

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

public class VendorProfile extends AppCompatActivity {

    EditText et_fullname,et_address,et_city ,et_state ,et_country ,et_emailid ,et_currentpassword,et_newpassword ,et_cnfpassword;
    Button btn_submit;
    String str_display_name,str_vender_name ,str_phone_no,str_dob ,str_address,str_city ,str_state,str_country
    ,str_currPassword,str_newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        Const.CONST_SHAREDPREFERENCES  = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        new GetVendorProfileAPI().execute();

        et_fullname = (EditText)this.findViewById(R.id.et_fullname);
        et_address = (EditText)this.findViewById(R.id.et_address);
        et_city = (EditText)this.findViewById(R.id.et_city);
        et_state = (EditText)this.findViewById(R.id.et_state);
        et_country = (EditText)this.findViewById(R.id.et_country);
        et_emailid = (EditText)this.findViewById(R.id.et_emailid);
        et_currentpassword = (EditText)this.findViewById(R.id.et_currentpassword);
        et_newpassword = (EditText)this.findViewById(R.id.et_newpassword);
        et_cnfpassword= (EditText)this.findViewById(R.id.et_cnfpassword);

        btn_submit = (Button) this.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_fullname.getText().length()<=0 || et_address.getText().length()<=0 ||
                        et_city.getText().length()<=0 || et_state.getText().length()<=0 ||
                        et_country.getText().length()<=0 || et_emailid.getText().length()<=0 ){

                    if(et_fullname.getText().length()<=0){
                        et_fullname.setError("Required !");
                    }
                    if(et_address.getText().length()<=0){
                        et_address.setError("Required !");
                    }
                    if(et_city.getText().length()<=0){
                        et_city.setError("Required !");
                    }
                    if(et_state.getText().length()<=0){
                        et_state.setError("Required !");
                    }
                    if(et_country.getText().length()<=0){
                        et_country.setError("Required !");
                    }
                    if(et_emailid.getText().length()<=0){
                        et_emailid.setError("Required !");
                    }
                }else if(et_currentpassword.getText().length()>0
                        &&(et_newpassword.getText().length()<=0
                        || et_cnfpassword.getText().length()<=0)){
                    if(et_newpassword.getText().length()<=0
                            || et_cnfpassword.getText().length()<=0){
                        if(et_newpassword.getText().length()<=0){
                            et_newpassword.setError("Required !");
                        }if(et_cnfpassword.getText().length()<=0){
                            et_cnfpassword.setError("Required !");
                        }
                    }
                }
                else{

                    if(et_currentpassword.getText().length()>0
                            &&(et_newpassword.getText().length()>0
                            || et_cnfpassword.getText().length()>0)){

                        if(!et_newpassword.getText().toString().equals(et_cnfpassword.getText().toString())){
                            et_cnfpassword.setError("Password not matched !");
                        } else{
                            str_currPassword = et_currentpassword.getText().toString();
                            str_newPassword = et_newpassword.getText().toString();
                            new ChangePasswordAPI().execute();
                        }
                    }

                    str_display_name= et_fullname.getText().toString();
                    str_vender_name= et_fullname.getText().toString();
                    str_phone_no="14785236952";
                    str_dob= "01/01/2018";
                    str_address= et_address.getText().toString();
                    str_city= et_city.getText().toString();
                    str_state= et_state.getText().toString();
                    str_country= et_country.getText().toString();

                    new UpdateVendorProfileAPI().execute();

                }

            }
        });

    }

    String resVendorProfile;
    protected ProgressDialog progressDialog;
    private class GetVendorProfileAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VendorProfile.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                RequestBody getProfileBody = new FormBody.Builder()
                        .add("user_id",CONST_SHAREDPREFERENCES.getString(PREF_USER_ID,"0"))
                        .add("role_id",CONST_SHAREDPREFERENCES.getString(PREF_USER_ROLE_ID,"0"))
                        .build();


                String responseVendorProfile = post(Const.SERVER_URL_API +"get_profile_detail", getProfileBody,"post");
                // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);
                resVendorProfile=responseVendorProfile;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resVendorProfile;
        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                Log.i("RES resVendorProfile---", resVendorProfile);
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resVendorProfile).getAsJsonObject();

                if(rootObj.get("status_code").getAsString().equalsIgnoreCase("1")){

                    String display_name = rootObj.get("data").getAsJsonObject().get("display_name").getAsString();
                    String email = rootObj.get("data").getAsJsonObject().get("email").getAsString();
                    String vender_name = rootObj.get("data").getAsJsonObject().get("vender_name").getAsString();
                    String phone_no = rootObj.get("data").getAsJsonObject().get("phone_no").getAsString();
                    String address = rootObj.get("data").getAsJsonObject().get("address").getAsString();
                    String city = rootObj.get("data").getAsJsonObject().get("city").getAsString();
                    String state = rootObj.get("data").getAsJsonObject().get("state").getAsString();
                    String country = rootObj.get("data").getAsJsonObject().get("country").getAsString();

                    et_fullname.setText(display_name);
                    et_address.setText(address);
                    et_city.setText(city);
                    et_state.setText(state);
                    et_country.setText(country);
                    et_emailid.setText(email);
                    //et_password.setText(phone_no);
                }else{

                    Toast.makeText(VendorProfile.this,"Something wrong try again after some time !",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }


    String resUpdateVendorProfile;
    private class UpdateVendorProfileAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VendorProfile.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                RequestBody getProfileBody = new FormBody.Builder()
                        .add("user_id",CONST_SHAREDPREFERENCES.getString(PREF_USER_ID,"0"))
                        .add("display_name",str_display_name)
                        .add("vender_name",str_vender_name)
                        .add("phone_no",str_phone_no)
                        .add("dob",str_dob)
                        .add("address",str_address)
                        .add("city",str_city)
                        .add("state",str_city)
                        .add("country",str_country)
                        .build();


                String responseUpdateVendorProfile = post(Const.SERVER_URL_API +"get_profile_detail", getProfileBody,"post");
                // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);
                resUpdateVendorProfile=responseUpdateVendorProfile;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resUpdateVendorProfile;
        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                Log.i("RES resUpdateVendor---", resUpdateVendorProfile);
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resUpdateVendorProfile).getAsJsonObject();

                if(rootObj.get("status_code").getAsString().equalsIgnoreCase("1")){

                    startActivity(new Intent(VendorProfile.this,DashboadVendor.class));
                    finish();

                }else{

                    Toast.makeText(VendorProfile.this,"Something wrong try again after some time !",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }


    String resChangePwd;
    private class ChangePasswordAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(VendorProfile.this, "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                RequestBody getProfileBody = new FormBody.Builder()
                        .add("user_id",CONST_SHAREDPREFERENCES.getString(PREF_USER_ID,"0"))
                        .add("old_password",str_currPassword)
                        .add("new_password",str_newPassword)
                        .add("confirm_password",str_newPassword)
                        .build();


                String responseChangePwd = post(Const.SERVER_URL_API +"get_profile_detail", getProfileBody,"post");
                // Log.d("URL ====",Const.SERVER_URL_API+"filter_venues?"+upend);
                resChangePwd=responseChangePwd;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resChangePwd;
        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                Log.i("RES resUpdateVendor---", resChangePwd);
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resChangePwd).getAsJsonObject();

                if(rootObj.get("status_code").getAsString().equalsIgnoreCase("1")){

                }else{

                    Toast.makeText(VendorProfile.this,"Password not changed, try again after some time !",Toast.LENGTH_LONG).show();
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
