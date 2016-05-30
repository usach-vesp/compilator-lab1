package com.compiler.template;


import com.compiler.machine.Robot;

import java.util.ArrayList;

public interface ActionMachine {

    public Robot base(String letter);

    public Robot union(ArrayList<Robot> machines);

    public Robot intersection(ArrayList<Robot> robots);

    public Robot closure(Robot machine);

}
