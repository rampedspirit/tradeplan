import { Pipe, PipeTransform } from '@angular/core';
import { FilterResponse } from 'src/gen/filter';

@Pipe({
  name: 'filterNameSearch'
})
export class FilterNameSearchPipe implements PipeTransform {

  transform(filters: FilterResponse[], searchText: string): FilterResponse[] {
    if (searchText == null || filters == null) {
      return filters;
    }
    return filters.filter(filter => filter.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }
}
