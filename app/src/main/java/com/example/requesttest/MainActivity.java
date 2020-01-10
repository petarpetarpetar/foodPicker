package com.example.requesttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    boolean loggedIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        //making main request Queue

        //URL variable that is going to be used in creating new requests

        final EditText loginUsername = findViewById(R.id.loginUsername);
        final EditText loginPassword = findViewById(R.id.loginPassword);
        final TextView loginStatus = findViewById(R.id.loginStatus);

        final Button loginButton = findViewById(R.id.loginButton);
        final Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener( new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       registerFunction();
                   }
               }
        );


        loginButton.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View view) {
                        loginFunction(loginUsername,loginPassword,loginStatus,requestQueue);
                    }
                }
        );
    }

    private void registerFunction()
    {
        startActivity(new Intent(MainActivity.this, RegisterScreen.class));
    }

    private void loginFunction(final EditText loginUsername, final EditText loginPassword, final TextView loginStatus,final RequestQueue requestQueue) {

        String URL = "https://foodpicker-api-deploy.herokuapp.com/users/name/"+loginUsername.getText().toString();
        Log.i("REST: ", "fetching data from "+URL);


        JsonArrayRequest objectRequest=new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArr) {

                        try {
                            JSONObject response = responseArr.getJSONObject(0);

                            try {
                                String responseUsername = response.getString("name");
                                String responsePassword = response.getString("password");

                                Log.e("checking pw", responsePassword + " " + loginPassword.getText().toString());

                                if(responsePassword.equals(loginPassword.getText().toString()))
                                {
                                    Log.e("password matching: ", "login successful");
                                    loggedIN = true;
                                    startActivity(new Intent(MainActivity.this, RegisterScreen.class));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }



                        if(loggedIN) {
                            loginStatus.setText("logged IN!");


                        }
                        else {
                            loginStatus.setText("Wrong password!");
                        }

                    }
                },

                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("Rest Response",error.toString());
                        loginStatus.setText("Non existing username!");
                    }
                }

        );



        requestQueue.add(objectRequest);


        //loginStatus.setText("trying to login as Username: " + loginUsername.getText().toString() + " and Password: " + loginPassword.getText().toString());
    }
}
