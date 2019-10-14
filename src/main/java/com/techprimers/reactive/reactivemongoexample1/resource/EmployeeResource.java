
package com.techprimers.reactive.reactivemongoexample1.resource;

import com.techprimers.reactive.reactivemongoexample1.model.Employee;
import com.techprimers.reactive.reactivemongoexample1.model.EmployeeEvent;
import com.techprimers.reactive.reactivemongoexample1.repository.EmployeeRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rest/employee")
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)

public class EmployeeResource {


    private EmployeeRepository employeeRepository;
    private String[] types= "add,update,remove,patch".split(",");

    public EmployeeResource(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(value = "/all")
    public Flux<Employee> getAllId() {
                return  employeeRepository.findAll();
    }



    @GetMapping("/{id}")
    public Mono<Employee> getId(@PathVariable("id") final String empId) {
        return employeeRepository.findById(empId);
    }


    @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EmployeeEvent> getEvents(@PathVariable("id")
                                    final String empId) {
        Random ran=new Random();
        return employeeRepository.findById(empId)
                .flatMapMany(employee -> {
                    Flux<Long> interval = Flux.interval(Duration.ofSeconds(5));


                    Flux<EmployeeEvent> employeeEventFlux =
                            Flux.fromStream(
                                    Stream.generate(() -> new EmployeeEvent(employeeRepository.findById(empId).block(),
                                            types[ran.nextInt(types.length)],new Date().toString()))
                            );


                        return Flux.zip(interval, employeeEventFlux)
                                .map(Tuple2::getT2).doOnError(ex-> System.err.println("some error  " +ex));
                                });
                }






}

