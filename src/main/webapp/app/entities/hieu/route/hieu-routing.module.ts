import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HieuComponent } from '../list/hieu.component';
import { HieuDetailComponent } from '../detail/hieu-detail.component';
import { HieuUpdateComponent } from '../update/hieu-update.component';
import { HieuRoutingResolveService } from './hieu-routing-resolve.service';

const hieuRoute: Routes = [
  {
    path: '',
    component: HieuComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HieuDetailComponent,
    resolve: {
      hieu: HieuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HieuUpdateComponent,
    resolve: {
      hieu: HieuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HieuUpdateComponent,
    resolve: {
      hieu: HieuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hieuRoute)],
  exports: [RouterModule],
})
export class HieuRoutingModule {}
