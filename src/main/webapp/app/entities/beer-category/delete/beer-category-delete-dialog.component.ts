import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBeerCategory } from '../beer-category.model';
import { BeerCategoryService } from '../service/beer-category.service';

@Component({
  templateUrl: './beer-category-delete-dialog.component.html',
})
export class BeerCategoryDeleteDialogComponent {
  beerCategory?: IBeerCategory;

  constructor(protected beerCategoryService: BeerCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.beerCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
