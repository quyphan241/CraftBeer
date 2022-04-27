import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ManufactorComponent } from './list/manufactor.component';
import { ManufactorDetailComponent } from './detail/manufactor-detail.component';
import { ManufactorUpdateComponent } from './update/manufactor-update.component';
import { ManufactorDeleteDialogComponent } from './delete/manufactor-delete-dialog.component';
import { ManufactorRoutingModule } from './route/manufactor-routing.module';

@NgModule({
  imports: [SharedModule, ManufactorRoutingModule],
  declarations: [ManufactorComponent, ManufactorDetailComponent, ManufactorUpdateComponent, ManufactorDeleteDialogComponent],
  entryComponents: [ManufactorDeleteDialogComponent],
})
export class ManufactorModule {}
