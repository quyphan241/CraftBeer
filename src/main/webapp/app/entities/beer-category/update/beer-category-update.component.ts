import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBeerCategory, BeerCategory } from '../beer-category.model';
import { BeerCategoryService } from '../service/beer-category.service';

@Component({
  selector: 'jhi-beer-category-update',
  templateUrl: './beer-category-update.component.html',
})
export class BeerCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    categoryName: [],
  });

  constructor(protected beerCategoryService: BeerCategoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beerCategory }) => {
      this.updateForm(beerCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beerCategory = this.createFromForm();
    if (beerCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.beerCategoryService.update(beerCategory));
    } else {
      this.subscribeToSaveResponse(this.beerCategoryService.create(beerCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeerCategory>>): void {
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

  protected updateForm(beerCategory: IBeerCategory): void {
    this.editForm.patchValue({
      id: beerCategory.id,
      categoryName: beerCategory.categoryName,
    });
  }

  protected createFromForm(): IBeerCategory {
    return {
      ...new BeerCategory(),
      id: this.editForm.get(['id'])!.value,
      categoryName: this.editForm.get(['categoryName'])!.value,
    };
  }
}
