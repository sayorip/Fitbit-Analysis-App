package com.example.pragati.simplebargraph;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signin_n extends AppCompatActivity {


    private EditText editTextUsername;
    private EditText editTextPassword;
    private static final String REGISTER_URL = Values.web_url + "Signin.php";
    public String status;
    public String msg;
    public String song_list_str;
    public static String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_n);


        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

    }


    public void Check_Signin(View v)
    {
        username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        SigninUser(username, password);

    }


    private void SigninUser(String username, String password) {
        String urlSuffix = "?username="+username+"&password="+password;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Signin_n.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                status = get_details("status", s);
                if (status.equals("success"))
                {
                    msg = get_details("msg", s);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Signin_n.this, MainListView.class);
                    intent.putExtra("username", Signin_n.username);
                    Values.username = Signin_n.username;
                    SharedPreferences sharedpreferences = getSharedPreferences(Values.MyPRE_Login, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", Values.username);
                    editor.putString("login_status", "true");
                    editor.commit();
                    startActivity(intent);
                }
                else
                {
                    msg = get_details("msg", s);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                String result_v = "";
                HttpURLConnection con = null;
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    System.setProperty("http.keepAlive", "false");
                    con = (HttpURLConnection) url.openConnection();
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
                                sb.append(line + "\n");
                            }
                            br.close();
                            result_v = sb.toString();


                    }


                }catch(Exception e){
                    return null;
                }finally {
                    con.disconnect();
                }

                return result_v;

            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

    public String get_details(String dts, String res)
    {
        String result = "";
        if (dts!= null && res!= null)
        {

            try {
                JSONObject json = new JSONObject(res);

                //Get the value
                result = json.optString(dts);


            } catch (JSONException e) {e.printStackTrace();}

        }

        return result;

    }

}



