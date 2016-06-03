package com.compiler.action;

import com.compiler.machine.Robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateAutomaton {

    private ArrayList<ArrayList<Robot>> machines;
    private GenerateMachine generateMachine = new GenerateMachine();
    private ArrayList<String> word;
    private ArrayList<String> operations;
    private ArrayList<Object> expressionRobot;
    private final String pattern = "^(\\w+$|^\\w+\\+\\w+)$|^(\\(\\w+\\)|\\(\\w+\\+\\w+\\))$|^(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\*$|^\\w*(\\(\\w+\\)|\\(\\w+\\+\\w+\\))$|^\\w+(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\*$|^\\w*(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\+\\w+$";

    public CreateAutomaton(){
        this.machines = new ArrayList<ArrayList<Robot>>();
        this.generateMachine = new GenerateMachine();
        this.word = new ArrayList<>();
        this.operations = new ArrayList<>();
        this.expressionRobot = new ArrayList<>();
    }

    public ArrayList<ArrayList<Robot>> getMachines(){ return this.machines; }

    public ArrayList<String> getWord(){ return this.word; }

    public ArrayList<String> getOperations(){ return this.operations; }

    public ArrayList<Object> getExpressionRobot(){ return this.expressionRobot; }

    public void setMachines(ArrayList<ArrayList<Robot>> machines){ this.machines = machines; }

    public void setWord(ArrayList<String> word){ this.word = word; }

    public void setOperations(ArrayList<String> operations){ this.operations = operations; }

    public void setExpressionRobot(ArrayList<Object> expressionRobot){ this.expressionRobot = expressionRobot; }

    public boolean isValid(String input){
        if (input.matches(pattern)){
            return this.balanceParenthesis(input);
        }
        return false;
    }

    private boolean balanceParenthesis(String input){
        int parenthesis = 0;
        for (String signal : input.split("")){
            if (signal.equals("(")){ parenthesis = parenthesis + 1; }
            if (signal.equals(")")){ parenthesis = parenthesis - 1; }
            if (parenthesis < 0){ break; }
        }
        return parenthesis == 0;
    }

    public void startProcess(String input){
        this.addAllToArray(input);
        this.processParenthesis(this.countParenthesis());
        this.processClosure(this.countClosure());
    }

    public void addAllToArray(String input){
        int index = 0;
        for (String signal : this.inputToArray(input)){
            if (this.isLetter(signal)){
                this.addWord(signal);
                this.addToMachine(signal, index);
            }else{
                this.addExpressionRobot(this.getIntersection(index), signal);
                index = index + 1;
            }
        }
        if (this.machines.size() > index){
            this.addExpressionRobot(this.getIntersection(index), "");
        }
    }

    private Robot getIntersection(int index){
        return generateMachine.intersection(this.machines.get(index));
    }

    public void addExpressionRobot(Robot robot, String operation){
        this.expressionRobot.add(robot);
        if (!operation.equals("")){
            this.expressionRobot.add(operation);
        }
    }

    public int countParenthesis(){
        int countParenthesis = 0;
        for (Object element : this.expressionRobot){
            if (element.equals("(")){
                countParenthesis = countParenthesis + 1;
            }
        }
        return countParenthesis;
    }

    public int countClosure(){
        int countClosure = 0;
        for (Object element : this.expressionRobot){
            if (element.equals("*")){
                countClosure = countClosure + 1;
            }
        }
        return countClosure;
    }

    public void processParenthesis(int countParenthesis){
        if (countParenthesis != 0){
            int openParenthesis = this.expressionRobot.indexOf("(");
            int closeParenthesis = this.expressionRobot.indexOf(")");
            List<Object> subList = this.expressionRobot.subList(openParenthesis, closeParenthesis + 1);
            if (subList.size() != 1){
                ArrayList<Robot> robots = new ArrayList<>();
                for (Object element : subList){
                    if (this.isLetter(element.toString())){
                        robots.add((Robot) element);
                    }
                }
                subList.clear();
                this.expressionRobot.add(generateMachine.union(robots));
                this.moveOperations();
            }
            this.processParenthesis(countParenthesis - 1);
        }
    }

    public void moveOperations(){
        if (this.expressionRobot.get(0).equals("*") || this.expressionRobot.get(0).equals("+")){
            this.expressionRobot.add(this.expressionRobot.get(0));
            this.expressionRobot.remove(0);
            this.moveOperations();
        }
    }

    public void processClosure(int countClosure){
        if (countClosure != 0){
            this .expressionRobot.add(generateMachine.closure((Robot) this.expressionRobot.get(this.expressionRobot.indexOf("*") - 1)));
            this.expressionRobot.remove(this.expressionRobot.indexOf("*") - 1);
            this.expressionRobot.remove(this.expressionRobot.indexOf("*"));
            this.moveOperations();
            this.processClosure(countClosure - 1);
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
