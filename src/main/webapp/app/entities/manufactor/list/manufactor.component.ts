import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IManufactor } from '../manufactor.model';
import { ManufactorService } from '../service/manufactor.service';
import { ManufactorDeleteDialogComponent } from '../delete/manufactor-delete-dialog.component';

@Component({
  selector: 'jhi-manufactor',
  templateUrl: './manufactor.component.html',
})
export class ManufactorComponent implements OnInit {
  manufactors?: IManufactor[];
  isLoading = false;

  constructor(protected manufactorService: ManufactorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.manufactorService.query().subscribe({
      next: (res: HttpResponse<IManufactor[]>) => {
        this.isLoading = false;
        this.manufactors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IManufactor): number {
    return item.id!;
  }

  delete(manufactor: IManufactor): void {
    const modalRef = this.modalService.open(ManufactorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.manufactor = manufactor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
