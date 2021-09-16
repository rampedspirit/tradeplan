import { ExpectedItem } from 'pegjs';

export enum TokenType {
    SPACE = "SPACE",
    DOT = ".",
    FUNCTION = "Function",
    ARGUMENT = "Argument",
    COMMA = ",",
    COMPARISON_OPERATOR = "Comparison Operator",
    ARITHMETIC_OPERATOR = "Arithmetic Operator",
    LOGICAL_OPERATOR = "Logical Operator",
    FUNCTION_BRACKET = "Bracket",
    BRACKET = "Bracket",
    VALUE = "Numeric Value"
}