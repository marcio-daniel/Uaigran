# Documentação Técnica e Funcional do Software Uaigran

## Resumo Funcional

O software Uaigran é uma aplicação de rede social desenvolvida utilizando o Spring Framework, que inclui funcionalidades como autenticação de usuários, operações CRUD (Create, Read, Update e Delete) em posts, comentários, amigos e curtidas. A aplicação usa AWS S3 para o armazenamento de arquivos e oferece uma configuração de segurança robusta usando o Spring Security. A integração com Swagger permite a documentação automática da API.

### Funcionalidades Principais:
- **Autenticação de Usuários:** Realiza autenticação gerando um token JWT para acesso.
- **Gerenciamento de Posts:** Os usuários podem criar, ler, atualizar e deletar posts.
- **Gerenciamento de Comentários:** Permite adicionar, atualizar listar e deletar comentários em posts.
- **Sistema de Amizades:** Facilita a adição, remoção e listagem de amigos.
- **Sistema de Curtidas:** Os usuários podem curtir e descurtir posts.
- **Upload de Arquivos:** Usa AWS S3 para upload e armazenamento de fotos.

### Público Alvo:
Esta documentação é direcionada a desenvolvedores que desejam entender a implementação técnica do software, contribuidores para o projeto, e qualquer um interessado em explorar ou estender as funcionalidades do sistema Uaigran.

---

## Descrição Técnica

### Estrutura de Arquivos

1. **`src/main/java/com/uaigran/UaigranApplication.java`**
   ```java
   @SpringBootApplication
   @EnableJpaRepositories("com.uaigran.models.repository")
   @ComponentScan(basePackages = { "com.uaigran" })
   @EntityScan("com.uaigran.models.entities")
   public class UaigranApplication {
       public static void main(String[] args) {
           SpringApplication.run(UaigranApplication.class, args);
       }
   }
   ```

2. **Configurações de AWS (`src/main/java/com/uaigran/config/AwsConfig.java`)**
   ```java
   @Configuration
   public class AwsConfig {
       @Bean
       public AmazonS3 amazonS3() {
           return AmazonS3ClientBuilder
               .standard()
               .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("uaigran", "uaigran")))
               .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://s3.localhost.localstack.cloud:4566", Regions.US_WEST_2.getName()))
               .build();
       }
   }
   ```

3. **Configurações de Segurança (`src/main/java/com/uaigran/config/SecurityConfigurations.java`)**
   ```java
   @Configuration
   @EnableWebSecurity
   public class SecurityConfigurations {
       @Autowired
       SecurityFilter securityFilter;

       private static final String[] AUTH_WHITELIST = {
           "/v3/api-docs/**",
           "/api/swagger-ui/**",
           "/api/swagger-ui.html"
       };

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
           return httpSecurity
               .cors(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(authorize -> authorize
                               .requestMatchers("/user/create").permitAll()
                               .requestMatchers("/authenticate").permitAll()
                               .requestMatchers(AUTH_WHITELIST).permitAll()
                               .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                               .anyRequest().authenticated()
                       )
               .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
       }

       @Bean
       public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
           return authenticationConfiguration.getAuthenticationManager();
       }

       @Bean
       public PasswordEncoder passwordEncoder() {
           return new BCryptPasswordEncoder();
       }

       @Bean
       CorsConfigurationSource corsConfigurationSource() {
           CorsConfiguration configuration = new CorsConfiguration();
           configuration.setAllowedOrigins(Arrays.asList("*"));
           configuration.setAllowedMethods(Arrays.asList("*"));
           configuration.setAllowedHeaders(Arrays.asList("*"));
           UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
           source.registerCorsConfiguration("/**", configuration);
           return source;
       }
   }
   ```

4. **Configuração do Swagger (`src/main/java/com/uaigran/config/SwaggerConfiguration.java`)**
   ```java
   @Configuration
   public class SwaggerConfiguration {
       @Bean
       public OpenAPI myOpenAPI() {
           Server devServer = new Server();
           devServer.setUrl("http://localhost:8082");
           devServer.setDescription("Server URL in Development environment");

           License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

           Info info = new Info()
                   .title("Uaigran API")
                   .version("1.0")
                   .description("Essa API tem o objetivo de realizar algumas das operações que ocorrem em uma rede social!")
                   .license(mitLicense);

           return new OpenAPI().info(info).servers(List.of(devServer));
       }
   }
   ```

