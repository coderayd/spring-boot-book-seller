package com.work.bookseller.repository;

import com.work.bookseller.model.Purchase;
import com.work.bookseller.repository.projection.PurchaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query("select b.title as title, p.price as price, p.createdDate as createdDate " +
            "from Purchase p left join Book b on p.book.id=b.id where p.user.id = :userId")
    List<PurchaseInfo> findAllPurchaseofByUser(@Param("userId") Integer userId);
}
