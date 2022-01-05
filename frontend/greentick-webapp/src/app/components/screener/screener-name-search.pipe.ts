import { Pipe, PipeTransform } from '@angular/core';
import { ScreenerResponse } from 'src/gen/screener';

@Pipe({
  name: 'screenerNameSearch'
})
export class ScreenerNameSearchPipe implements PipeTransform {

  transform(screeners: ScreenerResponse[], searchText: string): ScreenerResponse[] {
    if (searchText == null || screeners == null) {
      return screeners;
    }
    return screeners.filter(filter => filter.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }
}
