import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHieu, Hieu } from '../hieu.model';
import { HieuService } from '../service/hieu.service';

@Injectable({ providedIn: 'root' })
export class HieuRoutingResolveService implements Resolve<IHieu> {
  constructor(protected service: HieuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHieu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hieu: HttpResponse<Hieu>) => {
          if (hieu.body) {
            return of(hieu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Hieu());
  }
}
