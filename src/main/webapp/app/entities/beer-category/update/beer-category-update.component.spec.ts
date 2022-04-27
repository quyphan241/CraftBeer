import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeerCategoryService } from '../service/beer-category.service';
import { IBeerCategory, BeerCategory } from '../beer-category.model';

import { BeerCategoryUpdateComponent } from './beer-category-update.component';

describe('BeerCategory Management Update Component', () => {
  let comp: BeerCategoryUpdateComponent;
  let fixture: ComponentFixture<BeerCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beerCategoryService: BeerCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeerCategoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BeerCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeerCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beerCategoryService = TestBed.inject(BeerCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const beerCategory: IBeerCategory = { id: 456 };

      activatedRoute.data = of({ beerCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(beerCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BeerCategory>>();
      const beerCategory = { id: 123 };
      jest.spyOn(beerCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beerCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(beerCategoryService.update).toHaveBeenCalledWith(beerCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BeerCategory>>();
      const beerCategory = new BeerCategory();
      jest.spyOn(beerCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beerCategory }));
      saveSubject.complete();

      // THEN
      expect(beerCategoryService.create).toHaveBeenCalledWith(beerCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BeerCategory>>();
      const beerCategory = { id: 123 };
      jest.spyOn(beerCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beerCategoryService.update).toHaveBeenCalledWith(beerCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
