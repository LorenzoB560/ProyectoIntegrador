package org.grupob.empapp.validation.foto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImagenValidator implements ConstraintValidator<ImagenValida, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false; // Imagen obligatoria
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equalsIgnoreCase("image/jpeg") ||
                        contentType.equalsIgnoreCase("image/jpg") ||
                        contentType.equalsIgnoreCase("image/gif"))) {
            return false; // Formato incorrecto
        }

        if (file.getSize() > 200 * 1024) { // 200 KB
            return false; // Muy grande
        }

        return true;
    }
}
