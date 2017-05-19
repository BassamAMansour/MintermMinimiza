package com.digitalfundamentals.quinemccluskey.appClasses;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bassam on 5/6/2017.
 */
public class FirstStepMinimizer implements IFirstStepMinimizer {

    static int iterations = 0;

    @Override
    public List<Minterm> getPrimeImplicants(String minterms, String dontCares) {

        List<Minterm> primeImplicants;

        List<Minterm> mintermsInitialSortedList = getSortedMintermsFromString(minterms, dontCares);

        primeImplicants = getPrimeImplicants(mintermsInitialSortedList);

        primeImplicants = removeDuplicateMinterms(primeImplicants);

        return primeImplicants;
    }


    public List<Integer> getSortedMintermsValuesFromString(String mintermsString) {

        List<Minterm> minterms = getSortedMintermsFromString(mintermsString);
        List<Integer> mintermsValues = new ArrayList<>(minterms.size());

        for (Minterm minterm : minterms) {
            mintermsValues.add(minterm.getValue());
        }

        return mintermsValues;
    }

    public List<Minterm> getSortedMintermsFromString(String minterms) {
        return getSortedMintermsFromString(minterms, "");
    }

    @Override
    public List<Minterm> getSortedMintermsFromString(String minterms, String dontCares) {

        List<Minterm> mintermsList = getSortedMintermsFromString(minterms, false);

        List<Minterm> dontCaresList = getSortedMintermsFromString(dontCares, true);

        List<Minterm> allTermsList = new LinkedList<>();

        allTermsList.addAll(mintermsList);
        allTermsList.addAll(dontCaresList);

        allTermsList = sortMinterms(allTermsList);

        allTermsList = removeDuplicateMinterms(allTermsList);

        return allTermsList;
    }


    private List<Minterm> getPrimeImplicants(List<Minterm> mintermsInitialList) {

        List<Minterm> primeImplicants;

        List<Minterm>[] firstColumnOfMintermsArray = getFirstColumnOfMintermsArray(mintermsInitialList);

        List<Minterm>[][] columnsArray = getColumnsArray(firstColumnOfMintermsArray);

        primeImplicants = getNotCrossedMintermsList(columnsArray);

        return primeImplicants;
    }

    private List<Minterm>[] getFirstColumnOfMintermsArray(List<Minterm> mintermsInitialList) {

        int maximumMintermValue = mintermsInitialList.get(mintermsInitialList.size() - 1).getValue();

        int maximumNumberOfBits = (int) Math.ceil(Math.log(maximumMintermValue) / Math.log(2));

        if(maximumMintermValue == 1){
            maximumNumberOfBits++;
        }

        List<Minterm>[] firstColumnMinterms = new List[maximumNumberOfBits + 1];

        for (int i = 0; i < mintermsInitialList.size(); i++) {

            iterations++;

            Minterm minterm = mintermsInitialList.get(i);

            int indexOfTheGroup = minterm.getNumberOfOnesInBinary();

            if (firstColumnMinterms[indexOfTheGroup] == null) {
                List<Minterm> newGroupList = new LinkedList<Minterm>();
                newGroupList.add(minterm);
                firstColumnMinterms[indexOfTheGroup] = newGroupList;
            } else {
                firstColumnMinterms[indexOfTheGroup].add(minterm);
            }
        }
        for (int i = 0; i < firstColumnMinterms.length; i++) {
            if (firstColumnMinterms[i] == null) {
                firstColumnMinterms[i] = new LinkedList<Minterm>();
            }
        }

        return firstColumnMinterms;
    }

    private List<Minterm>[][] getColumnsArray(List<Minterm>[] firstColumnOfMintermsArray) {

        int maxNumberOfBits = firstColumnOfMintermsArray.length - 1;

        List<Minterm>[][] columnsArray = new List[maxNumberOfBits][];

        columnsArray[0] = firstColumnOfMintermsArray;

        for (int i = 1; i < maxNumberOfBits; i++) {

            if (isColumnEmpty(columnsArray[i - 1])) {
                break;
            }

            columnsArray[i] = crossMintermsInColumn(columnsArray[i - 1]);
        }

        return columnsArray;
    }


    private List<Minterm>[] crossMintermsInColumn(List<Minterm>[] columnArray) {

        List<Minterm>[] crossedGroupsOfMinterms = new List[columnArray.length - 1];

        for (int i = 0; i < crossedGroupsOfMinterms.length; i++) {
            crossedGroupsOfMinterms[i] = crossGroupsOfMinterms(columnArray[i], columnArray[i + 1]);
        }
        return crossedGroupsOfMinterms;
    }

