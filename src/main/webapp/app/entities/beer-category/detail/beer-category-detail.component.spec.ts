import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BeerCategoryDetailComponent } from './beer-category-detail.component';

describe('BeerCategory Management Detail Component', () => {
  let comp: BeerCategoryDetailComponent;
  let fixture: ComponentFixture<BeerCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BeerCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ beerCategory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BeerCategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BeerCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load beerCategory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.beerCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
