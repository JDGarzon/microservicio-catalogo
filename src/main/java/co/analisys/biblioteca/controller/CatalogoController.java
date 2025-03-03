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

@RestController
@RequestMapping("/libros")
public class CatalogoController {


    @Autowired
    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Operation(
        summary = "Consigue todos los libros",
        description = "Devuelve una lista de todos los libros en el catálogo"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CONSULTAR')")
    public Libro obtenerLibro(@PathVariable String id) {
        return catalogoService.obtenerLibro(new LibroId(id));
    }

    @Operation(
        summary = "Verifica si un libro está disponible",
        description = "Utilizando el id del libro, verifica si el libro está disponible y devuelve un booleano"
    )
    @GetMapping("/{id}/disponible")
    @PreAuthorize("hasRole('ROLE_CONSULTAR')")
    public boolean isLibroDisponible(@PathVariable String id) {
        Libro libro = catalogoService.obtenerLibro(new LibroId(id));
        return libro != null && libro.isDisponible();
    }

    @Operation(
        summary = "Actualiza la disponibilidad de un libro",
        description = "Utilizando el id del libro, actualiza la disponibilidad del libro"
    )
    @PutMapping("/{id}/disponibilidad")
    @PreAuthorize("hasRole('ROLE_EDITAR')")
    public void actualizarDisponibilidad(@PathVariable String id, @RequestBody boolean disponible) {
        catalogoService.actualizarDisponibilidad(new LibroId(id), disponible);
    }

    @Operation(
        summary = "Busca libros",
        description = "Busca libros en el catálogo utilizando un criterio de búsqueda"
    )
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ROLE_CONSULTAR')")
    public List<Libro> buscarLibros(@RequestParam String criterio) {
        return catalogoService.buscarLibros(criterio);
    }
}
