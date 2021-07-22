import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHieu, Hieu } from '../hieu.model';

import { HieuService } from './hieu.service';

describe('Service Tests', () => {
  describe('Hieu Service', () => {
    let service: HieuService;
    let httpMock: HttpTestingController;
    let elemDefault: IHieu;
    let expectedResult: IHieu | IHieu[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(HieuService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a Hieu', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Hieu()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Hieu', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Hieu', () => {
        const patchObject = Object.assign({}, new Hieu());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Hieu', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a Hieu', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addHieuToCollectionIfMissing', () => {
        it('should add a Hieu to an empty array', () => {
          const hieu: IHieu = { id: 123 };
          expectedResult = service.addHieuToCollectionIfMissing([], hieu);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(hieu);
        });

        it('should not add a Hieu to an array that contains it', () => {
          const hieu: IHieu = { id: 123 };
          const hieuCollection: IHieu[] = [
            {
              ...hieu,
            },
            { id: 456 },
          ];
          expectedResult = service.addHieuToCollectionIfMissing(hieuCollection, hieu);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Hieu to an array that doesn't contain it", () => {
          const hieu: IHieu = { id: 123 };
          const hieuCollection: IHieu[] = [{ id: 456 }];
          expectedResult = service.addHieuToCollectionIfMissing(hieuCollection, hieu);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(hieu);
        });

        it('should add only unique Hieu to an array', () => {
          const hieuArray: IHieu[] = [{ id: 123 }, { id: 456 }, { id: 20591 }];
          const hieuCollection: IHieu[] = [{ id: 123 }];
          expectedResult = service.addHieuToCollectionIfMissing(hieuCollection, ...hieuArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const hieu: IHieu = { id: 123 };
          const hieu2: IHieu = { id: 456 };
          expectedResult = service.addHieuToCollectionIfMissing([], hieu, hieu2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(hieu);
          expect(expectedResult).toContain(hieu2);
        });

        it('should accept null and undefined values', () => {
          const hieu: IHieu = { id: 123 };
          expectedResult = service.addHieuToCollectionIfMissing([], null, hieu, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(hieu);
        });

        it('should return initial array if no Hieu is added', () => {
          const hieuCollection: IHieu[] = [{ id: 123 }];
          expectedResult = service.addHieuToCollectionIfMissing(hieuCollection, undefined, null);
          expect(expectedResult).toEqual(hieuCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
