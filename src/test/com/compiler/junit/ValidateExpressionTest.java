package test.com.compiler.junit;

import com.compiler.action.ValidateExpression;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateExpressionTest {

    ValidateExpression pattern;

    @Before
    public void setUp() throws Exception {
        pattern = new ValidateExpression();
    }

    @Test
    public void isValid() throws Exception {
        assertEquals(pattern.isValid("AB"), true);
        assertEquals(pattern.isValid(""), false);
        assertEquals(pattern.isValid("(AB)"), true);
        assertEquals(pattern.isValid("A+B"), true);
        assertEquals(pattern.isValid("A(B)*"), true);
        assertEquals(pattern.isValid("A)B"), false);
        assertEquals(pattern.isValid("A(B"), false);
        assertEquals(pattern.isValid("(A+c)+B"), true);
        assertEquals(pattern.isValid("(A+c)*"), true);
        assertEquals(pattern.isValid("abc(abc+abc)+abc"), true);
    }

}