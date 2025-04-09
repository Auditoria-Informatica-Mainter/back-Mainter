package com.example.BackendProject.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {
	
	 private Integer  statusCode;
	    private String message;
	    private T data;
	    private List<String> errors;

	    public ApiResponse(int statusCode, String message, T data) {
	        this.statusCode = statusCode;
	        this.message = message;
	        this.data = data;
	    }

	    public ApiResponse(int statusCode, String message) {
	        this.statusCode = statusCode;
	        this.message = message;
	    }

	    public ApiResponse(List<String> errors) {
	        this.errors = errors;
	    }
	
}
