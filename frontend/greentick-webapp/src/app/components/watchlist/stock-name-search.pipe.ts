import { Pipe, PipeTransform } from '@angular/core';
import { StockResponse } from 'src/gen/stock';

@Pipe({
  name: 'stockNameSearch'
})
export class StockNameSearchPipe implements PipeTransform {

  transform(stocks: StockResponse[], searchText: string): StockResponse[] {
    if (searchText == null || stocks == null) {
      return stocks;
    }
    return stocks.filter(stock => stock.name.toLowerCase().indexOf(searchText.toLowerCase()) !== -1);
  }

}
