package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.action.CreateAutomaton;
import com.compiler.machine.Robot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CreateAutomatonTest {

    private CreateAutomaton automaton;
    private GenerateMachine generateMachine;

    @Before
    public void setUp() throws Exception {
        automaton = new CreateAutomaton();
        generateMachine = new GenerateMachine();
    }

    @Test
    public void isValid() throws Exception {
        assertEquals(automaton.isValid("AB"), true);
        assertEquals(automaton.isValid(""), false);
        assertEquals(automaton.isValid("(AB)"), true);
        assertEquals(automaton.isValid("A+B"), true);
        assertEquals(automaton.isValid("A(B)*"), true);
        assertEquals(automaton.isValid("A)B"), false);
        assertEquals(automaton.isValid("A(B"), false);
        assertEquals(automaton.isValid("(A+c)+B"), true);
        assertEquals(automaton.isValid("(A+c)*"), true);
        assertEquals(automaton.isValid("abc(abc+abc)+abc"), true);
    }

    @Test
    public void startProcess() throws Exception {
        Robot robot = automaton.startProcess("a");
        assertEquals(robot.getTransitions().get(0).get(0), "ø");
        assertEquals(robot.getTransitions().get(0).get(1), "a");
        assertEquals(robot.getTransitions().get(1).get(0), "ø");
        assertEquals(robot.getTransitions().get(1).get(1), "ø");
    }

    @Test
    public void startProcessAB() throws Exception {
        Robot robot = automaton.startProcess("ab");
        assertEquals(robot.getSizeColumn(), 3);
        assertEquals(robot.getSizeRow(), 3);
        assertEquals(Arrays.asList("ø", "a", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø"), robot.getTransitions().get(2));
    }

    @Test
    public void startProcessParenthesisAB() throws Exception {
        Robot robot = automaton.startProcess("(ab)");
        assertEquals(robot.getSizeColumn(), 3);
        assertEquals(robot.getSizeRow(), 3);
        assertEquals(Arrays.asList("ø", "a", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø"), robot.getTransitions().get(2));
        assertEquals(robot.getStateFinal(), "2");
    }

    @Test
    public void startProcessTwoParenthesisAB() throws Exception {
        Robot robot = automaton.startProcess("(ab)(cd)");
        assertEquals(robot.getSizeColumn(), 5);
        assertEquals(robot.getSizeRow(), 5);
        assertEquals(Arrays.asList("ø", "a", "ø", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "c", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "d"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(4));
        assertEquals(robot.getStateFinal(), "4");
    }

    @Test
    public void startProcessTwoAAddB() throws Exception {
        Robot robot = automaton.startProcess("a+b");
        assertEquals(robot.getSizeRow(), 6);
        assertEquals(robot.getSizeColumn(), 6);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "b", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(5));
        assertEquals(robot.getStateFinal(), "5");
    }

    @Test
    public void startProcessTwoWordWithIntersectionClosure() throws Exception {
        Robot robot = automaton.startProcess("a(b)*");
        assertEquals(robot.getSizeRow(), 5);
        assertEquals(robot.getSizeColumn(), 5);
        assertEquals(Arrays.asList("ø", "a", "ø", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "ε", "ø", "ε"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "b", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ε", "ø", "ε"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(4));
    }

    @Test
    public void startProcessThreeWordWithUnion() throws Exception {
        Robot robot = automaton.startProcess("(a+b)+c");
        assertEquals(robot.getSizeRow(), 10);
        assertEquals(robot.getSizeColumn(), 10);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ø", "ø", "ø", "ø", "ε", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "ε", "ø", "ε", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "a", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø", "ø", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "b", "ø", "ø", "ø", "ø"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø", "ø", "ø"), robot.getTransitions().get(5));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(6));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "c", "ø"), robot.getTransitions().get(7));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(8));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(9));
    }

    @Test
    public void startProcessTwoWordWithUnionAndClausure() throws Exception {
        Robot robot = automaton.startProcess("(a+b)*");
        assertEquals(robot.getSizeRow(), 8);
        assertEquals(robot.getSizeColumn(), 8);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "ε", "ø", "ε", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "a", "ø", "ø", "ø", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "b", "ø", "ø"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø"), robot.getTransitions().get(5));
        assertEquals(Arrays.asList("ø", "ε", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(6));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(7));
    }

    @Test
    public void startProcessTwoWordWithIntersectionUnionAndClausure() throws Exception {
        Robot robot = automaton.startProcess("ab(cd+ef)+gh");
        assertEquals(robot.getSizeRow(), 15);
        assertEquals(robot.getSizeColumn(), 15);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "b", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ε", "ø", "ø", "ε", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "c", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "d", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(5));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø", "ø", "ø", "ø"), robot.getTransitions().get(6));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "e", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(7));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "f", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(8));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε", "ø", "ø", "ø", "ø"), robot.getTransitions().get(9));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(10));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "g", "ø", "ø"), robot.getTransitions().get(11));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "h", "ø"), robot.getTransitions().get(12));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(13));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(14));
    }

    @Test
    public void addAllToArray() throws Exception {
        automaton.addAllToArray("ab");
        assertEquals(automaton.getWord(), Arrays.asList("a", "b"));
        Robot robot = (Robot) automaton.getExpressionRobot().get(0);
        assertEquals(automaton.getMachines().size(), 1);
        assertEquals(robot.getTransitions().get(0), Arrays.asList("ø", "a", "ø"));
        assertEquals(robot.getTransitions().get(1), Arrays.asList("ø", "ø", "b"));
        assertEquals(robot.getTransitions().get(2), Arrays.asList("ø", "ø", "ø"));
    }

    @Test
    public void addAllToArrayUnion() throws Exception {
        automaton.addAllToArray("ab+cd");
        assertEquals(automaton.getWord(), Arrays.asList("a", "b", "c", "d"));
        Robot robotOne = (Robot) automaton.getExpressionRobot().get(0);
        Robot robotTwo = (Robot) automaton.getExpressionRobot().get(2);
        assertEquals(automaton.getExpressionRobot().size(), 3);
        assertEquals(automaton.getExpressionRobot().get(1), "+");
        assertEquals(robotOne.getTransitions().get(0), Arrays.asList("ø", "a", "ø"));
        assertEquals(robotOne.getTransitions().get(1), Arrays.asList("ø", "ø", "b"));
        assertEquals(robotOne.getTransitions().get(2), Arrays.asList("ø", "ø", "ø"));
        assertEquals(robotTwo.getTransitions().get(0), Arrays.asList("ø", "c", "ø"));
        assertEquals(robotTwo.getTransitions().get(1), Arrays.asList("ø", "ø", "d"));
        assertEquals(robotTwo.getTransitions().get(2), Arrays.asList("ø", "ø", "ø"));
    }

    @Test
    public void addExpressionRobot() throws Exception {
        Robot robot = generateMachine.base("a");
        automaton.addExpressionRobot(robot, "");
        assertEquals(automaton.getExpressionRobot().size(), 1);
        assertEquals(automaton.getExpressionRobot().get(0), robot);
    }

    @Test
    public void addExpressionRobotUnion() throws Exception {
        Robot robotOne = generateMachine.base("a");
        Robot robotTwo = generateMachine.base("b");
        automaton.addExpressionRobot(robotOne, "+");
        automaton.addExpressionRobot(robotTwo, "");
        assertEquals(automaton.getExpressionRobot().size(), 3);
        assertEquals(automaton.getExpressionRobot().get(0), robotOne);
        assertEquals(automaton.getExpressionRobot().get(1), "+");
        assertEquals(automaton.getExpressionRobot().get(2), robotTwo);
    }

    @Test
    public void processParenthesis() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("(");
        arrayList.add(generateMachine.base("a"));
        arrayList.add(")");
        arrayList.add("*");
        automaton.setExpressionRobot(arrayList);
        automaton.processParenthesis(1);
        assertEquals(automaton.getExpressionRobot().size(), 2);
    }

    @Test
    public void processParenthesisWithUnion() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("(");
        arrayList.add(generateMachine.base("a"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("b"));
        arrayList.add(")");
        arrayList.add("*");
        automaton.setExpressionRobot(arrayList);
        automaton.processParenthesis(1);
        assertEquals(automaton.getExpressionRobot().size(), 2);
    }

    @Test
    public void processParenthesisWithThreeUnion() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("(");
        arrayList.add(generateMachine.base("a"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("b"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("c"));
        arrayList.add(")");
        arrayList.add("*");
        automaton.setExpressionRobot(arrayList);
        automaton.processParenthesis(1);
        assertEquals(automaton.getExpressionRobot().size(), 2);
    }

    @Test
    public void processParenthesisWithTwoParenthesis() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("(");
        arrayList.add(generateMachine.base("a"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("b"));
        arrayList.add(")");
        arrayList.add("*");
        arrayList.add("+");
        arrayList.add("(");
        arrayList.add(generateMachine.base("c"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("d"));
        arrayList.add(")");
        arrayList.add("*");
        automaton.setExpressionRobot(arrayList);
        automaton.processParenthesis(2);
        assertEquals(automaton.getExpressionRobot().size(), 5);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "b", "ø"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(5));

        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(3)).getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "c", "ø", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(3)).getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(3)).getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "d", "ø"), ((Robot) automaton.getExpressionRobot().get(3)).getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(3)).getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(3)).getTransitions().get(5));
    }

    @Test
    public void processClosure() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(generateMachine.base("a"));
        arrayList.add("*");
        arrayList.add("+");
        arrayList.add(generateMachine.base("b"));
        arrayList.add("*");
        automaton.setExpressionRobot(arrayList);
        automaton.processClosure(2);
        assertEquals(automaton.getExpressionRobot().size(), 3);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(0)).getTransitions().get(3));

        assertEquals(Arrays.asList("ø", "ε", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(2)).getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b", "ø"), ((Robot) automaton.getExpressionRobot().get(2)).getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε"), ((Robot) automaton.getExpressionRobot().get(2)).getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø"), ((Robot) automaton.getExpressionRobot().get(2)).getTransitions().get(3));
    }

    @Test
    public void processIntersection() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(generateMachine.base("a"));
        arrayList.add(generateMachine.base("b"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("c"));
        automaton.setExpressionRobot(arrayList);
        automaton.processIntersection();
        assertEquals(automaton.getExpressionRobot().size(), 3);
    }

    @Test
    public void processIntersectionTwo() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(generateMachine.base("a"));
        arrayList.add(generateMachine.base("b"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("c"));
        arrayList.add(generateMachine.base("d"));
        automaton.setExpressionRobot(arrayList);
        automaton.processIntersection();
        assertEquals(automaton.getExpressionRobot().size(), 3);
    }

    @Test
    public void processUnion() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(generateMachine.base("a"));
        arrayList.add("+");
        arrayList.add(generateMachine.base("b"));
        automaton.setExpressionRobot(arrayList);
        Robot robot = automaton.processUnion();
        assertEquals(robot.getSizeRow(), 6);
        assertEquals(robot.getSizeColumn(), 6);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "b", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(5));
    }

    @Test
    public void processUnionWithIntersection() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(generateMachine.base("a"));
        arrayList.add(generateMachine.base("b"));
        automaton.setExpressionRobot(arrayList);
        Robot robot = automaton.processUnion();
        assertEquals(robot.getSizeColumn(), 3);
        assertEquals(robot.getSizeRow(), 3);
        assertEquals(Arrays.asList("ø", "a", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø"), robot.getTransitions().get(2));
    }

    @Test
    public void processUnionOneElement() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(generateMachine.base("a"));
        automaton.setExpressionRobot(arrayList);
        Robot robot = automaton.processUnion();
        assertEquals(robot.getSizeColumn(), 2);
        assertEquals(robot.getSizeRow(), 2);
        assertEquals(robot.getTransitions().get(0).get(0), "ø");
        assertEquals(robot.getTransitions().get(0).get(1), "a");
        assertEquals(robot.getTransitions().get(1).get(0), "ø");
        assertEquals(robot.getTransitions().get(1).get(1), "ø");
    }

    @Test
    public void moveOperations() throws Exception {
        Robot robot = generateMachine.base("a");
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("*");
        arrayList.add("+");
        arrayList.add(robot);
        assertEquals(arrayList, Arrays.asList("*", "+", robot));
        automaton.setExpressionRobot(arrayList);
        automaton.moveOperations(0, robot);
        assertEquals(arrayList, Arrays.asList(robot, "*", "+"));
    }

    @Test
    public void moveOperationsWithIndexOne() throws Exception {
        Robot robot = generateMachine.base("a");
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("*");
        arrayList.add("+");
        arrayList.add(robot);
        assertEquals(arrayList, Arrays.asList("*", "+", robot));
        automaton.setExpressionRobot(arrayList);
        automaton.moveOperations(1, robot);
        assertEquals(arrayList, Arrays.asList("*", robot, "+"));
    }

    @Test
    public void countParenthesis() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("*");
        arrayList.add("+");
        arrayList.add("(");
        arrayList.add("");
        arrayList.add(")");
        automaton.setExpressionRobot(arrayList);
        assertEquals(automaton.countSignal("("), 1);
    }

    @Test
    public void countClosure() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("*");
        arrayList.add("(");
        arrayList.add("*");
        arrayList.add(")");
        automaton.setExpressionRobot(arrayList);
        assertEquals(automaton.countSignal("*"), 2);
    }

    @Test
    public void addToMachine() throws Exception {
        automaton.addToMachine("a", 0);
        automaton.addToMachine("b", 0);
        assertEquals(automaton.getMachines().size(), 1);
        assertEquals(automaton.getMachines().get(0).size(), 2);
    }

    @Test
    public void addToMachineUnion() throws Exception {
        automaton.addToMachine("a", 0);
        automaton.addToMachine("b", 1);
        assertEquals(automaton.getMachines().size(), 2);
        assertEquals(automaton.getMachines().get(0).size(), 1);
        assertEquals(automaton.getMachines().get(1).size(), 1);
    }
}
