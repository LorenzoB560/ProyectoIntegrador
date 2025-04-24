package org.grupob.empapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Data
public class InformacionExcepcion {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    //private String trace;
    private String message;
    private String path;
}