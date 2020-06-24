package com.filmi3k.movies.domain.entities.enums;

public enum BanAccount {
    NO(true),
    YES(false); // That means isEnabled will be "0" or banned

    private final boolean isBanned;

    BanAccount(boolean banned) {
       this.isBanned = banned;
   }

    public boolean toBoolean() {
        return this.isBanned;
    }
}
