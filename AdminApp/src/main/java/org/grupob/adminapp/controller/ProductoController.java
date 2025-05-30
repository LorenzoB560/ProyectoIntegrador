package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.grupob.adminapp.dto.CategoriaDTO;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.adminapp.dto.ProveedorDTO;
import org.grupob.adminapp.service.CategoriaServiceImp;
import org.grupob.adminapp.service.ProveedorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductoController {

    private final ProveedorServiceImp proveedorService;
    private final CategoriaServiceImp categoriaService;

    @GetMapping("/carga-masiva")
    public String cargarMasivaProductos(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);

        return "producto/carga-masiva";
    }


    @GetMapping("/borrado-masivo")
    public String eliminacionMasivaProductos(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/login"; // protección ante acceso directo sin login
        }
        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "producto/eliminacion-masiva";
    }

    @GetMapping("/detalle/{id}")
    public String vistaDetalleProducto(@PathVariable UUID id, Model model, HttpSession session) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) session.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/login"; // Redirige si no está logueado
        }
        // Aquí no cargamos datos, solo devolvemos la vista. El JS cargará los datos.
        model.addAttribute("loginAdminDTO", adminDTO); // Para el layout
        model.addAttribute("productoId", id); // Pasamos el ID para que JS lo use

        return "listados/detalle-vista-prod";
    }

    @GetMapping("/lista")
    public String vistaListaProductos(Model model, HttpSession session) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) session.getAttribute("adminLogueado");
        if (adminDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginAdminDTO", adminDTO);
        try {
            // Obtener lista de proveedores DTO usando el servicio
            List<ProveedorDTO> proveedores = proveedorService.findAll();
            model.addAttribute("listaProveedores", proveedores); // Añadir al modelo

            // Obtener lista de categorías DTO (como ya lo hacías)
            List<CategoriaDTO> categorias = categoriaService.devuelveTodas();
            model.addAttribute("listaCategorias", categorias);  // Añadir al modelo

        } catch (Exception e) {
            // Manejo básico de errores: log y quizás mensaje al usuario
            System.err.println("Error al cargar datos para filtros: " + e.getMessage());
            model.addAttribute("errorCargaFiltros", "No se pudieron cargar los filtros de proveedor o categoría.");
            model.addAttribute("listaProveedores", List.of());
            model.addAttribute("listaCategorias", List.of());
        }
        return "listados/listado-vista-prod"; // Ruta a la plantilla HTML de lista
    }
}
