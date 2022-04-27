import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IManufactor, Manufactor } from '../manufactor.model';

import { ManufactorService } from './manufactor.service';

describe('Manufactor Service', () => {
  let service: ManufactorService;
  let httpMock: HttpTestingController;
  let elemDefault: IManufactor;
  let expectedResult: IManufactor | IManufactor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManufactorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Manufactor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Manufactor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Manufactor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Manufactor', () => {
      const patchObject = Object.assign({}, new Manufactor());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Manufactor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Manufactor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addManufactorToCollectionIfMissing', () => {
      it('should add a Manufactor to an empty array', () => {
        const manufactor: IManufactor = { id: 123 };
        expectedResult = service.addManufactorToCollectionIfMissing([], manufactor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manufactor);
      });

      it('should not add a Manufactor to an array that contains it', () => {
        const manufactor: IManufactor = { id: 123 };
        const manufactorCollection: IManufactor[] = [
          {
            ...manufactor,
          },
          { id: 456 },
        ];
        expectedResult = service.addManufactorToCollectionIfMissing(manufactorCollection, manufactor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Manufactor to an array that doesn't contain it", () => {
        const manufactor: IManufactor = { id: 123 };
        const manufactorCollection: IManufactor[] = [{ id: 456 }];
        expectedResult = service.addManufactorToCollectionIfMissing(manufactorCollection, manufactor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manufactor);
      });

      it('should add only unique Manufactor to an array', () => {
        const manufactorArray: IManufactor[] = [{ id: 123 }, { id: 456 }, { id: 28816 }];
        const manufactorCollection: IManufactor[] = [{ id: 123 }];
        expectedResult = service.addManufactorToCollectionIfMissing(manufactorCollection, ...manufactorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manufactor: IManufactor = { id: 123 };
        const manufactor2: IManufactor = { id: 456 };
        expectedResult = service.addManufactorToCollectionIfMissing([], manufactor, manufactor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manufactor);
        expect(expectedResult).toContain(manufactor2);
      });

      it('should accept null and undefined values', () => {
        const manufactor: IManufactor = { id: 123 };
        expectedResult = service.addManufactorToCollectionIfMissing([], null, manufactor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manufactor);
      });

      it('should return initial array if no Manufactor is added', () => {
        const manufactorCollection: IManufactor[] = [{ id: 123 }];
        expectedResult = service.addManufactorToCollectionIfMissing(manufactorCollection, undefined, null);
        expect(expectedResult).toEqual(manufactorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
