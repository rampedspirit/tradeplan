import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { FilterLanguageConstants } from './filter-language.constants';

export class FilterLanguageEditorOptions implements monaco.editor.IStandaloneEditorConstructionOptions {
    language = FilterLanguageConstants.LanguageId;
    contextmenu = false;
    theme = FilterLanguageConstants.ThemeId
    fontSize = 18
    scrollBeyondLastLine = false;
    automaticLayout = true;
    fixedOverflowWidgets = true;
    wordBasedSuggestions = false;
    occurrencesHighlight = false;
    selectionHighlight = false;
}