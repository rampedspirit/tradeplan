import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Tab {
  id: string;
  type: string;
  title: string;
  data: any;
}

@Injectable({
  providedIn: 'root'
})
export class TabAreaService {

  private tabs: Tab[];

  public selectedTabIndex: number;
  public currentTabs: BehaviorSubject<Tab[]>;

  constructor() {
    this.tabs = [];
    this.currentTabs = new BehaviorSubject<Tab[]>(this.tabs);
  }

  public addTab(tab: Tab) {
    let matchingTab = this.tabs.find(t => t.id == tab.id);
    if (!matchingTab) {
      matchingTab = tab;
      this.tabs.push(tab);
      this.currentTabs.next(this.tabs);
    }
    this.selectedTabIndex = this.tabs.indexOf(matchingTab);
  }

  public removeTab(tab: Tab) {
    this.tabs = this.tabs.filter(t => t !== tab);
    this.currentTabs.next(this.tabs);
  }
}
