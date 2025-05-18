package org.grupob.empapp.converter;

import org.grupob.empapp.dto.*;
import org.grupob.empapp.dto.DimensionDTO;
import org.grupob.empapp.dto.TallaDTO;
import org.grupob.comun.entity.*; // Importa Libro, Electronico, Ropa, Mueble, Producto, Categoria, Proveedor, Talla
import org.grupob.comun.entity.auxiliar.Dimension; // Importa entidad Dimension
import org.grupob.comun.entity.auxiliar.jerarquia.Producto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Component
public class ProductoConverter {

    private final ModelMapper modelMapper;
    // Converters auxiliares para relaciones
    private final CategoriaConverter categoriaConverter;
    private final ProveedorConverter proveedorConverter;
    private final TallaConverter tallaConverter;
    // Podrías necesitar un DimensionConverter si Dimension fuera una entidad separada
    // private final DimensionConverter dimensionConverter;

    @Autowired
    public ProductoConverter(ModelMapper modelMapper, CategoriaConverter cc,
                             ProveedorConverter pc, TallaConverter tc /*, DimensionConverter dc */) {
        this.modelMapper = modelMapper;
        this.categoriaConverter = cc;
        this.proveedorConverter = pc;
        this.tallaConverter = tc;
        // this.dimensionConverter = dc;
    }

    // --- Método Principal de Conversión (Usando ModelMapper para Subclases) ---
    public ProductoDTO entityToDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO dtoResultante;

        // Intentar mapear directamente a la subclase DTO correspondiente
        if (producto instanceof Libro libro) {
            // Mapea entidad Libro a LibroDTO. Copiará campos comunes de Producto
            // y campos específicos de Libro si los nombres coinciden.
            dtoResultante = modelMapper.map(libro, LibroDTO.class);

            // Mapeo manual ADICIONAL si hay campos con nombres diferentes
            // o lógica compleja (generalmente no es necesario si los nombres son iguales)
            // Ejemplo: ((LibroDTO) dtoResultante).setAlgunaPropiedad(libro.getOtraPropiedad());

        } else if (producto instanceof Electronico electronico) {
            dtoResultante = modelMapper.map(electronico, ElectronicoDTO.class);
            // Mapear dimensión manualmente si ModelMapper no lo hace automáticamente
            // (porque Dimension es Embeddable y DimensionDTO es una clase separada)
            if (electronico.getDimension() != null && dtoResultante instanceof ElectronicoDTO electronicoDto) {
                electronicoDto.setDimension(mapDimensionManual(electronico.getDimension()));
                // O si tienes un converter específico:
                // electronicoDto.setDimension(dimensionConverter.entityToDto(electronico.getDimension()));
                // O si ModelMapper está configurado para mapearlo:
                // (no se necesita código extra aquí si modelMapper.map ya lo hizo)
            }

        } else if (producto instanceof Ropa ropa) {
            dtoResultante = modelMapper.map(ropa, RopaDTO.class);
            // Mapear lista de Tallas manualmente usando TallaConverter
            if (!CollectionUtils.isEmpty(ropa.getTallas()) && dtoResultante instanceof RopaDTO ropaDto) {
                ropaDto.setTallas(ropa.getTallas().stream()
                        .map(tallaConverter::entityToDto) // Importante tener TallaConverter
                        .collect(Collectors.toList()));
            }

        } else if (producto instanceof Mueble mueble) {
            dtoResultante = modelMapper.map(mueble, MuebleDTO.class);
            // Mapear dimensión manualmente (igual que en Electronico)
            if (mueble.getDimension() != null && dtoResultante instanceof MuebleDTO muebleDto) {
                muebleDto.setDimension(mapDimensionManual(mueble.getDimension()));
            }
            // Los colores (List<String>) deberían mapearse automáticamente por ModelMapper si
            // el campo se llama igual en la entidad Mueble y en MuebleDTO.

        } else {
            // Fallback: Mapear a DTO base si no es un tipo conocido
            dtoResultante = modelMapper.map(producto, ProductoDTO.class);
        }

        // --- Mapeo de relaciones comunes (Proveedor y Categorías) ---
        // Esto se hace DESPUÉS del mapeo inicial, sobre el dtoResultante
        if (dtoResultante != null) {
            // Mapear Proveedor
            if (producto.getProveedor() != null) {
                dtoResultante.setProveedor(proveedorConverter.entityToDTO(producto.getProveedor()));
            } else {
                dtoResultante.setProveedor(null);
            }

            // Mapear Categorías
            if (!CollectionUtils.isEmpty(producto.getCategoria())) { // Usa el nombre del campo en tu entidad Producto
                dtoResultante.setCategoria(producto.getCategoria().stream()
                        .map(categoriaConverter::convertirADTO)
                        .collect(Collectors.toSet()));
            } else {
                dtoResultante.setCategoria(null); // O un Set vacío: new HashSet<>()
            }
        }

        return dtoResultante;
    }


    // --- Método Auxiliar para Mapear Dimension (Manual) ---
    // Necesario si ModelMapper no mapea Dimension (Embeddable) a DimensionDTO (Clase) automáticamente
    private DimensionDTO mapDimensionManual(Dimension dimension) {
        if (dimension == null) {
            return null;
        }
        DimensionDTO dto = new DimensionDTO();
        dto.setAncho(dimension.getAncho());
        dto.setProfundo(dimension.getProfundo());
        dto.setAlto(dimension.getAlto());
        return dto;
    }



}