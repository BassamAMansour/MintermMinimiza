package com.digitalfundamentals.quinemccluskey.mintermminimiza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalfundamentals.quinemccluskey.appClasses.FirstStepMinimizer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String MINTERM_STRING_KEY = "mintermsStringKey";
    static final String DONT_CARE_STRING_KEY = "dontCaresStringKey";
    private final int MAXIMUM_ALLOWED_MINTERMS_VALUE = 100;
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
        String dontCaresString = "";

        if (isDontCareActivated((CheckBox) findViewById(R.id.dont_care_checkbox))) {
            dontCaresString = getStringFromEditText(R.id.dont_cares_edittext);
        }

        if (hasValueOverMaxAllowed(mintermsString) || hasValueOverMaxAllowed(dontCaresString)) {
            displayToast("You exceeded the maximum value of allowed minterms/don't cares which is " + MAXIMUM_ALLOWED_MINTERMS_VALUE);
            return;
        }

        Intent simplifiedFunctionActivity = new Intent(MainActivity.this, SimplifiedFunctionActivity.class);

        simplifiedFunctionActivity.putExtra(MINTERM_STRING_KEY, mintermsString);
        simplifiedFunctionActivity.putExtra(DONT_CARE_STRING_KEY, dontCaresString);

        startActivity(simplifiedFunctionActivity);

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


        dontCareEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMethodOfEnteringMintermsToast();
            }
        });
        dontCareEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    return true;
                } else {
                    return false;
                }

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

        mintermEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMethodOfEnteringMintermsToast();
            }
        });
        mintermEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    findViewById(R.id.dont_cares_edittext).requestFocus();
                    return true;
                } else {
                    return false;
                }
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

    private boolean hasValueOverMaxAllowed(String mintermsString) {

        if (mintermsString.equals("")) {
            return false;
        }
        FirstStepMinimizer firstStepMinimizer = new FirstStepMinimizer();

        List<Integer> mintermsValuesListSorted = firstStepMinimizer.getSortedMintermsValuesFromString(mintermsString);

        return mintermsValuesListSorted.get(mintermsValuesListSorted.size() - 1) > MAXIMUM_ALLOWED_MINTERMS_VALUE;

    }


    private boolean hasDigit(String mintermsString) {

        for (int i = 0; i < mintermsString.length(); i++) {

            if (Character.isDigit(mintermsString.charAt(i))) {
                return true;
            }
        }

        return false;
    }


    private boolean isDontCareActivated(CheckBox dontCareCheckBox) {

        return !dontCareCheckBox.isChecked();
    }

    private void displayToast(String message) {

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = this.getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
