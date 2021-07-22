jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HaService } from '../service/ha.service';
import { IHa, Ha } from '../ha.model';

import { HaUpdateComponent } from './ha-update.component';

describe('Component Tests', () => {
  describe('Ha Management Update Component', () => {
    let comp: HaUpdateComponent;
    let fixture: ComponentFixture<HaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let haService: HaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      haService = TestBed.inject(HaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ha: IHa = { id: 456 };

        activatedRoute.data = of({ ha });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ha));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ha>>();
        const ha = { id: 123 };
        jest.spyOn(haService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ha });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ha }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(haService.update).toHaveBeenCalledWith(ha);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ha>>();
        const ha = new Ha();
        jest.spyOn(haService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ha });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ha }));
        saveSubject.complete();

        // THEN
        expect(haService.create).toHaveBeenCalledWith(ha);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ha>>();
        const ha = { id: 123 };
        jest.spyOn(haService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ha });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(haService.update).toHaveBeenCalledWith(ha);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
