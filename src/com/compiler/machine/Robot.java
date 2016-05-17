package com.compiler.machine;

import java.util.HashMap;

public class Robot {

    private HashMap robot;

    public Robot(){
        this.robot = new HashMap<>();
    }

    public HashMap getRobot() {
        return robot;
    }

    public void setRobot(HashMap robot) {
        this.robot = robot;
    }

    public HashMap add_transitions(String firstWord, String secondWord){
        this.robot.put(firstWord, secondWord);
        return this.robot;
    }

    public HashMap finalState(String finalState) {
        this.robot.put("final", finalState);
        return this.robot;
    }

    public HashMap initialState(String initialState) {
        this.robot.put("initial", initialState);
        return robot;
    }
}
