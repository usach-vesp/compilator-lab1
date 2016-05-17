package com.compiler.template;


import com.compiler.machine.Robot;

import java.util.HashMap;

public interface ActionMachine {

    public Robot base(String letter);

    public void union();

    public Robot intersection(HashMap<String, String> firstMachine, HashMap<String, String> secondMachine);

    public void closure();

    public void remove();

    public void numberState();

}
