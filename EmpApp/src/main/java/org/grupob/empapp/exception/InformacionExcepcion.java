package org.grupob.empapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionExcepcion {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private List<String> listaErrores;
    private String message;
    private String path;
}
