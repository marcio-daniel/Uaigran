package com.uaigran.docs.swagger;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.FileProcessingException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.services.post.CreatePostRequest;
import com.uaigran.models.services.post.PostResponse;
import com.uaigran.models.services.post.UpdatePostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Posts", description = "Rotas que lidam com as operações dos posts na aplicação")
public interface PostControllerDocs {

    @Operation(
            summary = "Cria um novo post",
            description = "Cria um novo post com os dados informados!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post criado com sucesso!"
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
    public ResponseEntity<Object> create(@ModelAttribute CreatePostRequest request) throws InternalServerException, BadRequestException, FileProcessingException;

    @Operation(
            summary = "Exclui um post",
            description = "Exclui um post com os dados informados!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post excluido com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Post com os dados informados não encontrado !",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
            }
    )
    public ResponseEntity<Object> delete(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException;


    @Operation(
            summary = "Atualiza um post",
            description = "Atualiza um post com os dados informados!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post atualizado com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Post informado não encontrado !",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
            }
    )
    public ResponseEntity<Object> update(@RequestBody UpdatePostRequest request) throws InternalServerException, BadRequestException;

    @Operation(
            summary = "Gera uma lista de posts de um usuário",
            description = "Gera uma lista de posts de um usuário através do id do usuário!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de posts encontrada com sucesso!",
                            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))}

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
    public ResponseEntity<Object> list(@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException ;

    @Operation(
            summary = "Retorna os dados de um Post",
            description = "Retorna os dados de um Post através do id do post!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post encontrado com sucesso!",
                            content = {@Content(schema = @Schema(implementation = PostResponse.class))}

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Post informado não encontrado !",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
            }
    )
    public ResponseEntity<Object> getPost(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException ;


    @Operation(
            summary = "Gera uma lista de posts",
            description = "Gera uma lista de posts com todos os posts da aplicação!"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista dos posts encontrada com sucesso!",
                            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))}

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Houve um problema no servidor!",
                            content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}
                    ),
            }
    )
    public ResponseEntity<Object> feed() throws InternalServerException ;
}
