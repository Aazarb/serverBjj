package com.example.backend.dto;
import com.example.backend.enums.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggedStatusDto {

    private boolean authenticated;
    private RoleEnum role;

    public LoggedStatusDto(boolean authenticated, RoleEnum role) {
        this.authenticated = authenticated;
        this.role = role;
    }
}

