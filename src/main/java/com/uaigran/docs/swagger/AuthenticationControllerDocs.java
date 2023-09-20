package com.uaigran.docs.swagger;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.UnauthorizedException;
import com.uaigran.models.services.authentication.AuthenticationRequest;
import com.uaigran.models.services.authentication.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Essa rota autentica o usuário na aplicação!")
public interface AuthenticationControllerDocs {

    @Operation(
            summary = "Autentica o usuário",
            description = "Autentica o usuário na aplicação gerando um token."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário autenticado com sucesso!",
                            content = {@Content(schema = @Schema(implementation = AuthenticationResponse.class))}
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciais de usuário inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) throws UnauthorizedException;


    }
