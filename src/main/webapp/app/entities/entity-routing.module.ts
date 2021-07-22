import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'hieu',
        data: { pageTitle: 'projectjhipsterApp.hieu.home.title' },
        loadChildren: () => import('./hieu/hieu.module').then(m => m.HieuModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'projectjhipsterApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'projectjhipsterApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'ha',
        data: { pageTitle: 'projectjhipsterApp.ha.home.title' },
        loadChildren: () => import('./ha/ha.module').then(m => m.HaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
