import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IManufactor, Manufactor } from '../manufactor.model';
import { ManufactorService } from '../service/manufactor.service';

@Component({
  selector: 'jhi-manufactor-update',
  templateUrl: './manufactor-update.component.html',
})
export class ManufactorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected manufactorService: ManufactorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manufactor }) => {
      this.updateForm(manufactor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manufactor = this.createFromForm();
    if (manufactor.id !== undefined) {
      this.subscribeToSaveResponse(this.manufactorService.update(manufactor));
    } else {
      this.subscribeToSaveResponse(this.manufactorService.create(manufactor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManufactor>>): void {
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

  protected updateForm(manufactor: IManufactor): void {
    this.editForm.patchValue({
      id: manufactor.id,
      name: manufactor.name,
    });
  }

  protected createFromForm(): IManufactor {
    return {
      ...new Manufactor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
