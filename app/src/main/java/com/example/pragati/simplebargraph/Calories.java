package com.example.pragati.simplebargraph;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Calories extends AppCompatActivity {

    JSONArray obj;
    BarChart barChart;
//    private static final String cal_URL = Values.web_url + "http://pavanifall15apps.esy.es/fitnessApp/all_records.php";
    ArrayList<String> theDates;
    ArrayList<BarEntry> barEntries;
    List<Float> cb_data = new ArrayList<Float>();
    List<String> d_date = new ArrayList<String>();
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        String query = "http://pavanifall15apps.esy.es/fitnessApp/all_records.php?user_id=" + Values.username;
        new HttpAsyncTask().execute(query);

        ///////////////////////Google Analytics/////////////////////////////
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Set screen name.
        mTracker.setScreenName("Calorie Bar Graph");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

       // ProgressDialog loading;


        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);

        }


        @Override
        protected void onPostExecute(String result) {
            parseJsonObject(result);
        }

        public void parseJsonObject(String result) {

            String jsonStr = result;
            //   JSONArray restaurants = null;

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray person = (new JSONArray(jsonStr));
                    for (int i = 0; i < person.length(); i++) {
                        JSONObject js = person.getJSONObject(i);

                        // Calorie Burnt
                        String calorieBurnt = js.getString("activitiescalories");
                        float cb = Float.parseFloat(calorieBurnt);
                        System.out.println("cb float " +cb);
                        System.out.println("ActivityCalories" +calorieBurnt);
                        cb_data.add(Float.parseFloat(calorieBurnt));

                        String c_date = js.getString("dateTime");
                        d_date.add(c_date);
                        System.out.println("DateTime" +c_date);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

//
            barEntries = new ArrayList<>();
            for(int i=0; i< cb_data.size(); i++) {
                barEntries.add(new BarEntry(cb_data.get(i), i));
                System.out.print("bar enry" +barEntries);

            }
            barChart = (BarChart) findViewById(R.id.bargraph);
            BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

            theDates = new ArrayList<>();
            for (int i=0; i< d_date.size(); i++) {
                theDates.add(d_date.get(i));
            }

            BarData theData = new BarData(theDates, barDataSet);
            barChart.setDescription("Calories burnt");
            barChart.setData(theData);
            barChart.setEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
        }
    }
}
