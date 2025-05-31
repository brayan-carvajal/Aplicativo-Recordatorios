package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.RecordatorioDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.RecordatorioService;

@RestController
@RequestMapping("/recordatorio")
public class recordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    // POST - Crear recordatorio
    @PostMapping("/post")
    public ResponseEntity<Object> registerRecordatorio(@RequestBody RecordatorioDTO recordatorio) {
        ResponseDTO respuesta = recordatorioService.save(recordatorio);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    // GET - Listar todos los recordatorios activos
    @GetMapping("/get")
    public ResponseEntity<Object> getAllRecordatorios() {
        var lista = recordatorioService.findAll();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    // DELETE - Eliminar recordatorio (cambiar status)
    @DeleteMapping("/{idRecordatorio}")
    public ResponseEntity<Object> deleteRecordatorio(@PathVariable int idRecordatorio) {
        var message = recordatorioService.deleteRecordatorio(idRecordatorio);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // PUT - Actualizar recordatorio
    @PutMapping("/{idRecordatorio}")
    public ResponseEntity<Object> updateRecordatorio(@PathVariable int idRecordatorio, @RequestBody RecordatorioDTO recordatorio) {
        ResponseDTO respuesta = recordatorioService.updateRecordatorio(idRecordatorio, recordatorio);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
