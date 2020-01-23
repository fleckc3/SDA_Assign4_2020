package com.example.sdaassign4_2019;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Order class used for setting and getting order details in firebase db
 * @author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1
 */
public class Order {

    public Order() {

    }

    private String bookId;
    private String borrowerId;
    private String dateRequested;
    private String orderDateAndTime;
    private String returnDate;

    public Order(String bookId, String borrowerId, String dateRequested, String orderDateAndTime, String returnDate) {

        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.dateRequested = dateRequested;
        this.orderDateAndTime = orderDateAndTime;
        this. returnDate = returnDate;
    }

    public String getBookId() { return bookId; }

    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getBorrowerId() { return borrowerId; }

    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }

    public String getDateRequested() { return dateRequested; }

    public void setDateRequested(String dateRequested) { this.dateRequested = dateRequested; }

    public String getOrderDateAndTime() { return orderDateAndTime; }

    public void setOrderDateAndTime(String orderDateAndTime) { this.orderDateAndTime = orderDateAndTime; }

    public String getReturnDate() { return returnDate; }

    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

}
