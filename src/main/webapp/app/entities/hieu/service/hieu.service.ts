import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHieu, getHieuIdentifier } from '../hieu.model';

export type EntityResponseType = HttpResponse<IHieu>;
export type EntityArrayResponseType = HttpResponse<IHieu[]>;

@Injectable({ providedIn: 'root' })
export class HieuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hieus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hieu: IHieu): Observable<EntityResponseType> {
    return this.http.post<IHieu>(this.resourceUrl, hieu, { observe: 'response' });
  }

  update(hieu: IHieu): Observable<EntityResponseType> {
    return this.http.put<IHieu>(`${this.resourceUrl}/${getHieuIdentifier(hieu) as number}`, hieu, { observe: 'response' });
  }

  partialUpdate(hieu: IHieu): Observable<EntityResponseType> {
    return this.http.patch<IHieu>(`${this.resourceUrl}/${getHieuIdentifier(hieu) as number}`, hieu, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHieu>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHieu[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHieuToCollectionIfMissing(hieuCollection: IHieu[], ...hieusToCheck: (IHieu | null | undefined)[]): IHieu[] {
    const hieus: IHieu[] = hieusToCheck.filter(isPresent);
    if (hieus.length > 0) {
      const hieuCollectionIdentifiers = hieuCollection.map(hieuItem => getHieuIdentifier(hieuItem)!);
      const hieusToAdd = hieus.filter(hieuItem => {
        const hieuIdentifier = getHieuIdentifier(hieuItem);
        if (hieuIdentifier == null || hieuCollectionIdentifiers.includes(hieuIdentifier)) {
          return false;
        }
        hieuCollectionIdentifiers.push(hieuIdentifier);
        return true;
      });
      return [...hieusToAdd, ...hieuCollection];
    }
    return hieuCollection;
  }
}
