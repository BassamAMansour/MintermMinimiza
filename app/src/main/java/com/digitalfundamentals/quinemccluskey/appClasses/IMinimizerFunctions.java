package com.digitalfundamentals.quinemccluskey.appClasses;

import java.util.List;

/**
 * Created by Bassam on 5/4/2017.
 */
public interface IMinimizerFunctions {

    /**
     * This function returns a list containing the columns of each successive cross outs.
     * Each column contains a list of each group of minterms that belongs to the same number of ones in
     * their binary representation.
     * This should be the function used by the GUI (main function) that does the first step of minimization process
     */

    List<List<List<Minterm>>> getListOfColumns(String mintermsString);

    /**
     * Takes a raw string input and converts it to a list of minterms .
     * Checks also if the minterm being scanned is a don't care or not.
     * Should be used by getListOfColumns() function to generate the initial list of minterms.
     */

    List<Minterm> getMintermsListFromStringInput(String mintermsString);

    /**
     * This function will be initially used to sort the given "raw" list of minterms and categorises them according
     * to the number of ones in their binary representation.
     * Should be applied on the resulting list from getMintermsListFromStringInput() function;
     */

    List<List<Minterm>> sortMintermsAccordingToNumberOfOnesInBinary(List<Minterm> rawMinterms);

    /**
     * Adds column of groups of minterms to the main columns list.
     */

    List<List<List<Minterm>>> addColumnToColumnsList(List<List<List<Minterm>>> columnsList, List<List<Minterm>> column);

    /**
     * Adds group of minterms to the column.
     */

    List<List<Minterm>> addGroupToColumn(List<List<Minterm>> column, List<Minterm> group);

    /**
     * Adds a minterm to a given group of minterms.
     */

    List<Minterm> addMintermToGroup(List<Minterm> group, Minterm minterm);

    /**
     * Crosses the elements of the former group of minterms with the following group of minterms.
     */

    List<Minterm> crossMintermsOfAdjacentGroups(List<Minterm> firstGroup, List<Minterm> secondGroup);

    /**
     * Crosses two minterms together, returns null if the minterms' difference is not a power of 2.
     */

    Minterm crossTwoMinterms(Minterm firstMinterm, Minterm secondMinterm);

    /**
     * Returns a boolean indicating whether the number is a power of two or not.
     * should be used before crossing out the minterms by testing the difference between them first.
     */
    boolean isPowerOfTwo(int number);

}
