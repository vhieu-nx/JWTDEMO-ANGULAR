import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHieu, Hieu } from '../hieu.model';
import { HieuService } from '../service/hieu.service';

@Component({
  selector: 'jhi-hieu-update',
  templateUrl: './hieu-update.component.html',
})
export class HieuUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected hieuService: HieuService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hieu }) => {
      this.updateForm(hieu);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hieu = this.createFromForm();
    if (hieu.id !== undefined) {
      this.subscribeToSaveResponse(this.hieuService.update(hieu));
    } else {
      this.subscribeToSaveResponse(this.hieuService.create(hieu));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHieu>>): void {
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

  protected updateForm(hieu: IHieu): void {
    this.editForm.patchValue({
      id: hieu.id,
    });
  }

  protected createFromForm(): IHieu {
    return {
      ...new Hieu(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
