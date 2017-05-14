package com.digitalfundamentals.quinemccluskey.mintermminimiza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String MINTERM_STRING_KEY = "mintermsStringKey";
    static final String DONT_CARE_STRING_KEY = "dontCaresStringKey";
    private boolean toastDisplayedBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeLayout();

    }


    private void initializeLayout() {

        toastDisplayedBefore = false;

        initializeMintermEditText();

        initializeDontCareEditText();

        initializeDontCareCheckBox();

        initializeStartMinimizingButton();

    }


    private void initializeStartMinimizingButton() {

        Button startMinimizingButton = (Button) findViewById(R.id.start_simplification_button);

        startMinimizingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSimplifiedFunctionActivity();
            }
        });
    }

    private void startSimplifiedFunctionActivity() {

        String mintermsString = getStringFromEditText(R.id.minterms_edittext);

        if (!hasDigit(mintermsString)) {
            displayToast("Enter at least one minterm.");
            return;
        }

        String dontCaresString = getStringFromEditText(R.id.dont_cares_edittext);


        Intent simplifiedFunctionActivity = new Intent(MainActivity.this, SimplifiedFunctionActivity.class);

        simplifiedFunctionActivity.putExtra(MINTERM_STRING_KEY, mintermsString);
        simplifiedFunctionActivity.putExtra(DONT_CARE_STRING_KEY, dontCaresString);

        startActivity(simplifiedFunctionActivity);

    }

    private boolean hasDigit(String mintermsString) {

        for (int i = 0; i < mintermsString.length(); i++) {

            if (Character.isDigit(mintermsString.charAt(i))) {
                return true;
            }
        }

        return false;
    }


    private void initializeDontCareCheckBox() {

        final CheckBox dontCareCheckBox = (CheckBox) findViewById(R.id.dont_care_checkbox);

        dontCareCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDontCaresEditTextState(isDontCareActivated(dontCareCheckBox));
            }
        });

    }

    private void initializeDontCareEditText() {

        EditText dontCareEditText = (EditText) findViewById(R.id.dont_cares_edittext);

        if (!isDontCareActivated((CheckBox) findViewById(R.id.dont_care_checkbox))) {
            setDontCaresEditTextState(false);
        }

        dontCareEditText.setNextFocusDownId(R.id.start_simplification_button);
        dontCareEditText.setNextFocusForwardId(R.id.start_simplification_button);
        dontCareEditText.setNextFocusUpId(R.id.start_simplification_button);
        dontCareEditText.setNextFocusRightId(R.id.start_simplification_button);
        dontCareEditText.setNextFocusLeftId(R.id.start_simplification_button);

        dontCareEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMethodOfEnteringMintermsToast();
            }
        });
    }

    private void setDontCaresEditTextState(boolean activated) {

        EditText dontCaresEditText = (EditText) findViewById(R.id.dont_cares_edittext);

        dontCaresEditText.setFocusable(activated);
        dontCaresEditText.setFocusableInTouchMode(activated);

        if (!activated) {
            dontCaresEditText.setText("");
        }

    }

    private void initializeMintermEditText() {

        EditText mintermEditText = (EditText) findViewById(R.id.minterms_edittext);

        mintermEditText.setNextFocusDownId(R.id.dont_cares_edittext);
        mintermEditText.setNextFocusUpId(R.id.dont_cares_edittext);
        mintermEditText.setNextFocusForwardId(R.id.dont_cares_edittext);
        mintermEditText.setNextFocusLeftId(R.id.dont_cares_edittext);
        mintermEditText.setNextFocusRightId(R.id.dont_cares_edittext);

        mintermEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMethodOfEnteringMintermsToast();
            }
        });

    }

    private String getStringFromEditText(int editTextID) {

        EditText editText = (EditText) findViewById(editTextID);

        if (editText != null) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    private void displayMethodOfEnteringMintermsToast() {
        if (!toastDisplayedBefore) {
            displayToast(getString(R.string.method_of_entering_minterms));
            toastDisplayedBefore = true;
        }
    }

    private boolean isDontCareActivated(CheckBox dontCareCheckBox) {

        return !dontCareCheckBox.isChecked();
    }

    private void displayToast(String message) {

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
