package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.machine.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GenerateMachineTest extends GenerateMachine {

    private static GenerateMachine generateMachine = new GenerateMachine();
    private Robot robot;
    private ArrayList<ArrayList<String>> states;
    private ArrayList<String> transitions;

    @Before
    public void setUp() throws Exception {
        robot = new Robot();
        states = new ArrayList<ArrayList<String>>();
        transitions  = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        states.clear();
        transitions.clear();
    }

    @Test
    public void base() throws Exception {
        // 0: [ø, a]
        this.robot = generateMachine.base("a");
        assertEquals(this.robot.getStates().get(0).get(0), "ø");
        assertEquals(this.robot.getStates().get(0).get(1), "a");
        assertEquals(this.robot.getStateInitial(), "ø");
        assertEquals(this.robot.getStateFinal(), "a");
        assertEquals(this.robot.getStates().size(), 1);
        assertEquals(this.robot.getTransitions().size(), 2);
    }

    @Test
    public void union() {

    }

    @Test
    public void intersection() throws Exception {

    }

    @Test
    public void closure() throws Exception{

    }

}