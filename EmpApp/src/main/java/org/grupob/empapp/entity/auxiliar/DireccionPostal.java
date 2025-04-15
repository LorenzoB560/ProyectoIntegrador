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

    @Column(name = "via")
    private String via;

    @Column(name = "numero")
    private String numero;

    @Column(name = "piso")
    private String piso;

    @Column(name = "puerta")
    private String puerta;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "region")
    private String region;

    @Column(name = "pais")
    private String pais;

    // Constructores, getters y setters
}