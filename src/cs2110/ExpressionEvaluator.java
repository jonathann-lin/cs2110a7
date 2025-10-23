package cs2110;

import java.util.Scanner;

public class ExpressionEvaluator {


    /**
     * Evaluates the given well-formed mathematical expression `expr` and returns its value.
     * Currently, the `evaluate()` method supports: - Multi-digit int literals - Addition -
     * Multiplication - Parentheses - Whitespace handling - Subtraction - Unary negation -
     * Implict multiplication
     * <p>
     * Throws a MalformedExpressionException if the given expression is malformed.
     * Malformed expressions include when parenthesis are mismatched, invalid character, operator in wrong
     * position, trailing operator, whitespace between digits.
     *
     */
    public static int evaluate(String expr) throws MalformedExpressionException {
        Stack<Integer> operands = new LinkedStack<>();
        Stack<Character> operators = new LinkedStack<>(); // invariant: contains only '(', '+', and '*'

        boolean closingParenthesis = false; // will allow us to track if there's a closing parenthesis for implict multiplication
        boolean expectingOperator = false; // in infix notation, the first operand comes before an operator
        boolean canContinueNumber = false; //records whether there is a previous number that can be extended to multiple digits

        for (char c : expr.toCharArray()) { // arrays are Iterable, so can be used in enhanced-for loops
            if (c == '(') {
                if (expectingOperator) {
                    operators.push('*');
                    expectingOperator = false;
                    //throw new MalformedExpressionException("'(' cannot follow an operand");
                }
                operators.push('(');
                canContinueNumber = false;
                closingParenthesis = false;
            } else if (c == '*') {
                if (!expectingOperator) {
                    throw new MalformedExpressionException(
                            "'*' is not allowed in that position");
                }
                while (!operators.isEmpty() && operators.peek() == '*') {
                    oneStepSimplify(operands, operators);
                }
                operators.push('*');
                expectingOperator = false;
                canContinueNumber = false;
                closingParenthesis = false;

            } else if (c == '+') {
                if (!expectingOperator) {
                    throw new MalformedExpressionException(
                            "'+' is not allowed in that position");
                }
                while (!operators.isEmpty() && (operators.peek() == '*'
                        || operators.peek() == '+' || operators.peek() == '-')) {
                    oneStepSimplify(operands, operators);
                }
                operators.push('+');
                expectingOperator = false;
                canContinueNumber = false;
                closingParenthesis = false;

            } else if (c == ')') {
                if (!expectingOperator) {
                    throw new MalformedExpressionException(
                            "')' is not allowed in that position");
                }
                assert !operators.isEmpty() : "mismatched parentheses, extra ')'";
                if (operators.isEmpty()) {
                    throw new MalformedExpressionException("mismatched parentheses, extra ')'");
                }
                while (operators.peek() != '(') {
                    oneStepSimplify(operands, operators);
                    if (operators.isEmpty()) {
                        throw new MalformedExpressionException("mismatched parentheses, extra ')'");
                    }
                }
                operators.pop(); // remove '('
                closingParenthesis=true;
                canContinueNumber = false;
            } else if (c == '-') {
                if (!expectingOperator) { // Case of unary: treat like multiplication by -1
                    operators.push('*');
                    operands.push(-1);
                } else { // Otherwise just subtraction
                    while (!operators.isEmpty() && (operators.peek() == '*'
                            || operators.peek() == '+' || operators.peek() == '-')){
                        oneStepSimplify(operands, operators);
                    }
                    operators.push('-');
                    expectingOperator=false;
                    canContinueNumber=false;
                    closingParenthesis = false;

                }

            } else if (Character.isWhitespace(c)) {
                canContinueNumber = false; //ensure that additional digits of a multi-digit integer are not allowed after whitespace.
            } else { // c is a digit
                if (!(c >= '0' && c <= '9')) {
                    throw new MalformedExpressionException(
                            "expression contains an illegal character");
                }
                if (closingParenthesis){
                    operators.push('*');
                    expectingOperator=false;
                    closingParenthesis=false;
                }
                if (expectingOperator && !canContinueNumber) {
                    throw new MalformedExpressionException(
                            "Unexpected digit not after operator or multi-digit integer");
                }
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

        if (!expectingOperator) {
            throw new MalformedExpressionException(
                    "expression must end with an operand, not an operator");
        }
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                throw new MalformedExpressionException("mismatched parentheses, extra '('");
            }
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
        assert op == '+' || op == '*' || op == '-';

        int o2 = operands.pop(); // second operand is higher on stack
        int o1 = operands.pop();
        if (op == '+'){ // case of +
            operands.push(o1 + o2);
        }
        else if (op == '*'){ // case of *
            operands.push(o1*o2);
        }
        else { // case of -
            operands.push(o1-o2);
        }
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
                try {
                    System.out.println("= " + evaluate(expr));
                } catch (MalformedExpressionException malformedException) {
                    System.out.println("Error: " + malformedException.getMessage());
                }

            }
        }
    }
}
