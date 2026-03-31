package com.domain.message_service.app.message.service;

import com.domain.message_service.app.message.dto.AcknowledgementDto;

public interface MessageReceiptService {
    void acknowledge(AcknowledgementDto dto);
}
