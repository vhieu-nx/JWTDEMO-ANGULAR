import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHa, getHaIdentifier } from '../ha.model';

export type EntityResponseType = HttpResponse<IHa>;
export type EntityArrayResponseType = HttpResponse<IHa[]>;

@Injectable({ providedIn: 'root' })
export class HaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/has');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ha: IHa): Observable<EntityResponseType> {
    return this.http.post<IHa>(this.resourceUrl, ha, { observe: 'response' });
  }

  update(ha: IHa): Observable<EntityResponseType> {
    return this.http.put<IHa>(`${this.resourceUrl}/${getHaIdentifier(ha) as number}`, ha, { observe: 'response' });
  }

  partialUpdate(ha: IHa): Observable<EntityResponseType> {
    return this.http.patch<IHa>(`${this.resourceUrl}/${getHaIdentifier(ha) as number}`, ha, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHaToCollectionIfMissing(haCollection: IHa[], ...hasToCheck: (IHa | null | undefined)[]): IHa[] {
    const has: IHa[] = hasToCheck.filter(isPresent);
    if (has.length > 0) {
      const haCollectionIdentifiers = haCollection.map(haItem => getHaIdentifier(haItem)!);
      const hasToAdd = has.filter(haItem => {
        const haIdentifier = getHaIdentifier(haItem);
        if (haIdentifier == null || haCollectionIdentifiers.includes(haIdentifier)) {
          return false;
        }
        haCollectionIdentifiers.push(haIdentifier);
        return true;
      });
      return [...hasToAdd, ...haCollection];
    }
    return haCollection;
  }
}
