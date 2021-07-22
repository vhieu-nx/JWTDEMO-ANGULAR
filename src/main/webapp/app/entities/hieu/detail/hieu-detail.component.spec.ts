import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HieuDetailComponent } from './hieu-detail.component';

describe('Component Tests', () => {
  describe('Hieu Management Detail Component', () => {
    let comp: HieuDetailComponent;
    let fixture: ComponentFixture<HieuDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [HieuDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ hieu: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(HieuDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HieuDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load hieu on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.hieu).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
