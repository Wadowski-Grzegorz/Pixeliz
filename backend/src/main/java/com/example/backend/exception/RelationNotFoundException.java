package com.example.backend.exception;

import java.util.Map;

public class RelationNotFoundException extends DetailedException {
    public RelationNotFoundException(Map<String, Object> criteria){
        super("Relation not found", criteria);
    }

    public RelationNotFoundException(){
        super("Relation not found");
    }
}
