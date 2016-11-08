package com.example.pragati.simplebargraph;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity implements View.OnClickListener{


    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    public static String username,name,email,password;

    private Button buttonRegister;
    private static final String REGISTER_URL = Values.web_url + "register.php";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //check if user has already logged in
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = sharedpreferences.getBoolean("login_status", false);
        if(status == true)
        {
            Values.username = sharedpreferences.getString("username", "");
            Intent intent = new Intent(Register.this,Signin_n.class);
            intent.putExtra("username",Values.username);
            startActivity(intent);
        }
        //

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
    }

    public void registerUser() {
        name = editTextName.getText().toString().trim().toLowerCase();
        username = editTextUsername.getText().toString().trim().toLowerCase();
        password = editTextPassword.getText().toString().trim().toLowerCase();
        email = editTextEmail.getText().toString().trim().toLowerCase();

        register(name,username,password,email);
    }

    private void register(final String name, String username, String password, String email) {
        String urlSuffix = "?name="+name+"&username="+username+"&password="+password+"&email="+email;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if(s.equals("successfully registered"))
                {
                    Toast.makeText(getApplicationContext(),"Welcome " + name ,Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(Register.this,Signin_n.class);
                    intent1.putExtra("username", Register.username);
                    Values.username = Register.username; // setting username that can be accessed from anywhere
                    /*Set shared preferences*/
                    SharedPreferences sharedpreferences = getSharedPreferences(Values.MyPRE_Login, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", Values.username);
                    editor.putString("login_status", "true");
                    editor.commit();

                    startActivity(intent1);
                }
                else {
                    Toast.makeText(getApplicationContext(),s ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                String result_v = "";
                //BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(15000);
                    con.setReadTimeout(15000);
                    con.connect();
                    int status = con.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            br.close();
                            result_v = sb.toString();
                    }


                }catch(Exception e){
                    return null;
                }

                return result_v;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }


    public void Call_pd(View v)
    {
        Intent intent = new Intent(Register.this, Signin_n.class);
        //intent.putExtra("username", username);
        startActivity(intent);
    }

    public void Call_Signin(View v)
    {
        Intent intent = new Intent(Register.this, Signin_n.class);
        startActivity(intent);
    }

}