package com.work.bookseller.repository.projection;

import java.util.Date;

public interface PurchaseInfo {

    String getTitle();
    Double getPrice();
    Date getCreatedDate();
}
