package test.com.compiler.junit;

import com.compiler.machine.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RobotTest {

    private Robot robot;

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

}