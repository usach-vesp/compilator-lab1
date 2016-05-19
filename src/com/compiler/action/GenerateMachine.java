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
        ArrayList<String> transition = new ArrayList<>();
        transition.add(0, "ø");
        transition.add(1, letter);
        robot.assignRowTransition(0, transition);
        robot.setStateInitial("ø");
        robot.setStateFinal(letter);
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
        for(ArrayList row: firstMachine.getTransitions()){
            robot.assignRowTransition(firstMachine.getTransitions().indexOf(row), row);
        }
        robot.assignRightEpsilon(secondMachine.getTransitions().get(0).size() - 1);
        for(ArrayList row: secondMachine.getTransitions()){
            robot.assignLeftEpsilon(secondMachine.getSizeColumn());
            robot.assignColumnTransition(row.subList(1, row.size()));
        }
        return robot;
    }
}
