import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHa, Ha } from '../ha.model';
import { HaService } from '../service/ha.service';

@Injectable({ providedIn: 'root' })
export class HaRoutingResolveService implements Resolve<IHa> {
  constructor(protected service: HaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ha: HttpResponse<Ha>) => {
          if (ha.body) {
            return of(ha.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ha());
  }
}
