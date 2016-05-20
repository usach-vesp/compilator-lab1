package com.compiler.action;


import com.compiler.machine.Robot;
import com.compiler.template.ActionMachine;

import java.util.ArrayList;


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
    public void union() {
        /*
        * A + b => O ->O->O
        *            ->O->O
        */

    }

    @Override
    public Robot intersection(Robot firstMachine, Robot secondMachine) {
        /*
        * AB => ->O->aO->bO
        */
        Robot robot = this.generateOneMachine(firstMachine, secondMachine);;
        robot.setStateInitial(firstMachine.getStateInitial());
        robot.setStateFinal(secondMachine.getStateFinal());
        return robot;
    }

    @Override
    public Robot closure(String letter) {
        /*
        *                  <-e
        * (A)* => e->Oe->eO->aO->eO->eO
        *                  e->
        */
        Robot robot = new Robot();
        robot.addTransition(0);
        robot.getTransitions().get(0).add(0, "ø");
        robot.getTransitions().get(0).add(1, "ε");
        robot.getTransitions().get(0).add(2, "ø");
        robot.getTransitions().get(0).add(3, "ε");
        robot.addTransition(1);
        robot.getTransitions().get(1).add(0, "ø");
        robot.getTransitions().get(1).add(1, "ø");
        robot.getTransitions().get(1).add(2, letter);
        robot.getTransitions().get(1).add(3, "ø");
        robot.addTransition(2);
        robot.getTransitions().get(2).add(0, "ø");
        robot.getTransitions().get(2).add(1, "ε");
        robot.getTransitions().get(2).add(2, "ø");
        robot.getTransitions().get(2).add(3, "ε");
        robot.addTransition(3);
        robot.getTransitions().get(3).add(0, "ø");
        robot.getTransitions().get(3).add(1, "ø");
        robot.getTransitions().get(3).add(2, "ø");
        robot.getTransitions().get(3).add(3, "ø");
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
        for(ArrayList row: firstMachine.getTransitions().subList(0, firstMachine.getSizeRow() - 1)){
            robot.assignRowTransition(firstMachine.getTransitions().indexOf(row), row);
        }
        System.out.print(robot.getTransitions());
        if (secondMachine.getTransitions().get(0).size() > 0){
            robot.assignRightEpsilon(secondMachine.getTransitions().get(0).size() - 1);
        }
        for(ArrayList row: secondMachine.getTransitions()){
            robot.assignLeftEpsilon(secondMachine.getSizeColumn());
            robot.assignColumnTransition(row.subList(1, row.size()));
        }
        return robot;
    }

    private void copyFirstMachine(Robot firstMachine, Robot robot){
        for(ArrayList row: firstMachine.getTransitions().subList(0, firstMachine.getSizeRow() - 1)){
            robot.assignRowTransition(firstMachine.getTransitions().indexOf(row), row);
        }
    }
}
