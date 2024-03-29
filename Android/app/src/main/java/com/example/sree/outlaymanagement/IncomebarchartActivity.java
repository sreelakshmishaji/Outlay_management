package com.example.sree.outlaymanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class IncomebarchartActivity extends AppCompatActivity {
    SharedPreferences sh;
    String id;
    String sh_name = "MyData";
    String name[] = null, amount[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomebarchart);
        sh = getSharedPreferences(sh_name, MODE_PRIVATE);
        id = sh.getString("User_id", " ");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        String type = getIntent().getStringExtra("type");

        inbar ib = new inbar();
        ib.execute(id, startDate, endDate);
    }

    private class inbar extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wc = new WebServiceCaller();
            wc.setSoapObject("incomebarchart");
            wc.addProperty("Personid", strings[0]);
            wc.addProperty("Fromdate", strings[1]);
            wc.addProperty("Todate", strings[2]);
            wc.callWebService();
            return wc.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("allincome",s);
                JSONArray j = new JSONArray(s);
                name = new String[j.length()];
                amount = new String[j.length()];
                for (int i = 0; i < j.length(); i++) {
                    JSONObject jo = j.getJSONObject(i);
                    name[i] = jo.getString("Name");
                    amount[i] = jo.getString("Amount");
                }
                Intent i = new Intent(IncomebarchartActivity.this,IBarActivity.class);
                i.putExtra("myJSON",j.toString() );
                Toast.makeText(IncomebarchartActivity.this,"intent",Toast.LENGTH_SHORT).show();
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
