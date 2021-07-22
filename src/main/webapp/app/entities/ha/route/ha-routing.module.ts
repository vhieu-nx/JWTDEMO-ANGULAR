import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HaComponent } from '../list/ha.component';
import { HaDetailComponent } from '../detail/ha-detail.component';
import { HaUpdateComponent } from '../update/ha-update.component';
import { HaRoutingResolveService } from './ha-routing-resolve.service';

const haRoute: Routes = [
  {
    path: '',
    component: HaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HaDetailComponent,
    resolve: {
      ha: HaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HaUpdateComponent,
    resolve: {
      ha: HaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HaUpdateComponent,
    resolve: {
      ha: HaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(haRoute)],
  exports: [RouterModule],
})
export class HaRoutingModule {}
