import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeerCategory, getBeerCategoryIdentifier } from '../beer-category.model';

export type EntityResponseType = HttpResponse<IBeerCategory>;
export type EntityArrayResponseType = HttpResponse<IBeerCategory[]>;

@Injectable({ providedIn: 'root' })
export class BeerCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beer-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beerCategory: IBeerCategory): Observable<EntityResponseType> {
    return this.http.post<IBeerCategory>(this.resourceUrl, beerCategory, { observe: 'response' });
  }

  update(beerCategory: IBeerCategory): Observable<EntityResponseType> {
    return this.http.put<IBeerCategory>(`${this.resourceUrl}/${getBeerCategoryIdentifier(beerCategory) as number}`, beerCategory, {
      observe: 'response',
    });
  }

  partialUpdate(beerCategory: IBeerCategory): Observable<EntityResponseType> {
    return this.http.patch<IBeerCategory>(`${this.resourceUrl}/${getBeerCategoryIdentifier(beerCategory) as number}`, beerCategory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBeerCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeerCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBeerCategoryToCollectionIfMissing(
    beerCategoryCollection: IBeerCategory[],
    ...beerCategoriesToCheck: (IBeerCategory | null | undefined)[]
  ): IBeerCategory[] {
    const beerCategories: IBeerCategory[] = beerCategoriesToCheck.filter(isPresent);
    if (beerCategories.length > 0) {
      const beerCategoryCollectionIdentifiers = beerCategoryCollection.map(
        beerCategoryItem => getBeerCategoryIdentifier(beerCategoryItem)!
      );
      const beerCategoriesToAdd = beerCategories.filter(beerCategoryItem => {
        const beerCategoryIdentifier = getBeerCategoryIdentifier(beerCategoryItem);
        if (beerCategoryIdentifier == null || beerCategoryCollectionIdentifiers.includes(beerCategoryIdentifier)) {
          return false;
        }
        beerCategoryCollectionIdentifiers.push(beerCategoryIdentifier);
        return true;
      });
      return [...beerCategoriesToAdd, ...beerCategoryCollection];
    }
    return beerCategoryCollection;
  }
}
