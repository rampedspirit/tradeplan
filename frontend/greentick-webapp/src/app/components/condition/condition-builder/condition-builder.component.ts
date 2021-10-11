import { Component, Input, OnInit } from '@angular/core';
import { Filter, FilterService } from 'src/gen/filter';
import { FilterNotificationService } from '../../filter/filter-notification.service';
import { ConditionQuery } from './condition-query';

@Component({
  selector: 'app-condition-builder',
  templateUrl: './condition-builder.component.html',
  styleUrls: ['./condition-builder.component.scss']
})
export class ConditionBuilderComponent implements OnInit {

  @Input()
  conditionQuery: ConditionQuery;

  filters: Filter[];

  constructor(private filterService: FilterService, private filterNotificationService: FilterNotificationService) { }

  ngOnInit(): void {
    this.refresh();

    this.filterNotificationService.createSubject.subscribe(filter => {
      this.refresh();
    });

    this.filterNotificationService.updateSubject.subscribe(filter => {
      this.refresh();
    });

    this.filterNotificationService.deleteSubject.subscribe(id => {
      this.refresh();
    });
  }

  addCondition(isGroup: boolean) {
    this.conditionQuery.add(new ConditionQuery(isGroup));
  }

  refresh = () => {
    this.filterService.getAllFilters().subscribe(filters => {
      this.filters = filters;
    });
  }
}
