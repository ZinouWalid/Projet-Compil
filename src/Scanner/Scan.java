package Scanner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Scan {
    public static final String[] KEYWORD = {"PROGRAM", "VAR",
            "INT", "FUNCTION", "BEGIN", "IF", "END", "THEN", "RETURN",
            "ELSE"};
    public static final String IDENTIFIERS = "i j n test fib";
    public static final String NUMBER = " 0 1";
    public static final String OPERATORS = "+ - := =";
    public static final String SEPARATORS = "; , : ( )";

    public static boolean isIdentifier(String token) {
        return IDENTIFIERS.contains(token);
    }

    public static boolean isNumber(String token) {
        return NUMBER.contains(token);
    }

    public static boolean isKeyword(String token) {
        for (int i = 0; i < KEYWORD.length; i++) {
            if (KEYWORD[i].equals(token))
                return true;
        }
        return false;
    }

    public static boolean isSeparator(String token) {
        return SEPARATORS.contains(token);
    }

    public static boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    public static String getSymbolName(String symbol) {
        switch (symbol) {
            case ":=":
                return "Assignment OP";
            case "+":
            case "-":
                return "Arithmetic OP";
            case "=":
                return "Relation OP";
            default:
                return "symbol";
        }
    }

}
