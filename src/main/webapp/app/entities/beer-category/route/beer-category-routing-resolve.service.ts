import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBeerCategory, BeerCategory } from '../beer-category.model';
import { BeerCategoryService } from '../service/beer-category.service';

@Injectable({ providedIn: 'root' })
export class BeerCategoryRoutingResolveService implements Resolve<IBeerCategory> {
  constructor(protected service: BeerCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBeerCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((beerCategory: HttpResponse<BeerCategory>) => {
          if (beerCategory.body) {
            return of(beerCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BeerCategory());
  }
}
