import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { GreentickLanguageConstants } from '../greentick-language.constants';
import { ConditionLanguageConstants } from './condition-language.constants';

export class ConditionLanguageEditorOptions implements monaco.editor.IStandaloneEditorConstructionOptions {
    language = ConditionLanguageConstants.LanguageId;
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