package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.entity.EntidadBancaria;
import org.grupob.comun.entity.Especialidad;
import org.grupob.comun.entity.maestras.*;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.CuentaBancariaDTO;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.TarjetaCreditoDTO;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.empapp.dto.grupo_validaciones.*;
import org.grupob.comun.entity.Departamento;
import org.grupob.empapp.service.AltaEmpleadoServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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


        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (!altaEmpleadoServiceImp.usuarioExiste(loginUsuarioEmpleadoDTO)){
            return "redirect:/empapp/login";
        }
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
            BindingResult bindingResult,
//            @RequestParam("foto") MultipartFile foto,
            HttpSession sesion,
            Model model) {

        MultipartFile foto = datosFormulario.getArchivoFoto();

        // Validación manual
        if (foto == null || foto.isEmpty()) {
            bindingResult.rejectValue("archivoFoto", "error.imagen.obligatoria");
        } else if (!(foto.getContentType().equalsIgnoreCase("image/jpeg")
                || foto.getContentType().equalsIgnoreCase("image/jpg")
                || foto.getContentType().equalsIgnoreCase("image/gif"))) {
            bindingResult.rejectValue("archivoFoto", "error.imagen.formato");
        } else if (foto.getSize() > 200 * 1024) {
            bindingResult.rejectValue("archivoFoto", "error.imagen.tamano");
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.out.println(bindingResult);
            return "registro_empleado/datos-personales";
        }

        try {
            // Asignar la imagen al DTO
            datosFormulario.setFoto(foto.getBytes());
        } catch (IOException e) {
            bindingResult.rejectValue("archivoFoto", "error.imagen.carga");
            return "registro_empleado/datos-personales";
        }

        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            altaEmpleadoServiceImp.actualizarDatos(datosFormulario, datosAnteriores);
        }
        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-contacto";
    }


    @GetMapping("/datos-contacto")
    public String datosContacto(HttpSession sesion, Model model) {


        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (!altaEmpleadoServiceImp.usuarioExiste(loginUsuarioEmpleadoDTO)){
            return "redirect:/empapp/login";
        }
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
            altaEmpleadoServiceImp.actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-profesionales";
    }
    @GetMapping("/datos-profesionales")
    public String datosProfesionales(HttpSession sesion, Model model){

        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (!altaEmpleadoServiceImp.usuarioExiste(loginUsuarioEmpleadoDTO)){
            return "redirect:/empapp/login";
        }
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
            altaEmpleadoServiceImp.actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-economicos";
    }
    @GetMapping("/datos-economicos")
    public String datosEconomicos(HttpSession sesion, Model model) {

        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (!altaEmpleadoServiceImp.usuarioExiste(loginUsuarioEmpleadoDTO)){
            return "redirect:/empapp/login";
        }
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
            altaEmpleadoServiceImp.actualizarDatos(datosFormulario, datosAnteriores);
        }


        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/resumen";
    }
    @GetMapping("/resumen")
    public String resumen(HttpSession sesion, Model model) {

        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        if (!altaEmpleadoServiceImp.usuarioExiste(loginUsuarioEmpleadoDTO)){
            return "redirect:/empapp/login";
        }
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
    @PostMapping("/guardar-resumen")
    public String guardarResumen(@Validated(GrupoResumen.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
                                 BindingResult bindingResult,
                                 HttpSession sesion,
                                 Model model,
                                 HttpServletRequest request) {


        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            altaEmpleadoServiceImp.actualizarDatos(datosFormulario, datosAnteriores);
        }

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registro_empleado/resumen";
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        LoginUsuarioEmpleadoDTO loginUsuarioEmpleadoDTO = (LoginUsuarioEmpleadoDTO) sesion.getAttribute("usuarioLogeado");
        altaEmpleadoServiceImp.guardarEmpleado(datosFormulario, loginUsuarioEmpleadoDTO.getId().toString());
        return "redirect:/usuario-insertado";
    }
    @GetMapping("/usuario-insertado")
    public String usuarioInsertado(){
        return "registro_empleado/usuario-insertado";
    }

    @GetMapping("/volver-principio")
    public String volverPrincipio(HttpSession sesion) {
        sesion.removeAttribute("datos");
        return "redirect:/datos-personales";
    }


}
