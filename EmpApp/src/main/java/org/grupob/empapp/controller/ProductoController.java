package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
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
//        LoginUsuarioEmpleadoDTO loginEmpDTO = (LoginUsuarioEmpleadoDTO) session.getAttribute("usuarioLogueado");
//        if (loginEmpDTO == null) {
//            return "redirect:/login/pedir-usuario";
//        }
//
//        // --- CAMBIO AQUÍ: Añadir el objeto con ambas claves ---
//        model.addAttribute("loginEmpDTO", loginEmpDTO); // Clave usada en th:object de detalle-vista-prod
//        model.addAttribute("dto", loginEmpDTO);         // Clave usada en th:object de area-personal (por si el fragmento la necesita)
//        // -----------------------------------------------------
//
        model.addAttribute("productoId", id);

        return "listados/detalle-vista-prod";
    }
}