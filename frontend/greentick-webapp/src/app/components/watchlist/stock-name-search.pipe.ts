import { Pipe, PipeTransform } from '@angular/core';
import { SymbolResponse } from 'src/gen/watchlist';

@Pipe({
  name: 'stockNameSearch'
})
export class StockNameSearchPipe implements PipeTransform {

  transform(stocks: SymbolResponse[], searchText: string): SymbolResponse[] {
    if (searchText == null || stocks == null) {
      return stocks;
    }
    return stocks.filter(stock => stock.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1) ||
      stocks.filter(stock => stock.symbol.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }

}
