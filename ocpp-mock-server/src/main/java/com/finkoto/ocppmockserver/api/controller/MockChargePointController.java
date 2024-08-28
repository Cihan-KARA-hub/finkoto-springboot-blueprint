package com.finkoto.ocppmockserver.api.controller;


import com.finkoto.ocppmockserver.api.dto.ChargePointDto;
import com.finkoto.ocppmockserver.api.dto.PageableResponseDto;
import com.finkoto.ocppmockserver.services.MockChargePointServices;
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
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Tag(name = "Mock Charging - Charge Points Controller", description = "Charge Point API")
@RequestMapping("/csm/v1/mock-charge-points")
public class MockChargePointController {
    private final MockChargePointServices chargePointService;


    @Operation(summary = "Tüm Charge noktalarını oluşturulma sürelerine göre siralar  ve  bunlari sayafalayarak Json formatında getirir.")
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
    public ResponseEntity<PageableResponseDto<ChargePointDto>> getAllChargePoints(
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(chargePointService.getAll(pageable));
    }

    @Operation(summary = "Idsi verilen şarj noktasını geri döndürülür")
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
    public ResponseEntity<ChargePointDto> chargeGetById(@PathVariable Long id) {
        return Optional.ofNullable(chargePointService.findById(id)).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "şarj istasyonu oluşturur parametre verirken şarj istasyonunun donanım bilgileri ve connector bilgilerini de vermelisiniz. ")
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
    @PostMapping
    public ResponseEntity<Void> createChargePoint(@Validated @RequestBody ChargePointDto chargePointDto) {
        chargePointService.create(chargePointDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "verilen id'ye göre şarj istasyonlarının bilgilerini günceller.")
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
                                    schema = @Schema(type = "string")
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
    @PutMapping("/{id}")
    public ResponseEntity<ChargePointDto> updateChargePoint(@PathVariable Long id, @RequestBody ChargePointDto chargePointDto) {
        return ResponseEntity.ok(chargePointService.update(id, chargePointDto));
    }

    @Operation(summary = "şarj noktasını siler bununla berbar ilgili connectorler de linir")
    // 200 OK - Başarılı Silme Yanıtı
    @ApiResponse(
            responseCode = "200",
            description = "Successfully deleted the resource",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(),
                    examples = @ExampleObject(
                            name = "SuccessExample",
                            summary = "Successful deletion",
                            value = "{\"message\": \"Resource deleted successfully\"}"
                    )
            )
    )
// 204 No Content - Başarıyla Silindi, Gönderilecek İçerik Yok
    @ApiResponse(
            responseCode = "204",
            description = "Successfully deleted, no content",
            content = @Content
    )

// 404 Not Found - Kaynak Bulunamadı
    @ApiResponse(
            responseCode = "404",
            description = "Resource not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            name = "NotFoundExample",
                            summary = "Resource not found",
                            value = "{\"message\": \"Resource not found\", \"errorCode\": 404}"
                    )
            )
    )

// 401 Unauthorized - Yetkilendirme Hatası
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized access",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            name = "UnauthorizedExample",
                            summary = "Unauthorized request",
                            value = "{\"message\": \"Authentication required\", \"errorCode\": 401}"
                    )
            )
    )

// 500 Internal Server Error - Sunucu Hatası
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            name = "InternalServerErrorExample",
                            summary = "Internal server error occurred",
                            value = "{\"message\": \"An unexpected error occurred\", \"errorCode\": 500}"
                    )
            ),
            extensions = {
                    @Extension(name = "x-server-error", properties = {
                            @ExtensionProperty(name = "errorDetails", value = "Detailed internal server error information")
                    })
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChargePoint(@PathVariable Long id) {
        chargePointService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
