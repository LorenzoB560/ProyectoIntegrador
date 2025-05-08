package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupob.empapp.dto.CategoriaDTO;
import org.grupob.empapp.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.ProveedorDTO;
import org.grupob.empapp.service.CategoriaServiceImp;
import org.grupob.empapp.service.ProveedorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final ProveedorServiceImp proveedorService;
    private final CategoriaServiceImp categoriaService;
    // Puedes inyectar otros servicios que uses aquí...

    @Autowired // Inyección por constructor recomendada
    public ProductoController(ProveedorServiceImp proveedorService, CategoriaServiceImp categoriaService) {
        this.proveedorService = proveedorService;
        this.categoriaService = categoriaService;
    }


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
    @GetMapping("/lista")
    public String vistaListaProductos(Model model, HttpSession session) {
//        LoginUsuarioEmpleadoDTO loginEmpDTO = (LoginUsuarioEmpleadoDTO) session.getAttribute("usuarioLogueado");
//        if (loginEmpDTO == null) {
//            return "redirect:/login/pedir-usuario";
//        }
//
//        model.addAttribute("loginEmpDTO", loginEmpDTO);
//        model.addAttribute("dto", loginEmpDTO); // Para compatibilidad con header
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
            // Podrías añadir un atributo al modelo para mostrar un error en la vista si falla la carga
            model.addAttribute("errorCargaFiltros", "No se pudieron cargar los filtros de proveedor o categoría.");
            // Opcionalmente, inicializa las listas como vacías para evitar errores en Thymeleaf
            model.addAttribute("listaProveedores", List.of());
            model.addAttribute("listaCategorias", List.of());
        }
        return "listados/listado-vista-prod"; // Ruta a la plantilla HTML de lista
    }

}