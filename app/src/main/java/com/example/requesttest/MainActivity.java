package com.example.requesttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue= Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //making main request Queue


        //URL variable that is going to be used in creating new requests



        final EditText loginUsername = findViewById(R.id.loginUsername);
        final EditText loginPassword = findViewById(R.id.loginPassword);
        final TextView loginStatus = findViewById(R.id.loginStatus);


        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        loginFunction(loginUsername,loginPassword,loginStatus);
                    }
                }
        );






    }


    private void loginFunction(EditText loginUsername, EditText loginPassword, TextView loginStatus) {

        String URL = "https://postman-echo.com/get?foo1=bar1&foo2=bar2";

        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("Rest Response",error.toString());
                    }
                }

        );
        requestQueue.add(objectRequest);


        loginStatus.setText("trying to login as Username: " + loginUsername.getText().toString() + " and Password: " + loginPassword.getText().toString());
    }
}
