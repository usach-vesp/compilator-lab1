package test.com.compiler.junit;

import com.compiler.action.GenerateMachine;
import com.compiler.machine.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RobotTest {

    private Robot robot;
    private static GenerateMachine generateMachine = new GenerateMachine();

    @Before
    public void setUp() throws Exception {
        robot = new Robot();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getTransitions() throws Exception {
        assertEquals(robot.getTransitions(), Arrays.asList());
    }

    @Test
    public void setTransitions() throws Exception {
        ArrayList arrayList = new ArrayList();
        arrayList.add(0, "a");
        robot.setTransitions(arrayList);
        assertEquals(robot.getTransitions(), arrayList);
    }

    @Test
    public void getStateInitial() throws Exception {
        assertEquals(robot.getStateInitial(), "");
    }

    @Test
    public void setStateInitial() throws Exception {
        robot.setStateInitial("A");
        assertEquals(robot.getStateInitial(), "A");
    }

    @Test
    public void getStateFinal() throws Exception {
        assertEquals(robot.getStateFinal(), "");
    }

    @Test
    public void setStateFinal() throws Exception {
        robot.setStateFinal("A");
        assertEquals(robot.getStateFinal(), "A");
    }

    @Test
    public void getSizeRow() throws Exception {
        assertEquals(robot.getSizeRow(), 0);
    }

    @Test
    public void setSizeRow() throws Exception {
        robot.setSizeRow(1);
        assertEquals(robot.getSizeRow(), 1);
    }

    @Test
    public void getSizeColumn() throws Exception {
        assertEquals(robot.getSizeColumn(), 0);
    }

    @Test
    public void syncSize() throws Exception{
        ArrayList arrayList = new ArrayList();
        arrayList.add(0, "a");
        robot.getTransitions().add(arrayList);
        robot.syncSize();
        assertEquals(robot.getSizeRow(), 1);
        assertEquals(robot.getSizeColumn(), 1);
    }

    @Test
    public void assignRowTransition() throws Exception{
        assertEquals(robot.getSizeRow(), 0);
        assertEquals(robot.getSizeColumn(), 0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(0, "a");
        robot.assignRowTransition(0, arrayList);
        assertEquals(robot.getTransitions().get(0), Arrays.asList("a"));
        assertEquals(robot.getSizeRow(), 1);
        assertEquals(robot.getSizeColumn(), 1);
    }

    @Test
    public void assignColumnTransition() throws Exception{
        robot.getTransitions().add(new ArrayList<String>(Arrays.asList("a")));
        ArrayList arrayList = new ArrayList();
        arrayList.add(0, "b");
        assertEquals(robot.getTransitions().get(0).size(), 1);
        robot.assignColumnTransition(arrayList);
        assertEquals(robot.getTransitions().get(0).size(), 2);
        assertEquals(robot.getTransitions().get(0).get(0), "a");
        assertEquals(robot.getTransitions().get(0).get(1), "b");
    }

    @Test
    public void assignRightEmptyToAll() throws Exception{
        ArrayList arrayListOne = new ArrayList(Arrays.asList("0", "1"));
        ArrayList arrayListTwo = new ArrayList(Arrays.asList("2", "3"));
        robot.getTransitions().add(arrayListOne);
        robot.getTransitions().add(arrayListTwo);
        assertEquals(robot.getTransitions().get(0).size(), 2);
        assertEquals(robot.getTransitions().get(1).size(), 2);
        robot.assignRightEmptyToAll(1);
        assertEquals(robot.getTransitions().get(0).size(), 3);
        assertEquals(robot.getTransitions().get(1).size(), 3);
        assertEquals(robot.getTransitions().get(0).get(2), "ø");
        assertEquals(robot.getTransitions().get(1).get(2), "ø");
    }

    @Test
    public void assignRightEmptyToRow() throws Exception{
        ArrayList arrayList = new ArrayList(Arrays.asList("0", "1"));
        robot.getTransitions().add(arrayList);
        assertEquals(robot.getTransitions().get(0).size(), 2);
        robot.assignRightEmptyToRow(2, 0);
        assertEquals(robot.getTransitions().get(0).size(), 4);
        assertEquals(robot.getTransitions().get(0).get(2), "ø");
        assertEquals(robot.getTransitions().get(0).get(3), "ø");
    }

    @Test
    public void assignLeftEmpty() throws Exception{
        robot.assignLeftEmpty(2);
        assertEquals(robot.getTransitions().get(0).size(), 2);
        assertEquals(robot.getTransitions().get(0).get(0), "ø");
        assertEquals(robot.getTransitions().get(0).get(1), "ø");
    }

    @Test
    public void createUnionMachine() throws Exception{
        robot.assignLeftEmpty(1);
        assertEquals(robot.createUnionMachine(generateMachine.base("a")), 2);
        assertEquals(robot.getTransitions().get(0), Arrays.asList("ø", "ε", "ø"));
        assertEquals(robot.getTransitions().get(1), Arrays.asList("ø", "ø", "a"));
        assertEquals(robot.getTransitions().get(2), Arrays.asList("ø", "ø", "ø"));
    }

    @Test
    public void squareUnionMachine() throws Exception{
        ArrayList arrayListOne = new ArrayList(Arrays.asList("0", "1", "2"));
        ArrayList arrayListTwo = new ArrayList(Arrays.asList("3", "4"));
        ArrayList arrayListThree = new ArrayList(Arrays.asList("5"));
        robot.getTransitions().add(arrayListOne);
        robot.getTransitions().add(arrayListTwo);
        robot.getTransitions().add(arrayListThree);
        robot.syncSize();
        robot.squareUnionMachine();
        assertEquals(robot.getTransitions().get(0).size(), 3);
        assertEquals(robot.getTransitions().get(0).get(1), "1");
        assertEquals(robot.getTransitions().get(0).get(2), "2");
        assertEquals(robot.getTransitions().get(1).size(), 3);
        assertEquals(robot.getTransitions().get(1).get(1), "4");
        assertEquals(robot.getTransitions().get(1).get(2), "ø");
        assertEquals(robot.getTransitions().get(2).size(), 3);
        assertEquals(robot.getTransitions().get(2).get(1), "ø");
        assertEquals(robot.getTransitions().get(2).get(2), "ø");
    }

    @Test
    public void createLastTransitionUnion() throws Exception{
        ArrayList finalStates = new ArrayList(Arrays.asList(1));
        ArrayList arrayListOne = new ArrayList(Arrays.asList("0", "1"));
        ArrayList arrayListTwo = new ArrayList(Arrays.asList("0", "1"));
        robot.assignRowTransition(0, arrayListOne);
        robot.assignRowTransition(1, arrayListTwo);
        assertEquals(robot.getTransitions().get(1).size(), 2);
        assertEquals(robot.getTransitions().get(1).get(1), "1");
        robot.createLastTransitionUnion(finalStates);
        assertEquals(robot.getTransitions().get(1).size(), 2);
        assertEquals(robot.getTransitions().get(1).get(1), "ε");
    }

    @Test
    public void closureIntersection() throws Exception{

    }

    @Test
    public void addRightEmptyIfNecessary() throws Exception{
        ArrayList arrayList = new ArrayList(Arrays.asList("0", "1"));
        robot.assignRowTransition(0, arrayList);
        robot.addRightEmptyIfNecessary(0);
        assertEquals(robot.getTransitions().size(), 1);
        robot.addRightEmptyIfNecessary(3);
        assertEquals(robot.getTransitions().get(0).size(), 4);
    }

    @Test
    public void copyFirstMachine() throws Exception{
        robot.copyFirstMachine(generateMachine.base("a"));
        assertEquals(robot.getTransitions().get(0).size(), 2);
        assertEquals(robot.getTransitions().get(0).get(0), "ø");
        assertEquals(robot.getTransitions().get(0).get(1), "a");
    }

    @Test
    public void setStatesInitialFinal() throws Exception{
        ArrayList arrayList = new ArrayList(Arrays.asList("A", "B"));
        robot.assignRowTransition(0, arrayList);
        robot.syncSize();
        robot.setStatesInitialFinal();
        assertEquals(robot.getStateInitial(), "0");
        assertEquals(robot.getStateFinal(), "1");
    }

}