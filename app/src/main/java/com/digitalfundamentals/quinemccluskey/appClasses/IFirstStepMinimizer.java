package com.digitalfundamentals.quinemccluskey.appClasses;

import java.util.List;

/**
 * Created by Bassam on 5/6/2017.
 */
public interface IFirstStepMinimizer {

    List<Minterm> getPrimeImplicants(String minterms, String dontCares);
    List<Minterm> getSortedMintermsFromString(String minterms, String dontCares);

}
