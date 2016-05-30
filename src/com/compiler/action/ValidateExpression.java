package com.compiler.action;

public class ValidateExpression {

    private final String pattern = "^(\\w+$|^\\w+\\+\\w+)$|^(\\(\\w+\\)|\\(\\w+\\+\\w+\\))$|^(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\*$|^\\w*(\\(\\w+\\)|\\(\\w+\\+\\w+\\))$|^\\w+(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\*$|^\\w*(\\(\\w+\\)|\\(\\w+\\+\\w+\\))\\+\\w+$";

    public boolean isValid(String input){
        if (input.matches(pattern)){
            return this.balanceParenthesis(input);
        }
        return false;
    }

    private boolean balanceParenthesis(String input){
        int parenthesis = 0;
        for (String signal : input.split("")){
            if (signal.equals("(")){
                parenthesis = parenthesis + 1;
            }
            if (signal.equals(")")){
                parenthesis = parenthesis - 1;
            }
            if (parenthesis < 0){ break; }
        }
        return parenthesis == 0;
    }


}
