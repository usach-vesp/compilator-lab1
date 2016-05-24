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
        this.transitions.add(transitions);
        this.syncSize();
    }

    public void assignColumnTransition(List list){
        this.transitions.get(this.sizeRow).addAll(list);
        this.syncSize();
    }

    public void assignRightEpsilonToAll(int sizeForEpsilon){
        String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
        ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
        int index = -1;
        for (ArrayList row: this.listForRightEpsilon()){
            if (index == -1){ index = this.getTransitions().indexOf(row); }
            if (index <= this.getTransitions().indexOf(row)){
                this.getTransitions().get(index).addAll(epsilonArray);
            }
            index++;
        }
        this.syncSize();
    }

    public void assignRightEpsilonToRow(int sizeForEpsilon, int position){
        if (sizeForEpsilon != 0){
            String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
            ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
            this.transitions.get(position).addAll(epsilonArray);
            this.syncSize();
        }
    }

    public void assignLeftEpsilon(int sizeForEpsilon){
        String epsilon = new String(new char[sizeForEpsilon]).replace("\0", "ø,");
        ArrayList epsilonArray = new ArrayList(Arrays.asList(epsilon.split(",")));
        this.transitions.add(this.transitions.size(), epsilonArray);
    }

    public int createUnionMachine(Robot machine){
        this.copyForUnionMachine(machine.getTransitions(), this.getTransitions().size());
        this.getTransitions().get(0).add("ε");
        int sizeLastPosition = this.getTransitions().get(this.getTransitions().size() - 1).size();
        this.assignRightEpsilonToRow(sizeLastPosition - this.getTransitions().get(0).size(), 0);
        this.syncSize();
        return machine.getTransitions().size();
    }

    public void squareUnionMachine(){
        int index = 0;
        for (ArrayList row : this.getTransitions()){
            this.assignRightEpsilonToRow(this.getSizeColumn() - row.size(), index);
            index++;
        }
    }

    public void createLastTransitionUnion(ArrayList<Integer> sizesMachines){
        int finalStates = 0;
        for (int finalState : sizesMachines){
            finalStates += finalState;
            this.getTransitions().get(finalStates).set(this.getSizeColumn() - 1, "ε");
        }
        this.assignLeftEpsilon(this.getSizeColumn());
        this.syncSize();
    }

    private void addTransition(int index){
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

    private void copyForUnionMachine(ArrayList<ArrayList<String>> list, int index){
        for (ArrayList subList: list){
            this.assignLeftEpsilon(this.getTransitions().get(0).size());
            this.getTransitions().get(index).addAll(subList);
            index++;
        }
    }

}
