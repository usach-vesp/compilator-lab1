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
        assertEquals(this.robot.getStateInitial(), "ø");
        assertEquals(this.robot.getStateFinal(), "a");
        assertEquals(this.robot.getTransitions().size(), 1);
        assertEquals(this.robot.getTransitions().get(0).size(), 2);
    }

    @Test
    public void union() {

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
        assertEquals(robot.getSizeRow(), 2);
        assertEquals(Arrays.asList("ø", "a", "ø"), robot.getTransitions().get(0));
        assertEquals(Arrays.asList("ø", "ø", "b"), robot.getTransitions().get(1));
    }

    @Test
    public void closure() throws Exception{

    }

}