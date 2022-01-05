import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Tab {
  id: string;
  type: string;
  title: string;
  dirtyFlag: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TabAreaService {

  private tabs: Tab[];

  public selectedTabIndex: number;
  public openedTabs: BehaviorSubject<Tab[]>;

  constructor() {
    this.tabs = [];
    this.openedTabs = new BehaviorSubject<Tab[]>(this.tabs);
  }

  public openTab(tab: Tab) {
    let matchingTab = this.tabs.find(t => t.id == tab.id);
    if (!matchingTab) {
      matchingTab = tab;
      this.tabs.push(tab);
      this.openedTabs.next(this.tabs);
    }
    this.selectedTabIndex = this.tabs.indexOf(matchingTab);
  }

  public closeTab(id: string) {
    this.tabs = this.tabs.filter(t => t.id !== id);
    this.openedTabs.next(this.tabs);
  }
}
