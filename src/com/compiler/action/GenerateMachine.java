package com.compiler.action;


import com.compiler.machine.Robot;
import com.compiler.template.ActionMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GenerateMachine implements ActionMachine{



    @Override
    public Robot base(String letter) {
        /*
        * A => ->O->O
        */
        Robot robot = new Robot();
        ArrayList<String> transitionOne = new ArrayList<>();
        ArrayList<String> transitionTwo = new ArrayList<>();
        transitionOne.add(0, "ø");
        transitionOne.add(1, letter);
        transitionTwo.add(0, "ø");
        transitionTwo.add(1, "ø");
        robot.assignRowTransition(0, transitionOne);
        robot.assignRowTransition(1, transitionTwo);
        robot.setStateInitial("ø");
        robot.setStateFinal(letter);
        robot.syncSize();
        return robot;
    }

    @Override
    public Robot union(ArrayList<Robot> machines) {
        /*
        * A + b => O ->O->O
        *            ->O->O
        */
        Robot robot = new Robot();
        ArrayList<Integer> sizesMachines = new ArrayList();
        robot.assignLeftEmpty(1);
        for (Robot machine: machines){
            sizesMachines.add(robot.createUnionMachine(machine));
        }
        robot.assignRightEmptyToRow(1, 0);
        robot.squareUnionMachine();
        robot.createLastTransitionUnion(sizesMachines);
        return robot;
    }

    @Override
    public Robot intersection(Robot firstMachine, Robot secondMachine) {
        /*
        * AB => ->O->aO->bO
        */
        Robot robot = this.generateOneMachine(firstMachine, secondMachine);
        robot.setStateInitial(firstMachine.getStateInitial());
        robot.setStateFinal(secondMachine.getStateFinal());
        return robot;
    }

    @Override
    public Robot closure(Robot machine) {
        /*
        *                  <-e
        * (A)* => e->Oe->eO->aO->eO->eO
        *                  e->
        */
        Robot robot = new Robot();
        ArrayList<String> firstRow = new ArrayList<>(Arrays.asList("ø", "ε"));
        robot.assignRowTransition(0, firstRow);
        robot.assignRightEmptyToAll(machine.getTransitions().size() - 1);
        robot.getTransitions().get(0).add("ε");
        for (int i = 0; i < machine.getSizeRow() - 1; i++){
            robot.assignLeftEmpty(2 + i);
        }
        this.closureIntersection(robot, machine, 1, 2);
        robot.assignRowTransition(robot.getSizeRow(), firstRow);
        robot.assignLeftEmpty(robot.getSizeRow() + 1);
        robot.syncSize();
        return robot;
    }

    @Override
    public void remove() {

    }

    @Override
    public void numberState() {

    }

    private Robot generateOneMachine(Robot firstMachine, Robot secondMachine){
        Robot robot = new Robot();

        this.copyFirstMachine(robot, firstMachine);
        this.addRightEmptyIfNecessary(robot, secondMachine.getTransitions().get(0).size());
        for(ArrayList row: secondMachine.getTransitions()){
            robot.assignLeftEmpty(secondMachine.getSizeColumn());
            robot.assignColumnTransition(row.subList(1, row.size()));
        }
        return robot;
    }

    private void copyFirstMachine(Robot robot, Robot firstMachine){
        for(ArrayList row: this.subListFromMachine(firstMachine.getSizeRow(), firstMachine.getTransitions())){
            robot.assignRowTransition(firstMachine.getTransitions().indexOf(row), row);
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

    private void addRightEmptyIfNecessary(Robot robot, int size){
        if (size > 0){
            robot.assignRightEmptyToAll(size - 1);
        }
    }

    private int closureIntersection(Robot robot, Robot machine, int row, int column){
        if (robot.getTransitions().size() > row){
            robot.getTransitions().get(row).add(column, machine.getTransitions().get(row-1).get(column-1));
            robot.syncSize();
            robot.assignRightEmptyToRow(robot.getSizeColumn() - robot.getTransitions().get(row).size(), row);
            return this.closureIntersection(robot, machine, row+1, column+1);
        }
        return -1;
    }
}
