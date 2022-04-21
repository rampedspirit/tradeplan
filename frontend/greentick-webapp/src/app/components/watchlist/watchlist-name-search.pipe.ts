import { Pipe, PipeTransform } from '@angular/core';
import { WatchlistResponse } from 'src/gen/watchlist';

@Pipe({
  name: 'watchlistNameSearch'
})
export class WatchlistNameSearchPipe implements PipeTransform {

  transform(watchlists: WatchlistResponse[], searchText: string): WatchlistResponse[] {
    if (searchText == null || watchlists == null) {
      return watchlists;
    }
    return watchlists.filter(watchlist => watchlist.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }

}
