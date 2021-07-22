import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HieuComponent } from './list/hieu.component';
import { HieuDetailComponent } from './detail/hieu-detail.component';
import { HieuUpdateComponent } from './update/hieu-update.component';
import { HieuDeleteDialogComponent } from './delete/hieu-delete-dialog.component';
import { HieuRoutingModule } from './route/hieu-routing.module';

@NgModule({
  imports: [SharedModule, HieuRoutingModule],
  declarations: [HieuComponent, HieuDetailComponent, HieuUpdateComponent, HieuDeleteDialogComponent],
  entryComponents: [HieuDeleteDialogComponent],
})
export class HieuModule {}
