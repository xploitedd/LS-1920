# Relatório técnico da Fase 1

## Introdução

Este documento contém os aspectos relevantes do desenho e implementação da fase 1 do projecto de LS.

## Modelação da base de dados

### Modelação conceptual ###

O seguinte diagrama apresenta a modelo entidade-associação para a informação gerida pelo sistema. 

![alt text](./Resources/ER.svg "Modelo ER")

Destacam-se os seguintes aspectos deste modelo:

* (_include a list of relevant design issues_)

O modelo conceptual apresenta ainda as seguintes restrições:

* ROOM:
    - o valor capacity não pode ser inferior a 1 
* BOOKING:
    - begin e end têm minutos multiplos de 10
    - o tempo entre begin e end tem de ser superior a 10 minutos

    
### Modelação física ###

O modelo físico da base de dados está presente em [ResetTables.sql](../src/main/resources/sql/ResetTables.sql) (apaga as tabelas para recriar se já estiverem lá) e [CreateTables.sql](../src/main/resources/sql/CreateTables.sql) (não faz nada se as tabelas já existirem).

Destacam-se os seguintes aspectos deste modelo:

* (_include a list of relevant design issues_)

## Organização do software

### Processamento de comandos

##### Interface com o utilizador

O *input* do utilizador na consola é tratado pela classe `App` no método
`startApp` que tem como objectivo processar o *request* que o utilizador
passou na linha de comandos (através de `RouteRequest`), encontrar a *route*
correspondente ao *request* através do `Router`, obter a `View` da resposta
do *handler* e apresentar essa `View` passando um determinado `PrintWriter`,
neste caso um `PrintWriter` com `System.out`.

Caso alguma exceção seja obtida nesta parte do programa e se esse tipo
de exceção não for resultado de uma causa interna então o stack trace da 
mesma será apresentado, de modo a que o utilizador final possa analizar/enviar
a causa externa.

##### Handlers e Parâmetros de Request

Todos os *handlers* encontram-se no package `pt.isel.ls.handlers` sendo
que estes devem implementar a interface `RouteHandler`.
A interface `RouteHandler` contém um único método `execute` resposável
por executar o pedido e responder através de um `RouteResponse`.

Cada *handler* devolve um `RouteResponse` que, nesta fase do trabalho,
contém um `View`. O `View` é o responsável por apresentar a informação
obtida no *request* ao utilizador no formato pretendido, sendo que para
tal existem vários tipos de `View` (e.g `TableView`, `MessageView`, ...).

Os parâmetros dos comandos são passados através da classe `RouteRequest`
que para além da informação sobre o request, como o `Path`, contém métodos
para obter parâmetros de *path* (e.g `/user/{uid}`, onde `uid` é o parâmetro) 
e parâmetros do *request* em sí (e.g `?name=Jose`). Adicionalmente,
os parâmetros podem ser opcionais ou obrigatórios, sendo que se forem
obrigatórios e não estiverem presentes é lançada uma exceção `ParameterNotFoundException`.

Enquanto que os parâmetros do *path* são processados durante o processo
de *routing*, os parâmetros do *request* são processados pela classe
`RouteRequest` na chamada do método `of`, que tem como objectivo criar
um novo `RouteRequest` através de uma `String` de *request* (e.g 
`POST /users ?name=a&email=a@b.c`).

Para processar os parâmetros do *request* é usado o método `RouteRequest.parseParameters`
que recebe a secção de parâmetros da `String` de *request*. Este método começa
por separar todos os parâmetros em secções *key-value* (secções separadas
por `&`), percorrendo de seguida
cada uma destas secções e adicionado a um `HashMap` a chave e o valor correspondente
a cada sub-secção *key-value* (que estão separados por `=`). O `HashMap` é posteriormente devolvido ao método
que chamou `parseParameters`. Caso ocorra alguma exceção durante este processamento
será lançada uma exceção `RouteException`.

### Encaminhamento dos comandos

Para o `Router` poder encaminhar os comandos estes devem ser registados no ínicio
da aplicação através do método `registerRoute`. Este método recebe como
parâmetros: o Método do *Request*, o *Template* da `Route` e o *Handler* que contém
a lógica necessária aquando da chamada desta *route*.

Caso o `Router` encontre a *route* então o *handler* desta é retornado, caso
contrário um *handler* padrão (*handler* de 404) é passado ao *caller*.

##### Método do Request

