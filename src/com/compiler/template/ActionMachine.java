package com.compiler.template;


import com.compiler.machine.Robot;

import java.util.HashMap;

public interface ActionMachine {

    public Robot base(String letter);

    public void union();

    public Robot intersection(Robot firstMachine, Robot secondMachine);

    public Robot closure(Robot machine);

    public void remove();

    public void numberState();

}
