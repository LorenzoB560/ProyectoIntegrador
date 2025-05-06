package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/adminapp")
public class ProductoController {

    @GetMapping("/carga-masiva")
    public String cargarMasivaProductos(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/adminapp/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);

        return "producto/carga-masiva";
    }


    @GetMapping("/borrado-masivo")
    public String eliminacionMasivaProductos(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/adminapp/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "producto/eliminacion-masiva";
    }

    @GetMapping("/detalle/{id}")
    public String vistaDetalleProducto(@PathVariable UUID id, Model model, HttpSession session) {
//        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) session.getAttribute("adminLogueado");
//        if (adminDTO == null) {
//            return "redirect:/adminapp/login"; // Redirige si no está logueado
//        }
//
//        // Aquí no cargamos datos, solo devolvemos la vista. El JS cargará los datos.
//        model.addAttribute("loginAdminDTO", adminDTO); // Para el layout
        model.addAttribute("productoId", id); // Pasamos el ID para que JS lo use

        return "listados/detalle-vista-prod";
    }

    @GetMapping("/lista")
    public String vistaListaProductos(Model model, HttpSession session) {
//        LoginUsuarioEmpleadoDTO loginEmpDTO = (LoginUsuarioEmpleadoDTO) session.getAttribute("usuarioLogueado");
//        if (loginEmpDTO == null) {
//            return "redirect:/login/pedir-usuario";
//        }
//
//        model.addAttribute("loginEmpDTO", loginEmpDTO);
//        model.addAttribute("dto", loginEmpDTO); // Para compatibilidad con header

        return "listados/listado-vista-prod"; // Ruta a la plantilla HTML de lista
    }
}
