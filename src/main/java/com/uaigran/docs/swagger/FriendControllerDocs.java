package com.uaigran.docs.swagger;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.friend.FriendRequest;
import com.uaigran.models.services.friend.FriendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Amigos", description = "Rotas que lidam com as operações de amizade na aplicação")
public interface FriendControllerDocs {

    @Operation(
            summary = "Adiciona um amigo",
            description = "Adiciona uma relação de amizade entre usuários!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Amizade criada com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais de um ou mais usuários inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Os usuários informados já são amigos!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um erro no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> addFriend(@RequestBody FriendRequest request) throws ConflictException, InternalServerException, BadRequestException;

    @Operation(
            summary = "Remove um amigo",
            description = "Remove uma relação de amizade entre usuários!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Amizade removida com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais de um ou mais usuários inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um erro no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> removeFriend(@RequestBody FriendRequest request) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Retorna uma lista de amigos",
            description = "Retorna uma lista de amigos de um usuário!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de amigos encontrada com sucesso!",
                            content = {@Content(array =@ArraySchema(schema = @Schema(implementation = FriendResponse.class)))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais de usuário inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um erro no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public  ResponseEntity<Object> friendList(@PathVariable("owner_id") UUID owner_id) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Retorna uma lista usuários",
            description = "Retorna uma lista usuários que podem ser adicionados como amigos!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de usuarios encontrada com sucesso!",
                            content = {@Content(array =@ArraySchema(schema = @Schema(implementation = FriendResponse.class)))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais de usuário inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um erro no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> profiles() throws InternalServerException, BadRequestException;
}
