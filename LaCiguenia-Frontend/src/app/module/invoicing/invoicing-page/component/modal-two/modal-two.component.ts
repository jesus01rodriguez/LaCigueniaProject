import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DetailModel } from '@commons/domains/detail/DetailModel';
import { InvoiceModel } from '@commons/domains/invoice/InvoiceModel';
import { GenericResponse } from '@commons/response/GenericResponse';
import { SYMBOL_PRICE, TITLE, SUBTOTAL, IVA, TOTAL, DISCOUNT, ADD_PAY, CHANGE, FAIL, PAY} 
from '@module/invoicing/invoicing-page/component/modal-two/constans/modal-two';
import { DetailCreateUseCase } from '@repository/detail/case/DetailCreateUseCase';
import { InvoiceCreateUseCase } from '@repository/invoice/case/InvoiceCreateUseCase';

@Component({
  selector: 'app-modal-two',
  templateUrl: './modal-two.component.html',
  styleUrls: ['./modal-two.component.scss']
})
export class ModalTwoComponent{

  @Input() invoiceEnd!: InvoiceModel;
  @Input() detailInvoice!: DetailModel [];
  @Output() modalActivateTwo = new EventEmitter<boolean>();
  @Output() modalActivateThree = new EventEmitter<boolean>();

  textTitle = TITLE;
  textSubtotal = SUBTOTAL;
  textSymbolPrice =SYMBOL_PRICE;
  textIva = IVA;
  textTotal = TOTAL;
  textDiscount = DISCOUNT;
  textAddPay = ADD_PAY;
  textChange = CHANGE;
  textFail = FAIL;
  textPay = PAY;

  detail!: DetailModel;
  selectedPaymentMethod!:string;

  constructor(private invoiceCreateUseCase: InvoiceCreateUseCase, private detailCreateUseCase: DetailCreateUseCase){
  }

  generalInvoicePay(){

    this.invoiceEnd.invoicePay = this.selectedPaymentMethod;

    this.invoiceCreateUseCase.execute(this.invoiceEnd).subscribe(
      (res: GenericResponse) => {

        this.invoiceEnd.invoiceId = res.objectId;

        for(let detail of this.detailInvoice){
          this.detail = {
            detailId: 0,
            detailAmount: detail.detailAmount,
            detailSubTotal: (detail.detailAmount * detail.detailSubTotal),
            productEntity: detail.productEntity,
            invoiceEntity: this.invoiceEnd
          }
          this.detailCreateUseCase.execute(this.detail).subscribe(
            (res: GenericResponse) => {
              console.log("Respuesta del Detalle: " + res.message);
              if(res.statusCode == 200){
                this.modalEventTwo();
                this.modalEventThree();
              }
            }
          )
        }
      }
    )
  }

  modalEventTwo() {
    this.modalActivateTwo.emit(false);
  }

  modalEventThree() {
    this.modalActivateThree.emit(true);
  }
}
