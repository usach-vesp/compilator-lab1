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

    CreateAutomaton automaton;
    GenerateMachine generateMachine;

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
        assertEquals(automaton.getOperations(), Arrays.asList());
        automaton.addAllToArray("ab+cd");
        assertEquals(automaton.getWord(), Arrays.asList("a", "b", "c", "d"));
        assertEquals(automaton.getOperations(), Arrays.asList("+"));
    }

    @Test
    public void regexToRobot() throws Exception {
        automaton.startProcess("ab");
        Robot robot = automaton.regexToRobot();
        assertEquals(robot.getTransitions().size(), 3);
        assertEquals(robot.getTransitions().get(0), Arrays.asList("ø", "a", "ø"));
        assertEquals(robot.getTransitions().get(1), Arrays.asList("ø", "ø", "b"));
        assertEquals(robot.getTransitions().get(2), Arrays.asList("ø", "ø", "ø"));
    }

    @Test
    public void regexToRobotUnion() throws Exception {
        automaton.startProcess("a+b");
        Robot robot = automaton.regexToRobot();
        assertEquals(robot.getTransitions().size(), 6);
        assertEquals(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "a", "ø", "ø", "ø"), robot.getTransitions().get(1));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(2));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "b", "ø"), robot.getTransitions().get(3));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ε"), robot.getTransitions().get(4));
        assertEquals(Arrays.asList("ø", "ø", "ø", "ø", "ø", "ø"), robot.getTransitions().get(5));
    }

    @Test
    public void addIntersection() throws Exception {
        ArrayList<ArrayList<Robot>> machines = new ArrayList<ArrayList<Robot>>();
        machines.add(0, new ArrayList<Robot>(Arrays.asList(generateMachine.base("a"), generateMachine.base("b"))));
        automaton.setMachines(machines);
        automaton.addIntersection();
        assertEquals(automaton.getIntersection().size(), 1);
        assertEquals(automaton.getIntersection().get(0).getTransitions().get(0), Arrays.asList("ø", "a", "ø"));
        assertEquals(automaton.getIntersection().get(0).getTransitions().get(1), Arrays.asList("ø", "ø", "b"));
        assertEquals(automaton.getIntersection().get(0).getTransitions().get(2), Arrays.asList("ø", "ø", "ø"));
    }

    @Test
    public void addIntersectionWithUnion() throws Exception {
        ArrayList<ArrayList<Robot>> machines = new ArrayList<ArrayList<Robot>>();
        machines.add(0, new ArrayList<Robot>(Arrays.asList(generateMachine.base("a"))));
        machines.add(1, new ArrayList<Robot>(Arrays.asList(generateMachine.base("c"))));
        automaton.setMachines(machines);
        automaton.addIntersection();
        assertEquals(automaton.getIntersection().size(), 2);
        assertEquals(automaton.getIntersection().get(0).getTransitions().get(0), Arrays.asList("ø", "a"));
        assertEquals(automaton.getIntersection().get(0).getTransitions().get(1), Arrays.asList("ø", "ø"));
        assertEquals(automaton.getIntersection().get(1).getTransitions().get(0), Arrays.asList("ø", "c"));
        assertEquals(automaton.getIntersection().get(1).getTransitions().get(1), Arrays.asList("ø", "ø"));
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
