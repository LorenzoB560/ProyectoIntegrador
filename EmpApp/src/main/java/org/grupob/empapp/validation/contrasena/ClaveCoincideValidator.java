package org.grupob.empapp.validation.contrasena;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.empapp.dto.RegistroUsuarioEmpleadoDTO;
import org.grupob.empapp.dto.UsuarioDTO;

public class ClaveCoincideValidator implements ConstraintValidator<ClaveCoincide, Object> {

    @Override
    public void initialize(ClaveCoincide constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        RegistroUsuarioEmpleadoDTO usuario = (RegistroUsuarioEmpleadoDTO) obj;
        boolean isValid = usuario.getClave() != null &&
                usuario.getClave().equals(usuario.getConfirmarClave());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmarClave")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
