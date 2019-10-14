package com.techprimers.reactive.reactivemongoexample1.model;


import lombok.*;

import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEvent {

    private Employee employee;
    private String typeEvent;
    private String date;




}
