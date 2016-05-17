package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.machine.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class GenerateMachineTest extends GenerateMachine {

    private static GenerateMachine generateMachine = new GenerateMachine();
    private Robot robot;
    private HashMap response;

    @Before
    public void setUp() throws Exception {
        robot = new Robot();
    }

    @After
    public void tearDown() throws Exception {
        this.response.clear();
    }

    @Test
    public void base() throws Exception {
        this.robot = generateMachine.base("a");
        this.response = this.robot.getRobot();
        assertEquals(response.size(), 4);
        assertEquals(response.get("ø"), "a");
        assertEquals(response.get("a"), "ø");
        assertEquals(response.get("final"), "a");
        assertEquals(response.get("initial"), "ø");
    }

    @Test
    public void union() {

    }

    @Test
    public void intersection() throws Exception {
        HashMap<String, String> firsMachine = new HashMap<>();
        HashMap<String, String> secondMachine = new HashMap<>();
        firsMachine.put("a", "b");
        firsMachine.put("initial", "a");
        firsMachine.put("final", "b");
        secondMachine.put("c", "d");
        secondMachine.put("initial", "c");
        secondMachine.put("final", "d");
        this.robot = generateMachine.intersection(firsMachine, secondMachine);
        this.response = this.robot.getRobot();
        assertEquals(response.size(), 5);
        assertEquals(response.get("initial"), "a");
        assertEquals(response.get("final"), "d");
        assertEquals(response.get("a"), "b");
        assertEquals(response.get("b"), "c");
        assertEquals(response.get("c"), "d");
    }

}