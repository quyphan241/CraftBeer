import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBeer, Beer } from '../beer.model';
import { BeerService } from '../service/beer.service';
import { IBeerCategory } from 'app/entities/beer-category/beer-category.model';
import { BeerCategoryService } from 'app/entities/beer-category/service/beer-category.service';
import { IManufactor } from 'app/entities/manufactor/manufactor.model';
import { ManufactorService } from 'app/entities/manufactor/service/manufactor.service';

@Component({
  selector: 'jhi-beer-update',
  templateUrl: './beer-update.component.html',
})
export class BeerUpdateComponent implements OnInit {
  isSaving = false;

  beerCategoriesSharedCollection: IBeerCategory[] = [];
  manufactorsSharedCollection: IManufactor[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    country: [],
    price: [],
    description: [],
    beerCategory: [],
    manufactor: [],
  });

  constructor(
    protected beerService: BeerService,
    protected beerCategoryService: BeerCategoryService,
    protected manufactorService: ManufactorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beer }) => {
      this.updateForm(beer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beer = this.createFromForm();
    if (beer.id !== undefined) {
      this.subscribeToSaveResponse(this.beerService.update(beer));
    } else {
      this.subscribeToSaveResponse(this.beerService.create(beer));
    }
  }

  trackBeerCategoryById(_index: number, item: IBeerCategory): number {
    return item.id!;
  }

  trackManufactorById(_index: number, item: IManufactor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(beer: IBeer): void {
    this.editForm.patchValue({
      id: beer.id,
      name: beer.name,
      country: beer.country,
      price: beer.price,
      description: beer.description,
      beerCategory: beer.beerCategory,
      manufactor: beer.manufactor,
    });

    this.beerCategoriesSharedCollection = this.beerCategoryService.addBeerCategoryToCollectionIfMissing(
      this.beerCategoriesSharedCollection,
      beer.beerCategory
    );
    this.manufactorsSharedCollection = this.manufactorService.addManufactorToCollectionIfMissing(
      this.manufactorsSharedCollection,
      beer.manufactor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.beerCategoryService
      .query()
      .pipe(map((res: HttpResponse<IBeerCategory[]>) => res.body ?? []))
      .pipe(
        map((beerCategories: IBeerCategory[]) =>
          this.beerCategoryService.addBeerCategoryToCollectionIfMissing(beerCategories, this.editForm.get('beerCategory')!.value)
        )
      )
      .subscribe((beerCategories: IBeerCategory[]) => (this.beerCategoriesSharedCollection = beerCategories));

    this.manufactorService
      .query()
      .pipe(map((res: HttpResponse<IManufactor[]>) => res.body ?? []))
      .pipe(
        map((manufactors: IManufactor[]) =>
          this.manufactorService.addManufactorToCollectionIfMissing(manufactors, this.editForm.get('manufactor')!.value)
        )
      )
      .subscribe((manufactors: IManufactor[]) => (this.manufactorsSharedCollection = manufactors));
  }

  protected createFromForm(): IBeer {
    return {
      ...new Beer(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      country: this.editForm.get(['country'])!.value,
      price: this.editForm.get(['price'])!.value,
      description: this.editForm.get(['description'])!.value,
      beerCategory: this.editForm.get(['beerCategory'])!.value,
      manufactor: this.editForm.get(['manufactor'])!.value,
    };
  }
}
