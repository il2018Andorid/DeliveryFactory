package com.delivery.factory.deliveryfactory.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.delivery.factory.deliveryfactory.Adapter.MenuListAdapter;
import com.delivery.factory.deliveryfactory.DashboadVendor;
import com.delivery.factory.deliveryfactory.ModelPOJO.MenuListModel;
import com.delivery.factory.deliveryfactory.R;
import com.delivery.factory.deliveryfactory.VendorProfile;
import com.delivery.factory.deliveryfactory.utilities.Const;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.delivery.factory.deliveryfactory.utilities.APICall.post;
import static com.delivery.factory.deliveryfactory.utilities.Const.CONST_SHAREDPREFERENCES;
import static com.delivery.factory.deliveryfactory.utilities.Const.PREF_USER_ID;

public class MenuList extends Fragment {


    List<MenuListModel> data;
    MenuListAdapter adapter;

    public MenuList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_menu_list, container, false);

        data = new ArrayList<>();

        new GetMenuAPI().execute();

        RecyclerView recyclerView = (RecyclerView)convertView.findViewById(R.id.recyclerview);
        adapter = new MenuListAdapter(data, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        return convertView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    String resGetMenu;
    protected ProgressDialog progressDialog;
    private class GetMenuAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait...", true, false);

        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {
                RequestBody menuGetbody = new FormBody.Builder()
                        .add("user_id",CONST_SHAREDPREFERENCES.getString(PREF_USER_ID,"0"))
                        .build();

                String responseUSerTitles = post(Const.SERVER_URL_API +"get_food_menu_list",menuGetbody ,"post");
                Log.d("URL ====",Const.SERVER_URL_API +"get_food_menu_list");
                resGetMenu=responseUSerTitles;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resGetMenu;
        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            try{
                Log.i("RES resGetMenu---", resGetMenu);

                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(resGetMenu).getAsJsonObject();

                JsonArray objJson = rootObj.get("objects").getAsJsonArray();

                if(rootObj.get("status_code").getAsString().equalsIgnoreCase("1")){

                    JsonArray menuJsonObj = rootObj.get("data").getAsJsonArray();

                    for(int a=0;a<menuJsonObj.size();a++){

                        String category_name = menuJsonObj.get(a).getAsJsonObject().get("category_name").getAsString();
                        String subcategory_name =  menuJsonObj.get(a).getAsJsonObject().get("subcategory_name").getAsString();
                        String title =  menuJsonObj.get(a).getAsJsonObject().get("title").getAsString();
                        String price =  menuJsonObj.get(a).getAsJsonObject().get("price").getAsString();
                        String image =  menuJsonObj.get(a).getAsJsonObject().get("image").getAsString();
                        String food_id =  menuJsonObj.get(a).getAsJsonObject().get("food_id").getAsString();
                        String category_id =  menuJsonObj.get(a).getAsJsonObject().get("category_id").getAsString();
                        String subcatagory_id =  menuJsonObj.get(a).getAsJsonObject().get("subcatagory_id").getAsString();

                        data.add(new MenuListModel(title,category_name+subcategory_name,image));

                        adapter.notifyDataSetChanged();
                    }

                }else{
                    Toast.makeText(getContext(),"Something wrong try again after some time !",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

}
