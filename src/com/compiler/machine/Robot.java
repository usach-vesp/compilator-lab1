package com.compiler.machine;

import java.util.ArrayList;

public class Robot {

    private ArrayList<ArrayList<String>> states;
    private ArrayList<String> transitions;
    private String stateInitial;
    private String stateFinal;

    public Robot(){
        this.states = new ArrayList<ArrayList<String>>();
        this.transitions = new ArrayList<>();
        this.stateInitial = "";
        this.stateFinal = "";
    }

    public ArrayList<ArrayList<String>> getStates() {
        return states;
    }

    public void setStates(ArrayList<ArrayList<String>> states) {
        this.states = states;
    }

    public ArrayList<String> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<String> transitions) {
        this.transitions = transitions;
    }

    public String getStateInitial() {
        return stateInitial;
    }

    public void setStateInitial(String stateInitial) {
        this.stateInitial = stateInitial;
    }

    public String getStateFinal() {
        return stateFinal;
    }

    public void setStateFinal(String stateFinal) {
        this.stateFinal = stateFinal;
    }

    public void addTransitions(Integer index, String transition){
        this.transitions.add(index, transition);
    }

    public void assignTransition(Integer index, ArrayList transitions){
        this.states.add(index, transitions);
    }
}
