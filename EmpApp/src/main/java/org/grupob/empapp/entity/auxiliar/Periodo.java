package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Embeddable
public class Periodo {
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    protected Periodo(){}

    private Periodo(LocalDate fechaInicio, LocalDate fechaFin){
        this.fechaInicio=fechaInicio;
        this.fechaFin=fechaFin;
    }

    public static Periodo of(LocalDate fechaInicio, LocalDate fechaFin){
        return new Periodo(fechaInicio, fechaFin);
    }

    public static Periodo from(LocalDate fechaInicio){
        return new Periodo(fechaInicio, LocalDate.now());
    }


}