    private List<Minterm> crossGroupsOfMinterms(List<Minterm> firstGroupList, List<Minterm> secondGroupList) {

        List<Minterm> crossedGroup = new LinkedList<>();

        if (firstGroupList == null || secondGroupList == null || firstGroupList.isEmpty() || secondGroupList.isEmpty()) {
            return new LinkedList<Minterm>();
        }

        for (int i = 0; i < firstGroupList.size(); i++) {

            Minterm firstMinterm = firstGroupList.get(i);

            for (int j = 0; j < secondGroupList.size(); j++) {

                Minterm secondMinterm = secondGroupList.get(j);

                if (firstMinterm.getValue() < secondMinterm.getValue()) {

                    int differenceBetweenMinterms = secondMinterm.getValue() - firstMinterm.getValue();

                    if (isSameDifferenceList(firstMinterm.getDifferenceList(), secondMinterm.getDifferenceList())) {

                        if (isFromPowersOfTwo(differenceBetweenMinterms)) {

                            List<Integer> minimizedTermDifferenceList = new LinkedList<>(firstMinterm.getDifferenceList());

                            minimizedTermDifferenceList.add(differenceBetweenMinterms);

                            Minterm minimizedTerm = new Minterm(firstMinterm.getValue(), false);

                            minimizedTerm.setDifferenceList(minimizedTermDifferenceList);

                            firstMinterm.setChecked(true);
                            secondMinterm.setChecked(true);

                            crossedGroup.add(minimizedTerm);

                            iterations++;
                        }
                    }

                }
            }
        }
        return crossedGroup;
    }

    private List<Minterm> removeDuplicateMinterms(List<Minterm> primeImplicants) {

        for (int i = 0; i < primeImplicants.size(); i++) {

            int firstMintermValue = primeImplicants.get(i).getValue();
            List<Integer> firstMintermDifferenceList = primeImplicants.get(i).getDifferenceList();

            for (int j = i + 1; j < primeImplicants.size(); j++) {
                iterations++;
                int secondMintermValue = primeImplicants.get(j).getValue();
                List<Integer> secondMintermDifferenceList = primeImplicants.get(j).getDifferenceList();

                if ((firstMintermValue == secondMintermValue) && (isSameDifferenceList(firstMintermDifferenceList, secondMintermDifferenceList))) {
                    primeImplicants.remove(j);
                    j--;
                }
            }
        }


        return primeImplicants;
    }

    private List<Minterm> getSortedMintermsFromString(String minterms, boolean dontCare) {

        List<Minterm> mintermsList = new LinkedList<>();

        int stringIndex = 0;

        StringBuilder mintermString = new StringBuilder();

        while (stringIndex < minterms.length()) {

            iterations++;

            if (Character.isDigit(minterms.charAt(stringIndex))) {

                mintermString.append(minterms.charAt(stringIndex));

            } else if (mintermsList.size() != 0 && mintermString.toString().equals("")) {

                stringIndex++;
                continue;

            } else {

                Minterm minterm = new Minterm(Integer.parseInt(mintermString.toString()), dontCare);

                mintermsList.add(minterm);

                mintermString = new StringBuilder();
            }
            stringIndex++;
        }

        if (mintermString.length() != 0) {
            Minterm minterm = new Minterm(Integer.parseInt(mintermString.toString()), dontCare);

            mintermsList.add(minterm);
        }

        return mintermsList;
    }

    private boolean isSameDifferenceList(List<Integer> firstList, List<Integer> secondList) {
        if (firstList.size() == secondList.size()) {
            for (int i = 0; i < firstList.size(); i++) {
                if (firstList.get(i) != secondList.get(i)) {
                    return false;
                }
            }
            return true;

        } else {
            return false;
        }

    }

    private List<Minterm> getNotCrossedMintermsList(List<Minterm>[][] columnsArray) {

        List<Minterm> primeImplicants = new LinkedList<>();

        for (int i = 0; i < columnsArray.length; i++) {

            List<Minterm>[] columnArray = columnsArray[i];

            if (isColumnEmpty(columnArray)) {
                break;
            }

            for (int j = 0; j < columnArray.length; j++) {

                List<Minterm> groupOfMintermsList = columnArray[j];

                if (groupOfMintermsList == null) {
                    continue;
                }

                for (int k = 0; k < groupOfMintermsList.size(); k++) {

                    Minterm minterm = groupOfMintermsList.get(k);
                    iterations++;
                    if (!minterm.isChecked()) {
                        primeImplicants.add(minterm);
                    }
                }
            }
        }

        return primeImplicants;

    }

    private boolean isColumnEmpty(List<Minterm>[] columnArray) {

        for (int i = 0; i < columnArray.length; i++) {
            if (columnArray[i].size() != 0) {
                return false;
            }
        }
        return true;
    }

    private List<Minterm> sortMinterms(List<Minterm> mintermsList) {

        List<Minterm> sortedList = new LinkedList<>();

        while (mintermsList.size() != 0) {

            int smallestMintermIndex = 0;

            Minterm smallestMinterm = mintermsList.get(0);

            for (int i = 1; i < mintermsList.size(); i++) {
                if (smallestMinterm.getValue() > mintermsList.get(i).getValue()) {
                    smallestMinterm = mintermsList.get(i);
                    smallestMintermIndex = i;
                }
            }

            sortedList.add(smallestMinterm);
            mintermsList.remove(smallestMintermIndex);

        }

        if (mintermsList.size() > 0) {
            for (int i = 0; i < mintermsList.size(); i++) {
                sortedList.add(mintermsList.get(i));
            }
        }

        return sortedList;
    }

    private boolean isFromPowersOfTwo(int value) {
        return value > 0 && ((value & -value) == value);
    }
}
