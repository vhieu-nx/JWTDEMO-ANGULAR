import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHieu } from '../hieu.model';
import { HieuService } from '../service/hieu.service';

@Component({
  templateUrl: './hieu-delete-dialog.component.html',
})
export class HieuDeleteDialogComponent {
  hieu?: IHieu;

  constructor(protected hieuService: HieuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hieuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
