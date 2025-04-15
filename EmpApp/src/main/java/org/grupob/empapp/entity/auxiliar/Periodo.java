package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Periodo {
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    // Constructores, getters y setters


    public boolean coincideCon(Periodo otroPeriodo) {
        if (this.fechaFin == null && otroPeriodo.fechaFin == null) {
            return true;
        }
        if (this.fechaFin == null) {
            return this.fechaInicio.isBefore(otroPeriodo.fechaFin) || this.fechaInicio.isEqual(otroPeriodo.fechaFin);
        }
        if (otroPeriodo.fechaFin == null) {
            return otroPeriodo.fechaInicio.isBefore(this.fechaFin) || otroPeriodo.fechaInicio.isEqual(this.fechaFin);
        }
        return (this.fechaInicio.isBefore(otroPeriodo.fechaFin) || this.fechaInicio.isEqual(otroPeriodo.fechaFin)) &&
                (otroPeriodo.fechaInicio.isBefore(this.fechaFin) || otroPeriodo.fechaInicio.isEqual(this.fechaFin));
    }
}
