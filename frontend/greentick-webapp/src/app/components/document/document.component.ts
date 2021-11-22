import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { FilterLanguageLibrary } from 'src/app/lang/filter/filter-language-library';
import { LFunction } from 'src/app/lang/filter/filter-language.types';

@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss']
})
export class DocumentComponent implements OnInit {

  displayedColumns: string[] = ['name', 'description', 'isOptional'];

  library: FilterLanguageLibrary
  selectedFunction: LFunction;

  constructor(private route: ActivatedRoute, private router: Router, private httpClient: HttpClient) {
    this.library = new FilterLanguageLibrary();
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setSelectedFunction();
      }
    });
  }

  ngOnInit(): void {
    this.setSelectedFunction();
  }

  setSelectedFunction() {
    let name = this.route.snapshot.paramMap.get('name');
    if (name) {
      this.selectedFunction = this.library.functions.find(func => func.name == name);
      this.httpClient.get("./assets/documentation/" + name + ".html", { responseType: 'text' }).subscribe(doc => {
        this.selectedFunction.moreInfo = doc as string;
      }, error => {
        this.selectedFunction.moreInfo = "";
      })
    }
  }

}
