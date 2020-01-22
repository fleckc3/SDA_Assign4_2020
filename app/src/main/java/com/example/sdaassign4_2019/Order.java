package com.example.sdaassign4_2019;

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
