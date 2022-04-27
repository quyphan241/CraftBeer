import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManufactorDetailComponent } from './manufactor-detail.component';

describe('Manufactor Management Detail Component', () => {
  let comp: ManufactorDetailComponent;
  let fixture: ComponentFixture<ManufactorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManufactorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ manufactor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ManufactorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManufactorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load manufactor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.manufactor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
