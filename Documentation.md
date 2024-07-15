### Documentação do Software Uaigran

#### Sumário
Este documento contém a documentação técnica e funcional do software Uaigran, uma aplicação que simula funcionalidades de uma rede social. A seguir, você encontrará uma descrição detalhada do software, incluindo sua arquitetura, configuração, funcionalidades, e uma análise técnica dos principais componentes do código-fonte.

---

### 1. Resumo do Funcionamento do Software

A aplicação Uaigran é uma rede social que permite aos usuários realizar autenticação, criar, atualizar, listar e excluir posts, além de realizar operações relacionadas a comentários, curtidas e amizades entre usuários. A aplicação está estruturada com o uso do framework Spring Boot e integra-se com o Amazon S3 para armazenamento de arquivos. Além disso, utiliza APIs RESTful e segue boas práticas de segurança, utilizando JWT para autenticação.

---

### 2. Descrição Técnica de Todo o Código

#### 2.1 Arquitetura e Configurações

##### 2.1.1 `UaigranApplication.java`
O arquivo `UaigranApplication.java` é o ponto de entrada principal do software. A classe principal é anotada com:

- `@SpringBootApplication`: Define a aplicação Spring Boot.
- `@EnableJpaRepositories`: Habilita a inclusão dos repositórios JPA no pacote especificado.
- `@ComponentScan`: Define pacotes a serem escaneados para encontrar componentes Spring (controllers, services, etc.).
- `@EntityScan`: Define pacotes para escanear entidades JPA.

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

##### 2.1.2 `AwsConfig.java`
A classe `AwsConfig` configura a integração com o Amazon S3 utilizando credenciais estáticas e especifica a configuração do endpoint para o ambiente de desenvolvimento local.

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

##### 2.1.3 `SecurityConfigurations.java`
Configurando a segurança da aplicação utilizando Spring Security:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/create", "/authenticate", "/v3/api-docs/**", "/api/swagger-ui/**").permitAll()
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

##### 2.1.4 `SwaggerConfiguration.java`
Configuração para gerar documentações automáticas de API utilizando Swagger:

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

#### 2.2 Controllers

Os controllers são responsáveis por lidar com as requisições HTTP e mapear essas requisições para serviços correspondentes.

##### 2.2.1 `AuthenticationController.java`
Controlador responsável pela autenticação dos usuários.

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

##### 2.2.2 `CommentController.java`
Controlador para operações relacionadas a comentários.

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

##### 2.2.3 `FriendController.java`
Controlador para operações de amizades dos usuários.

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

##### 2.2.4 `LikeController.java`
Controlador para operações de curtidas dos posts.

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
    public ResponseEntity<Object> getLike(@PathVariable("post_id") UUID post_id, @PathVariable("user_id") UUID user_id) throws InternalServerException, BadRequestException {
        return _likeServices.getLike(post_id, user_id);
    }

    @DeleteMapping("/delete/{like_id}")
    public ResponseEntity<Object> deleteLike(@PathVariable("like_id") UUID like_id) throws InternalServerException, BadRequestException {
        return _likeServices.deleteLike(like_id);
    }
}
```

##### 2.2.5 `PostController.java`
Controlador para operações de posts.

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

##### 2.2.6 `UserController.java`
Controlador para operações de usuários.

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

#### 2.3 Models (Entidades)

As classes de entidade representam as tabelas do banco de dados e são anotadas com `@Entity`.

##### 2.3.1 `User.java`
Representa a entidade de usuário com todos os seus atributos e mapeamentos.

```java
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "email",unique = true,nullable = false)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "description")
    private String description;
    @Column(name = "photoUri")
    private String photoUri;
    @Column(name = "role",columnDefinition ="VARCHAR(100)")
    private UserRoles role;
    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Friend> friends;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Post> post_list;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> comment_list;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Like> like_list;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

##### 2.3.2 `Post.java`
Representa a entidade de post com todos os seus atributos e relações.

```java
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "post_id")
    private UUID post_id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "photoUri")
    private String photoUri;
    @Column(name = "post_date")
    private Date post_date;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Like> likes;
}
```

##### 2.3.3 `Comment.java`
Representa a entidade de comentário que está relacionada a um usuário e um post.

```java
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "comment_text")
    private String comment_text;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
```

##### 2.3.4 `Like.java`
Representa a entidade de curtida que está relacionada a um usuário e um post.

```java
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "likes")
public class Like {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
```

##### 2.3.5 `Friend.java`
Representa a entidade de amizade com uma chave composta.

```java
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friends")
public class Friend {
    @EmbeddedId
    private FriendPrimaryKey id;

    @ManyToOne
    @MapsId("owner")
    private User owner;

    @ManyToOne
    @MapsId("friend")
    private User friend;
}
```

##### 2.3.6 `FriendPrimaryKey.java`
Representa a chave composta da entidade `Friend`.

```java
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FriendPrimaryKey implements Serializable {
    private UUID owner;
    private UUID friend;
}
```

#### 2.4 Repositories

Os repositórios são responsáveis por todas as operações de persistência e recuperação de dados no banco de dados.

##### 2.4.1 `IUserRepository.java`
Repositório para a entidade `User`.

```java
@Repository
public interface IUserRepository extends JpaRepository<User,UUID> {
    UserDetails findByEmail(String email);
    @Override
    Optional<User> findById(UUID uuid);
}
```

##### 2.4.2 `IPostRepository.java`
Repositório para a entidade `Post`.

```java
@Repository
public interface IPostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUser(User user);
    @Override
    Optional<Post> findById(UUID uuid);
}
```

##### 2.4.3 `ICommentRepository.java`
Repositório para a entidade `Comment`.

```java
@Repository
public interface ICommentRepository extends JpaRepository<Comment, UUID> {
}
```

##### 2.4.4 `ILikeRepository.java`
Repositório para a entidade `Like`.

```java
@Repository
public interface ILikeRepository extends JpaRepository<Like, UUID> {
}
```

##### 2.4.5 `IFriendRepository.java`
Repositório para a entidade `Friend` com métodos personalizados para encontrar amigos pelo proprietário.

```java
@Repository
public interface IFriendRepository extends JpaRepository<Friend, FriendPrimaryKey> {
    @Override
    Optional<Friend> findById(FriendPrimaryKey friendPrimaryKey);
    List<Friend> findByOwner(User owner);
}
```

---

### 3. Conclusão

O software Uaigran é uma aplicação robusta que simula funcionalidades de uma rede social, permitindo a autenticação dos usuários, gerenciamento de posts, comentários, curtidas, amizades e integração com o Amazon S3 para armazenamento de arquivos. A aplicação é construída utilizando o framework Spring Boot e segue as melhores práticas de desenvolvimento, como o uso de JWT para autenticação, Swagger para documentação de APIs e Spring Data JPA para persistência de dados.

Esta documentação proporciona uma visão abrangente e detalhada dos principais componentes do software, incluindo seu funcionamento, configurações, controladores, modelos e repositórios.

