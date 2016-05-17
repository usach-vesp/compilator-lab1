package com.compiler.action;


import com.compiler.machine.Robot;
import com.compiler.template.ActionMachine;

import java.util.HashMap;

public class GenerateMachine implements ActionMachine{

    @Override
    public Robot base(String letter) {
        /*
        * A => ->O->O
        */
        Robot robot = new Robot();
        robot.add_transitions("ø", letter);
        robot.add_transitions(letter, "ø");
        robot.initialState("ø");
        robot.finalState(letter);
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
    public Robot intersection(HashMap<String, String> firstMachine, HashMap<String, String> secondMachine) {
        /*
        * AB => ->O->aO->bO
        */
        Robot robot = new Robot();
        String stateFinal = firstMachine.get("final");
        String stateInitial = secondMachine.get("initial");
        this.copyMachine(firstMachine, robot.getRobot());
        this.copyMachine(secondMachine, robot.getRobot());
        robot.add_transitions(stateFinal, stateInitial);
        robot.initialState(firstMachine.get("initial"));
        robot.finalState(secondMachine.get("final"));
        return robot;
    }

    @Override
    public void closure() {
        /*
        *                  <-e
        * (A)* => e->Oe->eO->aO->eO->eO
        *                  e->
        */

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
}
