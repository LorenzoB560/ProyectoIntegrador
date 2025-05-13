package org.grupob.empapp.service;

import org.grupob.comun.entity.Departamento;
import org.grupob.comun.entity.EntidadBancaria;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.entity.auxiliar.CuentaBancaria;
import org.grupob.comun.entity.auxiliar.Periodo;
import org.grupob.comun.entity.maestras.*;
import org.grupob.comun.repository.*;
import org.grupob.empapp.converter.CuentaBancariaConverter;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.comun.entity.Empleado;
import org.grupob.comun.repository.maestras.GeneroRepository;
import org.grupob.empapp.dto.CuentaBancariaDTO;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.TarjetaCreditoDTO;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AltaEmpleadoServiceImp implements AltaEmpleadoService {

    private final GeneroRepository generoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PaisRepository paisRepository;
    private final TipoViaRepository tipoViaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final EntidadBancariaRepository entidadBancariaRepository;
    private final TipoTarjetaRepository tipoTarjetaRepository;

    private final EmpleadoConverter empleadoConverter;
    private final CuentaBancariaConverter cuentaBancariaConverter;

    public AltaEmpleadoServiceImp(GeneroRepository generoRepository, EmpleadoRepository empleadoRepository, PaisRepository paisRepository, TipoViaRepository tipoViaRepository, EmpleadoConverter empleadoConverter, DepartamentoRepository departamentoRepository, TipoDocumentoRepository tipoDocumentoRepository, TipoDocumentoRepository tipoDocumentoRepository1, EspecialidadRepository especialidadRepository, EntidadBancariaRepository entidadBancariaRepository, TipoTarjetaRepository tipoTarjetaRepository, CuentaBancariaConverter cuentaBancariaConverter) {
        this.generoRepository = generoRepository;
        this.empleadoRepository = empleadoRepository;
        this.paisRepository = paisRepository;
        this.tipoViaRepository = tipoViaRepository;
        this.empleadoConverter = empleadoConverter;
        this.departamentoRepository = departamentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository1;
        this.especialidadRepository = especialidadRepository;
        this.entidadBancariaRepository = entidadBancariaRepository;
        this.tipoTarjetaRepository = tipoTarjetaRepository;
        this.cuentaBancariaConverter = cuentaBancariaConverter;
    }

    public List<Genero> devolverGeneros() {
        return generoRepository.findAll();
    }
    public List<Pais> devolverPaises(){
        return paisRepository.findAll();
    }
    public List<TipoVia> devolverTipoVias(){
        return tipoViaRepository.findAll();
    }
    public List<TipoDocumento> devolverTipoDocumentos(){
        return tipoDocumentoRepository.findAll();
    }
    public List<Departamento> devolverDepartamentos() {
        return departamentoRepository.findAll();
    }
    public List<Especialidad> devolverEspecialidades() {
        return especialidadRepository.findAll();
    }
    public List<EntidadBancaria> devolverEntidadesBancarias(){
        return entidadBancariaRepository.findAll();
    }
    public List<TipoTarjetaCredito> devolverTipoTarjetasCredito(){
        return tipoTarjetaRepository.findAll();
    }

    public List<String> devolverMeses(){
        return IntStream.rangeClosed(1, 12)
                .mapToObj(m -> String.format("%02d", m))
                .collect(Collectors.toList());
    }
    public List<String> devolverAnios(){
        int anioActual = Year.now().getValue();
       return IntStream.range(anioActual, anioActual + 21)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }

    public void guardarEmpleado(AltaEmpleadoDTO altaEmpleadoDTO, String id){


        Empleado empleado = empleadoConverter.convertirAEntidad(altaEmpleadoDTO);
        empleado.setGenero(generoRepository.findById(altaEmpleadoDTO.getIdGeneroSeleccionado()).orElseThrow());
        for (Especialidad especialidad : altaEmpleadoDTO.getEspecialidades()) {
            if (especialidad.getId() == null) {  // Verificar si la especialidad aún no tiene ID (si es nueva)
                especialidadRepository.save(especialidad);  // Guardar la especialidad
            }
        }
        empleado.setIdTipoTarjeta(tipoTarjetaRepository.findById(altaEmpleadoDTO.getIdTipoTarjeta()).orElseThrow());
        CuentaBancaria cuentaBancaria = CuentaBancaria.of(altaEmpleadoDTO.getCuentaBancaria().getIban());
        empleado.setCuentaCorriente(cuentaBancaria);
        
        // Asignar las especialidades al empleado
        empleado.setEspecialidades(altaEmpleadoDTO.getEspecialidades());
        empleado.setActivo(true);
        empleado.setPeriodo(Periodo.of(LocalDate.now(), null));
        empleado.setId(UUID.fromString(id));
        System.err.println(empleado);
//        empleado.setAceptacionTerminos(altaEmpleadoDTO.getAceptacionTerminos().equals("on"));
        empleadoRepository.save(empleado);
    }
    public boolean usuarioExiste(LoginUsuarioEmpleadoDTO sesion){
        return sesion != null;
    }

    public void actualizarDatos(AltaEmpleadoDTO datosNuevos, AltaEmpleadoDTO datosAnteriores) {
        if (datosNuevos == null) {
            datosNuevos = new AltaEmpleadoDTO(); // Nueva instancia si los nuevos datos son nulos
        }
        if (datosAnteriores == null) {
            return; // No hay nada anterior que recuperar
        }
        // ** DATOS PERSONALES **
        datosNuevos.setNombre(datosNuevos.getNombre() != null ? datosNuevos.getNombre() : datosAnteriores.getNombre());
        datosNuevos.setApellido(datosNuevos.getApellido() != null ? datosNuevos.getApellido() : datosAnteriores.getApellido());
        datosNuevos.setFoto(datosNuevos.getFoto() != null ? datosNuevos.getFoto() : datosAnteriores.getFoto());
        datosNuevos.setFechaNacimiento(datosNuevos.getFechaNacimiento() != null ? datosNuevos.getFechaNacimiento() : datosAnteriores.getFechaNacimiento());
        datosNuevos.setIdGeneroSeleccionado(datosNuevos.getIdGeneroSeleccionado() != null ? datosNuevos.getIdGeneroSeleccionado() : datosAnteriores.getIdGeneroSeleccionado());
        datosNuevos.setEdad(datosNuevos.getEdad() != null ? datosNuevos.getEdad() : datosAnteriores.getEdad());
        datosNuevos.setPaisNacimiento(datosNuevos.getPaisNacimiento() != null ? datosNuevos.getPaisNacimiento() : datosAnteriores.getPaisNacimiento());
        datosNuevos.setComentarios(datosNuevos.getComentarios() != null ? datosNuevos.getComentarios() : datosAnteriores.getComentarios());

        // ** DATOS CONTACTO **

        datosNuevos.setTipoDocumento(datosNuevos.getTipoDocumento() != null ? datosNuevos.getTipoDocumento() : datosAnteriores.getTipoDocumento());
        datosNuevos.setNumDocumento(datosNuevos.getNumDocumento() != null ? datosNuevos.getNumDocumento() : datosAnteriores.getNumDocumento());
        datosNuevos.setPrefijoTelefono(datosNuevos.getPrefijoTelefono() != null ? datosNuevos.getPrefijoTelefono() : datosAnteriores.getPrefijoTelefono());
        datosNuevos.setNumTelefono(datosNuevos.getNumTelefono() != null ? datosNuevos.getNumTelefono() : datosAnteriores.getNumTelefono());

        if (datosNuevos.getDireccion() == null) {
            if (datosAnteriores.getDireccion() != null) {
                datosNuevos.setDireccion(datosAnteriores.getDireccion());
            } else {
                datosNuevos.setDireccion(new DireccionPostalDTO());
            }
        } else if (datosAnteriores.getDireccion() != null) {
            DireccionPostalDTO nuevaDir = datosNuevos.getDireccion();
            DireccionPostalDTO anteriorDir = datosAnteriores.getDireccion();
            nuevaDir.setTipoVia(nuevaDir.getTipoVia() != null ? nuevaDir.getTipoVia() : anteriorDir.getTipoVia());
            nuevaDir.setVia(nuevaDir.getVia() != null ? nuevaDir.getVia() : anteriorDir.getVia());
            nuevaDir.setNumero(nuevaDir.getNumero() != null ? nuevaDir.getNumero() : anteriorDir.getNumero());
            nuevaDir.setPortal(nuevaDir.getPortal() != null ? nuevaDir.getPortal() : anteriorDir.getPortal());
            nuevaDir.setPlanta(nuevaDir.getPlanta() != null ? nuevaDir.getPlanta() : anteriorDir.getPlanta());
            nuevaDir.setPuerta(nuevaDir.getPuerta() != null ? nuevaDir.getPuerta() : anteriorDir.getPuerta());
            nuevaDir.setCodigoPostal(nuevaDir.getCodigoPostal() != null ? nuevaDir.getCodigoPostal() : anteriorDir.getCodigoPostal());
            nuevaDir.setLocalidad(nuevaDir.getLocalidad() != null ? nuevaDir.getLocalidad() : anteriorDir.getLocalidad());
            nuevaDir.setRegion(nuevaDir.getRegion() != null ? nuevaDir.getRegion() : anteriorDir.getRegion());
        }

        // ** DATOS PROFESIONALES **
        datosNuevos.setIdDepartamentoSeleccionado(
                datosNuevos.getIdDepartamentoSeleccionado() != null ?
                        datosNuevos.getIdDepartamentoSeleccionado() :
                        datosAnteriores.getIdDepartamentoSeleccionado());
        datosNuevos.setEspecialidades(datosNuevos.getEspecialidades() != null ? datosNuevos.getEspecialidades() : datosAnteriores.getEspecialidades());

        // ** DATOS ECONÓMICOS ** (Cuenta Bancaria y Tarjeta de Crédito)
        if (datosNuevos.getCuentaBancaria() == null) {
            if (datosAnteriores.getCuentaBancaria() != null) {
                datosNuevos.setCuentaBancaria(datosAnteriores.getCuentaBancaria());
            } else {
                datosNuevos.setCuentaBancaria(new CuentaBancariaDTO());
            }
        } else if (datosAnteriores.getCuentaBancaria() != null) {
            CuentaBancariaDTO nuevaCuenta = datosNuevos.getCuentaBancaria();
            CuentaBancariaDTO anteriorCuenta = datosAnteriores.getCuentaBancaria();
            nuevaCuenta.setIdEntidadBancaria(nuevaCuenta.getIdEntidadBancaria() != null ? nuevaCuenta.getIdEntidadBancaria() : anteriorCuenta.getIdEntidadBancaria());
            nuevaCuenta.setCodigoPais(nuevaCuenta.getCodigoPais() != null ? nuevaCuenta.getCodigoPais() : anteriorCuenta.getCodigoPais());
            nuevaCuenta.setDigitosControl(nuevaCuenta.getDigitosControl() != null ? nuevaCuenta.getDigitosControl() : anteriorCuenta.getDigitosControl());
            nuevaCuenta.setCodigoEntidadBancaria(nuevaCuenta.getCodigoEntidadBancaria() != null ? nuevaCuenta.getCodigoEntidadBancaria() : anteriorCuenta.getCodigoEntidadBancaria());
            nuevaCuenta.setSucursal(nuevaCuenta.getSucursal() != null ? nuevaCuenta.getSucursal() : anteriorCuenta.getSucursal());
            nuevaCuenta.setNumeroCuenta(nuevaCuenta.getNumeroCuenta() != null ? nuevaCuenta.getNumeroCuenta() : anteriorCuenta.getNumeroCuenta());
        }

        datosNuevos.setSalario(datosNuevos.getSalario() != null ? datosNuevos.getSalario() : datosAnteriores.getSalario());
        datosNuevos.setComision(datosNuevos.getComision() != null ? datosNuevos.getComision() : datosAnteriores.getComision());

        if (datosNuevos.getTarjetaCredito() == null) {
            if (datosAnteriores.getTarjetaCredito() != null) {
                datosNuevos.setTarjetaCredito(datosAnteriores.getTarjetaCredito());
            } else {
                datosNuevos.setTarjetaCredito(new TarjetaCreditoDTO());
            }
        } else if (datosAnteriores.getTarjetaCredito() != null) {
            TarjetaCreditoDTO nuevaTarjeta = datosNuevos.getTarjetaCredito();
            TarjetaCreditoDTO anteriorTarjeta = datosAnteriores.getTarjetaCredito();
            nuevaTarjeta.setNumero(nuevaTarjeta.getNumero() != null ? nuevaTarjeta.getNumero() : anteriorTarjeta.getNumero());
            nuevaTarjeta.setMesCaducidad(nuevaTarjeta.getMesCaducidad() != null ? nuevaTarjeta.getMesCaducidad() : anteriorTarjeta.getMesCaducidad());
            nuevaTarjeta.setAnioCaducidad(nuevaTarjeta.getAnioCaducidad() != null ? nuevaTarjeta.getAnioCaducidad() : anteriorTarjeta.getAnioCaducidad());
            nuevaTarjeta.setCvc(nuevaTarjeta.getCvc() != null ? nuevaTarjeta.getCvc() : anteriorTarjeta.getCvc());
        }

        //** RESUMEN
        datosNuevos.setAceptacionTerminos(datosNuevos.getAceptacionTerminos() != null ? datosNuevos.getAceptacionTerminos() : datosAnteriores.getAceptacionTerminos());
    }
}
