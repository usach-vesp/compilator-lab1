package com.compiler.template;


import java.util.HashMap;

public interface ActionMachine {

    public HashMap standard(HashMap<String, String> robot, String letter);

    public void union();

    public HashMap intersection(HashMap<String, String> firstMachine, HashMap<String, String> secondMachine);

    public void closure();

    public void remove();

    public void numberState();

}
