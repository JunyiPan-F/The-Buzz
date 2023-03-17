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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommentActivity extends AppCompatActivity {
    private RecyclerView rvComment;
    private CommentAdapter cmtAdapter;
    private ArrayList<CommentModel> commentList = new ArrayList<>();
    private ArrayList<MessageModel> messageList = new ArrayList<>();
    private CommentModel commentInfo = new CommentModel();
    private CommentAdapter.ViewHolder cmt_viewholder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url_cmt = "https://thebuzzaws.herokuapp.com/messages/:id/comments";
        // Request a comment response from the provided URL.
        StringRequest commentRequest = new StringRequest(Request.Method.GET, url_cmt,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        populateCommentListFromVolley(response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AWS", "That didn't work!");
            }
        });


        // Add the request to the RequestQueue.
        queue.add(commentRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongViewCast")
    private void populateCommentListFromVolley(String response){
        try {
            JSONArray json= new JSONArray(response);
            for (int i = 0; i < json.length(); ++i) {
                String str = json.getJSONObject(i).getString("str");
                String id = json.getJSONObject(i).getString("id");
                commentList.add(new CommentModel(str,id));
                commentInfo.setCommentsInfo(id,str);
                messageList.get(i).setCommentsInfo(commentInfo.getCommentsInfo());
                cmtAdapter.onBindViewHolder(cmt_viewholder,i);
            }
        } catch (final JSONException e) {
            Log.d("AWS", "Error parsing JSON file: " + e.getMessage());
            return;
        }

        Log.d("AWS", "Successfully parsed JSON file.");
        rvComment = findViewById(R.id.rvComment);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        cmtAdapter = new CommentAdapter(this, commentList);
        rvComment.setAdapter(cmtAdapter);
    }

    public Map<String,String> getCommentInfo(){
        return commentInfo.getCommentsInfo();
    }
}