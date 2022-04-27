import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BeerCategoryComponent } from './list/beer-category.component';
import { BeerCategoryDetailComponent } from './detail/beer-category-detail.component';
import { BeerCategoryUpdateComponent } from './update/beer-category-update.component';
import { BeerCategoryDeleteDialogComponent } from './delete/beer-category-delete-dialog.component';
import { BeerCategoryRoutingModule } from './route/beer-category-routing.module';

@NgModule({
  imports: [SharedModule, BeerCategoryRoutingModule],
  declarations: [BeerCategoryComponent, BeerCategoryDetailComponent, BeerCategoryUpdateComponent, BeerCategoryDeleteDialogComponent],
  entryComponents: [BeerCategoryDeleteDialogComponent],
})
export class BeerCategoryModule {}
