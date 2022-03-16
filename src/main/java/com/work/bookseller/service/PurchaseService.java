package com.work.bookseller.service;

import com.work.bookseller.dto.request.PurchaseRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.PurchaseResponseDto;
import com.work.bookseller.repository.projection.PurchaseInfo;

import java.util.List;

public interface PurchaseService {

    BaseResponseDto<PurchaseResponseDto> savePurchase(PurchaseRequestDto purchaseRequestDto);

    BaseResponseDto<List<PurchaseInfo>>  findPurchasedInfosOfUser(Integer userId);
}
