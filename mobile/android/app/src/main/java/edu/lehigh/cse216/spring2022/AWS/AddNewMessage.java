package edu.lehigh.cse216.spring2022.AWS;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewMessage extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newMessageText;
    private Button newMessageButton;

    public static AddNewMessage newInstance(){
        return new AddNewMessage();
    }


    public void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }


    public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View itemView = inflater.inflate(R.layout.new_message, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return itemView;
    }

    @Override
    public void onViewCreated(View itemView, Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);
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
                    newMessageButton.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
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
