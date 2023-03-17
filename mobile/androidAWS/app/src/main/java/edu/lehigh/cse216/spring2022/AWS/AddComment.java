package edu.lehigh.cse216.spring2022.AWS;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collection;
import java.util.Set;

public class AddComment extends BottomSheetDialogFragment {


    public static final String TAG = "ActionBottomDialog";

    private EditText newCommentText;
    private Button newCommentButton;
    private EditText editCommentText;
    private Button editCommentButton;
    public static AddComment newInstance() {
        return new AddComment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.new_comment, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return itemView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newCommentText = getView().findViewById(R.id.newCommentText);
        newCommentButton = getView().findViewById(R.id.newCommentButton);
        editCommentText = getView().findViewById(R.id.editCommentText);
        editCommentButton = getView().findViewById(R.id.editCommentButton);
        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newCommentText.setText(task);
            assert task != null;
            if (task.length() > 0)
                newCommentButton.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
        }
        newCommentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    newCommentButton.setEnabled(false);
                    newCommentButton.setTextColor(Color.GRAY);
                } else {
                    newCommentButton.setEnabled(true);
                    newCommentButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    newCommentButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editCommentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    editCommentButton.setEnabled(false);
                    editCommentButton.setTextColor(Color.GRAY);
                } else {
                    editCommentButton.setEnabled(true);
                    editCommentButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    editCommentButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newCommentText.getText().toString();
                MessageModel message = new MessageModel();
                String name = message.getName();
                Map<String,String> comment_info = new HashMap<String,String>();
                comment_info.put(text,name);
                message.setCommentsInfo(comment_info);
                message.setStatus(0);
            }
        });
    }

}