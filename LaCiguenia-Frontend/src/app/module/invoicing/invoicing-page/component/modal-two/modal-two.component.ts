import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DetailModel } from '@commons/domains/detail/DetailModel';
import { InvoiceModel } from '@commons/domains/invoice/InvoiceModel';
import { MethodPaymentModel } from '@commons/domains/payment/MethodPaymentModel';
import { GenericResponse } from '@commons/response/GenericResponse';
import { SYMBOL_PRICE, TITLE, SUBTOTAL, IVA, TOTAL, DISCOUNT, ADD_PAY, CHANGE, FAIL, PAY, METHOD_PAY} 
from '@module/invoicing/invoicing-page/component/modal-two/constans/modal-two';
import { DetailCreateUseCase } from '@repository/detail/case/DetailCreateUseCase';
import { InvoiceCreateUseCase } from '@repository/invoice/case/InvoiceCreateUseCase';
import { ReadMethodsPaymentUseCase } from '@repository/payment/case/ReadMethodsPaymentUseCase';

@Component({
  selector: 'app-modal-two',
  templateUrl: './modal-two.component.html',
  styleUrls: ['./modal-two.component.scss']
})
export class ModalTwoComponent implements OnInit{

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
  textMethodPay = METHOD_PAY;

  detail!: DetailModel;
  methodPaymentForm!: FormGroup;
  changesInvoice!: number;
  changesTotal: number = 0;

  methodPaymentModel: MethodPaymentModel [] = [];

  constructor(private formulary: FormBuilder, private invoiceCreateUseCase: InvoiceCreateUseCase, 
    private detailCreateUseCase: DetailCreateUseCase, private readMethodsPaymentUseCase: ReadMethodsPaymentUseCase){
      this.methodPaymentForm = this.formulary.group({
        selectedPaymentMethod: ['', [Validators.required]]
      });
  }

  ngOnInit(): void {
    this.readMethodsPayment();
  }

  readMethodsPayment(){
    this.readMethodsPaymentUseCase.execute().subscribe(
      (res: GenericResponse) => {
        for(let item of res.objectResponse){
          this.methodPaymentModel.push(item);
        }
      }
    );
  }

  payment!: MethodPaymentModel;

  generalInvoicePay(){

    this.invoiceEnd.paymentMethodEntity = this.methodPaymentForm.controls['selectedPaymentMethod'].value;

    if(this.invoiceEnd.paymentMethodEntity.paymentMethodName == null){
      console.log("Entramos al if")
      this.invoiceEnd.paymentMethodEntity = this.methodPaymentModel[0];
    }

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

  substractChanges(){
    if(this.invoiceEnd.invoiceTotal < this.changesInvoice){
      this.changesTotal = this.changesInvoice - this.invoiceEnd.invoiceTotal;
    }else{
      this.changesTotal = 0;
    }
  }

  modalEventTwo() {
    this.modalActivateTwo.emit(false);
  }

  modalEventThree() {
    this.modalActivateThree.emit(true);
  }
}
