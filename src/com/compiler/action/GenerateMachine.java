package com.compiler.action;


import com.compiler.machine.Robot;
import com.compiler.template.ActionMachine;

import java.util.ArrayList;
import java.util.Arrays;


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
        robot.syncSize();
        robot.setStatesInitialFinal();
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
        robot.setStatesInitialFinal();
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
        robot.setStatesInitialFinal();
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
        robot.closureIntersection(machine, 1, 2);
        robot.assignRowTransition(robot.getSizeRow(), firstRow);
        robot.assignLeftEmpty(robot.getSizeRow() + 1);
        robot.syncSize();
        robot.setStatesInitialFinal();
        return robot;
    }

    private Robot generateOneMachine(Robot firstMachine, Robot secondMachine){
        Robot robot = new Robot();

        robot.copyFirstMachine(firstMachine);
        robot.addRightEmptyIfNecessary(secondMachine.getTransitions().get(0).size());
        for(ArrayList row: secondMachine.getTransitions()){
            robot.assignLeftEmpty(secondMachine.getSizeColumn());
            robot.assignColumnTransition(row.subList(1, row.size()));
        }
        return robot;
    }

}
