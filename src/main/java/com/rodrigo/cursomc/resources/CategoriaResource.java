package com.rodrigo.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rodrigo.cursomc.domain.Categoria;
import com.rodrigo.cursomc.dto.CategoriaDTO;
import com.rodrigo.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {

		Categoria categoria = service.findById(id);

		return ResponseEntity.ok().body(categoria);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Categoria categoria) {

		categoria = service.insert(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId())
				.toUri();

		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Categoria categoria, @PathVariable Integer id) {
		categoria.setId(id);
		categoria = service.update(categoria);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll() {

		List<Categoria> categorias = service.findAll();
		List<CategoriaDTO> categoriasDTO = categorias.stream().map(c -> new CategoriaDTO(c))
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(categoriasDTO);
	}

	@GetMapping(value = "/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy) {

		Page<Categoria> categorias = service.findPage(page, linesPerPage, direction, orderBy);
		Page<CategoriaDTO> categoriasDTO = categorias.map(c -> new CategoriaDTO(c));

		return ResponseEntity.ok().body(categoriasDTO);
	}
}
