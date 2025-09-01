package com.best_store.right_bite.dto.adminPanel;

import lombok.Builder;

@Builder
public record AdminInfoDTO(String adminId, String adminEmail) {
}
