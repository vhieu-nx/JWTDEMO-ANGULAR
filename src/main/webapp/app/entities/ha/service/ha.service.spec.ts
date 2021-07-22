import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHa, Ha } from '../ha.model';

import { HaService } from './ha.service';

describe('Service Tests', () => {
  describe('Ha Service', () => {
    let service: HaService;
    let httpMock: HttpTestingController;
    let elemDefault: IHa;
    let expectedResult: IHa | IHa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(HaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Ha', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Ha()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ha', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Ha', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          new Ha()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ha', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Ha', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addHaToCollectionIfMissing', () => {
        it('should add a Ha to an empty array', () => {
          const ha: IHa = { id: 123 };
          expectedResult = service.addHaToCollectionIfMissing([], ha);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ha);
        });

        it('should not add a Ha to an array that contains it', () => {
          const ha: IHa = { id: 123 };
          const haCollection: IHa[] = [
            {
              ...ha,
            },
            { id: 456 },
          ];
          expectedResult = service.addHaToCollectionIfMissing(haCollection, ha);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ha to an array that doesn't contain it", () => {
          const ha: IHa = { id: 123 };
          const haCollection: IHa[] = [{ id: 456 }];
          expectedResult = service.addHaToCollectionIfMissing(haCollection, ha);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ha);
        });

        it('should add only unique Ha to an array', () => {
          const haArray: IHa[] = [{ id: 123 }, { id: 456 }, { id: 37361 }];
          const haCollection: IHa[] = [{ id: 123 }];
          expectedResult = service.addHaToCollectionIfMissing(haCollection, ...haArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ha: IHa = { id: 123 };
          const ha2: IHa = { id: 456 };
          expectedResult = service.addHaToCollectionIfMissing([], ha, ha2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ha);
          expect(expectedResult).toContain(ha2);
        });

        it('should accept null and undefined values', () => {
          const ha: IHa = { id: 123 };
          expectedResult = service.addHaToCollectionIfMissing([], null, ha, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ha);
        });

        it('should return initial array if no Ha is added', () => {
          const haCollection: IHa[] = [{ id: 123 }];
          expectedResult = service.addHaToCollectionIfMissing(haCollection, undefined, null);
          expect(expectedResult).toEqual(haCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
