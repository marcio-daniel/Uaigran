package com.uaigran.docs.swagger;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.user.CreateUserRequest;
import com.uaigran.models.services.user.UpdateUserRequest;
import com.uaigran.models.services.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Usuários", description = "Rotas que lidam com as operações de usuários na aplicação")
public interface UserControllerDocs {

    @Operation(
            summary = "Cria um novo usuário ",
            description = "Cria um novo usuário com os dados recebidos!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email já cadastrado no sistema!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> createUser(@RequestBody CreateUserRequest request) throws ConflictException, InternalServerException;

    @Operation(
            summary = "Atualiza os dados do usuário ",
            description = "Atualiza os dados do usuário pelo id passado!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados atualizados com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Usuário informado não encontrado !",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Erro ao processar a imagem!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
            }
    )
    public ResponseEntity<Object> updateProfile(@ModelAttribute UpdateUserRequest request) throws InternalServerException, BadRequestException, FileProcessingException;

    @Operation(
            summary = "Retorna os dados de um usuário",
            description = "Retorna os dados de um usuário pelo id passado!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado com sucesso!",
                            content = {@Content(schema = @Schema(implementation = UserResponse.class))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Usuário informado não encontrado !",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
            }
    )
    public ResponseEntity<Object> profile(@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException;

}
