package com.compiler.action;


import com.compiler.template.ActionMachine;

import java.util.HashMap;

public class GenerateMachine implements ActionMachine{

    @Override
    public HashMap standard(HashMap<String, String> robot, String letter) {
        /*
        * A => ->O->O
        */
        robot.put("ø", letter);
        robot.put(letter, "ø");
        this.initialState(robot, "ø");
        this.finalState(robot, letter);
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
    public HashMap intersection(HashMap<String, String> firstMachine, HashMap<String, String> secondMachine) {
        /*
        * AB => ->O->aO->bO
        */
        HashMap<String, String> robot = new HashMap<>();
        String stateFinal = firstMachine.get("final");
        String stateInitial = secondMachine.get("initial");
        for (String temporal: firstMachine.keySet()){
            robot.put(temporal, firstMachine.get(temporal));
        }
        robot.put(stateFinal, stateInitial);
        for (String temporal: secondMachine.keySet()){
            robot.put(temporal, secondMachine.get(temporal));
        }
        robot.put("initial", firstMachine.get("initial"));
        robot.put("final", secondMachine.get("final"));
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

    private HashMap finalState(HashMap<String, String> robot, String finalState) {
        robot.put("final", finalState);
        return robot;
    }

    private HashMap initialState(HashMap<String, String> robot, String initialState) {
        robot.put("initial", initialState);
        return robot;
    }

    @Override
    public void numberState() {

    }
}
