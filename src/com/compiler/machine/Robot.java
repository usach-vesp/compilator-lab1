package com.compiler.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Robot {

    private ArrayList<ArrayList<String>> transitions;
    private String stateInitial;
    private String stateFinal;
    private int sizeRow;
    private int sizeColumn;

    public Robot(){
        this.transitions = new ArrayList<ArrayList<String>>();
        this.stateInitial = "";
        this.stateFinal = "";
        this.sizeRow = 0;
        this.sizeColumn = 0;
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

    public int getSizeRow() {
        return sizeRow;
    }

    public void setSizeRow(int sizeRow) {
        this.sizeRow = sizeRow;
    }

    public int getSizeColumn() {
        return sizeColumn;
    }

    public void setSizeColumn(int sizeColumn) {
        this.sizeColumn = sizeColumn;
    }

    public void syncSize(){
        this.sizeRow = this.transitions.size();
        this.sizeColumn = this.transitions.get(0).size();
    }

    public void assignRowTransition(int index, ArrayList transitions){
        this.addTransition(index);
        this.transitions.add(index, transitions);
        this.syncSize();
    }

    public void assignColumnTransition(List list){
        this.transitions.get(this.sizeRow).addAll(list);
        this.syncSize();
    }

    public void assignRightEpsilon(int sizeForEpsilon){
        String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
        ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
        for (ArrayList row: this.listForRightEpsilon()){
            this.getTransitions().get(this.getTransitions().indexOf(row)).addAll(epsilonArray);
        }
        this.syncSize();
    }

    public void assignLeftEpsilon(int sizeForEpsilon){
        String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
        ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
        this.transitions.add(this.transitions.size(), epsilonArray);
    }

    public void addTransition(int index){
        if (this.transitions.size() < index){
            this.transitions.add(index, new ArrayList<String>());
        }
    }

    private List<ArrayList<String>> listForRightEpsilon(){
        List<ArrayList<String>> subList;
        if (this.sizeRow != 1) {
            if (this.transitions.get(0).size() != this.transitions.get(1).size()) {
                return this.transitions.subList(1, this.transitions.size());
            }
        }
        return this.transitions;
    }

}
