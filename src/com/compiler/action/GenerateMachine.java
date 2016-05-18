package com.compiler.action;


import com.compiler.machine.Robot;
import com.compiler.template.ActionMachine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateMachine implements ActionMachine{

    @Override
    public Robot base(String letter) {
        /*
        * A => ->O->O
        */
        Robot robot = new Robot();
        robot.addTransitions(0, "ø");
        robot.addTransitions(1, letter);
        robot.assignTransition(0, robot.getTransitions());
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
        Robot robot = new Robot();
        this.generateOneMachine(firstMachine, secondMachine);
//        String stateFinal = firstMachine.get("final");
//        String stateInitial = secondMachine.get("initial");
//        this.copyMachine(firstMachine, robot.getRobot());
//        this.copyMachine(secondMachine, robot.getRobot());
//        robot.add_transitions(stateFinal, stateInitial);
//        robot.initialState(firstMachine.get("initial"));
//        robot.finalState(secondMachine.get("final"));
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

    private HashMap copyMachine(HashMap<String, String> transition, HashMap<String, String> finalRobot){
        for (String temporal: transition.keySet()){
            finalRobot.put(temporal, transition.get(temporal));
        }
        return finalRobot;
    }

    private Robot generateOneMachine(Robot firstMachine, Robot secondMachine){
        Robot robot = new Robot();
        for(ArrayList column: firstMachine.getStates()){
            System.out.print(column);
        }
        return robot;
    }
}
