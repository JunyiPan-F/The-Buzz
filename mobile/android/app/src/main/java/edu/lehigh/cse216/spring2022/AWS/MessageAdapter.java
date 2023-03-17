package edu.lehigh.cse216.spring2022.AWS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        ToggleButton button;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.likeButton);
            text = itemView.findViewById(R.id.message);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageModel item = messageList.get(position);
        //sets the TextView of message_layout to the value of message
        holder.text.setText(item.getMessage());
        //sets the status of the ToggleButton in message_layout based on the value of status
        holder.button.setChecked(toBoolean(item.getStatus()));

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
