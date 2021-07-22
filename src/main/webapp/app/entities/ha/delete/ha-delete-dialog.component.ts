import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHa } from '../ha.model';
import { HaService } from '../service/ha.service';

@Component({
  templateUrl: './ha-delete-dialog.component.html',
})
export class HaDeleteDialogComponent {
  ha?: IHa;

  constructor(protected haService: HaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.haService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
