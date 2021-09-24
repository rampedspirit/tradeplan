import { Injectable } from '@angular/core';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import { FilterLanguage } from '../lang/filter/filter-language';
import { FillterLanguageConfiguration } from '../lang/filter/filter-language.configuration';
import { FilterLanguageConstants } from '../lang/filter/filter-language.constants';
import { FilterLanguageFormatter } from '../lang/filter/filter-language.formatter';
import { FilterLanguageIntellisense } from '../lang/filter/filter-language.intellisense';
import { FilterLanguageTheme } from '../lang/filter/filter-language.theme';

export class MonacoWrapper {
  static register: (language: monaco.languages.ILanguageExtensionPoint) => void;

  static setLanguageConfiguration: (languageId: string, configuration: monaco.languages.LanguageConfiguration)
    => monaco.IDisposable;

  static setMonarchTokensProvider: (languageId: string,
    languageDef: monaco.languages.IMonarchLanguage | monaco.Thenable<monaco.languages.IMonarchLanguage>)
    => monaco.IDisposable;

  static registerDocumentFormattingEditProvider: (languageId: string, provider: monaco.languages.DocumentFormattingEditProvider)
    => monaco.IDisposable;

  static registerCompletionItemProvider: (languageId: string, provider: monaco.languages.CompletionItemProvider)
    => monaco.IDisposable;

  static registerHoverProvider: (languageId: string, provider: monaco.languages.HoverProvider) => monaco.IDisposable;

  static defineTheme: (themeName: string, themeData: monaco.editor.IStandaloneThemeData) => void

  static setModelMarkers: (model: monaco.editor.ITextModel, owner: string, markers: monaco.editor.IMarkerData[]) => void;

  static onEditorLoad() {
    let intellisense: FilterLanguageIntellisense = new FilterLanguageIntellisense();
    MonacoWrapper.registerLanguage(FilterLanguageConstants.LanguageId,
      new FillterLanguageConfiguration(),
      new FilterLanguage(),
      new FilterLanguageFormatter(), intellisense, intellisense);

    MonacoWrapper.registerTheme(FilterLanguageConstants.ThemeId, new FilterLanguageTheme());
  }

  private static registerLanguage(id: string,
    languageConfig: monaco.languages.LanguageConfiguration,
    language: monaco.languages.IMonarchLanguage,
    formattingProvider: monaco.languages.DocumentFormattingEditProvider,
    completionItemProvider: monaco.languages.CompletionItemProvider,
    hoverProvider: monaco.languages.HoverProvider): void {

    MonacoWrapper.register({ id: id });
    MonacoWrapper.setLanguageConfiguration(id, languageConfig);
    MonacoWrapper.setMonarchTokensProvider(id, language);
    MonacoWrapper.registerDocumentFormattingEditProvider(id, formattingProvider);
    MonacoWrapper.registerCompletionItemProvider(id, completionItemProvider);
    MonacoWrapper.registerHoverProvider(id, hoverProvider);
  }

  private static registerTheme(name: string, data: monaco.editor.IStandaloneThemeData): void {
    MonacoWrapper.defineTheme(name, data);
  }
}

@Injectable({
  providedIn: 'root'
})
export class EditorService {

  /**
   * Registers the theme
   * @param name 
   * @param data 
   */
  public setModelMarkers(model: monaco.editor.ITextModel, owner: string, markers: monaco.editor.IMarkerData[]): void {
    MonacoWrapper.setModelMarkers(model, owner, markers);
  }
}