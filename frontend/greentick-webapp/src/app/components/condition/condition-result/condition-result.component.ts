import { Component, Input, OnInit } from '@angular/core';
import { ConditionLanguageResultEditorOptions } from 'src/app/lang/condition/condition-language-result.editor.options';
import { ConditionLanguageIntellisense } from 'src/app/lang/condition/condition-language.intellisense';
import { ConditionResultResponse, FilterResult } from 'src/gen/condition';
import { FilterService } from 'src/gen/filter';

@Component({
  selector: 'app-condition-result',
  templateUrl: './condition-result.component.html',
  styleUrls: ['./condition-result.component.scss']
})
export class ConditionResultComponent implements OnInit {

  @Input()
  result: ConditionResultResponse;

  code: string;
  editorOptions = new ConditionLanguageResultEditorOptions();

  constructor(private filterService: FilterService) { }

  ngOnInit(): void {
    this.filterService.getAllFilters().subscribe(filters => {
      ConditionLanguageIntellisense.FILTERS = filters;
      this.updateCode();
    });
  }

  updateCode() {
    this.code = this.replaceFilterIdWithName(this.result.code, this.result.filtersResult);
  }

  private replaceFilterIdWithName(code: string, filterResults: FilterResult[]) {
    let replacedCode = code;
    let filterIds = Array.from(code.matchAll(/[a-zA-Z0-9-]*/g)).map(match => {
      return ConditionLanguageIntellisense.FILTERS.find(filter => filter.id == match[0]);
    }).filter(f => f != null || f != undefined).map(f => f.id);

    filterIds.forEach(filterId => {
      let filterName = ConditionLanguageIntellisense.FILTERS.find(f => f.id == filterId).name;
      let status = filterResults.find(r => r.filterId = filterId).status;
      replacedCode = replacedCode.replace(filterId, filterName + "<" + status + ">");
    });
    return replacedCode;
  }
}
