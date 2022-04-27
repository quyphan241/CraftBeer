import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManufactor } from '../manufactor.model';
import { ManufactorService } from '../service/manufactor.service';

@Component({
  templateUrl: './manufactor-delete-dialog.component.html',
})
export class ManufactorDeleteDialogComponent {
  manufactor?: IManufactor;

  constructor(protected manufactorService: ManufactorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manufactorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
