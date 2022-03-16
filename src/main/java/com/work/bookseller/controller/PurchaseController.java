package com.work.bookseller.controller;

import com.work.bookseller.dto.request.PurchaseRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.PurchaseResponseDto;
import com.work.bookseller.repository.projection.PurchaseInfo;
import com.work.bookseller.security.UserPrincipal;
import com.work.bookseller.service.PurchaseService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    // Oturum açmış olan kullanıcının satın almalarını getirecek
    //@Parameter(hidden = true) -> springdpc-openapi de bu UserPrincipal ın görünmesini engelleyecektir.
    @GetMapping
    public BaseResponseDto<List<PurchaseInfo>> findPurchasedInfosOfUser(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return purchaseService.findPurchasedInfosOfUser(userPrincipal.getId());
    }

    @PostMapping
    public BaseResponseDto<PurchaseResponseDto> savePurchase(
            @RequestBody @Valid PurchaseRequestDto purchaseRequestDto) {
        return purchaseService.savePurchase(purchaseRequestDto);
    }
}
