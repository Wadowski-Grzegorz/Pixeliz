package com.example.backend.exception;

import java.util.Map;

public class RelationNotFound extends DetailedException {
    public RelationNotFound(Map<String, Object> criteria){
        super("Relation not found", criteria);
    }
}
