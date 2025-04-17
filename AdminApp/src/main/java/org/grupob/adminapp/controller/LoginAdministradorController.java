package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupob.adminapp.converter.AdministradorConverter;
import org.grupob.adminapp.dto.LoginAdministradorDTO;
import org.grupob.adminapp.entity.Administrador;
import org.grupob.adminapp.service.AdministradorServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("adminapp")
public class LoginAdministradorController {

    private final AdministradorServiceImp adminServicio;
    private final AdministradorConverter adminConverter;

    public LoginAdministradorController(AdministradorServiceImp adminServicio, AdministradorConverter adminConverter) {
        this.adminServicio = adminServicio;
        this.adminConverter = adminConverter;
    }

    // Endpoint inicial: /correo (ingresar nombre de usuario)
    @GetMapping("login")
    public String login(@ModelAttribute("loginAdminDTO") LoginAdministradorDTO loginAdminDTO) {

        return "login";
    }


    @PostMapping("login")
    public String comprobrarCredenciales(Model modelo,
                                         HttpSession sesion,
                                         @Valid @ModelAttribute("loginAdminDTO") LoginAdministradorDTO loginAdminDTO,
                                         BindingResult resultadoValidacion) {

        if (resultadoValidacion.hasErrors()) { //si hay errores
            return "login";
        }

        if (adminServicio.comprobarCredenciales(loginAdminDTO)) {
            Administrador admin = adminServicio.devuelveAdministradorPorCorreo(loginAdminDTO.getCorreo());
            admin = adminServicio.aumentarNumAccesos(admin);
            loginAdminDTO = adminConverter.convertirADTO(admin);
            modelo.addAttribute("loginAdminDTO", loginAdminDTO);
            // Guardamos en sesión lo que necesitemos
            sesion.setAttribute("adminLogueado", loginAdminDTO); // o solo el ID si prefieres

            // Redirigimos al endpoint GET
            return "redirect:/adminapp/area-personal";
        }
        modelo.addAttribute("ErrorCredenciales", "Usuario/Clave incorrecta");
        return "login";

    }

    @GetMapping("area-personal")
    public String areaPersonal(Model modelo, HttpSession sesion) {
        LoginAdministradorDTO adminDTO = (LoginAdministradorDTO) sesion.getAttribute("adminLogueado");

        if (adminDTO == null) {
            return "redirect:/adminapp/login"; // protección ante acceso directo sin login
        }

        modelo.addAttribute("loginAdminDTO", adminDTO);
        return "area-personal";
    }

    // Endpoint para desconectar
    @GetMapping("/desconectar")
    public String desconectarUsuario(HttpSession sesion) {


        sesion.removeAttribute("loginAdminDTO");
        // Redirige al inicio
        return "redirect:/adminapp/login";
    }


    @GetMapping("/devuelve-clave")
    @ResponseBody
    public String devuelveClave(@RequestParam(required = false) String correo) {
        try {
            Administrador admin = adminServicio.devuelveAdministradorPorCorreo(correo);
            return admin.getClave(); // Recuerda: esto es solo para pruebas/demo
        } catch (RuntimeException e) {
            return "Usuario no encontrado";
        }
    }

}


