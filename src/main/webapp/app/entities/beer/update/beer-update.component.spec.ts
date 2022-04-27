import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeerService } from '../service/beer.service';
import { IBeer, Beer } from '../beer.model';
import { IBeerCategory } from 'app/entities/beer-category/beer-category.model';
import { BeerCategoryService } from 'app/entities/beer-category/service/beer-category.service';
import { IManufactor } from 'app/entities/manufactor/manufactor.model';
import { ManufactorService } from 'app/entities/manufactor/service/manufactor.service';

import { BeerUpdateComponent } from './beer-update.component';

describe('Beer Management Update Component', () => {
  let comp: BeerUpdateComponent;
  let fixture: ComponentFixture<BeerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beerService: BeerService;
  let beerCategoryService: BeerCategoryService;
  let manufactorService: ManufactorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeerUpdateComponent],
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
      .overrideTemplate(BeerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beerService = TestBed.inject(BeerService);
    beerCategoryService = TestBed.inject(BeerCategoryService);
    manufactorService = TestBed.inject(ManufactorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BeerCategory query and add missing value', () => {
      const beer: IBeer = { id: 456 };
      const beerCategory: IBeerCategory = { id: 42596 };
      beer.beerCategory = beerCategory;

      const beerCategoryCollection: IBeerCategory[] = [{ id: 86204 }];
      jest.spyOn(beerCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: beerCategoryCollection })));
      const additionalBeerCategories = [beerCategory];
      const expectedCollection: IBeerCategory[] = [...additionalBeerCategories, ...beerCategoryCollection];
      jest.spyOn(beerCategoryService, 'addBeerCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      expect(beerCategoryService.query).toHaveBeenCalled();
      expect(beerCategoryService.addBeerCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        beerCategoryCollection,
        ...additionalBeerCategories
      );
      expect(comp.beerCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Manufactor query and add missing value', () => {
      const beer: IBeer = { id: 456 };
      const manufactor: IManufactor = { id: 78613 };
      beer.manufactor = manufactor;

      const manufactorCollection: IManufactor[] = [{ id: 54903 }];
      jest.spyOn(manufactorService, 'query').mockReturnValue(of(new HttpResponse({ body: manufactorCollection })));
      const additionalManufactors = [manufactor];
      const expectedCollection: IManufactor[] = [...additionalManufactors, ...manufactorCollection];
      jest.spyOn(manufactorService, 'addManufactorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      expect(manufactorService.query).toHaveBeenCalled();
      expect(manufactorService.addManufactorToCollectionIfMissing).toHaveBeenCalledWith(manufactorCollection, ...additionalManufactors);
      expect(comp.manufactorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const beer: IBeer = { id: 456 };
      const beerCategory: IBeerCategory = { id: 79300 };
      beer.beerCategory = beerCategory;
      const manufactor: IManufactor = { id: 7518 };
      beer.manufactor = manufactor;

      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(beer));
      expect(comp.beerCategoriesSharedCollection).toContain(beerCategory);
      expect(comp.manufactorsSharedCollection).toContain(manufactor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Beer>>();
      const beer = { id: 123 };
      jest.spyOn(beerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(beerService.update).toHaveBeenCalledWith(beer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Beer>>();
      const beer = new Beer();
      jest.spyOn(beerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beer }));
      saveSubject.complete();

      // THEN
      expect(beerService.create).toHaveBeenCalledWith(beer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Beer>>();
      const beer = { id: 123 };
      jest.spyOn(beerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beerService.update).toHaveBeenCalledWith(beer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBeerCategoryById', () => {
      it('Should return tracked BeerCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBeerCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackManufactorById', () => {
      it('Should return tracked Manufactor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackManufactorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
