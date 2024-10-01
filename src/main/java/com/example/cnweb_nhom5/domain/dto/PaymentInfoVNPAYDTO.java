package com.example.cnweb_nhom5.domain.dto;

public class PaymentInfoVNPAYDTO {
    Long paymentId;
    String receivedName;
    String receiverAddress;
    String receivedPhone;

    public PaymentInfoVNPAYDTO() {
    }

    public PaymentInfoVNPAYDTO(Long paymentId, String receivedName, String receiverAddress, String receivedPhone) {
        this.paymentId = paymentId;
        this.receivedName = receivedName;
        this.receiverAddress = receiverAddress;
        this.receivedPhone = receivedPhone;
    }
}
