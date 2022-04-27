import { IBeer } from 'app/entities/beer/beer.model';

export interface IManufactor {
  id?: number;
  name?: string | null;
  beers?: IBeer[] | null;
}

export class Manufactor implements IManufactor {
  constructor(public id?: number, public name?: string | null, public beers?: IBeer[] | null) {}
}

export function getManufactorIdentifier(manufactor: IManufactor): number | undefined {
  return manufactor.id;
}
