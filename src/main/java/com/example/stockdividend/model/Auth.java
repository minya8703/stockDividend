package com.example.stockdividend.model;

import lombok.Data;

import java.util.List;

public class Auth {

    @Data
    public static class SignIn{


    }
    @Data
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .roles(this.roles)
                    .build();
        }
    }
}
