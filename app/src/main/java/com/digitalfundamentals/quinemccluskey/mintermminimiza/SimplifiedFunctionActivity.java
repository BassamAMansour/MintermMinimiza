package com.digitalfundamentals.quinemccluskey.mintermminimiza;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SimplifiedFunctionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private final int MINIMIZER_LOADER_ID=0;
    private String mintermsString = "";
    private String dontCaresString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplified_function);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mintermsString = extras.getString(MainActivity.MINTERM_STRING_KEY);
            this.dontCaresString = extras.getString(MainActivity.DONT_CARE_STRING_KEY);
        }

        initializeLoader();

    }

    private void initializeLoader() {

        getSupportLoaderManager().initLoader(MINIMIZER_LOADER_ID, null, this);

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        modifyOnCreateLoaderLayoutsVisibility();

        return new MintermsLoader(SimplifiedFunctionActivity.this, mintermsString, dontCaresString);
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String minimizedLiterals) {

        modifyOnLoadFinishedLayoutsVisibility();

        displayMinimizedLiterals(minimizedLiterals);

    }


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void displayMinimizedLiterals(String minimizedLiterals) {

        TextView minimizedLiteralsTextView = (TextView) findViewById(R.id.minimized_literals_textview);

        minimizedLiteralsTextView.setText(minimizedLiterals);

    }

    private void modifyOnCreateLoaderLayoutsVisibility() {

        modifyDisplayResultsLayoutVisibility(View.GONE);
        modifyLoadingResultsLayoutVisibility(View.VISIBLE);
    }

    private void modifyOnLoadFinishedLayoutsVisibility() {

        modifyDisplayResultsLayoutVisibility(View.VISIBLE);
        modifyLoadingResultsLayoutVisibility(View.GONE);
    }

    private void modifyLoadingResultsLayoutVisibility(int visibilityID) {
        RelativeLayout loadingResultsLayout = (RelativeLayout) findViewById(R.id.loading_relative_layout);
        loadingResultsLayout.setVisibility(visibilityID);

    }

    private void modifyDisplayResultsLayoutVisibility(int visibilityID) {
        LinearLayout displayResultsLayout = (LinearLayout) findViewById(R.id.display_results_linear_layout);
        displayResultsLayout.setVisibility(visibilityID);
    }


}
