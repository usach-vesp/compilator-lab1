package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class GenerateMachineTest extends GenerateMachine {

    private static GenerateMachine generateMachine = new GenerateMachine();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void standard() throws Exception {
        HashMap<String, String> response;
        response = generateMachine.standard(new HashMap<String, String>(), "a");
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
        HashMap<String, String> response;
        firsMachine.put("a", "b");
        firsMachine.put("initial", "a");
        firsMachine.put("final", "b");
        secondMachine.put("c", "d");
        secondMachine.put("initial", "c");
        secondMachine.put("final", "d");
        response = generateMachine.intersection(firsMachine, secondMachine);
        assertEquals(response.size(), 5);
        assertEquals(response.get("initial"), "a");
        assertEquals(response.get("final"), "d");
        assertEquals(response.get("b"), "c");
    }

}