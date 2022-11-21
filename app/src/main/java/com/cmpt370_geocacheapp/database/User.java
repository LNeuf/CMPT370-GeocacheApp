package com.cmpt370_geocacheapp.database;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String username;

    public String password;
}
