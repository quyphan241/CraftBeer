import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManufactorComponent } from '../list/manufactor.component';
import { ManufactorDetailComponent } from '../detail/manufactor-detail.component';
import { ManufactorUpdateComponent } from '../update/manufactor-update.component';
import { ManufactorRoutingResolveService } from './manufactor-routing-resolve.service';

const manufactorRoute: Routes = [
  {
    path: '',
    component: ManufactorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManufactorDetailComponent,
    resolve: {
      manufactor: ManufactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManufactorUpdateComponent,
    resolve: {
      manufactor: ManufactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManufactorUpdateComponent,
    resolve: {
      manufactor: ManufactorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(manufactorRoute)],
  exports: [RouterModule],
})
export class ManufactorRoutingModule {}
