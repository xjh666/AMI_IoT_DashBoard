package com.example.foremanproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foremanproject.R;
import com.example.foremanproject.other.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xie Jihui on 5/24/2017.
 */

public class HostsOfAHostGroup extends AppCompatActivity {
    private LinearLayout totalList;
    private static String api = "";
    private static String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hosts_of_a_host_group);
        setTitle(title);
        sendRequest();
    }

    private void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, (UserInfo.getUrl() + api), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getHosts(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                String auth = Base64.encodeToString(UserInfo.getUNandPW().getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + auth);
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    private void getHosts(JSONObject response) throws JSONException {
        JSONArray arr = (JSONArray) response.get("results");
        totalList = (LinearLayout) findViewById(R.id.totallist);
        for(int i=0;i<arr.length();i++){
            JSONObject obj = (JSONObject) arr.get(i);

            LinearLayout linearlayout = new LinearLayout(this);
            linearlayout.setOrientation(LinearLayout.HORIZONTAL);
            totalList.addView(linearlayout);

            ImageView imageView = new ImageView(this);
            if( obj.get("configuration_status") == (Object) 1 || obj.get("configuration_status") == (Object) 0)
                imageView.setImageResource(R.drawable.ok_icon);
            else imageView.setImageResource(R.drawable.exclamation_icon);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(50, 160));

            TextView textView = new TextView(this);
            textView.setText(" " + (String) obj.get("name"));
            textView.setTextSize(22);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(200, 160));
            button.setText("Edit");
            button.setTag(obj.get("name"));
            button.setBackground(getResources().getDrawable(R.drawable.button_icon));

            linearlayout.addView(imageView);
            linearlayout.addView(textView);
            linearlayout.addView(button);

            totalList.addView(new LinearLayout(this));
        }
    }

    public static void setAPI(int id){
        api = "/api/hostgroups/" + id + "/hosts";
    }

    public static void setPageTitle(String str){
        title = str;
    }
}
