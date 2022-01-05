import PEG, { ExpectedItem, PegjsError } from "pegjs";
import { TokenType } from "./token-type";
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

export class ConditionLanguageParser {

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
        if (expectedItem.type == "other" && expectedItem.description == "filter_name") {
            return TokenType.FILTER;
        } else if (expectedItem.type == "literal" && (expectedItem["text" as keyof ExpectedItem] == "AND"
            || expectedItem["text" as keyof ExpectedItem] == "OR")) {
            return TokenType.LOGICAL_OPERATOR;
        }
        return TokenType.SPACE;
    }

    /**
     * Parses for the filter name errors
     * @param query 
     * @returns 
     */
    public parseForLibraryErrors(query: string, isValidFilterName: (name: string) => boolean): LibraryError[] {
        let libraryErrors: LibraryError[] = [];
        let lines: string[] = query.split("\n");
        for (let i = 0; i < lines.length; i++) {
            let matches: RegExpMatchArray[] = Array.from(lines[i].matchAll(/\w*/g));
            matches
                .filter(match => match[0].trim().length > 0 && !match[0].match("AND|OR"))
                .map(match => isValidFilterName(match[0]) ? null : this.getLibraryError(i + 1, match.index, match.index + match[0].length))
                .forEach(libraryError => libraryErrors.push(libraryError));
        }
        return libraryErrors;
    }

    private getLibraryError(lineNumber: number, startIndex: number, endIndex: number): LibraryError {
        return {
            startLineNumber: lineNumber,
            startColumn: startIndex,
            endLineNumber: lineNumber,
            endColumn: endIndex,
            message: "Invalid Filter Name"
        }
    }
}