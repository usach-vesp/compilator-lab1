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

    public void assignRightEmptyToAll(int sizeForEmpty){
        String empty = new String(new char[sizeForEmpty]).replace("\0", "ø,");
        ArrayList emptyArray = new ArrayList(Arrays.asList(empty.split(",")));
        int index = -1;
        for (ArrayList row: this.listForRightEmpty()){
            if (index == -1){ index = this.getTransitions().indexOf(row); }
            if (index <= this.getTransitions().indexOf(row)){
                this.getTransitions().get(index).addAll(emptyArray);
            }
            index++;
        }
        this.syncSize();
    }

    public void assignRightEmptyToRow(int sizeForEmpty, int position){
        if (sizeForEmpty != 0){
            String empty = new String(new char[sizeForEmpty]).replace("\0", "ø,");
            ArrayList emptyArray = new ArrayList(Arrays.asList(empty.split(",")));
            this.transitions.get(position).addAll(emptyArray);
            this.syncSize();
        }
    }

    public void assignLeftEmpty(int sizeForEmpty){
        String empty = new String(new char[sizeForEmpty]).replace("\0", "ø,");
        ArrayList emptyArray = new ArrayList(Arrays.asList(empty.split(",")));
        this.transitions.add(this.transitions.size(), emptyArray);
    }

    public int createUnionMachine(Robot machine){
        this.copyForUnionMachine(machine.getTransitions(), this.getTransitions().size());
        this.getTransitions().get(0).add("ε");
        int sizeLastPosition = this.getTransitions().get(this.getTransitions().size() - 1).size();
        this.assignRightEmptyToRow(sizeLastPosition - this.getTransitions().get(0).size(), 0);
        this.syncSize();
        return machine.getTransitions().size();
    }

    public void squareUnionMachine(){
        int index = 0;
        for (ArrayList row : this.getTransitions()){
            this.assignRightEmptyToRow(this.getSizeColumn() - row.size(), index);
            index++;
        }
    }

    public void createLastTransitionUnion(ArrayList<Integer> sizesMachines){
        int finalStates = 0;
        for (int finalState : sizesMachines){
            finalStates += finalState;
            this.getTransitions().get(finalStates).set(this.getSizeColumn() - 1, "ε");
        }
        this.assignLeftEmpty(this.getSizeColumn());
        this.syncSize();
    }

    public int closureIntersection(Robot machine, int row, int column){
        if (this.getTransitions().size() > row){
            this.getTransitions().get(row).add(column, machine.getTransitions().get(row-1).get(column-1));
            this.syncSize();
            this.assignRightEmptyToRow(this.getSizeColumn() - this.getTransitions().get(row).size(), row);
            return this.closureIntersection(machine, row+1, column+1);
        }
        return -1;
    }

    public void addRightEmptyIfNecessary(int size){
        if (size > 0){
            this.assignRightEmptyToAll(size - 1);
        }
    }

    public void copyFirstMachine(Robot firstMachine){
        for(ArrayList row: this.subListFromMachine(firstMachine.getSizeRow(), firstMachine.getTransitions())){
            this.assignRowTransition(firstMachine.getTransitions().indexOf(row), row);
        }
    }

    private void addTransition(int index){
        if (this.transitions.size() < index){
            this.transitions.add(index, new ArrayList<String>());
        }
    }

    private List<ArrayList<String>> listForRightEmpty(){
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
            this.assignLeftEmpty(this.getTransitions().get(0).size());
            this.getTransitions().get(index).addAll(subList);
            index++;
        }
    }

    private List<ArrayList<String>> subListFromMachine(int sizeRow, ArrayList<ArrayList<String>> list){
        List<ArrayList<String>> subList;
        if (sizeRow - 1 != 0){
            subList = list.subList(0, sizeRow - 1);
        }else {
            subList = list.subList(0, sizeRow);
        }
        return subList;
    }

}
