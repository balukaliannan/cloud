package com.example.angularmongo.todoapp.controllers;
import javax.validation.Valid;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.datastax.driver.core.utils.UUIDs;
import com.example.angularmongo.models.Todo;
import com.example.angularmongo.models.TodoDoc;
import com.example.angularmongo.todoapp.TodoRepository;
import com.example.cassandra.Customer;
import com.example.cassandra.CustomerRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TodoController {

    @Autowired
    TodoRepository todoRepository;
    
    @Autowired
	private CustomerRepository repository;

    @GetMapping("/todos")
    public List<Todo> getAllTodos() {
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
        List<TodoDoc> items=todoRepository.findAll(sortByCreatedAtDesc);
        List<Todo> todos=new ArrayList<Todo>();
        Todo todo=new Todo();
        for (TodoDoc todoDoc : items) {
			todo=new Todo();
			todo.setId(todoDoc.getId());
			todo.setTitle(todoDoc.getTitle());
			todo.setCompleted(false);
			todo.setCreatedAt(new Date());
			todos.add(todo);
		}
        return todos;
       // return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PostMapping("/todos")
    public TodoDoc createTodo(@Valid @RequestBody TodoDoc todo) {
        todo.setCompleted(false);
        return todoRepository.save(todo);
    }

    @GetMapping(value="/todos/{id}")
    public ResponseEntity<Optional<TodoDoc>> getTodoById(@PathVariable("id") String id) {
        Optional<TodoDoc> todo = todoRepository.findById(id);
        if(todo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(todo, HttpStatus.OK);
        }
    }

    @PutMapping(value="/todos/{id}")
    public ResponseEntity<Optional<TodoDoc>> updateTodo(@PathVariable("id") String id,
                                           @Valid @RequestBody TodoDoc todo) {
        Optional<TodoDoc> todoData = todoRepository.findById(id);
        if(todoData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
      //  todoData.setTitle(todo.getTitle());
       // todoData.setCompleted(todo.getCompleted());
       // TodoDoc updatedTodo = todoRepository.save(todoData);
        return new ResponseEntity<>(todoData, HttpStatus.OK);
    }

    @DeleteMapping(value="/todos/{id}")
    public void deleteTodo(@PathVariable("id") String id) {
        todoRepository.deleteById(id);
    }
    
    @GetMapping("/customer")
    public void getCustomer() {
    	this.repository.deleteAll();

		// save a couple of customers
		this.repository.save(new Customer(UUIDs.timeBased(), "Alice", "Smith"));
		this.repository.save(new Customer(UUIDs.timeBased(), "Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Customer customer : this.repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(this.repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : this.repository.findByLastName("Smith")) {
			System.out.println(customer);
		}
    } 
}


