import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBeerCategory, BeerCategory } from '../beer-category.model';

import { BeerCategoryService } from './beer-category.service';

describe('BeerCategory Service', () => {
  let service: BeerCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IBeerCategory;
  let expectedResult: IBeerCategory | IBeerCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BeerCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      categoryName: 'AAAAAAA',
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

    it('should create a BeerCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BeerCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BeerCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          categoryName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BeerCategory', () => {
      const patchObject = Object.assign({}, new BeerCategory());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BeerCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          categoryName: 'BBBBBB',
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

    it('should delete a BeerCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBeerCategoryToCollectionIfMissing', () => {
      it('should add a BeerCategory to an empty array', () => {
        const beerCategory: IBeerCategory = { id: 123 };
        expectedResult = service.addBeerCategoryToCollectionIfMissing([], beerCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beerCategory);
      });

      it('should not add a BeerCategory to an array that contains it', () => {
        const beerCategory: IBeerCategory = { id: 123 };
        const beerCategoryCollection: IBeerCategory[] = [
          {
            ...beerCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addBeerCategoryToCollectionIfMissing(beerCategoryCollection, beerCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BeerCategory to an array that doesn't contain it", () => {
        const beerCategory: IBeerCategory = { id: 123 };
        const beerCategoryCollection: IBeerCategory[] = [{ id: 456 }];
        expectedResult = service.addBeerCategoryToCollectionIfMissing(beerCategoryCollection, beerCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beerCategory);
      });

      it('should add only unique BeerCategory to an array', () => {
        const beerCategoryArray: IBeerCategory[] = [{ id: 123 }, { id: 456 }, { id: 32286 }];
        const beerCategoryCollection: IBeerCategory[] = [{ id: 123 }];
        expectedResult = service.addBeerCategoryToCollectionIfMissing(beerCategoryCollection, ...beerCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const beerCategory: IBeerCategory = { id: 123 };
        const beerCategory2: IBeerCategory = { id: 456 };
        expectedResult = service.addBeerCategoryToCollectionIfMissing([], beerCategory, beerCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beerCategory);
        expect(expectedResult).toContain(beerCategory2);
      });

      it('should accept null and undefined values', () => {
        const beerCategory: IBeerCategory = { id: 123 };
        expectedResult = service.addBeerCategoryToCollectionIfMissing([], null, beerCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beerCategory);
      });

      it('should return initial array if no BeerCategory is added', () => {
        const beerCategoryCollection: IBeerCategory[] = [{ id: 123 }];
        expectedResult = service.addBeerCategoryToCollectionIfMissing(beerCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(beerCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
