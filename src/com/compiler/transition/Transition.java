package com.compiler.transition;

import com.compiler.machine.Robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Transition {

    ArrayList<ArrayList<Integer>> transitions;
    ArrayList<HashMap<String, ArrayList<Integer>>> newStates;
    ArrayList<ArrayList<String>> afd;

    public Transition(){
        this.transitions = new ArrayList<ArrayList<Integer>>();
        this.transitions.add(new ArrayList<Integer>(Arrays.asList(0)));
        this.newStates = new ArrayList<HashMap<String, ArrayList<Integer>>>();
        this.afd = new ArrayList<ArrayList<String>>();
    }

    public ArrayList<ArrayList<Integer>> getTransitions() {
        return transitions;
    }

    public ArrayList<HashMap<String, ArrayList<Integer>>> getNewStates() {
        return newStates;
    }

    public ArrayList<ArrayList<String>> getAfd() {
        return afd;
    }

    public void turnToDeterministic(Robot robot, ArrayList<String> words){
        this.searchFirstState(robot, 0);
        this.searchStates(robot, words, 0);
        this.turnToAFD(words, 0);
    }

    private void turnToAFD(ArrayList<String> words, int index){
        String empty = new String(new char[this.transitions.size()]).replace("\0", "ø,");
        afd.add(index, new ArrayList(Arrays.asList(empty.split(","))));
        for (String letter : words){
            if (!this.newStates.get(index).get(letter).isEmpty()){
                afd.get(index).set(this.transitions.indexOf(this.newStates.get(index).get(letter)), letter);
            }
            this.transitions.indexOf(this.newStates.get(index).get(letter));
        }
        if (this.newStates.size() > index + 1){
            this.turnToAFD(words, index + 1);
        }
    }

    public void searchFirstState(Robot robot, int row){
        int index = 0;
        for (String element : robot.getTransitions().get(row)){
            if (element.equals("ε")){
                if (this.transitions.get(0).indexOf(index) == -1){
                    this.transitions.get(0).add(index);
                }
                this.searchFirstState(robot, index);
            }
            index++;
        }
        Collections.sort(this.transitions.get(0));
    }

    public void searchStates(Robot robot, ArrayList<String> words, int index){
        this.getNewTransitions(robot, words, index);
        if (this.transitions.size() > (index + 1)){
            this.searchStates(robot, words, index + 1);
        }
    }

    public void getNewTransitions(Robot robot, ArrayList<String> words, int index){
        this.newStates.add(new HashMap<String, ArrayList<Integer>>());
        int sizeNewStates = this.newStates.size();
        for (String letter : words){
            ArrayList<Integer> finalTransition = new ArrayList();
            for (Integer element : this.transitions.get(index)){
                for (Integer foundWithLetter : this.findInRow(robot, letter, element)){
                    finalTransition.add(foundWithLetter);
                    this.findWithEpsilon(robot, finalTransition, foundWithLetter);
                }
            }
            this.validateTransition(finalTransition);
            this.newStates.get(sizeNewStates - 1).put(letter, finalTransition);
        }
    }

    public ArrayList<Integer> findInRow(Robot robot, String letter, int position){
        ArrayList<Integer> foundElements = new ArrayList();
        int index = 0;
        for (String row : robot.getTransitions().get(position)){
            if (row.equals(letter)){
                foundElements.add(index);
            }
            index++;
        }
        return foundElements;
    }

    public void findWithEpsilon(Robot robot, ArrayList<Integer> finalTransition, Integer found){
        int indexElement = 0;
        for (String element : robot.getTransitions().get(found)) {
            if (element.equals("ε")) {
                finalTransition.add(indexElement);
                this.findWithEpsilon(robot, finalTransition, indexElement);
            }
            indexElement++;
        }
    }

    public void validateTransition(ArrayList<Integer> finalTransition){
        Collections.sort(finalTransition);
        if (this.transitions.indexOf(finalTransition) == -1 && finalTransition.size() > 0){
            this.transitions.add(finalTransition);
        }
    }

}
