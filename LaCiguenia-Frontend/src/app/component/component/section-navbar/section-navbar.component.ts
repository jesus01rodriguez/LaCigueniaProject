import { Component } from '@angular/core';
import { BOXPAY, INVENTORY, PRODUCTS, SALES } from '@component/component/section-navbar/constans/section-navbar';

@Component({
  selector: 'app-section-navbar',
  templateUrl: './section-navbar.component.html',
  styleUrls: ['./section-navbar.component.scss']
})
export class SectionNavbarComponent {
  boxPay = BOXPAY;
  sales = SALES;
  products = PRODUCTS;
  inventory = INVENTORY;

  accessMenu: boolean = false;

  activedMenu(){
    this.accessMenu = !this.accessMenu;
  }
}
