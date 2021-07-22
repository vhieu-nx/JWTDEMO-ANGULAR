import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HaComponent } from './list/ha.component';
import { HaDetailComponent } from './detail/ha-detail.component';
import { HaUpdateComponent } from './update/ha-update.component';
import { HaDeleteDialogComponent } from './delete/ha-delete-dialog.component';
import { HaRoutingModule } from './route/ha-routing.module';

@NgModule({
  imports: [SharedModule, HaRoutingModule],
  declarations: [HaComponent, HaDetailComponent, HaUpdateComponent, HaDeleteDialogComponent],
  entryComponents: [HaDeleteDialogComponent],
})
export class HaModule {}
