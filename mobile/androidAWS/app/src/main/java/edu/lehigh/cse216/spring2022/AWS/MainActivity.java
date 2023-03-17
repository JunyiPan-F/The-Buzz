package edu.lehigh.cse216.spring2022.AWS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private RecyclerView rvMessages;
    private MessageAdapter msgAdapter;
    private FloatingActionButton fab;
    private ArrayList<MessageModel> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        Intent intent = new Intent(this,SignInActivity.class);
        //a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url_msg = "https://thebuzzaws.herokuapp.com/messages";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_msg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        populateListFromVolley(response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AWS", "That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        // Instantiate the RequestQueue.
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        String mURL = "https://thebuzzaws.herokuapp.com/login";
        StringRequest postRequest = new StringRequest( com.android.volley.Request.Method.POST, mURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            postId(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("AWS", "That didn't work!");
                    }
                });
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                //add your parameters here as key-value pairs
//                params.put("username", username);
//                params.put("password", password);
//
//                return params;
//            }
//        };
        queue2.add(postRequest);


        //button for adding message
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMessage.newInstance().show(getSupportFragmentManager(), AddMessage.TAG);
            }
        });
    }

    private void postId(String response) throws IOException {
        URL url = new URL(response);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        Log.d("AWS", "Successful.");
    }

    public void onCustomToggleClick(View view) {
        Toast.makeText(this, "CustomToggle", Toast.LENGTH_SHORT).show();
    }
    //num is the like status of message; str is the message string
    private void populateListFromVolley(String response){
        try {
            JSONArray json= new JSONArray(response);
            for (int i = 0; i < json.length(); ++i) {
                int num = json.getJSONObject(i).getInt("num");
                String str = json.getJSONObject(i).getString("str");
                String id = json.getJSONObject(i).getString("id");
                ArrayList<Map<String,String>> commentInfo = (ArrayList<Map<String,String>>)json.getJSONObject(i).get("commentInfo");
                messageList.add(new MessageModel(str, num, id, commentInfo));
            }
        } catch (final JSONException e) {
            Log.d("AWS", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("AWS", "Successfully parsed JSON file.");
        rvMessages = findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessageAdapter(this, messageList);
        rvMessages.setAdapter(msgAdapter);
    }
}

