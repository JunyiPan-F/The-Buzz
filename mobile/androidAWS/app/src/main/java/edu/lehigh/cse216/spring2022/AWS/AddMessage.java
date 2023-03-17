package edu.lehigh.cse216.spring2022.AWS;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddMessage extends BottomSheetDialogFragment {


    public static final String TAG = "ActionBottomDialog";

    private EditText newMessageText;
    private Button newMessageButton;

    public static AddMessage newInstance(){
        return new AddMessage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.new_message, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return itemView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newMessageText = getView().findViewById(R.id.newMessageText);
        newMessageButton = getView().findViewById(R.id.newMessageButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newMessageText.setText(task);
            assert task != null;
            if (task.length() > 0)
                newMessageButton.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
        }
        newMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    newMessageButton.setEnabled(false);
                    newMessageButton.setTextColor(Color.GRAY);
                } else {
                    newMessageButton.setEnabled(true);
                    newMessageButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    newMessageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newMessageText.getText().toString();
                MessageModel message = new MessageModel();
                message.setMessage(text);
                message.setStatus(0);
            }
        });
    }
}