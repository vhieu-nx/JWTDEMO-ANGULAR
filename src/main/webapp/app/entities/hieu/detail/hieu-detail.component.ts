import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHieu } from '../hieu.model';

@Component({
  selector: 'jhi-hieu-detail',
  templateUrl: './hieu-detail.component.html',
})
export class HieuDetailComponent implements OnInit {
  hieu: IHieu | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hieu }) => {
      this.hieu = hieu;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
