package com.digitalfundamentals.quinemccluskey.appClasses;

import java.util.LinkedList;
import java.util.List;

public class SecondStepMinimizer {

    // tested
    // position of ones in a digit

    /**
     * additional function here u are given the integer and return the index of
     * one from right to left
     */
    public List PositionOfOnesInverted(int x) {
        String z = Integer.toBinaryString(x);
        int count = 0;
        List ones = new LinkedList<>();
        for (int i = z.length() - 1; i >= 0; i--) {
            if (z.charAt(i) == '1') {
                ones.add(z.length() - i - 1);
            }
        }
        return ones;
    }
    // tested

    /**
     * you are given here the implicant in the form 2( 1 , 4 ) and return a list
     * which contains the expansion in the form 2 , 3 , 6 ,7
     */
    public List[] FormExpandedTerms(List<Minterm> implicant) {
        int tst1 = implicant.size();
        List[] expandedterm = new LinkedList[implicant.size()];

        for (int i = 0; i < implicant.size(); i++) {
            Integer l = implicant.get(i).getValue();
            if (expandedterm[i] == null) {
                expandedterm[i] = new LinkedList();
            }
            for (int j = 0; j < (int) Math.pow(2, implicant.get(i).getDifferenceList().size()); j++) {
                List no_ones = this.PositionOfOnesInverted(j);
                int tmp_sum = implicant.get(i).getValue();
                for (int k = 0; k < no_ones.size(); k++) {
                    tmp_sum += (int) implicant.get(i).getDifferenceList().get((int) no_ones.get(k));
                }
                expandedterm[i].add(tmp_sum);
            }
        }
        return expandedterm;
    }

    // tested

    /**
     * here u are given the minterms and array of list of implicant (the
     * position of x's in the table and return a list with the index
     */
    public List MintermCover(List[] implicant, Integer minterm) {
        List IndexOfImplicants = new LinkedList();
        for (int i = 0; i < implicant.length; i++) {
            for (int j = 0; j < implicant[i].size(); j++) {
                if (implicant[i].get(j) == minterm) {
                    IndexOfImplicants.add((int) Math.pow(2, i));
                    break;
                }
            }
        }
        return IndexOfImplicants;
    }

    // tested

    /**
     * here it multiply out the implicants which are covered in the table and
     * return the result in binary where example : 3 means 0011 means 1st
     * implicant and 2nd one 5 means 0101 means 1st and 3rd implicants 4 means
     * 0100 means only the 3rd implicant
     */
    public List StartPetrick(List[] implicant, List minterm) {
        List<Integer> CurrentCovery = new LinkedList<Integer>();
        List<Integer> PrevCovery = new LinkedList<Integer>();
        List<Integer> intermediate = new LinkedList<Integer>();
        List<Integer> tmplist = new LinkedList<Integer>();
        int sizePrevCovery = 0;
        PrevCovery = MintermCover(implicant, (Integer) minterm.get(0));
        for (int i = 1; i < minterm.size(); i++) {
            CurrentCovery = MintermCover(implicant, (Integer) minterm.get(i));
            sizePrevCovery = PrevCovery.size();
            for (int m = 0; m < sizePrevCovery; m++) {
                for (int n = 0; n < CurrentCovery.size(); n++) {
                    int a = PrevCovery.get(m);
                    int b = CurrentCovery.get(n);
                    int result = a | b;
                    intermediate.add(result);
                }
            }
            while (!PrevCovery.isEmpty()) {
                PrevCovery.remove(0);
            }
            tmplist = filterList(intermediate);
            for (int k = 0; k < tmplist.size(); k++) {
                PrevCovery.add(tmplist.get(k));
            }
            while (!intermediate.isEmpty()) {
                intermediate.remove(0);
            }
        }
        return PrevCovery;
    }

    // tested

    /**
     * additional function used by StartPetrik here u give it the list resulted
     * from the multiplication of implicants and simplification is done here to
     * be returned
     */
    public List filterList(List unfiltered) {
        List x = unfiltered;
        for (int i = 0; i < x.size() - 1; i++) {
            for (int j = i + 1; j < x.size(); j++) {
                if (((int) x.get(i) & (int) x.get(j)) == (int) x.get(i)) {
                    x.remove(j);
                    j--;

                } else if (((int) x.get(i) & (int) x.get(j)) == (int) x.get(j)) {
                    x.remove(i);
                    i--;
                    break;
                }
            }
        }
        return x;
    }

    // tested

    /**
     * here u are given list of implicants and it return a list contains the
     * minimal cost ones and if there is more than one it is added in the list
     */
    public List minCost(List implicant) {
        List<Integer> result = new LinkedList<Integer>();
        result.add((Integer) implicant.get(0));
        for (int i = 1; i < implicant.size(); i++) {
            if (Integer.bitCount((int) result.get(0)) > Integer.bitCount((int) implicant.get(i))) {
                while (!result.isEmpty()) {
                    result.remove(0);
                }
                result.add((int) implicant.get(i));
            } else if (Integer.bitCount((int) result.get(0)) == Integer.bitCount((int) implicant.get(i))) {
                result.add((int) implicant.get(i));
            }
        }
        return result;
    }

    // tested

    /**
     * here u give this function the result from petrick in binary and it
     * display it in the form of literals
     */
    public String DisplayResult(int PetrickResult, List<Minterm> implicant, int maxterm) {

        String result = "";
        List<Integer> terms = new LinkedList<Integer>();
        List PositionOfOnes = this.PositionOfOnesInverted(PetrickResult);
        int index = 0;
        for (int k = 0; k < Integer.bitCount(PetrickResult); k++) {
            char literal = 'A';
            index = (Integer) PositionOfOnes.get(k);
            char[] binary = new char[Integer.toBinaryString(maxterm).length()];
            List OnesIndex = PositionOfOnesInverted(implicant.get(index).getValue());

            for (int i = OnesIndex.size() - 1; i >= 0; i--) {
                binary[binary.length - (int) OnesIndex.get(i) - 1] = '1';
            }
            int tst = implicant.get(index).getDifferenceList().size();
            for (int i = 0; i < implicant.get(index).getDifferenceList().size(); i++) {
                int indexOfDashes = this.logbase2(implicant.get(index).getDifferenceList().get(i));
                binary[binary.length - indexOfDashes - 1] = '-';
            }
            for (int i = 0; i < binary.length; i++) {
                if (binary[i] != '1' && binary[i] != '-')
                    binary[i] = '0';
            }
            for (int i = 0; i < binary.length; i++) {
                if (binary[i] == '0') {
                    result += literal + "'";
                } else if (binary[i] == '1') {
                    result += literal;
                }
                literal = (char) (literal + 1);
            }

            if (k != Integer.bitCount(PetrickResult) - 1) {
                result += " + ";
            }
        }
        return result;

    }

    /**
     * function to find the log of an integer to the base 2
     */
    public int logbase2(int x) {
        for (int i = 0; i < x; i++) {
            if ((int) Math.pow(2, i) == x)
                return i;
        }
        return 0;
    }

}
