package com.compiler.action;

import com.compiler.machine.Robot;
import com.compiler.machine.Transition;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    public Robot startProcess(String input) throws FileNotFoundException, UnsupportedEncodingException {
        Robot afnd = new Robot();
        Transition transition = new Transition();
        this.addAllToArray(input);
        this.processParenthesis(this.countSignal("("));
        this.processClosure(this.countSignal("*"));
        this.processIntersection();
        afnd = this.processUnion();
        afnd.syncSize();
        this.writeDotFile(afnd.getTransitions(), "AFND", new ArrayList<String>(Arrays.asList(afnd.getStateFinal())));
        transition.turnToDeterministic(afnd, this.word);
        this.writeDotFile(transition.getAfd(), "AFD", transition.getFinalState());
        return afnd;
    }

    private void writeDotFile(ArrayList<ArrayList<String>> transitions, String filename, ArrayList<String> finalState) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename + ".dot", "UTF-8");
        writer.println("digraph G {");
        int row = 0;
        for (ArrayList rowTransition : transitions){
            int column = 0;
            for (Object columnElement : rowTransition){
                if (!columnElement.toString().equals("ø")){
//                    shape = doublecircle
                    writer.println(row + " -> " + column + " [label="+ "\""+ columnElement + "\"" +"]; ");
                }
                column = column + 1;
            }
            row = row + 1;
        }
        for (String state : finalState){
            writer.println(state + " [shape = doublecircle];");
        }
        writer.println("}");
        writer.close();
    }

    public void addAllToArray(String input){
        int index = 0;
        for (String signal : this.inputToArray(input)){
            if (this.isLetter(signal)){
                this.addWord(signal);
                this.addToMachine(signal, index);
            }else{
                if (this.machines.size() == index){
                    this.expressionRobot.add(signal);
                }else{
                    this.addExpressionRobot(this.getIntersection(index), signal);
                    index = index + 1;
                }
            }
        }
        if (this.machines.size() > index){
            this.addExpressionRobot(this.getIntersection(index), "");
        }
    }

    private Robot getIntersection(int index){
        if (this.machines.get(index).size() != 1){
            return generateMachine.intersection(this.machines.get(index));
        }
        return this.machines.get(index).get(0);
    }

    public void addExpressionRobot(Robot robot, String operation){
        this.expressionRobot.add(robot);
        if (!operation.equals("")){
            this.expressionRobot.add(operation);
        }
    }

    public int countSignal(String signal){
        int count = 0;
        for (Object element : this.expressionRobot){
            if (element.equals(signal)){
                count = count + 1;
            }
        }
        return count;
    }

    public void processParenthesis(int countParenthesis){
        if (countParenthesis != 0){
            int openParenthesis = this.expressionRobot.indexOf("(");
            int closeParenthesis = this.expressionRobot.indexOf(")");
            List<Object> subList = this.expressionRobot.subList(openParenthesis, closeParenthesis + 1);
            int indexAdd = subList.indexOf("+");
            if (subList.size() != 1){
                ArrayList<Robot> robots = new ArrayList<>();
                for (Object element : subList){
                    if (this.isLetter(element.toString())){
                        robots.add((Robot) element);
                    }
                }
                subList.clear();
                if (indexAdd != -1){
                    this.expressionRobot.add(generateMachine.union(robots));
                }else{
                    this.expressionRobot.add(robots.get(0));
                }
                this.moveOperations(openParenthesis, (Robot) this.expressionRobot.get(this.expressionRobot.size() - 1));
            }
            this.processParenthesis(countParenthesis - 1);
        }
    }

    public void moveOperations(int openParenthesis, Robot lastRobot){
        if ((this.expressionRobot.get(openParenthesis).equals("*") || this.expressionRobot.get(openParenthesis).equals("+")
        )   || this.expressionRobot.get(openParenthesis) != lastRobot){
//                ){
            this.expressionRobot.add(this.expressionRobot.get(openParenthesis));
            this.expressionRobot.remove(openParenthesis);
            this.moveOperations(openParenthesis, lastRobot);
        }
    }

    public void processClosure(int countClosure){
        if (countClosure != 0){
            int indexClosure = this.expressionRobot.indexOf("*");
            this .expressionRobot.add(generateMachine.closure((Robot) this.expressionRobot.get(this.expressionRobot.indexOf("*") - 1)));
            this.expressionRobot.remove(this.expressionRobot.indexOf("*") - 1);
            this.expressionRobot.remove(this.expressionRobot.indexOf("*"));
            this.moveOperations(indexClosure - 1, (Robot) this.expressionRobot.get(this.expressionRobot.size() - 1));
            this.processClosure(countClosure - 1);
        }
    }

    public void processIntersection(){
        if ((this.countSignal("+") + 1) != (this.expressionRobot.size() - this.countSignal("+"))){
            ArrayList<Robot> robots = new ArrayList<>();
            int index = 0;
            for (Object robot : this.expressionRobot){
                if (this.isLetter(robot.toString())){
                    robots.add((Robot) robot);
                }else{
                    if (robots.size() != 1){
                        break;
                    }else{
                        index = index + 1;
                        robots.clear();
                    }
                }
            }
            this.expressionRobot.add(generateMachine.intersection(robots));
            this.expressionRobot.removeAll(robots);
            this.moveOperations(index + index, (Robot) this.expressionRobot.get(this.expressionRobot.size() - 1));
            this.processIntersection();
        }
    }

    public Robot processUnion(){
        if (this.expressionRobot.indexOf("+") != -1) {
            this.expressionRobot.removeAll(Collections.singleton("+"));
            ArrayList<Robot> robots = new ArrayList<>();
            for (Object element : this.expressionRobot) {
                robots.add((Robot) element);
            }
            return generateMachine.union(robots);
        }else if (this.expressionRobot.size() != 1){
            ArrayList<Robot> robots = new ArrayList<>();
            for (Object element : this.expressionRobot) {
                robots.add((Robot) element);
            }
            return generateMachine.intersection(robots);
        }
        return (Robot) this.expressionRobot.get(0);
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
