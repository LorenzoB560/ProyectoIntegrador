package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.converter.EmpleadoConverter;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.grupob.empapp.entity.maestras.Genero;
import org.grupob.empapp.repository.EmpleadoRepository;

import org.grupob.empapp.repository.maestras.GeneroRepository;
import org.grupob.empapp.service.AltaEmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class RegistroEmpleadoController {

    private final GeneroRepository generoRepository;
    private final AltaEmpleadoService altaEmpleadoService;
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoConverter empleadoConverter;

    public RegistroEmpleadoController(GeneroRepository generoRepository, AltaEmpleadoService altaEmpleadoService, EmpleadoRepository empleadoRepository, EmpleadoConverter empleadoConverter) {
        this.generoRepository = generoRepository;
        this.altaEmpleadoService = altaEmpleadoService;
        this.empleadoRepository = empleadoRepository;
        this.empleadoConverter = empleadoConverter;
    }


    @ModelAttribute //Cargo las colecciones una vez para añadirlas al modelo
    public void adicionColecciones(Model modelo) {
        List<Genero> listaGeneros = generoRepository.findAll();
        List<String> listaVias = List.of("Calle", "Avenida");
        modelo.addAttribute("listaGeneros", listaGeneros);
        modelo.addAttribute("listaVias", listaVias);
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

        return "datos-personales";
    }
    @PostMapping("/guardar-datos-personales")
    public String guardarDatosPersonales(
            @Validated(AltaEmpleadoDTO.GrupoPersonal.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            @RequestParam MultipartFile imagen,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            return "datos-personales";
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

        //Lo añado al modelo
        model.addAttribute("datos", datosFormulario);

        return "datos-direccion";
    }

    @PostMapping("/guardar-datos-direccion")
    public String guardarDatosDireccion(
            @Validated(AltaEmpleadoDTO.GrupoDireccion.class) @ModelAttribute("datos") AltaEmpleadoDTO datosFormulario,
            BindingResult bindingResult,
            HttpSession sesion,
            Model model) {

        // Si hay errores, volver a la misma página
        if (bindingResult.hasErrors()) {
            model.addAttribute("datos", datosFormulario);
            model.addAttribute("mensajeNOK", "El formulario tiene errores");
            return "datos-direccion";
        }



        AltaEmpleadoDTO datosAnteriores = (AltaEmpleadoDTO) sesion.getAttribute("datos");
        if (datosAnteriores != null) {
            actualizarDatos(datosFormulario, datosAnteriores);
        }

        System.err.println(datosFormulario);
        sesion.setAttribute("datos", datosFormulario);
        return "redirect:/informacion-laboral";
    }

    private void actualizarDatos(AltaEmpleadoDTO datosNuevos, AltaEmpleadoDTO datosAnteriores) {
        if (datosNuevos == null) {
            datosNuevos = new AltaEmpleadoDTO(); // Nueva instancia si los nuevos datos son nulos
        }

        if (datosAnteriores == null) {
            return; // No hay nada anterior que recuperar
        }

        // Actualizar los campos nuevos solo si están vacíos o nulos
        datosNuevos.setNombre(datosNuevos.getNombre() != null ? datosNuevos.getNombre() : datosAnteriores.getNombre());
        datosNuevos.setApellido(datosNuevos.getApellido() != null ? datosNuevos.getApellido() : datosAnteriores.getApellido());
        datosNuevos.setFechaNacimiento(datosNuevos.getFechaNacimiento() != null ? datosNuevos.getFechaNacimiento() : datosAnteriores.getFechaNacimiento());
        datosNuevos.setIdGeneroSeleccionado(datosNuevos.getIdGeneroSeleccionado() != null ? datosNuevos.getIdGeneroSeleccionado() : datosAnteriores.getIdGeneroSeleccionado());
    }

}
