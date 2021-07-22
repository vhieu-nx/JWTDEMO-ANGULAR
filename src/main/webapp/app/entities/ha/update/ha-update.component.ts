import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHa, Ha } from '../ha.model';
import { HaService } from '../service/ha.service';

@Component({
  selector: 'jhi-ha-update',
  templateUrl: './ha-update.component.html',
})
export class HaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected haService: HaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ha }) => {
      this.updateForm(ha);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ha = this.createFromForm();
    if (ha.id !== undefined) {
      this.subscribeToSaveResponse(this.haService.update(ha));
    } else {
      this.subscribeToSaveResponse(this.haService.create(ha));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ha: IHa): void {
    this.editForm.patchValue({
      id: ha.id,
      name: ha.name,
      description: ha.description,
    });
  }

  protected createFromForm(): IHa {
    return {
      ...new Ha(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
