import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBeerCategory } from '../beer-category.model';

@Component({
  selector: 'jhi-beer-category-detail',
  templateUrl: './beer-category-detail.component.html',
})
export class BeerCategoryDetailComponent implements OnInit {
  beerCategory: IBeerCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beerCategory }) => {
      this.beerCategory = beerCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
