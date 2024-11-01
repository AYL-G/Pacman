package com.example.demo_2;

import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    private Integer userID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String createdDate;
}
