package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.AltaEmpleadoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroEmpleadoController {

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
            //@RequestParam MultipartFile archivo,
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
            actualizarDatos(datosAnteriores, datosFormulario);
        } else {
            datosAnteriores = datosFormulario;
        }

        /*try {
            subirArchivo(archivo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        sesion.setAttribute("datos", datosAnteriores);

        return "redirect:/datos-contacto";
    }
    @GetMapping("/datos-contacto")
    public String datosContacto() {
        return "datos-contacto";
    }


    /*private void subirArchivo(MultipartFile archivo) throws IOException {
        if (!archivo.isEmpty()) {
            // Carpeta fija en el escritorio del usuario
            String ruta = "C:/archivos-servidor";
            File carpeta = new File(ruta);
            carpeta.mkdirs(); // Crea la carpeta si no existe

            // Guarda el archivo
            archivo.transferTo(new File(carpeta, archivo.getOriginalFilename()));
        }
    }*/

    private void actualizarDatos(AltaEmpleadoDTO sesionVieja, AltaEmpleadoDTO sesionNueva) {
        if (sesionVieja == null) {
            sesionVieja = new AltaEmpleadoDTO(); // Se crea una nueva instancia si la sesión ha expirado
        }

        if (sesionNueva == null) {
            return; // Si no hay nuevos datos, no hacemos nada
        }

        // Datos personales
        sesionVieja.setNombre(sesionNueva.getNombre() != null ? sesionNueva.getNombre() : sesionVieja.getNombre());
        sesionVieja.setApellido(sesionNueva.getApellido() != null ? sesionNueva.getApellido() : sesionVieja.getApellido());
        sesionVieja.setFechaNacimiento(sesionNueva.getFechaNacimiento() != null ? sesionNueva.getFechaNacimiento() : sesionVieja.getFechaNacimiento());
        //sesionVieja.setGeneroSeleccionado(sesionNueva.getGeneroSeleccionado() != null ? sesionNueva.getGeneroSeleccionado() : sesionVieja.getGeneroSeleccionado());
        //sesionVieja.setNacionalidadSeleccionada(sesionNueva.getNacionalidadSeleccionada() != null ? sesionNueva.getNacionalidadSeleccionada() : sesionVieja.getNacionalidadSeleccionada());
    }
}
