import { Component } from '@angular/core';
import { SUPPLIER } from '@module/admin/admin-page/component/section-four/constans/section-four';

@Component({
  selector: 'app-section-four',
  templateUrl: './section-four.component.html',
  styleUrls: ['./section-four.component.scss']
})
export class SectionFourComponent {
  suplier = SUPPLIER;
}
