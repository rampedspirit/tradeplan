import PEG, { ExpectedItem, PegjsError } from "pegjs";
import { FilterLanguageLibrary } from "./filter-language-library";
import { LFunction, LArgument } from "./filter-language.types";
import { TokenType } from './token-type';

const parser: PEG.Parser = require("./grammar.pegjs");

export class SytntaxError {
    startLineNumber: number;
    startColumn: number;
    endLineNumber: number;
    endColumn: number;
    message: string;
    expectedTokenTypes: Set<TokenType>;
}

export class LibraryError {
    startLineNumber: number;
    startColumn: number;
    endLineNumber: number;
    endColumn: number;
    message: string;
}

export class FilterLanguageParser {

    private library: FilterLanguageLibrary = new FilterLanguageLibrary();

    /**
     * Parses for syntax error
     * @param query
     * @returns 
     */
    public parseForSyntaxError(query: string): SytntaxError {
        try {
            parser.parse(query)
        } catch (error: any) {
            let pegError = error as PegjsError;
            let tokenTypes: Set<TokenType> = new Set(pegError.expected.map(this.getToken));
            let message: string = "Expected " + Array.from(tokenTypes).join(" or ");
            return {
                startLineNumber: pegError.location.start.line,
                startColumn: pegError.location.start.column,
                endLineNumber: pegError.location.end.line,
                endColumn: pegError.location.end.column,
                message: message,
                expectedTokenTypes: tokenTypes
            }
        }
        return null;
    }

    private getToken(expectedItem: ExpectedItem): TokenType {
        if (expectedItem.type == "other" && expectedItem.description == "function_name") {
            return TokenType.FUNCTION;
        } if (expectedItem.type == "other" &&
            (expectedItem.description == "function_open_bracket" || expectedItem.description == "function_close_bracket")) {
            return TokenType.FUNCTION;
        } else if (expectedItem.type == "literal" &&
            (expectedItem["text" as keyof ExpectedItem] == "(" || expectedItem["text" as keyof ExpectedItem] == ")")) {
            return TokenType.BRACKET;
        } else if (expectedItem.type == "other" && expectedItem.description == "argument") {
            return TokenType.ARGUMENT;
        } else if (expectedItem.type == "literal" && expectedItem["text" as keyof ExpectedItem] == ".") {
            return TokenType.DOT;
        } if (expectedItem.type == "other" && expectedItem.description == "value") {
            return TokenType.VALUE;
        } else if (expectedItem.type == "literal" && expectedItem["text" as keyof ExpectedItem] == "+") {
            return TokenType.ARITHMETIC_OPERATOR;
        } else if (expectedItem.type == "other" && expectedItem.description == "comparison_operator") {
            return TokenType.COMPARISON_OPERATOR;
        } else if (expectedItem.type == "literal" && (expectedItem["text" as keyof ExpectedItem] == "AND"
            || expectedItem["text" as keyof ExpectedItem] == "OR")) {
            return TokenType.LOGICAL_OPERATOR;
        }
        return TokenType.SPACE;
    }

    /**
     * Parses for the library errors
     * @param query 
     * @returns 
     */
    public parseForLibraryErrors(query: string): LibraryError[] {
        let libraryErrors: LibraryError[] = [];
        let lines: string[] = query.split("\n");
        for (let i = 0; i < lines.length; i++) {
            Array.from(lines[i].matchAll(/(\w*\(\w*[,\w*]*\))(\.\w*\(\w*[,\w*]*\))*/g))
                .map(match => this.validateFunctionChain(match[0], i + 1, match.index))
                .forEach(libraryError => libraryErrors.push(libraryError));
        }
        return libraryErrors;
    }

    private validateFunctionChain(functionChain: string, lineNumber: number, startIndex: number): LibraryError {
        let matchArray: RegExpMatchArray[] = Array.from(functionChain.matchAll(/\w*\(\w*[,\w*]*\)/g));
        for (let i = 0; i < matchArray.length; i++) {
            let errorMsg = null;
            let functionCall: string = matchArray[i][0];
            let funcName: string = this.getFunctionName(functionCall);
            let func: LFunction = this.library.functions.find(f => f.name == funcName);

            if (!func) {
                errorMsg = "Unknown function name : " + functionCall;
            } else if (!this.isFunctionValid(functionCall, func)) {
                errorMsg = "Invalid function arguments : " + functionCall;
            } else if ((!func.isChainOptional) && func.nextFunctions.length > 0
                && i == matchArray.length - 1) {
                errorMsg = "Function chain not terminated: " + functionCall;
            } else if (i < matchArray.length - 1
                && !func.nextFunctions.includes(this.getFunctionName(matchArray[i + 1][0]))) {
                errorMsg = "Invalid function chain : " + functionCall;
            }

            if (errorMsg) {
                return {
                    startLineNumber: lineNumber,
                    startColumn: startIndex,
                    endLineNumber: lineNumber,
                    endColumn: startIndex + functionChain.length,
                    message: errorMsg
                }
            }
        }
        return null;
    }

    private isFunctionValid(functionCall: string, func: LFunction): boolean {
        let functionPattern = func.name;
        functionPattern += "\\(";
        for (let i = 0; i < func.arguments.length; i++) {
            let arg: LArgument = func.arguments[i];
            functionPattern += "(";
            if (i != 0) {
                functionPattern += ","
            }
            functionPattern += "(" + arg.regex.source + ")";
            functionPattern += ")";
            if (arg.isOptional) {
                functionPattern += "?"
            }
        }
        functionPattern += "\\)";
        return functionCall.search(functionPattern) != -1;
    }

    private getFunctionName(functionCall: string): string {
        return functionCall.match(/\w*/)[0];
    }

    public getParseTree(query: string): string {
        return parser.parse(query);
    }
}