export interface IHieu {
  id?: number;
}

export class Hieu implements IHieu {
  constructor(public id?: number) {}
}

export function getHieuIdentifier(hieu: IHieu): number | undefined {
  return hieu.id;
}
