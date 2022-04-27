import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ManufactorService } from '../service/manufactor.service';

import { ManufactorComponent } from './manufactor.component';

describe('Manufactor Management Component', () => {
  let comp: ManufactorComponent;
  let fixture: ComponentFixture<ManufactorComponent>;
  let service: ManufactorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ManufactorComponent],
    })
      .overrideTemplate(ManufactorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManufactorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ManufactorService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.manufactors?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
