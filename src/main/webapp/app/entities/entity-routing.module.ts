import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'beer',
        data: { pageTitle: 'Beers' },
        loadChildren: () => import('./beer/beer.module').then(m => m.BeerModule),
      },
      {
        path: 'beer-category',
        data: { pageTitle: 'BeerCategories' },
        loadChildren: () => import('./beer-category/beer-category.module').then(m => m.BeerCategoryModule),
      },
      {
        path: 'manufactor',
        data: { pageTitle: 'Manufactors' },
        loadChildren: () => import('./manufactor/manufactor.module').then(m => m.ManufactorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
