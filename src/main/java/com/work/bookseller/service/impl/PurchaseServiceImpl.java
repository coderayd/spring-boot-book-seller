package com.work.bookseller.service.impl;

import com.work.bookseller.dto.request.PurchaseRequestDto;
import com.work.bookseller.dto.response.BaseResponseDto;
import com.work.bookseller.dto.response.BookResponseDto;
import com.work.bookseller.dto.response.PurchaseResponseDto;
import com.work.bookseller.dto.response.UserResponseDto;
import com.work.bookseller.exception.EntityNotFoundException;
import com.work.bookseller.exception.InsufficientBalanceException;
import com.work.bookseller.model.Book;
import com.work.bookseller.model.Purchase;
import com.work.bookseller.model.User;
import com.work.bookseller.repository.BookRepository;
import com.work.bookseller.repository.PurchaseRepository;
import com.work.bookseller.repository.UserRepository;
import com.work.bookseller.repository.projection.PurchaseInfo;
import com.work.bookseller.service.PurchaseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BaseResponseDto<List<PurchaseInfo>> findPurchasedInfosOfUser(Integer userId) {

        // Oturum açmış kişi olduğundan veritabanında var mı diye bakmaya gerek yok bence
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        List<PurchaseInfo> purchaseInfoList = purchaseRepository.findAllPurchaseofByUser(userId);
        BaseResponseDto<List<PurchaseInfo>> responsePurchaseList = BaseResponseDto.<List<PurchaseInfo>>builder()
                .code(HttpStatus.OK.value())
                .description("Purchase Information Has Been Brought")
                .data(purchaseInfoList)
                .build();

        if(purchaseInfoList.size() == 0){
//            throw new UserPurchaseException("User Has No Purchase Actions");
            responsePurchaseList.setDescription("User Has No Purchase Actions");
        }

        return responsePurchaseList;
    }

    @Override
    public BaseResponseDto<PurchaseResponseDto> savePurchase(PurchaseRequestDto purchaseRequestDto) {

        User user = userRepository.findById(purchaseRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        Book book = bookRepository.findById(purchaseRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book Not Found"));

        if( book.getPrice() > purchaseRequestDto.getPrice() ){
            throw new InsufficientBalanceException("You have insufficient balance, please load balance.");
        }

        Purchase purchase = Purchase.builder()
                .user(user)
                .book(book)
                .price(book.getPrice())
                .build();
        purchase = purchaseRepository.save(purchase);

   //     BookResponseDto bookResponseDto = modelMapper.map(book, BookResponseDto.class);
   //     UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        PurchaseResponseDto purchaseResponseDto = modelMapper.map(purchase, PurchaseResponseDto.class);
    //    purchaseResponseDto.setUser(userResponseDto);
    //    purchaseResponseDto.setBook(bookResponseDto);

        return BaseResponseDto.<PurchaseResponseDto>builder()
                .code(HttpStatus.OK.value())
                .description("Purchase Saved")
                .data(modelMapper.map(purchase, PurchaseResponseDto.class))
                .build();
    }

}
