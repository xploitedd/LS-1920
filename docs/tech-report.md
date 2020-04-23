# Relatório Técnico do Projecto

## Introdução

Este documento contém os aspectos relevantes do desenho e implementação do projecto
de Laboratório de Software

## Modelação da base de dados

### Modelação conceptual ###

O seguinte diagrama apresenta a modelo entidade-associação para a informação gerida pelo sistema. 

![alt text](./Resources/ER.svg "Modelo ER")

Destacam-se os seguintes aspectos deste modelo:

- É necessária a existência de uma entidade fraca `DESCRIPTION` dado que as salas podem ou não ter 
descrição (*nullable*) e de modo a evitar a presença de valores *null* em `ROOM` optou-se pela criação
da nova entidade

O modelo conceptual apresenta ainda as seguintes restrições:

* ROOM:
    - o valor capacity não pode ser inferior a 1 
* BOOKING:
    - begin e end têm minutos multiplos de 10
    - o tempo entre begin e end tem de ser superior a 10 minutos

 
### Modelação física ###

O modelo físico da base de dados está presente em [ResetTables.sql](../src/main/resources/sql/ResetTables.sql) (apaga as tabelas para recriar se já estiverem lá) e [CreateTables.sql](../src/main/resources/sql/CreateTables.sql) (não faz nada se as tabelas já existirem).

## Organização do software

### Processamento de comandos

##### Interface com o utilizador

Para a interface com a consola foi criada uma nova classe `ConsoleApplication` que define como se obtém
o *input* do utilizador. Com o *input* obtido, esta nova classe passa o mesmo para ser processado a uma
instância de `AppProcessor`, previamente criado pela `App`.

A classe `AppProcessor` tem como objectivo processar os comandos, obtendo a sua resposta, mas também
tem como objectivo modificar o tipo de *output* do comando (nesta fase encontram-se disponíveis `TEXT/HTML` 
e `TEXT/PLAIN`), bem como modificar o destino do *output*, através dos *headers* passados ao *request*.

##### Handlers e Parâmetros de Request

Todos os *handlers* encontram-se no package `pt.isel.ls.handlers` sendo
que estes devem implementar a interface `RouteHandler`.
A interface `RouteHandler` contém um único método `execute` resposável
por executar o pedido e responder através de um `RouteResponse`.

Cada *handler* devolve um `HandlerResponse` que, nesta fase do trabalho,
contém um `View`. O `View` é o responsável por apresentar a informação
obtida no *request* ao utilizador no formato pretendido, sendo que para
tal existem vários tipos de `View` (e.g `TableView`, `MessageView`, ...).

Os parâmetros dos comandos são passados através da classe `RouteRequest`
que para além da informação sobre o request, como o `Path`, contém métodos
para obter parâmetros de *path* (e.g `/user/{uid}`, onde `uid` é o parâmetro) 
e parâmetros do *request* em sí (e.g `?name=Jose`).

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

##### Resolução de Headers

A resolução dos *headers* de um determinado *request* é feita de forma semelhante à resolução dos parâmetros
de um *request*. Contudo ambos os *headers* e os parâmetros são opcionais, pelo que se o utilizador passar um destes é
necessário averiguar se se tratam de *headers* ou parâmetros. Para isto a resolução é feita primeiro assumindo
que foram passados parâmetros e caso seja lançada uma exceção nesta resolução então assume-se que foram passados *headers*.

Os *headers* de cada *request* são guardados na classe associada `RouteRequest`, pelo que todos os troços de
execução com acesso a uma instância de `RouteRequest` poderão obter um determinado *header*. Os *headers* disponíveis
estão ainda especificados na classe `HeaderType`, sendo que se o utilizador não passar um *header* válido
uma exceção será lançada.

A resolução do valor dos *headers* `accept` e `file-name` é feita na classe `AppProcessor`, visto que é esta
classe que tem como objectivo redirecionar e modificar o tipo de *output*.

### Encaminhamento dos comandos

Para o `Router` poder encaminhar os comandos estes devem ser registados no ínicio
da aplicação através do método `registerRoute`. Este método recebe como
parâmetros: o Método do *Request*, o *Template* da `Route` e o *Handler* que contém
a lógica necessária aquando da chamada desta *route*.

Caso o `Router` encontre a *route* então o *handler* desta é retornado, caso
contrário um *handler* padrão (*handler* de 404) é passado ao *caller*.

##### Método do Request

- `EXIT` que termina a aplicação
- `GET` que tem como objectivo ir procurar dados à fonte de dados
- `POST` que tem como objectivo inserir novos dados na fonte de dados da aplicação
- `OPTION` que lista as *routes* disponíveis e a sua descrição
- `PUT` que altera um recurso
- `DELETE` que elimina um determinado recurso

##### Template da Route

Para representar um *template*, isto é, uma representação das regras que o *path*
deve seguir, existe uma classe `RouteTemplate`.

Esta classe é responsável por transformar uma `String` num novo *template* (método `of`) e
por avaliar se um determinado `Path` observa este *template* (método `match`).

Para transformar uma `String` num novo *template* é necessário primeiramente
dividir a *string* em vários segmentos (secções do `Path` delimitadas por `/`).
Com todos os segmentos é decidido se o segmento é constante `ConstantTemplateSegment` (o nome do segmento
deve corresponder sempre, e.g `users`) ou se este é variável `VariableTemplateSegment` (o nome do segmento pode variar
consoante o pedido, e.g `{uid}`). Foi removida a opção de poder conter segmentos variáveis opcionais pois tal
aumentava a complexidade do código presente nos *handlers*.

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
de dados, isto é, para cada relação da base de dados deve existir uma classe de *queries* que
operará sob esta relação.

Todas as classes de *queries* deve extender a classe abstracta `DatabaseQueries`. Esta classe abstracta
obriga a que cada momento de criação de uma nova classe deste tipo tenha de receber pelo constructor
uma instância de `Connection` (que será obtida através do uso do `ConnectionProvider` nos *handlers*).

### Processamento de erros

Todos os erros que ocorrentes durante o processamento de uma *route* são lançados através de exceções,
nomeadamente através de `RouteException` ou seus derivados. Estas exceções apenas devem ser tratadas
no `AppProcessor`, classe que decidirá qual é a melhor forma de apresentar os erros lançados.

### Domain Specific Language (DSL)

// TODO

## Avaliação crítica

Na fase 1 do projecto *bookings* que fossem criados no mesmo horário e na mesma sala eram considerados
como válidos, contudo este problema foi resolvido na fase 2 pelo que isto não é mais possível.

Nesta fase foram realizados todos os objectivos propostos pelo enunciado da mesma.

Pretendem-se ainda melhorar os seguintes aspectos:
- API de construção de *queries sql* (ver [JDBI Fluent API](http://jdbi.org/#_fluent_api) por exemplo)
- Adicionar outro tipo de exceções (derivadas ou não de `RouteException`)