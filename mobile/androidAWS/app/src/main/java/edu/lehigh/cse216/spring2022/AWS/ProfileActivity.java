package edu.lehigh.cse216.spring2022.AWS;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
    TextView name,email,id;
    ProfileModel profileModel = new ProfileModel();
    //private ArrayList<ProfileModel> profArrayList = new ArrayList<>();
    //private ProfileAdapter profAdapter;
    //ListView profList;
    //String usr_name, usr_email, usr_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        String UsrNameToken = getIntent().getStringExtra("USER_NAME");
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url_prof = "https://thebuzzaws.herokuapp.com/:userId/profiles";

        // Request a comment response from the provided URL.
        StringRequest profRequest = new StringRequest(Request.Method.GET, url_prof,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        populateListFromVolley(response, UsrNameToken);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AWS", "That didn't work!");
            }
        });

        name = findViewById(R.id.textName);
        id = findViewById(R.id.textId);
        email = findViewById(R.id.textEmail);

        // Add the request to the RequestQueue.
        queue.add(profRequest);


//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            usr_name = acct.getDisplayName();
//            usr_email = acct.getEmail();
//            usr_id = acct.getId();
//        }
//        name.setText(usr_name);
//        id.setText(usr_id);
//        email.setText(usr_email);

    }


    @SuppressLint("WrongViewCast")
    private void populateListFromVolley(String response, String UsrNameToken){
        try {
            JSONArray json= new JSONArray(response);
            for (int i = 0; i < json.length(); ++i) {
                String  usr_id = json.getJSONObject(i).getString("userId");
                String usr_name = json.getJSONObject(i).getString("username");
                String usr_email = json.getJSONObject(i).getString("email");
//                profArrayList.get(i).setEmail(email);
//                profArrayList.get(i).setId(id);
//                profArrayList.get(i).setName(name);
                if(usr_name.compareTo(UsrNameToken) == 0){
                    profileModel.setName(usr_email);
                    name.setText(usr_name);
                    profileModel.setId(usr_id);
                    id.setText(usr_id);
                    profileModel.setEmail(usr_email);
                    email.setText(usr_email);
                }
            }
        } catch (final JSONException e) {
            Log.d("AWS", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("AWS", "Successfully parsed JSON file.");

    }
}