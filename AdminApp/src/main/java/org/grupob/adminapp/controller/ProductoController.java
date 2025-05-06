package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/producto")
public class ProductoController {

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