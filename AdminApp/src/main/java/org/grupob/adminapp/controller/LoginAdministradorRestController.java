package org.grupob.adminapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupob.adminapp.converter.AdministradorConverter;
import org.grupob.comun.dto.LoginAdministradorDTO;
import org.grupob.comun.entity.Administrador;
import org.grupob.adminapp.service.AdministradorServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginAdministradorRestController {

    private final AdministradorServiceImp adminServicio;
    private final AdministradorConverter adminConverter;

    public LoginAdministradorRestController(AdministradorServiceImp adminServicio, AdministradorConverter adminConverter) {
        this.adminServicio = adminServicio;
        this.adminConverter = adminConverter;
    }


   /* @PostMapping("login")
    public ResponseEntity<LoginAdministradorDTO> comprobrarCredenciales(HttpSession sesion,
                                                    @RequestBody @Valid LoginAdministradorDTO loginAdminDTO) {

*//*    public ResponseEntity<String> comprobrarCredenciales(HttpSession sesion,
                                                                        @RequestBody @Valid LoginAdministradorDTO loginAdminDTO) {*//*
       LoginAdministradorDTO adminDTO = adminServicio.comprobarCredenciales(loginAdminDTO);
        sesion.setAttribute("adminLogueado", adminDTO);
        String idSesion = sesion.getId();

         return ResponseEntity.ok(adminDTO);
//        return ResponseEntity.ok(idSesion);
    }*/

    @PostMapping("login")
    public ResponseEntity<LoginAdministradorDTO> comprobrarCredenciales(
            HttpSession sesion,
            @RequestBody @Valid LoginAdministradorDTO loginAdminDTO) {

        LoginAdministradorDTO adminDTO = adminServicio.comprobarCredenciales(loginAdminDTO);
        sesion.setAttribute("adminLogueado", adminDTO);

        return ResponseEntity.ok(adminDTO);
    }


    @GetMapping("/devuelve-clave")
    public String devuelveClave(@RequestParam(required = false) String correo) {
        try {
            Administrador admin = adminServicio.devuelveAdministradorPorUsuario(correo);
            return admin.getClave(); // Recuerda: esto es solo para pruebas/demo
        } catch (RuntimeException e) {
            return "Usuario no encontrado";
        }
    }

}


