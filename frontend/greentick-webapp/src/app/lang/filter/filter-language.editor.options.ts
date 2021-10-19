import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { GreentickLanguageConstants } from '../greentick-language.constants';
import { FilterLanguageConstants } from './filter-language.constants';

export class FilterLanguageEditorOptions implements monaco.editor.IStandaloneEditorConstructionOptions {
    language = FilterLanguageConstants.LanguageId;
    contextmenu = false;
    theme = GreentickLanguageConstants.ThemeId
    fontSize = 18
    scrollBeyondLastLine = false;
    automaticLayout = true;
    fixedOverflowWidgets = true;
    wordBasedSuggestions = false;
    occurrencesHighlight = false;
    selectionHighlight = false;
}