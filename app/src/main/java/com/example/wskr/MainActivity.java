package com.example.wskr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText Vvod;
    private Button button;
    private TextView textView2;
    private TextView textView3;
    private Button button3;
    private Object MainActivity2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vvod = findViewById(R.id.Vvod);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        button3 = findViewById(R.id.button3);

        button3.setOnClickListener(
                view -> {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);

                }
        );

        button.setOnClickListener(view -> {
            if(Vvod.getText().toString().trim().equals(""))
                Toast.makeText(MainActivity.this, R.string.TextView2, Toast.LENGTH_LONG).show();
            else {
                String city = Vvod.getText().toString();
                String key = "957df1da202909b3ae5f4d756c49032f";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric";

                new URLinfo().execute(url);

            }

        });
    }

    private class URLinfo extends AsyncTask<String, String, String> {
        protected  void onPreExecute() {
            super.onPreExecute();
            textView2.setText("Ожидание");
        }



        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
            }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                textView2.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp"));
                textView3.setText("Облачность: " + jsonObject.getJSONObject("weather").getDouble("main"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        }

    }

