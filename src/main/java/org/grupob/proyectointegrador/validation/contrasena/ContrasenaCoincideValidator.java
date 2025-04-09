package org.grupob.proyectointegrador.validation.contrasena;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.proyectointegrador.dto.UsuarioDTO;

public class ContrasenaCoincideValidator implements ConstraintValidator<ContrasenaCoincide, Object> {

    @Override
    public void initialize(ContrasenaCoincide constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UsuarioDTO usuario = (UsuarioDTO) obj;
        boolean isValid = usuario.getContrasena() != null &&
                usuario.getContrasena().equals(usuario.getConfirmacionContrasena());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmacionContrasena")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
