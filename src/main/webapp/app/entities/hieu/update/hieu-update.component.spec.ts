jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HieuService } from '../service/hieu.service';
import { IHieu, Hieu } from '../hieu.model';

import { HieuUpdateComponent } from './hieu-update.component';

describe('Component Tests', () => {
  describe('Hieu Management Update Component', () => {
    let comp: HieuUpdateComponent;
    let fixture: ComponentFixture<HieuUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let hieuService: HieuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HieuUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HieuUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HieuUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      hieuService = TestBed.inject(HieuService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const hieu: IHieu = { id: 456 };

        activatedRoute.data = of({ hieu });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(hieu));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Hieu>>();
        const hieu = { id: 123 };
        jest.spyOn(hieuService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ hieu });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: hieu }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(hieuService.update).toHaveBeenCalledWith(hieu);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Hieu>>();
        const hieu = new Hieu();
        jest.spyOn(hieuService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ hieu });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: hieu }));
        saveSubject.complete();

        // THEN
        expect(hieuService.create).toHaveBeenCalledWith(hieu);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Hieu>>();
        const hieu = { id: 123 };
        jest.spyOn(hieuService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ hieu });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(hieuService.update).toHaveBeenCalledWith(hieu);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
