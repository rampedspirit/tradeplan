import { Component, OnInit } from '@angular/core';
import { Tab, TabAreaService } from 'src/app/services/tab-area.service';
import { Filter } from 'src/app/_gen';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  constructor(public tabAreaService: TabAreaService) { }

  ngOnInit(): void {
  }

  closeTab(tab: Tab) {
    this.tabAreaService.removeTab(tab);
  }
}
