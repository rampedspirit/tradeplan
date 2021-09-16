import { Pipe, PipeTransform } from '@angular/core';
import { Filter } from 'src/app/_gen';

@Pipe({
  name: 'filterNameSearch'
})
export class FilterNameSearchPipe implements PipeTransform {

  transform(filters: Filter[], searchText: string): Filter[] {
    if (searchText == null || filters == null) {
      return filters;
    }
    return filters.filter(filter => filter.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }
}
