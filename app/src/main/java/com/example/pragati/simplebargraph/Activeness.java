package com.example.pragati.simplebargraph;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class Activeness extends AppCompatActivity {

    JSONArray obj;
    BarChart barChart;
    ArrayList<String> theDates;
    ArrayList<BarEntry> barEntries;
    List<Float> cb_data = new ArrayList<Float>();
    List<String> d_date = new ArrayList<String>();

    private Tracker mTracker;
    //private ViewPager mViewPager;
    @SuppressWarnings("FieldCanBeLocal")
    // private ImagePagerAdapter mImagePagerAdapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activeness);

        String query = "http://pavanifall15apps.esy.es/fitnessApp/get_info.php?user_id=" + Values.username + "&latest=true";
        new HttpAsyncTask().execute(query);



        ///////////////////////Google Analytics/////////////////////////////
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Set screen name.
        mTracker.setScreenName("Activeness Pie Graph");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());




    }

//    public void linegraph(View view){
//        Intent intent = new Intent(this, LineGraph.class);
//        startActivity(intent);
//    }



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
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);

        }

        float a,b,c,d;
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
                        System.out.print(result);

                        String activitiesminutesSedentary = js.getString("activitiesminutesSedentary");
                        String activitiesminutesLightlyActive = js.getString("activitiesminutesLightlyActive");
                        String activitiesminutesFairlyActive = js.getString("activitiesminutesFairlyActive");
                        String activitiesminutesVeryActive = js.getString("activitiesminutesVeryActive");

                        a = Float.parseFloat(activitiesminutesSedentary);
                        b = Float.parseFloat(activitiesminutesLightlyActive);
                        c = Float.parseFloat(activitiesminutesFairlyActive);
                        d = Float.parseFloat(activitiesminutesVeryActive);


                        System.out.print("value:" +a +b +c +d);

                        // Calorie Burnt
                        // String calorieBurnt = js.getString("activitiescalories");
                        //    float cb = Float.parseFloat(calorieBurnt);
                        //System.out.println("cb float " +cb);
                        //System.out.println("ActivityCalories" +calorieBurnt);
                        //    cb_data.add(Float.parseFloat(calorieBurnt));

//                        String c_date = js.getString("dateTime");
//                        d_date.add(c_date);
//                        System.out.println("DateTime" +c_date);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            PieChart pieChart = (PieChart) findViewById(R.id.chart1);

            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(a, 0));
            entries.add(new Entry(b, 1));
            entries.add(new Entry(c, 2));
            entries.add(new Entry(d, 3));

            PieDataSet dataset = new PieDataSet(entries, "# of Calls");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Sedentary");
            labels.add("Lightly Active");
            labels.add("FairlyActive");
            labels.add("Very Active");

            PieData data = new PieData(labels, dataset);
            dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
            pieChart.setDescription("Activity during Awake Time");
            pieChart.setData(data);

            pieChart.animateY(5000);

            pieChart.saveToGallery("/sd/mychart.jpg", 100);
        }

//
//            barEntries = new ArrayList<>();
//            for(int i=0; i< cb_data.size(); i++) {
//                barEntries.add(new BarEntry(cb_data.get(i), i));
//                System.out.print("bar enry" +barEntries);
//
//            }
//            barChart = (BarChart) findViewById(R.id.bargraph);
//            BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
//
//            theDates = new ArrayList<>();
//            for (int i=0; i< d_date.size(); i++) {
//                theDates.add(d_date.get(i));
//            }
//
//            BarData theData = new BarData(theDates, barDataSet);
//            barChart.setData(theData);
//            barChart.setEnabled(true);
//            barChart.setDragEnabled(true);
//            barChart.setScaleEnabled(true);
    }
}

