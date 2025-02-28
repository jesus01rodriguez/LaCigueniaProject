package LaCiguenia.service.cashclosure.implement;

import LaCiguenia.commons.constans.response.GeneralResponse;
import LaCiguenia.commons.constans.response.cashclosure.ICashClosureResponse;
import LaCiguenia.commons.converter.cashclosure.CashClosureConverter;
import LaCiguenia.commons.domains.dto.cashclosure.CashClosureDTO;
import LaCiguenia.commons.domains.dto.cashclosure.CashClosureInformationDTO;
import LaCiguenia.commons.domains.entity.cashclosure.CashClosureEntity;
import LaCiguenia.commons.domains.entity.invoice.InvoiceEntity;
import LaCiguenia.commons.domains.entity.opening.OpeningEntity;
import LaCiguenia.commons.domains.responseDTO.GenericResponseDTO;
import LaCiguenia.component.cashclosure.implement.CashClosureComponent;
import LaCiguenia.repository.cashclosure.ICashClosureRepository;
import LaCiguenia.repository.invoice.IInvoiceRepository;
import LaCiguenia.repository.opening.IOpeningRepository;
import LaCiguenia.service.cashclosure.ICashClosureService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CashClosureService implements ICashClosureService {

    private final ICashClosureRepository iCashClosureRepository;
    private final IInvoiceRepository iInvoiceRepository;
    private final IOpeningRepository iOpeningRepository;
    private final CashClosureConverter cashClosureConverter;
    private final CashClosureComponent cashClosureComponent;

    public CashClosureService(ICashClosureRepository iCashClosureRepository, CashClosureConverter cashClosureConverter, CashClosureComponent cashClosureComponent, IInvoiceRepository iInvoiceRepository, IOpeningRepository iOpeningRepository) {
        this.iCashClosureRepository = iCashClosureRepository;
        this.cashClosureConverter = cashClosureConverter;
        this.cashClosureComponent = cashClosureComponent;
        this.iInvoiceRepository = iInvoiceRepository;
        this.iOpeningRepository = iOpeningRepository;
    }


    @Override
    public ResponseEntity<GenericResponseDTO> createCashClosure(CashClosureDTO cashClosureDTO) {
        try {
            Optional<CashClosureEntity> cashClosureEntityExist =
                    this.iCashClosureRepository.findById(cashClosureDTO.getCashClosureId());
            if (!cashClosureEntityExist.isPresent()) {
                CashClosureEntity cashClosureEntity =
                        this.cashClosureConverter.convertCashClosureDTOToCashClosureEntity(cashClosureDTO);
                this.iCashClosureRepository.save(cashClosureEntity);
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_SUCCESS)
                        .objectResponse(GeneralResponse.CREATE_SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_FAIL)
                        .objectResponse(ICashClosureResponse.CASH_CLOSURE_SUCCESS)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
            }
        } catch (Exception e) {
            log.error(GeneralResponse.INTERNAL_SERVER_ERROR, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponseDTO.builder()
                            .message(GeneralResponse.INTERNAL_SERVER)
                            .objectResponse(null)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> readCashClosure(Integer cashClosureId) {
        try {
            Optional<CashClosureEntity> cashClosureEntityExist =
                    this.iCashClosureRepository.findById(cashClosureId);
            if (!cashClosureEntityExist.isPresent()) {
                CashClosureDTO cashClosureDTO =
                        this.cashClosureConverter.convertCashClosureEntityToCashClosureDTO(cashClosureEntityExist.get());
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_SUCCESS)
                        .objectResponse(cashClosureDTO)
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_FAIL)
                        .objectResponse(ICashClosureResponse.CASH_CLOSURE_FAIL)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
            }
        } catch (Exception e) {
            log.error(GeneralResponse.INTERNAL_SERVER_ERROR, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponseDTO.builder()
                            .message(GeneralResponse.INTERNAL_SERVER)
                            .objectResponse(null)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> readCashClosures() {
        try {
            List<CashClosureDTO> listCashClosureDTOExist = new ArrayList<>();
            List<CashClosureEntity> listCashClosureEntityExist =
                    this.iCashClosureRepository.findAll();
            if (!listCashClosureEntityExist.isEmpty()) {
                listCashClosureEntityExist.forEach(item ->
                        listCashClosureDTOExist.add(this.cashClosureConverter.convertCashClosureEntityToCashClosureDTO(item)));
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_SUCCESS)
                        .objectResponse(listCashClosureDTOExist)
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_FAIL)
                        .objectResponse(ICashClosureResponse.CASH_CLOSURES_FAIL)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
            }
        } catch (Exception e) {
            log.error(GeneralResponse.INTERNAL_SERVER_ERROR, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponseDTO.builder()
                            .message(GeneralResponse.INTERNAL_SERVER)
                            .objectResponse(null)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> readLastCashClosures() {
        try {
            Optional<CashClosureEntity> listCashClosureEntityExist =
                    this.iCashClosureRepository.lastFindCashClosure();
            if (!listCashClosureEntityExist.isEmpty()) {
                CashClosureDTO cashClosureDTO =
                        this.cashClosureConverter.convertCashClosureEntityToCashClosureDTO(listCashClosureEntityExist.get());
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_SUCCESS)
                        .objectResponse(cashClosureDTO)
                        .objectId(cashClosureDTO.getCashClosureId())
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_FAIL)
                        .objectResponse(ICashClosureResponse.CASH_CLOSURES_FAIL)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
            }
        } catch (Exception e) {
            log.error(GeneralResponse.INTERNAL_SERVER_ERROR, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponseDTO.builder()
                            .message(GeneralResponse.INTERNAL_SERVER)
                            .objectResponse(null)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> informationForCashClosures() {
        /*try {
            Integer lastOpeningId = this.iOpeningRepository.lastOpeningId();
            Optional<OpeningEntity> openingEntityExist = this.iOpeningRepository.findById(lastOpeningId);

            if (openingEntityExist.isPresent()) {
                List<InvoiceEntity> salesForCash = this.iInvoiceRepository.findAllSalesForCash(lastOpeningId);
                List<InvoiceEntity> salesForCredit = this.iInvoiceRepository.findAllSalesForCredit(lastOpeningId);
                List<InvoiceEntity> salesForDebit = this.iInvoiceRepository.findAllSalesForDebit(lastOpeningId);

                Double totalCash = this.cashClosureComponent.totalSalesCashForDay(salesForCash);
                Double totalCredit = this.cashClosureComponent.totalSalesCreditForDay(salesForCredit);
                Double totalDebit = this.cashClosureComponent.totalSalesDebitForDay(salesForDebit);

                OpeningEntity openingEntity = openingEntityExist.get();
                Double totalOpeningBox = openingEntity.getOpeningTotal();
                Double totalCashBox = totalOpeningBox + totalCash;
                Double totalMethodPay = totalCredit + totalCash + totalDebit;
                Double totalCashClosure = totalMethodPay;

                CashClosureInformationDTO cashClosureInformationDTO = new CashClosureInformationDTO();
                cashClosureInformationDTO.setCashClosureInformationStore(openingEntity.getStoreEntity().getStoreName());
                cashClosureInformationDTO.setCashClosureInformationOpeningBox(totalOpeningBox);
                cashClosureInformationDTO.setCashClosureInformationTotalCash(totalCash);
                cashClosureInformationDTO.setCashClosureInformationTotalCredit(totalCredit);
                cashClosureInformationDTO.setCashClosureInformationTotalDebit(totalDebit);
                cashClosureInformationDTO.setCashClosureInformationTotalIva(this.cashClosureComponent.getTotalIva());
                cashClosureInformationDTO.setCashClosureInformationTotalCashBox(totalCashBox);
                cashClosureInformationDTO.setCashClosureInformationTotalSalesMethodPay(totalMethodPay);
                cashClosureInformationDTO.setCashClosureInformationTotalClosure(totalCashClosure);

                return ResponseEntity.ok(GenericResponseDTO.builder()
                        .message(GeneralResponse.OPERATION_SUCCESS)
                        .objectResponse(cashClosureInformationDTO)
                        .statusCode(HttpStatus.OK.value())
                        .build());
            }
            return ResponseEntity.ok(GenericResponseDTO.builder()
                    .message(GeneralResponse.OPERATION_SUCCESS)
                    .objectResponse(null)
                    .statusCode(HttpStatus.OK.value())
                    .build());
        } catch (Exception e) {
            log.error(GeneralResponse.INTERNAL_SERVER_ERROR, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponseDTO.builder()
                            .message(GeneralResponse.INTERNAL_SERVER)
                            .objectResponse(null)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }*/
        return null;
    }
}