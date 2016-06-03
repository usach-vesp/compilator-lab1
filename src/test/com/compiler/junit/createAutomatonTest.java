package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.action.CreateAutomaton;
import com.compiler.machine.Robot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
    public void moveOperations() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("*");
        arrayList.add("+");
        arrayList.add("(");
        arrayList.add("");
        arrayList.add(")");
        assertEquals(arrayList, Arrays.asList("*", "+", "(", "", ")"));
        automaton.setExpressionRobot(arrayList);
        automaton.moveOperations();
        assertEquals(arrayList, Arrays.asList("(", "", ")", "*", "+"));
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
        assertEquals(automaton.countParenthesis(), 1);
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
