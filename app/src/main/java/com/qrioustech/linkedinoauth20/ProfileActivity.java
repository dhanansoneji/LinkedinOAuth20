package com.qrioustech.linkedinoauth20;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class ProfileActivity extends AppCompatActivity {

    private static final String PROFILE_URL = "https://api.linkedin.com/v2/me";
    private static String ACCESS_TOKEN ;
    private static String EMAIL_URL = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    private TextView welcomeText;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences preferences = this.getSharedPreferences("user_info", 0);

        welcomeText = findViewById(R.id.activity_profile_welcome_text);

        //Request basic profile of the user
        ACCESS_TOKEN = preferences.getString("accessToken", null);
        Log.e("ACCESS",ACCESS_TOKEN);
        if(ACCESS_TOKEN!=null){

            try {
                new GetProfileRequestAsyncTask().execute(PROFILE_URL);
                new GetEmailRequestAsyncTask().execute(EMAIL_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class GetProfileRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(ProfileActivity.this, "", "Loading",true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {

            if(urls.length>0){

                URL url1 = null;
                String url = urls[0];
                try {
                    url1 = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection)url1.openConnection();

                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                    con.setRequestProperty("cache-control", "no-cache");
                    con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder jsonString = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        jsonString.append(line);
                    }
                    br.close();

                    return new JSONObject(jsonString.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(data!=null){
                try {
                    Log.e("RESPONSE",data.toString());
                    Log.e("LastName",data.getString("localizedLastName"));
                    Log.e("FirstName",data.getString("localizedFirstName"));
                    Log.e("ProfilePicture",data.getJSONObject("profilePicture").getString("displayImage"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }
    private class GetEmailRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(ProfileActivity.this, "", "Loading",true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {

            if(urls.length>0){

                URL url1 = null;
                String url = urls[0];
                try {
                    url1 = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection)url1.openConnection();

                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                    con.setRequestProperty("cache-control", "no-cache");
                    con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder jsonString = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        jsonString.append(line);
                    }
                    br.close();
                    return new JSONObject(jsonString.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(data!=null){
                try {
                    Log.e("RESPONSE",data.toString());
                    Log.e("elements", String.valueOf(data.getJSONArray("elements")));
                    Log.e("handle",data.getJSONArray("elements").getJSONObject(0).getString("handle"));
                    Log.e("emailAddress",data.getJSONArray("elements").getJSONObject(0).getJSONObject("handle~").getString("emailAddress"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

}
