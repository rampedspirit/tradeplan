import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

export class FilterLanguageFormatter implements monaco.languages.DocumentFormattingEditProvider {

    public provideDocumentFormattingEdits(model: monaco.editor.ITextModel, options: monaco.languages.FormattingOptions, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.TextEdit[]> {
        return [{
            text: this.doFormat(model.getValue()),
            range: model.getFullModelRange()
        }];
    }

    private doFormat(text: string): string {
        let groups: string[] = this.getGroups(text);
        let formattedLine: string = "";
        let indent: number = 0;
        for (let i = 0; i < groups.length; i++) {
            if (groups[i] == "[") {
                formattedLine += (i == 0 ? "" : "\n") + this.getIndentText(indent) + "[";
                ++indent;
            } else if (groups[i] == "]") {
                --indent;
                formattedLine += "\n" + this.getIndentText(indent) + "]";
            } else {
                formattedLine += (i == 0 ? "" : "\n") + this.getIndentText(indent) + groups[i];
            }
        }
        return formattedLine;
    }

    private getGroups(text: string): string[] {
        let line: string = text.replace(/\s+/gm, " ");
        let matches: IterableIterator<RegExpMatchArray> = line.matchAll(/^\[|(?<=\s)\[|\](?=\s)|\]|AND|OR/gm);
        let groups: string[] = [];

        let startIndex: number = 0;
        Array.from(matches).forEach(match => {
            let lineFragment = line.substring(startIndex, match.index).trim();
            let matchLength = match[0].length;
            if (lineFragment && lineFragment.length > 0) {
                groups.push(lineFragment);
            }
            groups.push(line.substring(match.index, match.index + matchLength));
            startIndex = match.index + matchLength;
        })
        let lastFragment = line.substring(startIndex).trim();
        if (lastFragment && lastFragment.length > 0) {
            groups.push(lastFragment);
        }
        return groups;
    }

    private getIndentText(indent: number): string {
        let indentText = "";
        for (let i = 0; i < indent; i++) {
            indentText += "\t";
        }
        return indentText;
    }

}