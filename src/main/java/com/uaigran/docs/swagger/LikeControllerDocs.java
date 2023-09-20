package com.uaigran.docs.swagger;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.like.CreateLikeRequest;
import com.uaigran.models.services.like.LikeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Curtidas", description = "Rotas que lidam com as operações de curtidas de posts na aplicação")
public interface LikeControllerDocs {

    @Operation(
            summary = "Cria um nova curtida",
            description = "Cria um nova curtida em um post especifico!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Curtida criada com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais de usuário ou post inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> createLike(@RequestBody CreateLikeRequest request) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Retorna os dados da curtida",
            description = "Retorna os dados da curtida do usário no post especifico!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Curtida encontrada com sucesso!",
                            content = {@Content(schema = @Schema(implementation = LikeResponse.class))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais de usuário ou post inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> getLike(@PathVariable("post_id") UUID post_id, @PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Deleta os dados da curtida",
            description = "Deleta os dados da curtida do usário no post especifico!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Curtida excluida com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais da curtida inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> deleteLike(@PathVariable("like_id") UUID like_id) throws InternalServerException, BadRequestException;
}
