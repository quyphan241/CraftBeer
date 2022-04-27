import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManufactor, Manufactor } from '../manufactor.model';
import { ManufactorService } from '../service/manufactor.service';

@Injectable({ providedIn: 'root' })
export class ManufactorRoutingResolveService implements Resolve<IManufactor> {
  constructor(protected service: ManufactorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManufactor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manufactor: HttpResponse<Manufactor>) => {
          if (manufactor.body) {
            return of(manufactor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Manufactor());
  }
}
