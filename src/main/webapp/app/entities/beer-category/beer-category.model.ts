import { IBeer } from 'app/entities/beer/beer.model';

export interface IBeerCategory {
  id?: number;
  categoryName?: string | null;
  beers?: IBeer[] | null;
}

export class BeerCategory implements IBeerCategory {
  constructor(public id?: number, public categoryName?: string | null, public beers?: IBeer[] | null) {}
}

export function getBeerCategoryIdentifier(beerCategory: IBeerCategory): number | undefined {
  return beerCategory.id;
}