Nesta fase do trabalho apenas foram necessários 3 métodos:
- `EXIT` que termina a aplicação
- `GET` que tem como objectivo ir procurar dados à fonte de dados
- `POST` que tem como objectivo inserir novos dados na fonte de dados da aplicação

##### Template da Route

Para representar um *template*, isto é, uma representação das regras que o *path*
deve seguir, existe uma classe `RouteTemplate`.

Esta classe é responsável por transformar uma `String` num novo *template* (método `of`) e
por avaliar se um determinado `Path` observa este *template* (método `match`).

Para transformar uma `String` num novo *template* é necessário primeiramente
dividir a *string* em vários segmentos (secções do `Path` delimitadas por `/`).
Com todos os segmentos é decidido se o segmento é constante `ConstantTemplateSegment` (o nome do segmento
deve corresponder sempre, e.g `users`) ou se este é variável `VariableTemplateSegment` (o nome do segmento pode variar
consoante o pedido, e.g `{uid}`). Nos segmentos variáveis ainda há opção para
que um segmento variável seja opcional e para ser deste tipo é preciso cumprir o seguinte
contracto:

- Deve ser o último segmento
- Deve estar no formato `{uid}?`, onde `uid` é a chave do segmento e `?`
indica que o mesmo é opcional

A lista com todos os `TemplateSegment` é então passada ao constructor de `RouteTemplate`
e uma nova instância desta classe é devolvida ao *caller* de `of`.

No método `match` foi decidido realizar duas coisas ao mesmo tempo, de forma
a optimizar a performance do programa reduzindo as iterações:

- Verificar se o `Path` especifiado observa o *template*
- Obter os parâmetros variáveis com valores em `Path` e chaves presentes
no `TemplateSegment`.

Para analizar um possível `match` são percorridos todos os segmentos do *path*
ao mesmo tempo que são percorridos os segmentos do *template*, avaliando
um-a-um. Caso haja segmentos a menos (excepto opcionais), ou segmentos a mais, no `Path` então
o `match` retorna vazio. Caso contrário e um `match` ocorra, um mapa com os parâmetros de
`Path` é retornado.

É de notar que **todos os valores dos parâmetros** são encapsulados através da classe
`Parameter` que tem métodos para transformar estes valores (em `String`) noutros
tipos (`int` e `long`, por exemplo).

### Gestão de ligações

Para a gestão das ligações existe a classe `ConnectionProvider` que recebe no seu constructor a fonte de
dados. Todo o processamento transacional é tratado através do método `execute` desta classe que recebe
como parâmetro o bloco transacional a ser processado na forma da interface funcional `Provider`.

Para além do processamento transacional, a classe `ConnectionProvider` gere também o tempo de vida de
cada conexão à base de dados, sendo que este deve ser o mesmo que o tempo de vida de cada transação.

Caso algum *handler* necessite de realizar pedidos ao *data source* corrente
da aplicação, esse mesmo *handler* deverá conter um constructor público
no qual recebe uma classe do tipo `ConnectionProvider`. Cada método do *handler* poderá assim
realizar *queries* transacionais à fonte de dados presente no `ConnectionProvider`.

### Acesso a dados

Para realizar o acesso a dados (obter e inserir informação na fonte de dados) existem classes de
*queries* no package `pt.isel.ls.sql.queries`. Cada classe de *queries* representa uma parte do modelo
de dados, isto é, para cada modelo relacional da base de dados deve existir uma classe de *queries* que
operá sob o modelo.

Todas as classes de *queries* deve extender a classe abstracta `DatabaseQueries`. Esta classe abstracta
obriga a que cada momento de criação de uma nova classe deste tipo tenha de receber pelo constructor
uma instância de `Connection` (que será obtida através do uso do `ConnectionProvider` nos *handlers*).

### Processamento de erros

Todos os erros são lançados através de exceções e estas exceções lançadas durante o decorrer da aplicação
são todas tratadas na classe `App`. Caso a exceção tenha como fonte causas internas ou expectáveis então
uma `View` com a descrição do erro será apresentada ao utilizador. Caso a exceção tenha como origem a má
configuração do ambiente onde a aplicação está a correr, o *stack trace* da mesma será apresentado ao
utilizador para que este possa partilhar/resolver.

As exceções expectáveis pela aplicação deverão ser *wrapped* na classe `RouteException`, ou seja, a exceção
original não passa para o método chamador, mas sim a `RouteException` que contém informação sobre o erro.

## Avaliação crítica

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)
