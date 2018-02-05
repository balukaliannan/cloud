package com.example.angularmongo.todoapp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.angularmongo.models.TodoDoc;
@Repository
public interface TodoRepository extends MongoRepository<TodoDoc, String> {

}
