package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.machine.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GenerateMachineTest extends GenerateMachine {

    private static GenerateMachine generateMachine = new GenerateMachine();
    private Robot robot;
    private ArrayList<String> transitions;

    @Before
    public void setUp() throws Exception {
        robot = new Robot();
        transitions  = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        transitions.clear();
    }

    @Test
    public void base() throws Exception {
        // 0: [ø, a]
        this.robot = generateMachine.base("a");
        assertEquals(this.robot.getTransitions().get(0).get(0), "ø");
        assertEquals(this.robot.getTransitions().get(0).get(1), "a");
        assertEquals(this.robot.getTransitions().get(1).get(0), "ø");
        assertEquals(this.robot.getTransitions().get(1).get(1), "ø");
        assertEquals(this.robot.getStateInitial(), "ø");
        assertEquals(this.robot.getStateFinal(), "a");
        assertEquals(this.robot.getTransitions().size(), 2);
        assertEquals(this.robot.getTransitions().get(0).size(), 2);
    }

    @Test
    public void union() {
        Robot firstMachine = generateMachine.base("a");
        Robot secondMachine = generateMachine.base("b");
        robot = generateMachine.union(new ArrayList(Arrays.asList(firstMachine, secondMachine)));
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
    public void unionThreeMachine() {
        Robot firstMachine = generateMachine.base("a");
        Robot secondMachine = generateMachine.base("b");
        Robot thirdMachine = generateMachine.base("c");
        robot = generateMachine.union(new ArrayList(Arrays.asList(firstMachine, secondMachine, thirdMachine)));
        assertEquals(robot.getSizeRow(), 8);
        assertEquals(robot.getSizeColumn(), 8);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ε", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "b", "ø", "ø", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "c", "ø"), robot.getTransitions().get(5));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(6));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(7));
    }

    @Test
    public void unionBaseWithClosureMachine() {
        Robot firstMachine = generateMachine.base("a");
        Robot secondMachine = generateMachine.closure(generateMachine.base("b"));
        robot = generateMachine.union(new ArrayList(Arrays.asList(firstMachine, secondMachine)));
        assertEquals(robot.getSizeRow(), 8);
        assertEquals(robot.getSizeColumn(), 8);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ε", "ø", "ε", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "b", "ø", "ø"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ε", "ø", "ε", "ø"), robot.getTransitions().get(5));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(6));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(7));
    }

    @Test
    public void intersection() throws Exception {
        Robot firstMachine;
        Robot secondMachine;
        firstMachine = generateMachine.base("a");
        secondMachine = generateMachine.base("b");
        robot = generateMachine.intersection(firstMachine, secondMachine);
        assertEquals(robot.getStateInitial(), "ø");
        assertEquals(robot.getStateFinal(), "b");
        assertEquals(robot.getSizeColumn(), 3);
        assertEquals(robot.getSizeRow(), 3);
        assertEquals(Arrays.asList("ø", "a", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø"), robot.getTransitions().get(2));
    }

    @Test
    public void closure() throws Exception{
        Robot machine;
        machine = generateMachine.base("a");
        robot = generateMachine.closure(machine);
        assertEquals(robot.getSizeRow(), 4);
        assertEquals(robot.getSizeColumn(), 4);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø"), robot.getTransitions().get(3));
    }

    @Test
    public void closureTwoWords() throws Exception{
        Robot firstMachine;
        Robot secondMachine;
        firstMachine = generateMachine.base("a");
        secondMachine = generateMachine.base("b");
        robot = generateMachine.closure(generateMachine.intersection(firstMachine, secondMachine));
        assertEquals(robot.getSizeRow(), 5);
        assertEquals(robot.getSizeColumn(), 5);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ø", "ε"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "b", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ε", "ø", "ø", "ε"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(4));
    }

    @Test
    public void TwoClosureIntersection() throws Exception{
        Robot firstMachine = generateMachine.closure(generateMachine.base("a"));
        Robot secondMachine = generateMachine.closure(generateMachine.base("b"));
        robot = generateMachine.intersection(firstMachine, secondMachine);
        assertEquals(robot.getSizeRow(), 7);
        assertEquals(robot.getSizeColumn(), 7);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø", "ø"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ε", "ø", "ε"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "b", "ø"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ε", "ø", "ε"), robot.getTransitions().get(5));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(6));
    }

}