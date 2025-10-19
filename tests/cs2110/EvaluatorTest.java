package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EvaluatorTest {

    @DisplayName("WHEN we evaluate an expression containing only a single digit, THEN that digit "
            + "is returned.")
    @Test
    public void testDigit() throws MalformedExpressionException{
        assertEquals(0, ExpressionEvaluator.evaluate("0"));
        assertEquals(1, ExpressionEvaluator.evaluate("1"));
        assertEquals(5, ExpressionEvaluator.evaluate("5"));
    }

    @DisplayName("WHEN we evaluate an expression containing only a single digit within "
            + "parentheses, THEN that digit is returned.")
    @Test
    public void testParenthesizedDigit() throws MalformedExpressionException{
        assertEquals(0, ExpressionEvaluator.evaluate("(0)"));
        assertEquals(2, ExpressionEvaluator.evaluate("(2)"));
        assertEquals(4, ExpressionEvaluator.evaluate("((4))"));
    }

    @DisplayName("WHEN we evaluate an expression containing one addition operation applied to two "
            + "single-digit operands, THEN the correct result is returned.")
    @Test
    public void testOneAddition() throws MalformedExpressionException{
        assertEquals(3, ExpressionEvaluator.evaluate("1+2"));
        assertEquals(11, ExpressionEvaluator.evaluate("4+7"));
        assertEquals(9, ExpressionEvaluator.evaluate("9+0"));
    }
    @DisplayName("WHEN we evaluate an expression containing one multiplication operation applied to "
            + "two single-digit operands, THEN the correct result is returned.")
    @Test
    public void testOneMultiplication() throws MalformedExpressionException{
        assertEquals(2, ExpressionEvaluator.evaluate("1*2"));
        assertEquals(28, ExpressionEvaluator.evaluate("4*7"));
        assertEquals(0, ExpressionEvaluator.evaluate("9*0"));
    }

    @DisplayName("WHEN we evaluate an expression containing one addition operation applied to two "
            + "single-digit operands with additional parentheses, THEN the correct result is "
            + "returned.")
    @Test
    public void testOneOperatorParentheses() throws MalformedExpressionException{
        assertEquals(3, ExpressionEvaluator.evaluate("(1+2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("(1)+2"));
        assertEquals(3, ExpressionEvaluator.evaluate("1+(2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("(1)+(2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("((1)+2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("(1+(2))"));
        assertEquals(3, ExpressionEvaluator.evaluate("((1)+(2))"));
    }

    @DisplayName("WHEN an expression contains multiple of the same operator, THEN "
            + "it is correctly evaluated")
    @Test
    public void testOneOperatorMultipleTimes() throws MalformedExpressionException{
        assertEquals(6, ExpressionEvaluator.evaluate("1+2+3"));
        assertEquals(21, ExpressionEvaluator.evaluate("4+8+9"));
        assertEquals(28, ExpressionEvaluator.evaluate("1+2+3+4+5+6+7"));
        assertEquals(84, ExpressionEvaluator.evaluate("4*7*3"));
        assertEquals(180, ExpressionEvaluator.evaluate("5*6*2*3"));
    }

    @DisplayName("WHEN an expression contains both addition and multiplication but no "
            + "parentheses, THEN the order of operations is respected.")
    @Test
    public void testBothOperators() throws MalformedExpressionException{
        assertEquals(7, ExpressionEvaluator.evaluate("1+2*3"));
        assertEquals(5, ExpressionEvaluator.evaluate("1*2+3"));
        assertEquals(15, ExpressionEvaluator.evaluate("1+2+3*4"));
        assertEquals(11, ExpressionEvaluator.evaluate("1+2*3+4"));
        assertEquals(14, ExpressionEvaluator.evaluate("1*2+3*4"));
        assertEquals(25, ExpressionEvaluator.evaluate("1+2*3*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("1*2*3+4"));
    }

    @DisplayName("WHEN an expression contains both addition and multiplication and "
            + "non-nested parentheses, THEN the order of operations is respected.")
    @Test
    public void testBothOperatorsParentheses() throws MalformedExpressionException{
        assertEquals(14, ExpressionEvaluator.evaluate("2+(3*4)"));
        assertEquals(20, ExpressionEvaluator.evaluate("(2+3)*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("(2*3)+4"));
        assertEquals(14, ExpressionEvaluator.evaluate("2*(3+4)"));
        assertEquals(45, ExpressionEvaluator.evaluate("(2+3)*(4+5)"));
        assertEquals(70, ExpressionEvaluator.evaluate("2*(3+4)*5"));
    }

    @DisplayName("WHEN an expression contains both addition and multiplication and "
            + "nested parentheses, THEN the order of operations is respected.")
    @Test
    public void testBothOperatorsNestedParentheses() throws MalformedExpressionException{
        assertEquals(94, ExpressionEvaluator.evaluate("2*(3+4*(5+6))"));
    }

    @DisplayName("WHEN an expression contains multi-digit integers, THEN the "
            + "expression is evaluated correctly")
    @Test

    public void testMultiDigitEvaluation() throws MalformedExpressionException{
        //Basic operations
        assertEquals(31 , ExpressionEvaluator.evaluate("26+5"));
        assertEquals(31 , ExpressionEvaluator.evaluate("5+26"));
        assertEquals(76 , ExpressionEvaluator.evaluate("21+55"));
        assertEquals(65 , ExpressionEvaluator.evaluate("13*5"));
        assertEquals(76 , ExpressionEvaluator.evaluate("4*19"));
        assertEquals(120 , ExpressionEvaluator.evaluate("12*10"));


        //With parentheses
        assertEquals(14, ExpressionEvaluator.evaluate("2+(3*4)"));
        assertEquals(20, ExpressionEvaluator.evaluate("(2+3)*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("(2*3)+4"));
        assertEquals(14, ExpressionEvaluator.evaluate("2*(3+4)"));
        assertEquals(45, ExpressionEvaluator.evaluate("(2+3)*(4+5)"));
        assertEquals(70, ExpressionEvaluator.evaluate("2*(3+4)*5"));
        assertEquals(160, ExpressionEvaluator.evaluate("(8+24)*(4+1)"));
        assertEquals(210, ExpressionEvaluator.evaluate("2*((10+5)*7)"));
        assertEquals(123, ExpressionEvaluator.evaluate("100+(23)"));
        assertEquals(144, ExpressionEvaluator.evaluate("(12*11)+(12)"));
        assertEquals(99, ExpressionEvaluator.evaluate("(50+49)"));
        assertEquals(225, ExpressionEvaluator.evaluate("(15*15)"));
        assertEquals(240, ExpressionEvaluator.evaluate("(12+8)*(10+2)"));
        assertEquals(72, ExpressionEvaluator.evaluate("(6*6)+(6*6)"));
        assertEquals(194346, ExpressionEvaluator.evaluate("(166+(19*56)+(26*6))+((5*2*41+525)+5*5)*201"));

    }

    @DisplayName("WHEN an expression contains white space, THEN the expression "
            + "still evaluates properly")
    @Test
    public void testWhitespaceTrue() throws MalformedExpressionException{
        assertEquals(7, ExpressionEvaluator.evaluate("1      +2*  3   "));
        assertEquals(5, ExpressionEvaluator.evaluate("1*2 +3"));
        assertEquals(15, ExpressionEvaluator.evaluate(" 1 + 2 + 3 * 4 "));
        assertEquals(11, ExpressionEvaluator.evaluate("1  +2 * 3+ 4"));
        assertEquals(14, ExpressionEvaluator.evaluate("1* 2+3*4"));
        assertEquals(25, ExpressionEvaluator.evaluate("1 +2         *3*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("1*2*3+4       "));
        assertEquals(14, ExpressionEvaluator.evaluate("2+(3*4)"));
        assertEquals(20, ExpressionEvaluator.evaluate("     (2+3)*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("(2*3)+4 "));
        assertEquals(14, ExpressionEvaluator.evaluate("2*(3+   4)"));
        assertEquals(45, ExpressionEvaluator.evaluate(" ( 2 + 3  )*  ( 4 + 5  ) "));
        assertEquals(70, ExpressionEvaluator.evaluate("2* (3+4)   *5"));
        assertEquals(72, ExpressionEvaluator.evaluate("(6*6)+( 6* 6)"));
        assertEquals(99, ExpressionEvaluator.evaluate("(50+49) "));

    }

    @DisplayName("WHEN an inpute has an invalid whitespace input, THEN throw a "
            + "MalformedExpressionException")

    @Test
    public void testWhitespaceFalse() throws MalformedExpressionException{
        assertThrows(MalformedExpressionException.class, () -> ExpressionEvaluator.evaluate("(8+ 2   4)*(4  +1)"));
        assertThrows(MalformedExpressionException.class,() -> ExpressionEvaluator.evaluate("2*((1 0+ 5)*7)"));
        assertThrows(MalformedExpressionException.class, ()->ExpressionEvaluator.evaluate("1  0 0+ ( 23)"));
        assertThrows(MalformedExpressionException.class, () ->ExpressionEvaluator.evaluate("(15*1 5)"));
        assertThrows(MalformedExpressionException.class, ()->ExpressionEvaluator.evaluate("(12*11)+(1 2)"));
        assertThrows(MalformedExpressionException.class,()-> ExpressionEvaluator.evaluate("(12+8  )*(10  +2)"));
        assertThrows(MalformedExpressionException.class, ()->ExpressionEvaluator.evaluate("( 1 6 6 +  (19*  56)+(2  6*6  ))+(  (5 * 2       *41  + 52 5 ) + 5 * 5 )*  20 1 "));
    }




    // TODO: Add unit testing for all of the features that you add to the ExpressionEvaluator
    //  over the course of the assignment. Be sure that your tests have descriptive method names
    //  and @DisplayNames. Your tests will be evaluated for their correctness and coverage.
}
