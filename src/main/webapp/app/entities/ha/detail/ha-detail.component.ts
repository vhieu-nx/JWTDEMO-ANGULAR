import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHa } from '../ha.model';

@Component({
  selector: 'jhi-ha-detail',
  templateUrl: './ha-detail.component.html',
})
export class HaDetailComponent implements OnInit {
  ha: IHa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ha }) => {
      this.ha = ha;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
