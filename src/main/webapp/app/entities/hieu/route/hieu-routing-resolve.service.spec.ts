jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHieu, Hieu } from '../hieu.model';
import { HieuService } from '../service/hieu.service';

import { HieuRoutingResolveService } from './hieu-routing-resolve.service';

describe('Service Tests', () => {
  describe('Hieu routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: HieuRoutingResolveService;
    let service: HieuService;
    let resultHieu: IHieu | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(HieuRoutingResolveService);
      service = TestBed.inject(HieuService);
      resultHieu = undefined;
    });

    describe('resolve', () => {
      it('should return IHieu returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHieu = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultHieu).toEqual({ id: 123 });
      });

      it('should return new IHieu if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHieu = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultHieu).toEqual(new Hieu());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Hieu })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHieu = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultHieu).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
