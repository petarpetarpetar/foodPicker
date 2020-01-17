package com.example.requesttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterScreen extends AppCompatActivity {

    private EditText Email;
    private EditText UserName;
    private EditText Password;
    private EditText RePassword;
    private ImageButton BackToLogin;
    private Button RegisterButton;
    private TextInputLayout EmailLayout;
    private TextInputLayout UsernameLayout;
    private TextInputLayout PasswordLayout;
    private TextInputLayout RePasswordLayout;
    private boolean uspesanRegister;
    private TextView Response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);


        final RequestQueue rQueue= Volley.newRequestQueue(this);

        Email = findViewById(R.id.registerMail);
        UserName = findViewById(R.id.registerName);
        Password = findViewById(R.id.registerPassword);
        RePassword = findViewById(R.id.registerPasswordRepeated);
        EmailLayout = findViewById(R.id.textInputLayoutMail);
        UsernameLayout = findViewById(R.id.textInputLayoutIme);
        PasswordLayout = findViewById(R.id.textInputLayoutSifra);
        RePasswordLayout = findViewById(R.id.textInputLayoutSifraPonovo);
        Response = findViewById(R.id.response);


        BackToLogin = findViewById(R.id.back_button);
        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterScreen.this, MainActivity.class));
            }
        });

        RegisterButton = findViewById(R.id.registerButton);

        RegisterButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                RegisterFunction(Email, UserName, Password, RePassword,rQueue);
            }

        });

    }

    private void RegisterFunction(final EditText registerEmail, final EditText registerUsername, final EditText registerPassword,
                                  final EditText registerPasswordRepeat, final RequestQueue rQueue)
    {

        String URL = "https://foodpicker-api-deploy.herokuapp.com/users";

        EmailLayout.setErrorEnabled(false);
        UsernameLayout.setErrorEnabled(false);
        PasswordLayout.setErrorEnabled(false);
        RePasswordLayout.setErrorEnabled(false);

        if(TextUtils.isEmpty(registerUsername.getText().toString()))
        {
            UsernameLayout.setErrorEnabled(true);
            UsernameLayout.setError("Ovo polje je obavezno!");
        }

        else if(TextUtils.isEmpty(registerEmail.getText().toString()))
        {
            EmailLayout.setErrorEnabled(true);
            EmailLayout.setError("Ovo polje je obavezno!");
        }

        else if(TextUtils.isEmpty(registerPassword.getText().toString()))
        {
            PasswordLayout.setErrorEnabled(true);
            PasswordLayout.setError("Ovo polje je obavezno!");
        }

        else if(TextUtils.isEmpty(registerPasswordRepeat.getText().toString()))
        {
            RePasswordLayout.setErrorEnabled(true);
            RePasswordLayout.setError("Ovo polje je obavezno!");
        }

        else if(!registerPassword.getText().toString().equals(registerPasswordRepeat.getText().toString()))
        {
            PasswordLayout.setErrorEnabled(true);
            PasswordLayout.setError("Sifre nisu podjednake!");
            RePasswordLayout.setErrorEnabled(true);
            RePasswordLayout.setError("Sifre nisu podjednake!");
        }

        else if(!EmailCheck(registerEmail.getText().toString()))
        {
            EmailLayout.setErrorEnabled(true);
            EmailLayout.setError("E-Mail nije dobrog formata!");
        }

        else
        {
            PostNewUser(URL, rQueue, registerEmail.getText().toString(), registerUsername.getText().toString(), registerPassword.getText().toString());
        }

    }

    public boolean EmailCheck(String Email)
    {
        Pattern p =  Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(Email);
        boolean matchFound = m.matches();

        return matchFound;
    }

    public void PostNewUser(final String Url, final RequestQueue rQueue, final String Email, final String Username, final String Password)
    {
        JSONObject parametri = new JSONObject();

        uspesanRegister = true;

        try
        {
            parametri.put("mail", Email);
            parametri.put("name", Username);
            parametri.put("password", Password);
        }
        catch (Exception e)
        {
            RegisterButton.setText("Greska prilikom registracije.");
            return;
        }

        JsonObjectRequest jsor = new JsonObjectRequest(Request.Method.POST, Url, parametri, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("res", response.toString());
                try {
                    String ResponseEmail = response.getString("mail");
                    Log.d("mejl", ResponseEmail);
                    String ResponseUsername = response.getString("name");
                    Log.d("ime", ResponseUsername);
                    String ResponsePassword = response.getString("password");
                    Log.d("pass", ResponsePassword);
                    if(Email.equals(ResponseEmail) && Username.equals(ResponseUsername) && Password.equals(ResponsePassword))
                    {
                        Log.e("uspesan", "fin");
                        uspesanRegister = true;
                    }
                }
                catch (Exception e)
                {
                    Log.e("NEuspesan", "fin");
                    uspesanRegister = false;
                }
                //RegisterButton.setText("Uspesna registracija!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("greska", error.toString());
                //RegisterButton.setText("Neuspesna registracija.");
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        rQueue.add(jsor);

        if (uspesanRegister)
        {
            Response.setTextColor(Color.GREEN);
            Response.setText("Uspesna registracija!");
            startActivity(new Intent(RegisterScreen.this, MainActivity.class));
        }

        else
        {
            Response.setTextColor(Color.RED);
            Response.setText("E-Mail i Ime moraju biti jedinstveni!");
        }

    }
}
