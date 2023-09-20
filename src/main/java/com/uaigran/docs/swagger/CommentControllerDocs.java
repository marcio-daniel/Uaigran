package com.uaigran.docs.swagger;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.comment.CommentResponse;
import com.uaigran.models.services.comment.CreateCommentRequest;
import com.uaigran.models.services.comment.UpdateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Comentários", description = "Rotas que lidam com as operações dos comentários dos posts")
public interface CommentControllerDocs {

    @Operation(
            summary = "Cria um novo comentário ",
            description = "Cria um novo comentário em um post especifico!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Comentário criado com sucesso!"
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
    public ResponseEntity<Object> createComment(@ModelAttribute CreateCommentRequest request) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Atualiza um comentário",
            description = "Atualiza um comentário com os dados passado!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comentário atualizado com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais do comentário inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> updateComment(@RequestBody UpdateCommentRequest request) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Lista os comentários",
            description = "Lista os comentários de um post pelo id do post!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comentários encontrado com sucesso!",
                            content = {@Content(array =@ArraySchema(schema = @Schema(implementation = CommentResponse.class)))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais do post inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> list(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Exclui um comentário",
            description = "Exclui um comentário de um post pelo id do comentário!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comentário excluido com sucesso!",
                            content = {@Content(schema = @Schema(implementation = CommentResponse.class))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciais do comentário inválidas!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    )
            }
    )
    public ResponseEntity<Object> deleteComment(@PathVariable("comment_id") UUID comment_id) throws InternalServerException, BadRequestException;
}
