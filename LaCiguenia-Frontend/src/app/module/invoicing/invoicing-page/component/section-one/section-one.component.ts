import { Component, ElementRef, EventEmitter, OnChanges, OnInit, Output, ViewChild } from '@angular/core';
import { CategoryModel } from '@commons/domains/category/CategoryModel';
import { ProductModel } from '@commons/domains/product/ProductModel';
import { GenericResponse } from '@commons/response/GenericResponse';
import { ReadCategoriesUseCase } from '@repository/category/case/ReadCategoriesUseCase';
import { ReadProductForCategoryUseCase } from '@repository/product/case/ReadProductForCategoryUseCase';
import { ALL_PRODUCTS } from '@module/invoicing/invoicing-page/component/section-one/constants/section-one';

@Component({
  selector: 'app-section-one',
  templateUrl: './section-one.component.html',
  styleUrls: ['./section-one.component.scss']
})

export class SectionOneComponent implements OnInit{

  @Output() sendProductSelector = new EventEmitter<string>();
  @Output() sendProduct = new EventEmitter<ProductModel[]>();
  @Output() activeMessageCategory = new EventEmitter<boolean>();
  @ViewChild('productInput') productInput!: ElementRef;

  textAllProduct = ALL_PRODUCTS;

  product!: ProductModel [];
  productSelection!: string;
  category!: CategoryModel [];
  productGroupsSelector: ProductModel[][] = [];

  constructor(private readCategoriesUseCase: ReadCategoriesUseCase, private readProductForCategoryUseCase: ReadProductForCategoryUseCase){}


  ngOnInit(): void {
    this.getCategory();
  }

  readProductForName() {
    this.sendProductSelector.emit(this.productSelection);
  }
  
  ngAfterViewInit() {
    this.productInput.nativeElement.addEventListener('keydown', () => {
      this.readProductForName();
    });
  }
  
  getCategory(){
    this.readCategoriesUseCase.execute().subscribe(
      (res: GenericResponse) => {
        this.category = res.objectResponse;
      },
      error => {
        console.error("Error en la solicitud: " + error);
      });
  }

  readProductForCategory(index: number){
    if(index !== 0){
      this.readProductForCategoryUseCase.execute(index).subscribe(
        (res: GenericResponse) => {
          if(res.message != "Operación fallida"){
            this.activeMessageCategory.emit(false);
            this.product = res.objectResponse;
            this.sendProduct.emit(this.product);
          }else{
            this.activeMessageCategory.emit(true);
          }
        }
      )
    }else{
      this.activeMessageCategory.emit(false);
      this.product = [];
      this.sendProduct.emit(this.product);
    }
  }
  
  readProductAllProduct(){
    this.activeMessageCategory.emit(false);
    this.product = [];
    this.sendProduct.emit(this.product);
  }
}