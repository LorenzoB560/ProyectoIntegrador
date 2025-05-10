package org.grupob.empapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.grupob.comun.dto.LoginUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.CategoriaDTO;
import org.grupob.empapp.dto.ProveedorDTO;
import org.grupob.empapp.service.CategoriaServiceImp;
import org.grupob.empapp.service.CookieService;
import org.grupob.empapp.service.ProveedorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
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
    private final CookieService cookieService;

    @Autowired // Inyección por constructor recomendada
    public ProductoController(ProveedorServiceImp proveedorService, CategoriaServiceImp categoriaService, CookieService cookieService) {
        this.proveedorService = proveedorService;
        this.categoriaService = categoriaService;
        this.cookieService = cookieService;
    }


    @GetMapping("/detalle/{id}")
    public String vistaDetalleProducto(@PathVariable UUID id, Model model, HttpServletRequest request,
                                       @CookieValue(name = "usuario", required = false) String usuariosCookie) {
        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        if(dto==null){
            return "redirect:/empapp/login";
        }

        model.addAttribute("dto", dto);
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        model.addAttribute("ultimaPagina", ultimaPagina);

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
        model.addAttribute("contador", contador);
//        // -----------------------------------------------------
//
        model.addAttribute("productoId", id);

        return "listados/detalle-vista-prod";
    }
    @GetMapping("/lista")
    public String vistaListaProductos(Model model, HttpServletRequest request,
                                      @CookieValue(name = "usuario", required = false) String usuariosCookie) {
        LoginUsuarioEmpleadoDTO dto = (LoginUsuarioEmpleadoDTO) request.getSession().getAttribute("usuarioLogeado");

        if(dto==null){
            return "redirect:/empapp/login";
        }

        model.addAttribute("dto", dto);
        String ultimaPagina = cookieService.obtenerValorCookie(request, "ultimaPagina");
        model.addAttribute("ultimaPagina", ultimaPagina);

        String ultimoUsuario = (String) request.getSession().getAttribute("ultimoUsuario");
        int contador = cookieService.obtenerInicios(usuariosCookie, ultimoUsuario);
        model.addAttribute("contador", contador);
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