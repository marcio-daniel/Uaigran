# Documentação do Software Uaigran

## Resumo do Software
O software Uaigran é uma aplicação para gerenciar uma rede social, fornecendo meios para autenticação de usuários, criação e manuseio de postagens, comentários, likes e relações de amizade. A aplicação utiliza o framework Spring Boot e integra serviços como AWS S3 para armazenamento de arquivos, além de configurações de segurança com autenticação JWT.

## Documentação Técnica

### Arquitetura e Configurações
#### [src/main/java/com/uaigran/UaigranApplication.java](src/main/java/com/uaigran/UaigranApplication.java)
O ponto de entrada da aplicação Spring Boot que configura a aplicação com os seguintes componentes:
- `@SpringBootApplication`: Marca a classe como uma aplicação Spring Boot.
- `@EnableJpaRepositories`: Habilita a criação de repositórios JPA.
- `@EntityScan`: Habilita a varredura de entidades do pacote especificado.
- `@ComponentScan`: Define o pacote base para a varredura dos componentes.

#### [src/main/java/com/uaigran/config/AwsConfig.java](src/main/java/com/uaigran/config/AwsConfig.java)
Configuração da integração com AWS S3:
- `amazonS3()`: Configura o cliente S3 com credenciais e endpoint locais para desenvolvimento.

#### [src/main/java/com/uaigran/config/SecurityConfigurations.java](src/main/java/com/uaigran/config/SecurityConfigurations.java)
Configuração de segurança com Spring Security:
- Define endpoints públicos e protegidos, configura CORS, e define um filtro de autenticação JWT.

#### [src/main/java/com/uaigran/config/SwaggerConfiguration.java](src/main/java/com/uaigran/config/SwaggerConfiguration.java)
Configuração do Swagger para documentação da API:
- Configurações básicas do OpenAPI, incluindo a definição do servidor de desenvolvimento e informações da API.

### Controladores REST
Os controladores são responsáveis por gerenciar as requisições HTTP e são estruturados com endpoints específicos para operações CRUD de diferentes entidades da aplicação. A seguir são listados os principais controladores:

#### [src/main/java/com/uaigran/controllers/AuthenticationController.java](src/main/java/com/uaigran/controllers/AuthenticationController.java)
- Operações:
  - `POST /authenticate`: Autentica o usuário e retorna um token JWT.

#### [src/main/java/com/uaigran/controllers/CommentController.java](src/main/java/com/uaigran/controllers/CommentController.java)
- Operações:
  - `POST /comment/create`: Cria um comentário.
  - `POST /comment/update`: Atualiza um comentário.
  - `GET /comment/list/{post_id}`: Lista os comentários de um post.
  - `DELETE /comment/delete/{comment_id}`: Exclui um comentário.

#### [src/main/java/com/uaigran/controllers/FriendController.java](src/main/java/com/uaigran/controllers/FriendController.java)
- Operações:
  - `POST /friend/add`: Adiciona um amigo.
  - `DELETE /friend/delete`: Remove um amigo.
  - `GET /friend/list/{owner_id}`: Lista os amigos de um usuário.
  - `GET /friend/profiles`: Lista perfis de possíveis amigos.

#### [src/main/java/com/uaigran/controllers/LikeController.java](src/main/java/com/uaigran/controllers/LikeController.java)
- Operações:
  - `POST /like/create`: Cria uma curtida.
  - `GET /like/{post_id}/{user_id}`: Obtém os dados da curtida.
  - `DELETE /like/delete/{like_id}`: Deleta uma curtida.

#### [src/main/java/com/uaigran/controllers/PostController.java](src/main/java/com/uaigran/controllers/PostController.java)
- Operações:
  - `POST /post/create`: Cria uma postagem.
  - `DELETE /post/delete/{post_id}`: Deleta uma postagem.
  - `POST /post/update`: Atualiza uma postagem.
  - `GET /post/list/{user_id}`: Lista postagens de um usuário.
  - `GET /post/{post_id}`: Obtém os dados de uma postagem.
  - `GET /post/feed`: Obtém o feed de postagens.

#### [src/main/java/com/uaigran/controllers/UserController.java](src/main/java/com/uaigran/controllers/UserController.java)
- Operações:
  - `POST /user/create`: Cria um usuário.
  - `POST /user/profile/update`: Atualiza o perfil de um usuário.
  - `GET /user/profile/{user_id}`: Obtém os dados do perfil de um usuário.

### Repositórios
São interfaces responsáveis por gerenciar a persistência de dados. Utilizam Spring Data JPA para simplificar o acesso ao banco de dados:

- [ICommentRepository.java](src/main/java/com/uaigran/models/repository/ICommentRepository.java): Repositório para comentários.
- [IFriendRepository.java](src/main/java/com/uaigran/models/repository/IFriendRepository.java): Repositório para amizades.
- [ILikeRepository.java](src/main/java/com/uaigran/models/repository/ILikeRepository.java): Repositório para curtidas.
- [IPostRepository.java](src/main/java/com/uaigran/models/repository/IPostRepository.java): Repositório para postagens.
- [IUserRepository.java](src/main/java/com/uaigran/models/repository/IUserRepository.java): Repositório para usuários.

### Serviços
Os serviços encapsulam a lógica de negócios e a interação com os repositórios:

- [AuthenticationServices.java](src/main/java/com/uaigran/models/services/authentication/AuthenticationServices.java): Serviço de autenticação.
- [CommentServices.java](src/main/java/com/uaigran/models/services/comment/CommentServices.java): Serviço de comentários.
- [FileUploadService.java](src/main/java/com/uaigran/models/services/fileUpload/FileUploadService.java): Serviço de upload de arquivos.
- [FriendServices.java](src/main/java/com/uaigran/models/services/friend/FriendServices.java): Serviço de amizades.

### Exceções Personalizadas
As exceções personalizadas gerenciam diferentes tipos de erros que podem ocorrer na aplicação:

- [BadRequestException.java](src/main/java/com/uaigran/exceptions/BadRequestException.java): Exceção para erros de requisição inválida.
- [ConflictException.java](src/main/java/com/uaigran/exceptions/ConflictException.java): Exceção para erros de conflito.
- [InternalServerException.java](src/main/java/com/uaigran/exceptions/InternalServerException.java): Exceção para erros internos do servidor.
- [UnauthorizedException.java](src/main/java/com/uaigran/exceptions/UnauthorizedException.java): Exceção para erros de autenticação.

### Entidades
Representam as tabelas do banco de dados e possuem os seguintes mapeamentos JPA:

- [Comment.java](src/main/java/com/uaigran/models/entities/Comment.java): Entidade para comentários.
- [Friend.java](src/main/java/com/uaigran/models/entities/Friend.java): Entidade para amizades.
- [Like.java](src/main/java/com/uaigran/models/entities/Like.java): Entidade para curtidas.
- [Post.java](src/main/java/com/uaigran/models/entities/Post.java): Entidade para postagens.
- [User.java](src/main/java/com/uaigran/models/entities/User/User.java): Entidade para usuários.

## Conclusão
O software Uaigran fornece uma estrutura robusta para a implementação de funcionalidades típicas de redes sociais, com uma arquitetura bem definida e configurada utilizando as melhores práticas do Spring Boot. A documentação detalhada assegura a compreensão completa do funcionamento interno da aplicação, bem como facilita a implementação e o suporte contínuo.