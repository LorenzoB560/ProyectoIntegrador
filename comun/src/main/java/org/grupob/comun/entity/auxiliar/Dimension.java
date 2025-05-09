package org.grupob.comun.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Embeddable
public class Dimension {
    private BigDecimal ancho;
    private BigDecimal profundo;
    private BigDecimal alto;

    protected Dimension(){}

    private Dimension(BigDecimal ancho, BigDecimal profundo, BigDecimal alto) {
        this.ancho = ancho;
        this.profundo = profundo;
        this.alto = alto;
    }

    public static Dimension of(BigDecimal ancho, BigDecimal profundo, BigDecimal alto){
        return new Dimension(ancho, profundo, alto);
    }



}
