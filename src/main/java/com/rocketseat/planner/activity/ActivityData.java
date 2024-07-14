package com.rocketseat.planner.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityData(String title, LocalDateTime occurs_at, UUID id) {
}