5. **Controladores (`src/main/java/com/uaigran/controllers/`)**
   - **AuthenticationController.java**
   - **CommentController.java**
   - **FriendController.java**
   - **LikeController.java**
   - **PostController.java**
   - **UserController.java**

6. **Documentação de Interface Swagger (`src/main/java/com/uaigran/docs/swagger/`)**
   - **AuthenticationControllerDocs.java**
   - **CommentControllerDocs.java**
   - **FriendControllerDocs.java**
   - **LikeControllerDocs.java**
   - **PostControllerDocs.java**
   - **UserControllerDocs.java**

7. **Exceções (`src/main/java/com/uaigran/exceptions/`)**
   - **BadRequestException.java**
   - **ConflictException.java**
   - **ExceptionsHandler.java**
   - **FileProcessingException.java**
   - **InternalServerException.java**
   - **UnauthorizedException.java**

8. **Entidades e Repositórios (`src/main/java/com/uaigran/models/`)**
   - **entities**
     - **Comment.java**
     - **Friend.java**
     - **FriendPrimaryKey.java**
     - **Like.java**
     - **Post.java**
     - **User.java**
   - **repository**
     - **ICommentRepository.java**
     - **IFriendRepository.java**
     - **ILikeRepository.java**
     - **IPostRepository.java**
     - **IUserRepository.java**

9. **Serviços (`src/main/java/com/uaigran/models/services/`)**
   - **authentication**
     - **AuthenticationRequest.java**
     - **AuthenticationResponse.java**
     - **AuthenticationServices.java**
     - **IAuthenticationServices.java**
   - **comment**
     - **CommentResponse.java**
     - **CommentServices.java**
     - **CreateCommentRequest.java**
     - **ICommentServices.java**
     - **UpdateCommentRequest.java**
   - **fileUpload**
     - **AwsService.java**
     - **FileUploadService.java**
     - **IFileUploadService.java**
   - **friend**
     - **FriendRequest.java**
     - **FriendResponse.java**
     - **FriendServices.java**

### Inicialização e Configuração

- **Spring Boot:** Uaigran é uma aplicação Spring Boot, o que significa que pode ser inicializada facilmente utilizando a classe `UaigranApplication`.
- **Banco de Dados:** Utiliza Spring Data JPA para a interação com o banco de dados, incluindo repositórios para cada entidade.
- **AWS S3:** Configurado via `AwsConfig` para integração com localstack para testes locais.
- **Spring Security:** Confiável por `SecurityConfigurations` para gerenciar segurança, autenticação e autorização utilizando tokens JWT.
- **Swagger:** Configurado para oferecer documentação da API de forma fácil, ajudando desenvolvedores a entender e testar endpoints.

### Módulos de Serviço

- **Authentication Services:**
  - Implementa autenticação de usuários utilizando a interface `IAuthenticationServices` e classe `AuthenticationServices`.
  - Gera token JWT para os usuários autenticados.

- **Comment Services:**
  - Gerencia os comentários adicionados aos posts, incluindo criação, atualização, deleção e listagem.
  - Usa `CommentServices` para implementar as regras de negócio requeridas.

- **File Upload Services:**
  - Faz o upload das fotos dos usuários para a AWS S3.
  - Implementa funções de conversão de arquivos multipart e upload seguro.

- **Friend Services:**
  - Gerencia operações de amizade incluindo adição, remoção e listagem de amigos.
  - Usa `FriendServices` para encapsular a lógica de negocio de relacionamentos de amizade.

---

## Conclusão
A aplicação Uaigran foi construída com uma arquitetura sólida e escalável, utilizando tecnologias modernas e bem estabelecidas no ecossistema Java/Spring. Esta documentação fornece um roteiro para desenvolvedores que desejam entender cada componente e serviço dentro da aplicação, garantindo suporte a futuras extensões e colaborações. Com uma API bem documentada via Swagger, bem como um sistema de segurança robusto, a Uaigran está preparada para ser uma rede social funcional e segura para seus usuários.