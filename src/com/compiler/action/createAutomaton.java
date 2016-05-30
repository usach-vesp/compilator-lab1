package com.compiler.action;

import com.compiler.machine.Robot;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateAutomaton {

    ArrayList<ArrayList<Robot>> machines;
    ArrayList<Robot> intersection;
    GenerateMachine generateMachine = new GenerateMachine();
    ArrayList<String> word;
    ArrayList<String> operations;
    private final String pattern = "^(\\w+$|^\\w+\\+\\w+)$|^(\\(\\w+\\)|\\(\\w+\\+\\w+\\))$|^(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\*$|^\\w*(\\(\\w+\\)|\\(\\w+\\+\\w+\\))$|^\\w+(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\*$|^\\w*(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\+\\w+$";

    public CreateAutomaton(){
        this.machines = new ArrayList<ArrayList<Robot>>();
        this.intersection = new ArrayList();
        this.generateMachine = new GenerateMachine();
        this.word = new ArrayList<>();
        this.operations = new ArrayList<>();
    }

    public ArrayList<ArrayList<Robot>> getMachines(){ return this.machines; }

    public ArrayList<Robot> getIntersection() { return  this.intersection; }

    public ArrayList<String> getWord(){ return this.word; }

    public ArrayList<String> getOperations(){ return this.operations; }

    public void setMachines(ArrayList<ArrayList<Robot>> machines){ this.machines = machines; }

    public boolean isValid(String input){
        if (input.matches(pattern)){
            return this.balanceParenthesis(input);
        }
        return false;
    }

    private boolean balanceParenthesis(String input){
        int parenthesis = 0;
        for (String signal : input.split("")){
            if (signal.equals("(")){
                parenthesis = parenthesis + 1;
            }
            if (signal.equals(")")){
                parenthesis = parenthesis - 1;
            }
            if (parenthesis < 0){ break; }
        }
        return parenthesis == 0;
    }

    public void startProcess(String input){
        this.addAllToArray(input);
        this.addIntersection();
    }

    public void addAllToArray(String input){
        int index = 0;
        for (String signal : this.inputToArray(input)){
            if (this.isLetter(signal)){
                this.addWord(signal);
                this.addToMachine(signal, index);
            }else{
                operations.add(signal);
                index = index + 1;
            }
        }
    }

    public Robot regexToRobot(){
        Robot robot = this.intersection.get(0);
        int operator = 0;
        int robots = 1;
        if (this.operations.size() != 0){
            robot = this.intersection.get(0);
            while (robots < this.intersection.size()){
                if (this.operations.size() > operator){
                    if (this.operations.get(operator).equals("+")){
                        robot = generateMachine.union(new ArrayList<Robot>(Arrays.asList(robot, this.intersection.get(robots))));
                        robots = robots + 2;
                    }
                }
                robots = robots + 1;
            }
        }
        return robot;
    }

    public void addIntersection(){
        for (ArrayList<Robot> intersections : this.machines){
            if (intersections.size() < 2){
                intersection.add(intersections.get(0));
            }else{
                intersection.add(generateMachine.intersection(intersections));
            }
        }
    }

    private void addWord(String letter){
        if (word.indexOf(letter) == -1){
            word.add(letter);
        }
    }

    private ArrayList<String> inputToArray(String input){
        ArrayList<String> arrayList = new ArrayList<>();
        for (String signal : input.split("")){
            if (!signal.isEmpty()){
                arrayList.add(signal);
            }
        }
        return arrayList;
    }

    private boolean isLetter(String signal){
        return !signal.equals("+") && !signal.equals("(") && !signal.equals(")") && !signal.equals("*");
    }

    public void addToMachine(String letter, int index){
        if (this.machines.size() <= index || this.machines.size() == 0){
            this.machines.add(index, new ArrayList<Robot>());
        }
        this.machines.get(index).add(generateMachine.base(letter));
    }

}
