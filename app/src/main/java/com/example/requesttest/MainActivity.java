package com.example.requesttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.service.voice.VoiceInteractionService;
import android.text.TextUtils;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    boolean loggedIN;

    //private static int SPLASH_TIME_OUT = 2;

    private TextInputLayout NameLayout;
    private TextInputLayout PasswordLayout;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final RequestQueue requestQueue= Volley.newRequestQueue(this);

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        String username;
        String password;

        if(preferences.contains("username") && preferences.contains("password"))
        {
            username = preferences.getString("username", "");
            password = preferences.getString("password", "");
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            Log.e("uso", "remember me" );
            loginFunction(username, password, Info, requestQueue, 1);
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

        */

        //making main request Queue

        //URL variable that is going to be used in creating new requests

        Name = findViewById(R.id.loginUsername);
        Password = findViewById(R.id.loginPassword);
        Info = findViewById(R.id.loginStatus);

        Login = findViewById(R.id.loginButton);
        Register = findViewById(R.id.registerButton);
        NameLayout = findViewById(R.id.textInputLayoutUsername);
        PasswordLayout = findViewById(R.id.textInputLayoutPassword);

        Register.setOnClickListener( new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       registerFunction();
                   }
               }
        );


        Login.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View view) {
                        loginFunction(Name.getText().toString(), Password.getText().toString(), Info ,requestQueue, 0);
                    }
                }
        );
    }

    private void registerFunction()
    {
        startActivity(new Intent(MainActivity.this, RegisterScreen.class));
    }

    private void loginFunction(final String loginUsername, final String loginPassword, final TextView loginStatus,final RequestQueue requestQueue, final int flag) {

        if(TextUtils.isEmpty(loginUsername) && flag != 1)
        {
            NameLayout.setErrorEnabled(true);
            NameLayout.setError("Ovo polje je obavezno!");
        }

        else if(TextUtils.isEmpty(loginPassword) && flag != 1)
        {
            PasswordLayout.setErrorEnabled(true);
            PasswordLayout.setError("Ovo polje je obavezno!");
        }

        else {

            String URL = "https://foodpicker-api-deploy.herokuapp.com/users/name/" + loginUsername;
            Log.i("REST: ", "fetching data from " + URL);


            JsonArrayRequest objectRequest = new JsonArrayRequest(
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
                                    String responseEmail = response.getString("mail");

                                    Log.e("checking pw", responsePassword + " " + loginPassword);

                                    if (responsePassword.equals(loginPassword)) {
                                        Log.e("password matching: ", "login successful");
                                        loggedIN = true;

                                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("username", loginUsername);
                                        editor.putString("mail", responseEmail);
                                        editor.putString("password", loginPassword);
                                        editor.apply();

                                        Intent intent = new Intent (MainActivity.this, BarcodeScannerActivity.class);

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(flag == 0) {


                                if (loggedIN) {
                                    loginStatus.setTextColor(Color.GREEN);
                                    loginStatus.setText("Uspesna prijava!");


                                } else {
                                    loginStatus.setTextColor(Color.RED);
                                    loginStatus.setText("Pogresna sifra i/ili korisničko ime!");
                                }
                            }

                        }
                    },

                    new Response.ErrorListener() {

                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response", error.toString());
                            loginStatus.setTextColor(Color.RED);
                            loginStatus.setText("Nepostojeće korisničko ime!");
                        }
                    }

            );

            requestQueue.add(objectRequest);
        }

        //loginStatus.setText("trying to login as Username: " + loginUsername + " and Password: " + loginPassword);
    }
}
