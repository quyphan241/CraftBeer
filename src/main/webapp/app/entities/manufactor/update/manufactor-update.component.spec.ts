import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ManufactorService } from '../service/manufactor.service';
import { IManufactor, Manufactor } from '../manufactor.model';

import { ManufactorUpdateComponent } from './manufactor-update.component';

describe('Manufactor Management Update Component', () => {
  let comp: ManufactorUpdateComponent;
  let fixture: ComponentFixture<ManufactorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manufactorService: ManufactorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ManufactorUpdateComponent],
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
      .overrideTemplate(ManufactorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManufactorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manufactorService = TestBed.inject(ManufactorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const manufactor: IManufactor = { id: 456 };

      activatedRoute.data = of({ manufactor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(manufactor));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Manufactor>>();
      const manufactor = { id: 123 };
      jest.spyOn(manufactorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manufactor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manufactor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(manufactorService.update).toHaveBeenCalledWith(manufactor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Manufactor>>();
      const manufactor = new Manufactor();
      jest.spyOn(manufactorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manufactor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manufactor }));
      saveSubject.complete();

      // THEN
      expect(manufactorService.create).toHaveBeenCalledWith(manufactor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Manufactor>>();
      const manufactor = { id: 123 };
      jest.spyOn(manufactorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manufactor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manufactorService.update).toHaveBeenCalledWith(manufactor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
