package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.machine.Robot;
import com.compiler.transition.Transition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TransitionTest {

    Transition transition;
    GenerateMachine generateMachine = new GenerateMachine();
    Robot robot;

    @Before
    public void setUp() throws Exception {
        transition = new Transition();
        robot = new Robot();
        ArrayList<Robot> machines = new ArrayList<Robot>(Arrays.asList(generateMachine.base("a"),
                generateMachine.base("b")));
        robot = generateMachine.union(machines);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void turnToDeterministic() throws Exception {
        transition.turnToDeterministic(robot, new ArrayList<String>(Arrays.asList("a", "b")));
    }

    @Test
    public void searchFirstState() throws Exception {
        ArrayList arrayList = new ArrayList(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø"));
        transition.searchFirstState(arrayList);
        assertEquals(transition.getTransitions().size(), 3);
        assertEquals(transition.getTransitions().get(0), 0);
        assertEquals(transition.getTransitions().get(1), 1);
        assertEquals(transition.getTransitions().get(2), 3);
    }

    @Test
    public void searchStates() throws Exception{
        ArrayList<String> words = new ArrayList<>(Arrays.asList("a", "b"));
        transition.searchFirstState(robot.getTransitions().get(0));
        transition.searchStates(robot, words, 0);
        assertEquals(transition.getTransitions().size(), 3);
        assertEquals(transition.getTransitions().get(0), new ArrayList<Integer>(Arrays.asList(0, 1, 3)));
        assertEquals(transition.getTransitions().get(1), new ArrayList<Integer>(Arrays.asList(2, 5)));
        assertEquals(transition.getTransitions().get(2), new ArrayList<Integer>(Arrays.asList(4, 5)));
        assertEquals(transition.getNewStates().size(), 3);
        assertEquals(transition.getNewStates().get(0).get("a"), new ArrayList<Integer>(Arrays.asList(2, 5)));
        assertEquals(transition.getNewStates().get(0).get("b"), new ArrayList<Integer>(Arrays.asList(4, 5)));
        assertEquals(transition.getNewStates().get(1).get("a"), new ArrayList<Integer>());
        assertEquals(transition.getNewStates().get(1).get("b"), new ArrayList<Integer>());
        assertEquals(transition.getNewStates().get(2).get("a"), new ArrayList<Integer>());
        assertEquals(transition.getNewStates().get(2).get("b"), new ArrayList<Integer>());
    }

    @Test
    public void getNewTransitions() throws Exception{
        transition.searchFirstState(new ArrayList<String>(Arrays.asList("ø", "ε", "ø", "ε", "ø", "ø")));
        transition.getNewTransitions(robot, new ArrayList<String>(Arrays.asList("a", "b")), 0);
        assertEquals(transition.getTransitions().size(), 3);
        assertEquals(transition.getTransitions().get(0), new ArrayList<Integer>(Arrays.asList(0, 1, 3)));
        assertEquals(transition.getTransitions().get(1), new ArrayList<Integer>(Arrays.asList(2, 5)));
        assertEquals(transition.getTransitions().get(2), new ArrayList<Integer>(Arrays.asList(4, 5)));
        assertEquals(transition.getNewStates().size(), 1);
        assertEquals(transition.getNewStates().get(0).get("a"), new ArrayList<Integer>(Arrays.asList(2, 5)));
        assertEquals(transition.getNewStates().get(0).get("b"), new ArrayList<Integer>(Arrays.asList(4, 5)));
    }


    @Test
    public void findInRow() throws Exception{
        assertEquals(transition.findInRow(robot, "a", 0), new ArrayList<Integer>());
        assertEquals(transition.findInRow(robot, "a", 1), new ArrayList<Integer>(Arrays.asList(2)));
    }

    @Test
    public void findWithEpsilon() throws Exception{
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(0));
        transition.findWithEpsilon(robot, arrayList, 0);
        assertEquals(arrayList, Arrays.asList(0, 1, 3));
    }

    @Test
    public void validateTransition() throws Exception{
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(0, 1));
        transition.validateTransition(arrayList);
        assertEquals(transition.getTransitions().size(), 1);
        assertEquals(transition.getTransitions().get(0).size(), 2);
        assertEquals(transition.getTransitions().get(0), Arrays.asList(0, 1));
    }

}