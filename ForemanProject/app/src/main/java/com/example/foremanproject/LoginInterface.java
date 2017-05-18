package com.example.foremanproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginInterface extends AppCompatActivity {

    Button button;
    EditText urlEdit;
    EditText userNameEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NukeSSLCerts.nuke();
        setTitle("Welcome to Foreman");
        setContentView(R.layout.activity_login_interface);

        button = (Button)findViewById(R.id.button);
        urlEdit   = (EditText)findViewById(R.id.URL);
        userNameEdit   = (EditText)findViewById(R.id.USERNAME);
        passwordEdit   = (EditText)findViewById(R.id.PASSWORD);
    }

    public void Login(View view) throws AuthFailureError {
        String url = urlEdit.getText().toString();
        final Intent intent = new Intent(this, MainInterface.class);

        if(url.equals(""))
            Toast.makeText(LoginInterface.this, "Please Enter url", Toast.LENGTH_LONG).show();

        else if(userNameEdit.getText().toString().equals(""))
            Toast.makeText(LoginInterface.this, "Please Enter Username", Toast.LENGTH_LONG).show();

        else if(passwordEdit.getText().toString().equals(""))
            Toast.makeText(LoginInterface.this, "Please Enter Password", Toast.LENGTH_LONG).show();

        else {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            if(!url.substring(0,4).equals("https"))
                url = "https://" + url;
            if(!url.substring(url.length()-1).equals("/"))
                url = url + "/";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url + "api/common_parameters", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(LoginInterface.this, "Successful", Toast.LENGTH_LONG).show();
                            SendReqeust.setUrl(urlEdit.getText().toString());
                            SendReqeust.setUsername(userNameEdit.getText().toString());
                            SendReqeust.setPassword(passwordEdit.getText().toString());
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginInterface.this, "Incorrect url/Username/Password", Toast.LENGTH_LONG).show();
                        }
                    }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers = new HashMap<>();
                        // add headers <key,value>
                        String auth = Base64.encodeToString((userNameEdit.getText().toString() + ":" + passwordEdit.getText().toString()).getBytes(),Base64.NO_WRAP);
                        headers.put("Authorization", "Basic "+ auth);

                        return headers;
                    }
            };
            // Add the request to the RequestQueue.
            queue.add(jsObjRequest);
        }
    }
}
