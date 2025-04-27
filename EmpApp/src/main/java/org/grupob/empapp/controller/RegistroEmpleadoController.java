package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.comun.entity.EntidadBancaria;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.entity.maestras.*;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.CuentaBancariaDTO;
import org.grupob.empapp.dto.TarjetaCreditoDTO;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosContacto;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosEconomicos;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosProfesionales;
import org.grupob.empapp.dto.grupo_validaciones.GrupoDatosPersonales;
import org.grupob.comun.entity.Departamento;
import org.grupob.empapp.service.AltaEmpleadoServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class RegistroEmpleadoController {

    private final AltaEmpleadoServiceImp altaEmpleadoServiceImp;

    public RegistroEmpleadoController(AltaEmpleadoServiceImp altaEmpleadoServiceImp) {
        this.altaEmpleadoServiceImp = altaEmpleadoServiceImp;
    }


    @ModelAttribute //Cargo las colecciones una vez para añadirlas al modelo
    public void adicionColecciones(Model modelo) {
        List<Genero> listaGeneros = altaEmpleadoServiceImp.devolverGeneros();
        List<Pais> listaPaises = altaEmpleadoServiceImp.devolverPaises();
        List<TipoVia> listaTipoVias = altaEmpleadoServiceImp.devolverTipoVias();
        List<TipoDocumento> tipoDocumentos = altaEmpleadoServiceImp.devolverTipoDocumentos();
        List<Departamento> listaDepartamentos = altaEmpleadoServiceImp.devolverDepartamentos();
        List<Especialidad> listaEspecialidades = altaEmpleadoServiceImp.devolverEspecialidades();
        List<EntidadBancaria> listaEntidadesBancarias = altaEmpleadoServiceImp.devolverEntidadesBancarias();
        List<TipoTarjetaCredito> listaTipoTarjetas = altaEmpleadoServiceImp.devolverTipoTarjetasCredito();



        modelo.addAttribute("listaGeneros", listaGeneros);
        modelo.addAttribute("listaPaises", listaPaises);
        modelo.addAttribute("listaTipoVias", listaTipoVias);
        modelo.addAttribute("listaTipoDocumentos", tipoDocumentos);
        modelo.addAttribute("listaDepartamentos", listaDepartamentos);
        modelo.addAttribute("listaEspecialidades", listaEspecialidades);
        modelo.addAttribute("listaEntidadesBancarias", listaEntidadesBancarias);
        modelo.addAttribute("listaTipoTarjetas", listaTipoTarjetas);
        modelo.addAttribute("meses", altaEmpleadoServiceImp.devolverMeses());
        modelo.addAttribute("anios", altaEmpleadoServiceImp.devolverAnios());
    }

    @GetMapping("/datos-personales")
    public String datosPersonales(HttpSession sesion, Model model) {

        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }

        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registro_empleado/datos-personales";
    }
    @PostMapping("/guardar-datos-personales")
    public String guardarDatosPersonales(
            @Validated(GrupoDatosPersonales.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            @RequestParam MultipartFile imagen,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registro_empleado/datos-personales";
        }


        try {
            datosFormulario.setFoto(imagen.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-contacto";
    }
    @GetMapping("/datos-contacto")
    public String datosContacto(HttpSession sesion, Model model) {

        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }

        // Asegurarse de que la dirección esté inicializada
        if(datosFormulario.getDireccion() == null) {
            datosFormulario.setDireccion(new DireccionPostalDTO());
        }

        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registro_empleado/datos-contacto";
    }

    @PostMapping("/guardar-datos-contacto")
    public String guardarDatosContacto(
            @Validated(GrupoDatosContacto.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registro_empleado/datos-contacto";
        }



        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Asegurarse de que la dirección no sea nula en los datos del formulario
        if (datosFormulario.getDireccion() == null) {
            datosFormulario.setDireccion(new DireccionPostalDTO());
        }
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-profesionales";
    }
    @GetMapping("/datos-profesionales")
    public String datosProfesionales(HttpSession sesion, Model model){

        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }

        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registro_empleado/datos-profesionales";
    }
    @PostMapping("/guardar-datos-profesionales")
    public String guardarDatosProfesionales(
            @Validated(GrupoDatosProfesionales.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registro_empleado/datos-profesionales";
        }



        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-economicos";
    }
    @GetMapping("/datos-economicos")
    public String datosEconomicos(HttpSession sesion, Model model) {
        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }
        // Asegurarse de que la cuenta bancaria esté inicializada
        if(datosFormulario.getCuentaBancaria() == null) {
            datosFormulario.setCuentaBancaria(new CuentaBancariaDTO());
        }
        // Asegurarse de que la tarjeta de credito esté inicializada
        if(datosFormulario.getTarjetaCredito() == null) {
            datosFormulario.setTarjetaCredito(new TarjetaCreditoDTO());
        }
        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registro_empleado/datos-economicos";
    }
    @PostMapping("/guardar-datos-economicos")
    public String guardarDatosEconomicos(
            @Validated(GrupoDatosEconomicos.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        System.err.println(datosFormulario);
        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registro_empleado/datos-economicos";
        }



        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Asegurarse de que la cuenta bancaria esté inicializada
        if(datosFormulario.getCuentaBancaria() == null) {
            datosFormulario.setCuentaBancaria(new CuentaBancariaDTO());
        }

        // Asegurarse de que la tarjeta de credito esté inicializada
        if(datosFormulario.getTarjetaCredito() == null) {
            datosFormulario.setTarjetaCredito(new TarjetaCreditoDTO());
        }
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }


        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-profesionales";
    }
    @GetMapping("/resumen")
    public String resumen(HttpSession sesion, Model model) {
        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }
        if(datosFormulario.getDireccion() == null) {
            datosFormulario.setDireccion(new DireccionPostalDTO());
        }
        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registro_empleado/resumen";
    }
    @PostMapping("/guardar-empleado")
    public String guardarEmpleado(HttpSession sesion) {

        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        System.err.println(datosFormulario);
        altaEmpleadoServiceImp.guardarEmpleado(datosFormulario);
        return "redirect:/usuario-insertado";
    }
    @GetMapping("/usuario-insertado")
    public String usuarioInsertado(){
        return "registro_empleado/usuario-insertado";
    }

    @GetMapping("/volver-principio")
    public String volverPrincipio(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/datos-personales";
    }


    private void actualizarDatos(AltaEmpleadoDTO datosNuevos, AltaEmpleadoDTO datosAnteriores) {
        if (datosNuevos == null) {
            datosNuevos = new AltaEmpleadoDTO(); // Nueva instancia si los nuevos datos son nulos
        }

        if (datosAnteriores == null) {
            return; // No hay nada anterior que recuperar
        }

        // ** DATOS PERSONALES **
        datosNuevos.setNombre(datosNuevos.getNombre() != null ? datosNuevos.getNombre() : datosAnteriores.getNombre());
        datosNuevos.setApellido(datosNuevos.getApellido() != null ? datosNuevos.getApellido() : datosAnteriores.getApellido());
        datosNuevos.setFechaNacimiento(datosNuevos.getFechaNacimiento() != null ? datosNuevos.getFechaNacimiento() : datosAnteriores.getFechaNacimiento());
        datosNuevos.setIdGeneroSeleccionado(datosNuevos.getIdGeneroSeleccionado() != null ? datosNuevos.getIdGeneroSeleccionado() : datosAnteriores.getIdGeneroSeleccionado());

        // ** DATOS DIRECCIÓN **
        // Asegurar que el objeto dirección no sea nulo
        if (datosNuevos.getDireccion() == null) {
            // Si el anterior tiene dirección, lo copiamos completamente
            if (datosAnteriores.getDireccion() != null) {
                datosNuevos.setDireccion(datosAnteriores.getDireccion());
            } else {
                // Si ninguno tiene dirección, creamos uno nuevo
                datosNuevos.setDireccion(new DireccionPostalDTO());
            }
        } else if (datosAnteriores.getDireccion() != null) {
            // Si ambos tienen dirección, completamos campos nulos con datos anteriores
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

        // ** FOTO DE PERFIL **
//        datosNuevos.setFoto(datosNuevos.getFoto() != null ? datosNuevos.getFoto() : datosAnteriores.getFoto());
    }


}
