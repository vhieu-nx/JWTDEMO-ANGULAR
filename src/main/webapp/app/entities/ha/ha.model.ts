export interface IHa {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class Ha implements IHa {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getHaIdentifier(ha: IHa): number | undefined {
  return ha.id;
}
