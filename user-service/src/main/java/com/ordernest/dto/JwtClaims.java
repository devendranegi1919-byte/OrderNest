package com.ordernest.dto;

import com.ordernest.entity.UserRole;
import java.util.UUID;

public record JwtClaims(UUID userId, String email, UserRole role) {}