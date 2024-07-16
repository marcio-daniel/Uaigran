## Documentação do Software

### Resumo do Software

Este software é um serviço de redes sociais que permite aos usuários realizar autenticação, criar e gerenciar usuários, postar conteúdo, e interagir com esse conteúdo através de comentários, likes e amizades. O software é construído utilizando o framework Spring Boot e inclui funcionalidades de segurança, armazenamento de arquivos na AWS S3 e documentação de API com Swagger.

### Arquitetura Técnica

#### 1. Estrutura de Diretórios

- src/main/java/com/uaigran
  - UaigranApplication.java
  - config
    - AwsConfig.java
    - SecurityConfigurations.java
    - SwaggerConfiguration.java
  - controllers
    - AuthenticationController.java
    - CommentController.java
    - FriendController.java
    - LikeController.java
    - PostController.java
    - UserController.java
  - docs
    - erros
      - ErrorMessage.java
      - FieldValidationError.java
      - ValidationErro.java
    - swagger
      - AuthenticationControllerDocs.java
      - CommentControllerDocs.java
      - FriendControllerDocs.java
      - LikeControllerDocs.java
      - PostControllerDocs.java
      - UserControllerDocs.java
  - exceptions
    - BadRequestException.java
    - ConflictException.java
    - ExceptionsHandler.java
    - FileProcessingException.java
    - InternalServerException.java
    - UnauthorizedException.java
  - models
    - entities
      - Comment.java
      - Friend.java
      - FriendPrimaryKey.java
      - Like.java
      - Post.java
      - User
        - User.java
        - UserRoles.java
    - repository
      - ICommentRepository.java
      - IFriendRepository.java
      - ILikeRepository.java
      - IPostRepository.java
      - IUserRepository.java
    - services
      - authentication
        - AuthenticationRequest.java
        - AuthenticationResponse.java
        - AuthenticationServices.java
        - IAuthenticationServices.java
      - comment
        - CommentResponse.java
        - CommentServices.java
        - CreateCommentRequest.java
        - ICommentServices.java
        - UpdateCommentRequest.java
      - fileUpload
        - AwsService.java
        - FileUploadService.java
        - IFileUploadService.java
      - friend
        - FriendRequest.java
        - FriendResponse.java
        - FriendServices.java
 
### Descrição Técnica

#### 2. Aplicação Principal

