package org.grupob.adminapp.service;


import java.io.InputStream;

public interface ProductoMasivoService {
     void cargaMasiva(InputStream jsonInput);
     void borradoMasivo(String opcion);
}
