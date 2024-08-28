package com.finkoto.chargestation.api.controller;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.services.ChargingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@Tag(name = "Central  Charging - Charging Sessions Controller", description = "Sessions API")
@RequestMapping("/csm/v1/charging-sessions")
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;

    @Operation(summary = "Tüm Charge Sessionlari oluşturulma sürelerine göre siralar  ve  bunlari sayafalayarak Json formatında getirir.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful Operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(),
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "An example of a successful response",
                                    value = "{\"message\": \"Operation completed successfully\"}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "X-Request-ID",
                                    description = "Request identifier",
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ServerErrorExample",
                                    summary = "An example of a 500 response",
                                    value = "{\"message\": \"Internal server error\", \"errorCode\": 500}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "Retry-After",
                                    description = "Time in seconds before the client can retry",
                                    schema = @Schema(type = "integer", format = "int32")
                            )
                    },
                    extensions = {
                            @Extension(name = "x-error-details", properties = {
                                    @ExtensionProperty(name = "support", value = "Contact support with error details")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UnauthorizedExample",
                                    summary = "An example of a 401 response",
                                    value = "{\"message\": \"Unauthorized access\", \"errorCode\": 401}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-unauthorized-info", properties = {
                                    @ExtensionProperty(name = "reason", value = "User needs to authenticate")
                            })
                    }
            )
    })
    @GetMapping
    public ResponseEntity<PageableResponseDto<ChargingSessionDto>> getAllChargingSessions(@Valid @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(chargingSessionService.getAll(pageable));
    }

    @Operation(summary = "Idsi verilen session geri döndürülür")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful Operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(),
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "An example of a successful response",
                                    value = "{\"message\": \"Operation completed successfully\"}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "X-Request-ID",
                                    description = "Request identifier",
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ServerErrorExample",
                                    summary = "An example of a 500 response",
                                    value = "{\"message\": \"Internal server error\", \"errorCode\": 500}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "Retry-After",
                                    description = "Time in seconds before the client can retry",
                                    schema = @Schema(type = "integer", format = "int32")
                            )
                    },
                    extensions = {
                            @Extension(name = "x-error-details", properties = {
                                    @ExtensionProperty(name = "support", value = "Contact support with error details")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UnauthorizedExample",
                                    summary = "An example of a 401 response",
                                    value = "{\"message\": \"Unauthorized access\", \"errorCode\": 401}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-unauthorized-info", properties = {
                                    @ExtensionProperty(name = "reason", value = "User needs to authenticate")
                            })
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChargingSessionDto> getChargeSessionsById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(chargingSessionService.findById(id));
    }

    @Operation(summary = "Şarj  başlatma istegi gönderir .")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Resource Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(),
                            examples = @ExampleObject(
                                    name = "CreatedExample",
                                    summary = "An example of a 201 response",
                                    value = "{ \"message\": \"Resource created successfully\"}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "Location",
                                    description = "URL of the newly created resource",
                                    schema = @Schema(type = "string"),
                                    examples = @ExampleObject(
                                            description = "example Request",
                                            value = "You must write mandatory parameters")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ConflictExample",
                                    summary = "An example of a 409 response",
                                    value = "{\"message\": \"Conflict detected\", \"errorCode\": 409}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-conflict-info", properties = {
                                    @ExtensionProperty(name = "conflictType", value = "Resource already exists")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UnauthorizedExample",
                                    summary = "An example of a 401 response",
                                    value = "{\"message\": \"Unauthorized access\", \"errorCode\": 401}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-unauthorized-info", properties = {
                                    @ExtensionProperty(name = "reason", value = "User needs to authenticate")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "NotFoundExample",
                                    summary = "An example of a 404 response",
                                    value = "{\"message\": \"Resource not found\", \"errorCode\": 404}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-custom-info", properties = {
                                    @ExtensionProperty(name = "info", value = "Additional info about 404 response")
                            })
                    },
                    headers = {
                            @Header(
                                    name = "X-Error-Code",
                                    description = "Custom header for error code",
                                    schema = @Schema(type = "string")
                            )
                    },
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ServerErrorExample",
                                    summary = "An example of a 500 response",
                                    value = "{\"message\": \"Internal server error\", \"errorCode\": 500}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "Retry-After",
                                    description = "Time in seconds before the client can retry",
                                    schema = @Schema(type = "integer", format = "int32")
                            )
                    },
                    extensions = {
                            @Extension(name = "x-error-details", properties = {
                                    @ExtensionProperty(name = "support", value = "Contact support with error details")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BadRequestExample",
                                    summary = "An example of a 400 response",
                                    value = "{\"message\": \"Invalid request parameters\", \"errorCode\": 400, \"details\": \"The 'name' field is required.\"}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "X-Error-Info",
                                    description = "Detailed error information",
                                    schema = @Schema(type = "string")
                            )
                    },
                    extensions = {
                            @Extension(name = "x-bad-request-info", properties = {
                                    @ExtensionProperty(name = "validationErrors", value = "List of fields that failed validation")
                            })
                    }
            )
    })
    //TODO  burda bir token verilecek
    @PostMapping("/start")
    public ResponseEntity<Void> start(@Valid @RequestParam int ocppId, @RequestParam Long connectorId) {
        Exception check = chargingSessionService.sendRemoteStartTransactionRequest(connectorId, ocppId);
        if (check != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Şarj  durdurma istegi gönderir .")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Resource Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(),
                            examples = @ExampleObject(
                                    name = "CreatedExample",
                                    summary = "An example of a 201 response",
                                    value = "{ \"message\": \"Resource created successfully\"}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "Location",
                                    description = "URL of the newly created resource",
                                    schema = @Schema(type = "string"),
                                    examples = @ExampleObject(
                                            description = "example Request",
                                            value = "You must write mandatory parameters")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ConflictExample",
                                    summary = "An example of a 409 response",
                                    value = "{\"message\": \"Conflict detected\", \"errorCode\": 409}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-conflict-info", properties = {
                                    @ExtensionProperty(name = "conflictType", value = "Resource already exists")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "UnauthorizedExample",
                                    summary = "An example of a 401 response",
                                    value = "{\"message\": \"Unauthorized access\", \"errorCode\": 401}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-unauthorized-info", properties = {
                                    @ExtensionProperty(name = "reason", value = "User needs to authenticate")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "NotFoundExample",
                                    summary = "An example of a 404 response",
                                    value = "{\"message\": \"Resource not found\", \"errorCode\": 404}"
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-custom-info", properties = {
                                    @ExtensionProperty(name = "info", value = "Additional info about 404 response")
                            })
                    },
                    headers = {
                            @Header(
                                    name = "X-Error-Code",
                                    description = "Custom header for error code",
                                    schema = @Schema(type = "string")
                            )
                    },
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ServerErrorExample",
                                    summary = "An example of a 500 response",
                                    value = "{\"message\": \"Internal server error\", \"errorCode\": 500}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "Retry-After",
                                    description = "Time in seconds before the client can retry",
                                    schema = @Schema(type = "integer", format = "int32")
                            )
                    },
                    extensions = {
                            @Extension(name = "x-error-details", properties = {
                                    @ExtensionProperty(name = "support", value = "Contact support with error details")
                            })
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BadRequestExample",
                                    summary = "An example of a 400 response",
                                    value = "{\"message\": \"Invalid request parameters\", \"errorCode\": 400, \"details\": \"The 'name' field is required.\"}"
                            )
                    ),
                    headers = {
                            @Header(
                                    name = "X-Error-Info",
                                    description = "Detailed error information",
                                    schema = @Schema(type = "string")
                            )
                    },
                    extensions = {
                            @Extension(name = "x-bad-request-info", properties = {
                                    @ExtensionProperty(name = "validationErrors", value = "List of fields that failed validation")
                            })
                    }
            )
    })
    @PutMapping("/stop/{id}")
    public ResponseEntity<Void> stop(@Valid @PathVariable Long id) {
        boolean check = chargingSessionService.sendRemoteStopTransactionRequest(id);
        if (check) return ResponseEntity.ok().build();
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
