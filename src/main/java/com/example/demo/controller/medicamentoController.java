package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.MedicamentoDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.MedicamentoService;

@RestController
@RequestMapping("/medicamento")
public class medicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    // POST - Crear medicamento
    @PostMapping("/post")
    public ResponseEntity<Object> registerMedicamento(@RequestBody MedicamentoDTO medicamento) {
        ResponseDTO respuesta = medicamentoService.save(medicamento);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    // GET - Listar todos los medicamentos activos
    @GetMapping("/get")
    public ResponseEntity<Object> getAllMedicamentos() {
        var lista = medicamentoService.findAll();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    // DELETE - Eliminar medicamento (cambiar status)
    @DeleteMapping("/{idMedicamento}")
    public ResponseEntity<Object> deleteMedicamento(@PathVariable int idMedicamento) {
        var message = medicamentoService.deleteMedicamento(idMedicamento);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // PUT - Actualizar medicamento
    @PutMapping("/{idMedicamento}")
    public ResponseEntity<Object> updateMedicamento(@PathVariable int idMedicamento, @RequestBody MedicamentoDTO medicamento) {
        ResponseDTO respuesta = medicamentoService.updateMedicamento(idMedicamento, medicamento);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
