import { ICategory } from 'app/entities/category/category.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  category?: string | null;
  price?: string;
  quantity?: string | null;
  description?: string | null;
  product_category?: ICategory | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public category?: string | null,
    public price?: string,
    public quantity?: string | null,
    public description?: string | null,
    public product_category?: ICategory | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
