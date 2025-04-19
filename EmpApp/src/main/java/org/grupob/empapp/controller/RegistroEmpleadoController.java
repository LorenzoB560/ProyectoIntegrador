package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.dto.auxiliar.DireccionPostalDTO;
import org.grupob.empapp.dto.grupoValidaciones.GrupoDireccion;
import org.grupob.empapp.dto.grupoValidaciones.GrupoFotoPerfil;
import org.grupob.empapp.dto.grupoValidaciones.GrupoLaboral;
import org.grupob.empapp.dto.grupoValidaciones.GrupoPersonal;
import org.grupob.empapp.entity.Departamento;
import org.grupob.empapp.entity.maestras.Genero;
import org.grupob.empapp.repository.DepartamentoRepository;
import org.grupob.empapp.repository.EmpleadoRepository;

import org.grupob.empapp.repository.maestras.GeneroRepository;
import org.grupob.empapp.service.AltaEmpleadoService;
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

    private final GeneroRepository generoRepository;
    private final AltaEmpleadoService altaEmpleadoService;
    private final DepartamentoRepository departamentoRepository;

    public RegistroEmpleadoController(GeneroRepository generoRepository, AltaEmpleadoService altaEmpleadoService, DepartamentoRepository departamentoRepository) {
        this.generoRepository = generoRepository;
        this.altaEmpleadoService = altaEmpleadoService;
        this.departamentoRepository = departamentoRepository;
    }

    @ModelAttribute //Cargo las colecciones una vez para añadirlas al modelo
    public void adicionColecciones(Model modelo) {
        List<Genero> listaGeneros = generoRepository.findAll();
        List<String> listaVias = List.of("Calle", "Avenida");
        List<Departamento> listaDepartamentos = departamentoRepository.findAll();
        modelo.addAttribute("listaGeneros", listaGeneros);
        modelo.addAttribute("listaVias", listaVias);
        modelo.addAttribute("listaDepartamentos", listaDepartamentos);
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

        return "registroEmpleado/datos-personales";
    }
    @PostMapping("/guardar-datos-personales")
    public String guardarDatosPersonales(
            @Validated(GrupoPersonal.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            System.err.println(bindingResult.toString());
            return "registroEmpleado/datos-personales";
        }


        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }

        //altaEmpleadoService.guardarEmpleado(datosFormulario, imagen);
        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/datos-direccion";
    }
    @GetMapping("/datos-direccion")
    public String datosDireccion(HttpSession sesion, Model model) {

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

        return "registroEmpleado/datos-direccion";
    }

    @PostMapping("/guardar-datos-direccion")
    public String guardarDatosDireccion(
            @Validated(GrupoDireccion.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            return "registroEmpleado/datos-direccion";
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
        return "redirect:/datos-laborales";
    }
    @GetMapping("/datos-laborales")
    public String datosLaborales(HttpSession sesion, Model model){

        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }

        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registroEmpleado/datos-laborales";
    }
    @PostMapping("/guardar-datos-laborales")
    public String guardarDatosLaborales(
            @Validated(GrupoLaboral.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            return "registroEmpleado/datos-laborales";
        }



        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/foto-perfil";
    }
    @GetMapping("/foto-perfil")
    public String fotoPerfil(HttpSession sesion, Model model) {
        //Obtengo la sesión de personales
        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");

        // Si no existe, creo el objeto de datos formulario y su sesión respectivamente.
        if(datosFormulario == null) {
            datosFormulario = new AltaEmpleadoDTO();
            sesion.setAttribute("datos", datosFormulario);
        }

        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "registroEmpleado/foto-perfil";
    }
    @PostMapping("/guardar-foto-perfil")
    public String guardarFotoPerfil(
            @Validated(GrupoFotoPerfil.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            @RequestParam MultipartFile imagen,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            return "registroEmpleado/foto-perfil";
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
        return "redirect:/resumen";
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

        return "registroEmpleado/resumen";
    }
    @PostMapping("/guardar-empleado")
    public String guardarEmpleado(HttpSession sesion) {

        AltaEmpleadoDTO datosFormulario = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        System.err.println(datosFormulario);
        altaEmpleadoService.guardarEmpleado(datosFormulario);
        return "redirect:/usuario-insertado";
    }
    @GetMapping("/usuario-insertado")
    public String usuarioInsertado(){
        return "registroEmpleado/usuario-insertado";
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
            nuevaDir.setPiso(nuevaDir.getPiso() != null ? nuevaDir.getPiso() : anteriorDir.getPiso());
            nuevaDir.setPuerta(nuevaDir.getPuerta() != null ? nuevaDir.getPuerta() : anteriorDir.getPuerta());
            nuevaDir.setCodigoPostal(nuevaDir.getCodigoPostal() != null ? nuevaDir.getCodigoPostal() : anteriorDir.getCodigoPostal());
            nuevaDir.setLocalidad(nuevaDir.getLocalidad() != null ? nuevaDir.getLocalidad() : anteriorDir.getLocalidad());
            nuevaDir.setRegion(nuevaDir.getRegion() != null ? nuevaDir.getRegion() : anteriorDir.getRegion());
            nuevaDir.setPais(nuevaDir.getPais() != null ? nuevaDir.getPais() : anteriorDir.getPais());
        }

        // ** DATOS LABORALES **
        datosNuevos.setIdDepartamentoSeleccionado(
                datosNuevos.getIdDepartamentoSeleccionado() != null ?
                        datosNuevos.getIdDepartamentoSeleccionado() :
                        datosAnteriores.getIdDepartamentoSeleccionado());

        // ** FOTO DE PERFIL **
        datosNuevos.setFoto(datosNuevos.getFoto() != null ? datosNuevos.getFoto() : datosAnteriores.getFoto());
    }


}
