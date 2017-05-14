package com.digitalfundamentals.quinemccluskey.mintermminimiza;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.digitalfundamentals.quinemccluskey.appClasses.FinalStageMinimizer;


/**
 * Created by Bassam on 5/14/2017.
 */

public class MintermsLoader extends AsyncTaskLoader<String> {

    String mintermsString, dontCaresString;
    FinalStageMinimizer finalStageMinimizer;


    public MintermsLoader(Context context, String mintermsString, String dontCaresString) {
        super(context);
        this.mintermsString = mintermsString;
        this.dontCaresString = dontCaresString;
        this.finalStageMinimizer = new FinalStageMinimizer(this.mintermsString, this.dontCaresString);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {

        return finalStageMinimizer.getMinimizedResult();
    }
}
