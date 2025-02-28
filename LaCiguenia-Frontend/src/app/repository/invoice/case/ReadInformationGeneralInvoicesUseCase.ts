import { InvoiceModel } from "@commons/domains/invoice/InvoiceModel";
import { UseCase } from "@commons/helpers/UserCase";
import { GenericResponse } from "@commons/response/GenericResponse";
import { InvoiceRepository } from "../InvoiceRepository";
import { Observable } from "rxjs";

export class ReadInformationGeneralInvoicesUseCase implements UseCase<InvoiceModel, GenericResponse>{

    constructor(private invoiceRepository: InvoiceRepository){}

    execute() : Observable<GenericResponse>{
        return this.invoiceRepository.readInformationGeneralInvoices();
    }
}