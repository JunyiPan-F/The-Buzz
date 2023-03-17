package edu.lehigh.cse216.spring2022.AWS;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageModel> messageList = new ArrayList<>();
    private MainActivity activity;

    public MessageAdapter(MainActivity activity, ArrayList<MessageModel> messageList){
        this.activity = activity;
        this.messageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //makes the layout of every item in the adapter in the format of message_layout.xml
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ToggleButton btn_like;
        ToggleButton btn_dislike;
        TextView msg;
        Button btn_usrName;
        Button btn_viewC;
        Button btn_addC;

        ViewHolder(View itemView) {
            super(itemView);
            btn_like = itemView.findViewById(R.id.likeButton);
            btn_dislike = itemView.findViewById(R.id.dislikeButton);
            msg = itemView.findViewById(R.id.message);
            btn_usrName = itemView.findViewById(R.id.btn_id);
            btn_viewC = itemView.findViewById(R.id.viewCommentButton);
            btn_addC = itemView.findViewById(R.id.newCommentButton);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageModel item = messageList.get(position);
        //sets the TextView of message_layout to the value of message
        holder.msg.setText(item.getMessage());
        //sets the status of the ToggleButton in message_layout based on the value of status
        holder.btn_like.setChecked(toBoolean(item.getStatus()));
        holder.btn_dislike.setChecked(toBoolean(item.getStatus()));
        //sets the name to the value of name
        holder.btn_usrName.setText(item.getName());
        holder.btn_usrName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ProfileActivity.class);
                intent.putExtra("USER_NAME", item.getName());
                activity.startActivity(intent);
            }
        });
        holder.btn_viewC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CommentActivity.class);
                activity.startActivity(intent);
            }
        });

        holder.btn_addC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddComment.newInstance().show(activity.getSupportFragmentManager(), AddComment.TAG);
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
        return messageList.size();
    }

    public void setMessages(ArrayList<MessageModel> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }
}
