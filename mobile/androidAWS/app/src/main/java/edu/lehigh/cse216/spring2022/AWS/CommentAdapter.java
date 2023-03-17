package edu.lehigh.cse216.spring2022.AWS;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<CommentModel> commentList = new ArrayList<>();
    private Map<String,String> commentInfo = new HashMap<>();
    private CommentActivity activity;

    public CommentAdapter(CommentActivity activity, ArrayList<CommentModel> commentList){
        this.activity = activity;
        this.commentList = commentList;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //makes the layout of every item in the adapter in the format of comment_view.xml
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view, parent, false);
        return new CommentAdapter.ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cmt;
        Button btn_usrName;

        ViewHolder(View itemView) {
            super(itemView);
            cmt = itemView.findViewById(R.id.post_comment);
            btn_usrName = itemView.findViewById(R.id.btn_id);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        CommentModel item = commentList.get(position);
        //sets the TextView of comment_view to the value of comment
        holder.cmt.setText(item.getComment());
        //sets the name to the value of name
        holder.btn_usrName.setText(item.getCommentsInfo().entrySet().stream().findFirst().get().getKey());
        holder.cmt.setText(item.getCommentsInfo().entrySet().stream().findFirst().get().getValue());
        holder.cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddComment.class);
                intent.putExtra("USER_NAME", item.getName());
                activity.startActivity(intent);
            }
        });
        holder.btn_usrName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("USER_NAME", item.getName());
                activity.startActivity(intent);
            }
        });
    }

    //converts an integer to a boolean value
    private boolean toBoolean(int n){
        return n!=0;
    }

    //returns the size of the ArrayList of messages
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setComments(ArrayList<CommentModel> commentList){
        this.commentList = commentList;
        notifyDataSetChanged();
    }
}
