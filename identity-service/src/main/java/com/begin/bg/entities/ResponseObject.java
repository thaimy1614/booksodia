/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.begin.bg.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Duong Quoc Thai CE171563
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private String status;
    private String message;
    private Object data;

}
