package com.compiler.transition;

import com.compiler.machine.Robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Transition {

    ArrayList<ArrayList<Integer>> transitions;
    ArrayList<HashMap<String, ArrayList<Integer>>> newStates;

    public Transition(){
        this.transitions = new ArrayList<ArrayList<Integer>>();
        this.newStates = new ArrayList<HashMap<String, ArrayList<Integer>>>();
    }

    public ArrayList<ArrayList<Integer>> getTransitions() {
        return transitions;
    }

    public ArrayList<HashMap<String, ArrayList<Integer>>> getNewStates() {
        return newStates;
    }

    public void turnToDeterministic(Robot robot, ArrayList<String> words){
        this.searchFirstState(robot.getTransitions().get(0));
        this.searchStates(robot, words, 0);
    }

    public void searchFirstState(ArrayList<String> row){
        ArrayList transition = new ArrayList(Arrays.asList(0));
        int index = 0;
        for (String element : row){
            if (element.equals("ε")){
                transition.add(index);
            }
            index++;
        }
        this.transitions.add(transition);
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
        if (this.transitions.indexOf(finalTransition) == -1 && finalTransition.size() > 0){
            this.transitions.add(finalTransition);
        }
    }

}
