package com.example.requesttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);


        final RequestQueue rQueue= Volley.newRequestQueue(this);

        final EditText registerEmail = findViewById(R.id.registerMail);
        final EditText registerUsername = findViewById(R.id.registerName);
        final EditText registerPassword = findViewById(R.id.registerPassword);
        final EditText registerPasswordRepeat = findViewById(R.id.registerPasswordRepeated);


        final Button backToLoginButton = findViewById(R.id.Back_button);
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterScreen.this, MainActivity.class));
            }
        });

        final Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                RegisterFunction(registerEmail,registerUsername,registerPassword,registerPasswordRepeat,rQueue);
            }

        });

    }

    private void RegisterFunction(final EditText registerEmail, final EditText registerUsername, final EditText registerPassword,
                                  final EditText registerPasswordRepeat, final RequestQueue rQueue)
    {

        String URL = "https://foodpicker-api-deploy.herokuapp.com/users";

        //vlado radi rest post jebi se

    }
}
