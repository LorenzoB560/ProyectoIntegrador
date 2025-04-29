package org.grupob.empapp.validation.tipo_documento;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupob.comun.repository.PaisRepository;
import org.grupob.comun.repository.TipoDocumentoRepository;
import org.grupob.comun.repository.TipoViaRepository;
import org.grupob.empapp.validation.pais.ExistePais;
import org.grupob.empapp.validation.tipo_documento.ExisteTipoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExisteTipoDocumentoValidator implements ConstraintValidator<ExisteTipoDocumento, String> {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    public boolean isValid(String doc, ConstraintValidatorContext context) {
        return doc != null && tipoDocumentoRepository.existsByDocumento(doc);
    }
}
