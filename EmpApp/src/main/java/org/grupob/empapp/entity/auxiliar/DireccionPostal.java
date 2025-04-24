package org.grupob.empapp.entity.auxiliar;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class DireccionPostal {
    @Column(name = "tipo_via")
    private String tipoVia;

    private String via;


    private String numero;


    private String portal;


    private String planta;


    private String puerta;


    private String localidad;

    @Column(name = "codigo_postal")
    private String codigoPostal;


    private String region;


    // Constructores, getters y setters
}