import { Pipe, PipeTransform } from '@angular/core';
import { Condition } from 'src/gen/condition';

@Pipe({
  name: 'conditionNameSearch'
})
export class ConditionNameSearchPipe implements PipeTransform {

  transform(conditions: Condition[], searchText: string): Condition[] {
    if (searchText == null || conditions == null) {
      return conditions;
    }
    return conditions.filter(filter => filter.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }

}
