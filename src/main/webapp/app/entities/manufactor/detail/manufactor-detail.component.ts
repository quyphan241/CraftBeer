import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManufactor } from '../manufactor.model';

@Component({
  selector: 'jhi-manufactor-detail',
  templateUrl: './manufactor-detail.component.html',
})
export class ManufactorDetailComponent implements OnInit {
  manufactor: IManufactor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manufactor }) => {
      this.manufactor = manufactor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
