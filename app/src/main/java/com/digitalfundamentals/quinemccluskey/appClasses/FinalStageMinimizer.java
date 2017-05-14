package com.digitalfundamentals.quinemccluskey.appClasses;

import java.util.List;

/**
 * Created by Bassam on 5/13/2017.
 */
public class FinalStageMinimizer {

    String minterms, dontCares;


    public FinalStageMinimizer(String minterms, String dontCares) {
        this.minterms = minterms;
        this.dontCares = dontCares;
    }

    public String getMinimizedResult() {

        String minimizedResult;
        FirstStepMinimizer firstStepMinimizer = new FirstStepMinimizer();
        SecondStepMinimizer secondStepMinimizer = new SecondStepMinimizer();

        List<Minterm> primeImplicants = firstStepMinimizer.getPrimeImplicants(minterms, dontCares);

        List[] expandedMinterms = secondStepMinimizer.FormExpandedTerms(primeImplicants);

        List<Integer> mintermsValues = firstStepMinimizer.getSortedMintermsValuesFromString(minterms);

        List<Integer> petrickResultList = secondStepMinimizer.StartPetrick(expandedMinterms, mintermsValues);

        List<Integer> minimumCostTermsList = secondStepMinimizer.minCost(petrickResultList);

        minimizedResult = secondStepMinimizer.DisplayResult(minimumCostTermsList.get(0), primeImplicants, mintermsValues.get(mintermsValues.size() - 1));

        return minimizedResult;
    }

    public void setMinterms(String minterms) {
        this.minterms = minterms;
    }

    public void setDontCares(String dontCares) {
        this.dontCares = dontCares;
    }
}
