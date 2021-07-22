import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HaDetailComponent } from './ha-detail.component';

describe('Component Tests', () => {
  describe('Ha Management Detail Component', () => {
    let comp: HaDetailComponent;
    let fixture: ComponentFixture<HaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [HaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ha: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(HaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ha on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ha).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
