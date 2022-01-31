import { Pipe, PipeTransform } from '@angular/core';
import { ConditionResponse } from 'src/gen/condition';

@Pipe({
  name: 'conditionNameSearch'
})
export class ConditionNameSearchPipe implements PipeTransform {

  transform(conditions: ConditionResponse[], searchText: string): ConditionResponse[] {
    if (searchText == null || conditions == null) {
      return conditions;
    }
    return conditions.filter(filter => filter.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }

}
