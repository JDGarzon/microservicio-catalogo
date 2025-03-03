package co.analisys.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/libros")
public class CatalogoController {


    @Autowired
    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Operation(
        summary = "Obtiene un libro por su ID",
        description = "Devuelve la información detallada de un libro específico identificado por su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CONSULTAR')")
    public Libro obtenerLibro(@PathVariable String id) {
        return catalogoService.obtenerLibro(new LibroId(id));
    }

    @Operation(
        summary = "Verifica la disponibilidad de un libro",
        description = "Comprueba si un libro específico está disponible en el catálogo y devuelve un booleano."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de disponibilidad devuelto correctamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}/disponible")
    @PreAuthorize("hasRole('ROLE_CONSULTAR')")
    public boolean isLibroDisponible(@PathVariable String id) {
        Libro libro = catalogoService.obtenerLibro(new LibroId(id));
        return libro != null && libro.isDisponible();
    }

    @Operation(
        summary = "Actualiza la disponibilidad de un libro",
        description = "Modifica el estado de disponibilidad de un libro identificado por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/{id}/disponibilidad")
    @PreAuthorize("hasRole('ROLE_EDITAR')")
    public void actualizarDisponibilidad(@PathVariable String id, @RequestBody boolean disponible) {
        catalogoService.actualizarDisponibilidad(new LibroId(id), disponible);
    }

    @Operation(
        summary = "Busca libros en el catálogo",
        description = "Realiza una búsqueda en el catálogo utilizando un criterio de búsqueda proporcionado como parámetro."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de libros coincidentes devuelta correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ROLE_CONSULTAR')")
    public List<Libro> buscarLibros(@RequestParam String criterio) {
        return catalogoService.buscarLibros(criterio);
    }
}
