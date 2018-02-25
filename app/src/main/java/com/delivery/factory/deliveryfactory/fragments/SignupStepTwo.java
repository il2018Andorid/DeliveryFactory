package com.delivery.factory.deliveryfactory.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.delivery.factory.deliveryfactory.R;
import com.delivery.factory.deliveryfactory.utilities.Const;

import static com.delivery.factory.deliveryfactory.SignupLocum.spinnerCategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupStepTwo extends Fragment {

    public static LinearLayout ll_rbMonthly,ll_rbYearly;
    public static ImageView iv_planDetails;
    public static TextInputLayout TIL_password,TIL_confirmPassword,TIL_catTypeNumber;
    public static EditText et_password,et_confirmPassword,et_catTypeNumber;
    public static Spinner sp_category;
    public static RadioGroup rg_plan,rg_plantype;
    public static RadioButton rb_enterprise,rb_basic ,rb_monthly,rb_yearly;
    public static TextView tv_rb_monthly, tv_rb_yearly,tv_termscondition;
    public static boolean isTermsAccepted= false;

    public SignupStepTwo() {
        // Required empty public constructor
    }


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View convertView = inflater.inflate(R.layout.fragment_signup_step_two, container, false);

        ll_rbMonthly = (LinearLayout)convertView.findViewById(R.id.ll_rbMonthly);
        ll_rbYearly = (LinearLayout)convertView.findViewById(R.id.ll_rbYearly);

        iv_planDetails = (ImageView)convertView.findViewById(R.id.iv_planDetails);
        sp_category= (Spinner)convertView.findViewById(R.id.sp_category);
        TIL_catTypeNumber = (TextInputLayout)convertView.findViewById(R.id.TIL_catTypeNumber);

        et_password = (EditText)convertView.findViewById(R.id.et_password);
        et_confirmPassword = (EditText)convertView.findViewById(R.id.et_confirmPassword);
        et_catTypeNumber = (EditText)convertView.findViewById(R.id.et_catTypeNumber);


        rg_plan = (RadioGroup)convertView.findViewById(R.id.rg_plan);
        rg_plantype = (RadioGroup)convertView.findViewById(R.id.rg_plantype);

        rb_enterprise = (RadioButton) convertView.findViewById(R.id.rb_enterprise);
        rb_basic = (RadioButton)convertView.findViewById(R.id.rb_basic);
        rb_monthly = (RadioButton)convertView.findViewById(R.id.rb_monthly);
        rb_yearly = (RadioButton)convertView.findViewById(R.id.rb_yearly);

        tv_rb_monthly =  (TextView)convertView.findViewById(R.id.tv_rb_monthly);
        tv_rb_yearly =  (TextView)convertView.findViewById(R.id.tv_rb_yearly);
        tv_termscondition =  (TextView)convertView.findViewById(R.id.tv_termscondition);

        rb_enterprise.setChecked(true);
        rb_basic.setChecked(false);

        rb_monthly.setChecked(false);
        ll_rbMonthly.setBackgroundResource(R.drawable.rounded_corner_bordergrey);
        rb_monthly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.md_grey_500));

        rb_yearly.setChecked(true);
        ll_rbYearly.setBackgroundResource(R.drawable.rounded_corner_greenfill_white_borde);
        rb_yearly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));

        tv_rb_monthly.setText(getString(R.string.pound_currency)+"  50  ");
        tv_rb_yearly.setText(getString(R.string.pound_currency)+"  500  ");

        rb_monthly.setChecked(false);
        ll_rbMonthly.setBackgroundResource(R.drawable.rounded_corner_bordergrey);
        rb_monthly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.md_grey_900));

        rb_yearly.setChecked(true);
        ll_rbYearly.setBackgroundResource(R.drawable.rounded_corner_greenfill_white_borde);
        rb_yearly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.white));


        rb_monthly.setTextColor(getResources().getColor(R.color.md_grey_500,getActivity().getTheme()));
        tv_rb_monthly.setTextColor(getResources().getColor(R.color.md_grey_500,getActivity().getTheme()));
        rb_yearly.setTextColor(getResources().getColor(R.color.white,getActivity().getTheme()));
        tv_rb_yearly.setTextColor(getResources().getColor(R.color.white,getActivity().getTheme()));

        rg_plan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId=rg_plan.getCheckedRadioButtonId();
                RadioButton radioplanButton=(RadioButton)convertView.findViewById(selectedId);

                if(radioplanButton.getText().toString().equalsIgnoreCase(rb_enterprise.getText().toString())){
                    tv_rb_monthly.setText(getString(R.string.pound_currency)+"  50  ");
                    tv_rb_yearly.setText(getString(R.string.pound_currency)+"  500  ");

                }else if(radioplanButton.getText().toString().equalsIgnoreCase(rb_basic.getText().toString())){
                    tv_rb_monthly.setText(getString(R.string.pound_currency)+"  15  ");
                    tv_rb_yearly.setText(getString(R.string.pound_currency)+"  150  ");

                }
            }
        });

        rg_plantype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId=rg_plantype.getCheckedRadioButtonId();
                RadioButton radioplanTypeButton=(RadioButton)convertView.findViewById(selectedId);

                if(radioplanTypeButton.getText().toString().equalsIgnoreCase(rb_monthly.getText().toString())){
                    rb_monthly.setChecked(true);
                    rb_monthly.setBackgroundResource(R.drawable.rounded_corner_green_borde);
                    rb_yearly.setChecked(false);
                    rb_yearly.setBackgroundResource(R.drawable.rounded_corner_bordergrey);
                }else if(radioplanTypeButton.getText().toString().equalsIgnoreCase(rb_yearly.getText().toString())){
                    rb_monthly.setChecked(false);
                    rb_monthly.setBackgroundResource(R.drawable.rounded_corner_bordergrey);
                    rb_yearly.setChecked(true);
                    rb_yearly.setBackgroundResource(R.drawable.rounded_corner_green_borde);
                }
            }
        });

        rb_monthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_monthly.isChecked()==true){
                    rb_monthly.setChecked(true);
                    ll_rbMonthly.setBackgroundResource(R.drawable.rounded_corner_greenfill_white_borde);
                    rb_monthly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.white));

                    rb_yearly.setChecked(false);
                    ll_rbYearly.setBackgroundResource(R.drawable.rounded_corner_bordergrey);
                    rb_yearly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.md_grey_900));

                    rb_monthly.setTextColor(getResources().getColor(R.color.white,getActivity().getTheme()));
                    tv_rb_monthly.setTextColor(getResources().getColor(R.color.white,getActivity().getTheme()));
                    rb_yearly.setTextColor(getResources().getColor(R.color.md_grey_500,getActivity().getTheme()));
                    tv_rb_yearly.setTextColor(getResources().getColor(R.color.md_grey_500,getActivity().getTheme()));

                }
            }
        });

        rb_yearly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rb_yearly.isChecked()==true) {
                    rb_monthly.setChecked(false);
                    ll_rbMonthly.setBackgroundResource(R.drawable.rounded_corner_bordergrey);
                    rb_monthly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.md_grey_900));

                    rb_yearly.setChecked(true);
                    ll_rbYearly.setBackgroundResource(R.drawable.rounded_corner_greenfill_white_borde);
                    rb_yearly.setButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.white));


                    rb_monthly.setTextColor(getResources().getColor(R.color.md_grey_500,getActivity().getTheme()));
                    tv_rb_monthly.setTextColor(getResources().getColor(R.color.md_grey_500,getActivity().getTheme()));
                    rb_yearly.setTextColor(getResources().getColor(R.color.white,getActivity().getTheme()));
                    tv_rb_yearly.setTextColor(getResources().getColor(R.color.white,getActivity().getTheme()));
                }
            }
        });

        // Initializing an ArrayAdapter
       sp_category.setAdapter(spinnerCategoryAdapter);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0) {
                    TIL_catTypeNumber.setVisibility(View.VISIBLE);
                    et_catTypeNumber.setVisibility(View.VISIBLE);

                    if (parent.getSelectedItem().toString().equalsIgnoreCase("Nurse(Dental)")) {
                        TIL_catTypeNumber.setHint("NMC Number");
                    } else if (parent.getSelectedItem().toString().equalsIgnoreCase("Nurse(Non-Dental)")) {
                        TIL_catTypeNumber.setHint("NMC Number");
                    } else if (parent.getSelectedItem().toString().equalsIgnoreCase("Dentist")) {
                        TIL_catTypeNumber.setHint("GDC Number");
                    } else if (parent.getSelectedItem().toString().equalsIgnoreCase("General Practitioner")) {
                        TIL_catTypeNumber.setHint("GMC Number");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_planDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WebView webView = new WebView(getContext());
                webView.setWebViewClient(new MyWebViewClient());

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

                webView.loadUrl(Const.SERVER_URL_ONLY+"factory-signup-plandetails");
                // Log.d("url_load",Const.SERVER_URL_ONLY+"factory-signup-plandetails");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("PlanDetails")
                        .setView(webView)
                        .setNeutralButton("OK", null)
                        .show();
            }
        });

        tv_termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebView webView = new WebView(getContext());
                webView.setWebViewClient(new MyWebViewClient());

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

                webView.loadUrl(Const.SERVER_URL_ONLY+"signup-terms");
                // Log.d("url_load",Const.SERVER_URL_ONLY+"factory-signup-plandetails");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(webView)
                        .setNeutralButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isTermsAccepted =true;
                                tv_termscondition.setError(null);
                                tv_termscondition.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.checkbox_on_background, 0);
                            }
                        })
                        .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isTermsAccepted=false;
                                tv_termscondition.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.checkbox_off_background, 0);
                                tv_termscondition.setError("Please Read and Accept !");
                            }
                        })

                        .show();

            }
        });


        return convertView;
    }

    ProgressDialog prDialog;
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(getActivity());
            prDialog.setMessage("Please wait ...");
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(prDialog!=null){
                prDialog.dismiss();
            }
        }
    }

}
