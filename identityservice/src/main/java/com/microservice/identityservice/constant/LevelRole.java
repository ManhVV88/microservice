package com.microservice.identityservice.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum LevelRole {
    ADMIN(9999),
    USER(1);

    int level;

    public boolean hasHigherAuthority(LevelRole otherRole) {
        return this.level < otherRole.level;
    }
}
