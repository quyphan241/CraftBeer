import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManufactor, getManufactorIdentifier } from '../manufactor.model';

export type EntityResponseType = HttpResponse<IManufactor>;
export type EntityArrayResponseType = HttpResponse<IManufactor[]>;

@Injectable({ providedIn: 'root' })
export class ManufactorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manufactors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(manufactor: IManufactor): Observable<EntityResponseType> {
    return this.http.post<IManufactor>(this.resourceUrl, manufactor, { observe: 'response' });
  }

  update(manufactor: IManufactor): Observable<EntityResponseType> {
    return this.http.put<IManufactor>(`${this.resourceUrl}/${getManufactorIdentifier(manufactor) as number}`, manufactor, {
      observe: 'response',
    });
  }

  partialUpdate(manufactor: IManufactor): Observable<EntityResponseType> {
    return this.http.patch<IManufactor>(`${this.resourceUrl}/${getManufactorIdentifier(manufactor) as number}`, manufactor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IManufactor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManufactor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addManufactorToCollectionIfMissing(
    manufactorCollection: IManufactor[],
    ...manufactorsToCheck: (IManufactor | null | undefined)[]
  ): IManufactor[] {
    const manufactors: IManufactor[] = manufactorsToCheck.filter(isPresent);
    if (manufactors.length > 0) {
      const manufactorCollectionIdentifiers = manufactorCollection.map(manufactorItem => getManufactorIdentifier(manufactorItem)!);
      const manufactorsToAdd = manufactors.filter(manufactorItem => {
        const manufactorIdentifier = getManufactorIdentifier(manufactorItem);
        if (manufactorIdentifier == null || manufactorCollectionIdentifiers.includes(manufactorIdentifier)) {
          return false;
        }
        manufactorCollectionIdentifiers.push(manufactorIdentifier);
        return true;
      });
      return [...manufactorsToAdd, ...manufactorCollection];
    }
    return manufactorCollection;
  }
}