**UaigranApplication.java**
```java
// Classe de entrada principal da aplicação Spring Boot
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

#### 3. Configurações

**AwsConfig.java**
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

**SecurityConfigurations.java**
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
                        .anyRequest().authenticated())
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

**SwaggerConfiguration.java**
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

#### 4. Controladores

**AuthenticationController.java**
```java
@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController implements AuthenticationControllerDocs {
    @Autowired
    private IAuthenticationServices _authenticateServices;

    @PostMapping()
    @Override
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) throws UnauthorizedException {
        return _authenticateServices.authenticate(request);
    }
}
```

**CommentController.java**
```java
@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController implements CommentControllerDocs {
    @Autowired
    private ICommentServices _commentServices;

    @PostMapping("/create")
    @Override
    public ResponseEntity<Object> createComment(@ModelAttribute CreateCommentRequest request) throws InternalServerException, BadRequestException {
        return _commentServices.createComment(request);
    }

    @PostMapping("/update")
    @Override
    public ResponseEntity<Object> updateComment(@RequestBody UpdateCommentRequest request) throws InternalServerException, BadRequestException {
        return _commentServices.updateComment(request);
    }

    @GetMapping("/list/{post_id}")
    @Override
    public ResponseEntity<Object> list(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException {
        return _commentServices.list(post_id);
    }

    @DeleteMapping("/delete/{comment_id}")
    @Override
    public ResponseEntity<Object> deleteComment(@PathVariable("comment_id") UUID comment_id) throws InternalServerException, BadRequestException {
        return _commentServices.deleteComment(comment_id);
    }
}
```

**FriendController.java**
```java
@RestController
@RequestMapping("/friend")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FriendController implements FriendControllerDocs {
    @Autowired
    private IFriendServices _friendServices;

    @PostMapping("/add")
    public ResponseEntity<Object> addFriend(@RequestBody FriendRequest request) throws ConflictException, InternalServerException, BadRequestException {
        return _friendServices.addFriend(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> removeFriend(@RequestBody FriendRequest request) throws InternalServerException, BadRequestException {
        return _friendServices.removeFriend(request);
    }

    @GetMapping("/list/{owner_id}")
    public  ResponseEntity<Object> friendList(@PathVariable("owner_id") UUID owner_id) throws InternalServerException, BadRequestException {
        return  _friendServices.findAllFriendsByOwner(owner_id);
    }

    @GetMapping("/profiles")
    public ResponseEntity<Object> profiles() throws InternalServerException, BadRequestException {
        return _friendServices.profiles();
    }
}
```

**LikeController.java**
```java
@RestController
@RequestMapping("/like")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LikeController implements LikeControllerDocs {
    @Autowired
    private ILikeServices _likeServices;

    @PostMapping("/create")
    public ResponseEntity<Object> createLike(@RequestBody CreateLikeRequest request) throws InternalServerException, BadRequestException {
        return _likeServices.createLike(request);
    }

    @GetMapping("/{post_id}/{user_id}")
    public ResponseEntity<Object> getLike(@PathVariable("post_id") UUID post_id,@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _likeServices.getLike(post_id,user_id);
    }

    @DeleteMapping("/delete/{like_id}")
    public ResponseEntity<Object> deleteLike(@PathVariable("like_id") UUID like_id) throws InternalServerException, BadRequestException {
        return _likeServices.deleteLike(like_id);
    }
}
```

**PostController.java**
```java
@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController implements PostControllerDocs {
    @Autowired
    private IPostServices _postServices;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@ModelAttribute CreatePostRequest request) throws InternalServerException, BadRequestException, FileProcessingException {
        return _postServices.createPost(request);
    }

    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<Object> delete(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException {
        return _postServices.deletePost(post_id);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@RequestBody UpdatePostRequest request) throws InternalServerException, BadRequestException {
        return _postServices.updatePost(request);
    }

    @GetMapping("/list/{user_id}")
    public ResponseEntity<Object> list(@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _postServices.list(user_id);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<Object> getPost(@PathVariable("post_id") UUID post_id) throws InternalServerException, BadRequestException {
        return _postServices.getPost(post_id);
    }

    @GetMapping("/feed")
    public ResponseEntity<Object> feed() throws InternalServerException {
        return _postServices.feed();
    }
}
```

**UserController.java**
```java
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController implements UserControllerDocs {
    @Autowired
    private IUserServices _userServices;

    @PostMapping("/create")
    @Override
    public ResponseEntity<Object> createUser(@RequestBody CreateUserRequest request) throws ConflictException, InternalServerException {
        return _userServices.createUser(request);
    }

    @PostMapping("/profile/update")
    @Override
    public ResponseEntity<Object> updateProfile(@ModelAttribute UpdateUserRequest request) throws InternalServerException, BadRequestException, FileProcessingException {
        return _userServices.updateProfile(request);
    }

    @GetMapping("/profile/{user_id}")
    @Override
    public ResponseEntity<Object> profile(@PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _userServices.profile(user_id);
    }
}
```

### Conclusão

O código do software é bem estruturado e organizado em camadas, seguindo boas práticas de desenvolvimento Java com Spring Boot. Ele encapsula as funcionalidades da aplicação em módulos separados e utiliza injeção de dependências para gerir serviços. Além disso, usa Swagger para documentar a API, garantindo que as interfaces sejam claras e acessíveis tanto para desenvolvedores quanto para usuários finais. As configurações da aplicação e de segurança estão bem definidas, e o uso de serviços AWS S3 para armazenamento de arquivos é uma adição eficaz para o gerenciamento de mídia na plataforma.

Esta documentação técnica fornece uma visão abrangente do software, que pode ser utilizada como base para desenvolvimento futuro, manutenção ou integração com outras aplicações e serviços.