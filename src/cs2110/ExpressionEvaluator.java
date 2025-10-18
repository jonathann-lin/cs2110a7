package cs2110;

import java.util.Scanner;

public class ExpressionEvaluator {

    // TODO: Update these specs as you build out the functionality of the `evaluate()` method.

    /**
     * Evaluates the given well-formed mathematical expression `expr` and returns its value.
     * Currently, the `evaluate()` method supports: - Single-digit int literals - Addition -
     * Multiplication - Parentheses
     */
    public static int evaluate(String expr) {
        Stack<Integer> operands = new LinkedStack<>();
        Stack<Character> operators = new LinkedStack<>(); // invariant: contains only '(', '+', and '*'

        boolean expectingOperator = false; // in infix notation, the first operand comes before an operator
        boolean canContinueNumber = false; //records whether there is a previous number that can be extended to multiple digits

        for (char c : expr.toCharArray()) { // arrays are Iterable, so can be used in enhanced-for loops
            if (c == '(') {
                assert !expectingOperator : "'(' cannot follow an operand";
                operators.push('(');
                canContinueNumber = false;
            } else if (c == '*') {
                assert expectingOperator : "'*' must follow an operand, not an operator";
                while (!operators.isEmpty() && operators.peek() == '*') {
                    oneStepSimplify(operands, operators);
                }
                operators.push('*');
                expectingOperator = false;
                canContinueNumber = false;
            } else if (c == '+') {
                assert expectingOperator : "'+' must follow an operand, not an operator";
                while (!operators.isEmpty() && (operators.peek() == '*'
                        || operators.peek() == '+')) {
                    oneStepSimplify(operands, operators);
                }
                operators.push('+');
                expectingOperator = false;
                canContinueNumber = false;
            } else if (c == ')') {
                assert expectingOperator : "')' must follow an operand, not an operator";
                assert !operators.isEmpty() : "mismatched parentheses, extra ')'";
                while (operators.peek() != '(') {
                    oneStepSimplify(operands, operators);
                    assert !operators.isEmpty() : "mismatched parentheses, extra ')'";
                }
                operators.pop(); // remove '('
                canContinueNumber = false;
            } else { // c is a digit
                assert c >= '0' && c <= '9' : "expression contains an illegal character";
                assert !expectingOperator
                        || canContinueNumber : "Can not be expecting an operator, (expecting operand) unless we are continuing from a previous number";
                if (canContinueNumber) { //extending from previous number
                    int prev = operands.pop();
                    operands.push(prev * 10 + (c - '0'));
                } else { //starting new number
                    operands.push(c - '0'); // convert c to an int and auto-box
                }
                expectingOperator = true;
                canContinueNumber = true;
            }
        }

        assert expectingOperator : "expression must end with an operand, not an operator";
        while (!operators.isEmpty()) {
            assert operators.peek() != '(' : "mismatched parentheses, extra '('";
            oneStepSimplify(operands, operators);
        }

        // If the above assertions pass, the operands stack should include exactly one value,
        // the return value. We'll include two assertions to verify this as a sanity check.
        assert !operands.isEmpty();
        int result = operands.pop();
        assert operands.isEmpty();
        return result;
    }

    /**
     * Helper method that partially simplifies the expression by `pop()`ping one operator from the
     * `operators` stack, `pop()`ping its two operands from the `operands` stack, evaluating the
     * operator, and then `push()`ing its result onto the `operands` stack. Requires that
     * `operators.peek()` is '+' or '*' and `operands` includes at least two elements.
     */
    private static void oneStepSimplify(Stack<Integer> operands, Stack<Character> operators) {
        char op = operators.pop();
        assert op == '+' || op == '*';

        int o2 = operands.pop(); // second operand is higher on stack
        int o1 = operands.pop();
        operands.push(op == '+' ? o1 + o2 : o1 * o2);
    }


    /**
     * A very basic calculator application.
     */
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            while (true) { // repeat indefinitely
                System.out.print("Enter an expression, or enter \"q\" to quit: ");
                String expr = in.nextLine();
                if (expr.equals("q")) {
                    break; // exit loop
                }
                System.out.println("= " + evaluate(expr));
            }
        }
    }
}
