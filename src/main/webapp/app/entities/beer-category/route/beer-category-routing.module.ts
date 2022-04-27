import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BeerCategoryComponent } from '../list/beer-category.component';
import { BeerCategoryDetailComponent } from '../detail/beer-category-detail.component';
import { BeerCategoryUpdateComponent } from '../update/beer-category-update.component';
import { BeerCategoryRoutingResolveService } from './beer-category-routing-resolve.service';

const beerCategoryRoute: Routes = [
  {
    path: '',
    component: BeerCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BeerCategoryDetailComponent,
    resolve: {
      beerCategory: BeerCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BeerCategoryUpdateComponent,
    resolve: {
      beerCategory: BeerCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BeerCategoryUpdateComponent,
    resolve: {
      beerCategory: BeerCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(beerCategoryRoute)],
  exports: [RouterModule],
})
export class BeerCategoryRoutingModule {}
