import { IBeerCategory } from 'app/entities/beer-category/beer-category.model';
import { IManufactor } from 'app/entities/manufactor/manufactor.model';

export interface IBeer {
  id?: number;
  name?: string | null;
  country?: string | null;
  price?: number | null;
  description?: string | null;
  beerCategory?: IBeerCategory | null;
  manufactor?: IManufactor | null;
}

export class Beer implements IBeer {
  constructor(
    public id?: number,
    public name?: string | null,
    public country?: string | null,
    public price?: number | null,
    public description?: string | null,
    public beerCategory?: IBeerCategory | null,
    public manufactor?: IManufactor | null
  ) {}
}

export function getBeerIdentifier(beer: IBeer): number | undefined {
  return beer.id;
}
