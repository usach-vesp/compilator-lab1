package com.compiler.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Robot {

    private ArrayList<ArrayList<String>> states;
    private ArrayList<ArrayList<String>> transitions;
    private String stateInitial;
    private String stateFinal;
    private Integer sizeRow;
    private Integer sizeColumn;

    public Robot(){
        this.states = new ArrayList<ArrayList<String>>();
        this.transitions = new ArrayList<ArrayList<String>>();
        this.stateInitial = "";
        this.stateFinal = "";
    }

    public ArrayList<ArrayList<String>> getStates() {
        return states;
    }

    public void setStates(ArrayList<ArrayList<String>> states) {
        this.states = states;
    }

    public ArrayList<ArrayList<String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<ArrayList<String>> transitions) {
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

    public Integer getSizeRow() {
        return sizeRow;
    }

    public void setSizeRow(Integer sizeRow) {
        this.sizeRow = sizeRow;
    }

    public Integer getSizeColumn() {
        return sizeColumn;
    }

    public void setSizeColumn(Integer sizeColumn) {
        this.sizeColumn = sizeColumn;
    }

    private void syncSize(){
        this.sizeRow = this.transitions.size();
        this.sizeColumn = this.transitions.get(0).size();
    }

    public void assignRowTransition(Integer index, ArrayList transitions){
        this.addTransition(index);
        this.transitions.add(index, transitions);
        this.syncSize();
    }

    public void assignColumnTransition(List list){
        this.transitions.get(this.sizeRow).addAll(list);
        this.syncSize();
    }

    public void assignRightEpsilon(Integer sizeForEpsilon){
        String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
        ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
        for (ArrayList row: this.getTransitions()){
            this.getTransitions().get(this.getTransitions().indexOf(row)).addAll(epsilonArray);
        }
        this.syncSize();
    }

    public void assignLeftEpsilon(Integer sizeForEpsilon){
        String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
        ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
        this.transitions.add(this.transitions.size(), epsilonArray);
    }

    private void addTransition(Integer index){
        if (this.getTransitions().size() < index){
            this.getTransitions().add(index, new ArrayList<String>());
        }
    }
}
