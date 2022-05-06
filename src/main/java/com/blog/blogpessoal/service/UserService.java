package com.blog.blogpessoal.service;


import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.blog.blogpessoal.model.User;
import com.blog.blogpessoal.model.UserLogin;
import com.blog.blogpessoal.repositories.UserRepository;

/*
 * A Classe UserService imprementa as regras de negócio do Recurso Usuario
 * 
 * 1- Usuario não pode estar duplicado no banco de dados
 * 2 - Senha do Usuario deve ser criptografada
 * 
 * Toda a implementação do metodos Cadastrar, Atualizar e Logar
 * estão na classe serviço, enquanto a Classe Controller se limitara a checar apenas a resposta da requisição
 * 
 */

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	/* 
	 * Cadastrar Usuario
	 * 
	 * Checa se o usuario ja existe no Banco de dados atraves do metodo findByUser,
	 * pois não pode existir 2 usuarios com o mesmo email.
	 * Se não existir retorna um Optional vazio.
	 * 
	 * isPresent() - Se um valor estiver presente retorna true, caso contrario retorna false
	 * 
	 * empty - Retorna uma instancia de Optional vazia 
	 */
	
	public Optional<User> userRegister(User user) {
		
		if(userRepository.findByUser(user.getUser()).isPresent()) {
			return Optional.empty();
		}
		
		
		/*
		 * Se o usuario não existir no banco, 
		 * a senha sera criptocrafada atravvés do metodo passwordEncrypt
		 */
		
		user.setPassword(passwordEncrypt(user.getPassword()));
		
		
		/**
		 * Assim como na Expressão Lambda, o resultado do método save será retornado dentro
		 * de um Optional, com o Usuario persistido no Banco de Dados.
		 * 
		 * of​ -> Retorna um Optional com o valor fornecido, mas o valor não pode ser nulo. 
		 * Se não tiver certeza de que o valor não é nulo use ofNullable.
		 */
		
		return Optional.of(userRepository.save(user));
				
	}
	
	
	/*
	 * Atualizar Usuario
	 * 
	 * Checa se o usuario ja existe no Banco atraves do metodo FindById,
	 * pois não é possivel atualizar 1 usuario inexistente
	 * Se não existir retorna um Optional Vazio
	 */
	
	public Optional<User> updateUser(User user) {
		
		if(userRepository.findById(user.getId()).isPresent()) {
			
			
			Optional<User> foundUser = userRepository.findByUser(user.getUser());
			
			/*
			 * Se o usuario existir no banco e o id do usuario encontrado for diferente
			 * do usuario id do usuario enviado na requisição, a atualização dos dados
			 * do usuario não pode ser realizada
			 * 
			 */
			
			if((foundUser.isPresent()) && (foundUser.get().getId() != user.getId())) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "User already exists!", null);
			}
			
			
			/**
		 	* Se o Usuário existir no Banco de Dados e o Id for o mesmo, a senha será criptografada
		 	* através do Método criptografarSenha.
		 	*/
			
			user.setPassword(passwordEncrypt(user.getPassword()));
			
			
			/**
		 	* Assim como na Expressão Lambda, o resultado do método save será retornado dentro
		 	* de um Optional, com o Usuario persistido no Banco de Dados ou um Optional vazio,
			* caso aconteça algum erro.
			* 
			* ofNullable​ -> Se um valor estiver presente, retorna um Optional com o valor, 
			* caso contrário, retorna um Optional vazio.
		 	*/
			return Optional.ofNullable(userRepository.save(user));
		}
		
		return Optional.empty();
	}
	
	
	/*
	 * A principal função do metodo autenticar, que é executado no endpoint de login,
	 * é gerar o token do usuario codigicado em Base64.
	 * O login propiciamente dito é executado pela BasicSecurityConfig em conjunto com as classes,
	 * UserDetailsService e UserDetails
	 */
	
	public Optional<UserLogin> login(Optional<UserLogin> userLogin) {
		
		
		/*
		 * Cria um objeto Optional do tipo Usuario para receber o resultado no metodo findByUser();
		 * 
		 * O metodo autenticar recebe como parametro um objeto da clase UsuarioLogin, ao inves do Usuario
		 * 
		 * 
		 * get() -> Se um valor estiver preset no objeto ele retorna o valor, caso contrario,
		 * lança uma Exception NoSuchElementeException.
		 * Então para user o get() é preciso ter certeza de que o Optional não esta vazio
		 * 
		 * O get() funciona como uma chave que abre o Objeto Optional e permite acessar os 
		 * metodos do objeto encapsulado
		 */

		Optional<User> user = userRepository.findByUser(userLogin.get().getUser());
		
		
		
		/*
		 * Verifica se o usuario existe
		 *
		 */

		if (user.isPresent()) {
			
			/**
			 *  Checa se a senha enviada, depois de criptografada, é igual a senha
			 *  gravada no Banco de Dados, através do Método compararSenhas.
			 * 
			 *  O Método Retorna verdadeiro se as senhas forem iguais, e falso caso contrário.
			 * 
			 */
			
			if (comparePassword(userLogin.get().getPassword(), user.get().getPassword())) {
				
				/*
				 * Se as senhas forem iguais atualiza o objeto userLogin com os dados recuperados do banco de dados,
				 * e insere o Token Guardado atraves do metodo de gerarToken
				 * 
				 * Desta forma, sera possivel exibir o nome e a foto no front
				 */
				
				userLogin.get().setId(user.get().getId());
				userLogin.get().setName(user.get().getName());
				userLogin.get().setPhoto(user.get().getPhoto());
				userLogin.get().setToken(generateToken(userLogin.get().getUser(),userLogin.get().getPassword()));
				userLogin.get().setPassword(user.get().getPassword());
				
				
				/*
				 * Retorna o objeto userLogin atualizado para a classe Controller.
				 * A Classe controladora verificara se deu tudo certo nesta operação e retornara o status
				 */
				
				return userLogin;
				
			}
		}
		
		
		/*
		 * empty - > Retorna uma instancia de Optional vazia, caso não seja encontrada
		 */

		return Optional.empty();
	}
	
	/*
	 * Metodo Criptografar Senhas
	 * 
	 * Instancia um objeto da Clase BCryptPasswordEncoder para criptografar a senha do usuario
	 * 
	 * O metodo encode retorna  asenha criptografada do formato BCrypt.
	 *  Para mais detalhes, consulte a documentação do BCryptPasswordEncoder.
	 * 
	 */
	
	private String passwordEncrypt(String password) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(password);
	}
	
	
	/*
	 * Metodo de Comparar Senhas
	 * 
	 * Verifica a senha enviada, depois de criptografada, 
	 * é igual a gravada no banco de dados.
	 * 
	 * Instancia um objeto da classe BcryptPasswordEncoder,
	 * para comparar a senha do usuario com a senha gravada no banco de dados
	 * 
	 * 
	 * matches -> verifica se a senha codificada obtida do banco conrresponde
	 * a senha enviada depois que ela também for codificada.
	 * Retorna verdadeiro se as senhas coincidem e false se não coincidem.
	 */
	
	private boolean comparePassword(String passwordRequest, String password) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 
		
		return encoder.matches(passwordRequest, password);
		
	}
	
	/*
	 * Metodo Gerar Token
	 * 
	 * A primeira linha, monta uma String (token) seguindo o padrão Basic, 
	 * atraves da concatenação de caracteres que sera codificada (nao criptografada)
	 * no formato Base64, atraves da Dependencia Apache Commons Codec.
	 * 
	 * Essa String tem o formato padrão: <username>:<password> que não pode ser alterado
	 * 
	 * Na segunda linha, faremos a codificação em Base64 da String
	 * 
	 * Observe que o vetor tokenBase64 é do tipo Byte para receber o resultado da codificação,
	 * porque durante o processo é necessário trabalhar diretamente com os bits (0 e 1) da String.
	 * 
	 * Base64.encodeBase64 -> aplica o algoritmo de codificação do código Decimal para Base64,
	 * que foi gerado no proximo metodo. Para mais detalhes, veja Codificação64 bits na documentação
	 * 
	 * Charset.forName("US-ASCII") -> Retorna o codigo ASCII (formato decimal)
	 * de cada caractere da String. Para mais detalhes, veja a Tabela ASCII na Documentação.
	 * 
	 * 
	 * Na Terceira linha, acrescenta a palavra Basic acompanhada de um espaço em branco(Obrigatorio)
	 * alem de converter o vetor de Bytes novamente em String e concatenar tudo em uma unica String.
	 * 
	 * O espaço depois da palavra Basic é obrigatorio. Caso não seja inserido, o Token não será reconhecido.
	 * 
	 * 
	 */
	
	private String generateToken(String user, String password) {
		
		String token = user+ ":" + password;
		byte[] encodeAuth = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		
		return "Basic " + new String(encodeAuth);
	}
}
	
