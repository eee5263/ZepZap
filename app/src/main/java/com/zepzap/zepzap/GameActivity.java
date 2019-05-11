package com.zepzap.zepzap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.internal.Util;


public class GameActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private int score;
    private Timer timer;
    private TextView remainingSec;
    private static int totalMillis = 30000;
    private Button timerBtn;
    private TextView givenStringText;
    private EditText userStringText;
    private TextView scoreText;
    private String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        scoreText = (TextView)findViewById(R.id.scoreText);
        score = 0;
        remainingSec = findViewById(R.id.remainingSecText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        timerBtn = (Button)findViewById(R.id.timerBtn);

        givenStringText = (TextView) findViewById(R.id.givenText);
        givenStringText.setText("0123456789 0123456789");
        userStringText = (EditText) findViewById(R.id.typeText);
        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = 0;
                timerBtn.setEnabled(false);
                timer = new Timer(totalMillis, 1000);
                timer.start();
            }
        });

        sendJsonDataToServer();
    }
    public void parseString() {
        String givenString = givenStringText.getText().toString();
        String userString = userStringText.getText().toString();
        int userSize = userString.length();
        int givenSize = givenString.length();
        for (int i = 0; i < givenSize; i++) {
            if (i == givenSize) {
                break;
            }
            if (givenString.charAt(i) == userString.charAt(i)) {
                score++;
            }
        }
        showStats();
    }

    private void sendScoreToServer(){
        String URL = "http://3.16.157.244:8080/api";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        String token = new Token().getToken();
        StringEntity params = null;
        try {
            params = new StringEntity("{\"landMark\":\"GYEONGBOKGUNG\",\"score\":\"20\"} ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            //4
            httpPost.setEntity(params);
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqd3QudG9rZW5Jc3N1ZXIiLCJJRCI6MSwiTkFNRSI6Ik1pbmt5dSJ9.IIt9TUn9TnwzjoJGb9PH9VGifTAC-SP7ss30evhr3zw");

            //5
            org.apache.http.HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (UnsupportedEncodingException e)    {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
    }

    private void getRandomZepetoImage() throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL("http://3.16.157.244:8080/api/image/random");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqd3QudG9rZW5Jc3N1ZXIiLCJJRCI6MSwiTkFNRSI6Ik1pbmt5dSJ9.IIt9TUn9TnwzjoJGb9PH9VGifTAC-SP7ss30evhr3zw");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        String imageUrl = result.toString();
    }


    public void showStats() {
        scoreText.setText("Score: " + Integer.toString(score));
        sendScoreToServer();
    }

    public class Timer extends CountDownTimer {
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/1000);
            progressBar.setProgress(progressBar.getMax()-progress);
            remainingSec.setText("seconds remaining: " + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            remainingSec.setText("done!");
            timerBtn.setEnabled(true);
            parseString();
            showStats();
        }
    }

    private void sendJsonDataToServer(){

        String URL = "http://3.16.157.244:8080/api/users/join";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        StringEntity params = null;
        try {
            params = new StringEntity("{\"hashCode\":\"myzepetocode\",\"name\":\"Minkyu\"} ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            //4
            httpPost.setEntity(params);
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            //5
            org.apache.http.HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (UnsupportedEncodingException e)    {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
    }
}
