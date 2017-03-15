package ajuda.ai.backend.v1.tasks;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.user.User;

/**
 * Ao inicializar, cadastra a AMA. Temporário. Apenas para testes.
 * 
 * @author Rafael Lins
 *
 */
@Singleton
@Startup
public class InitConfigurations {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private Logger log;
	
	@Transactional
	@PostConstruct
	public void execute() {
		User rafael = null;
		
		try {
			log.info("Verificando se há o usuário 'rafael' no Sistema");
			rafael = (User) entityManager.createQuery("FROM User WHERE username = 'rafael'").getSingleResult();
			log.info("Já tem. Finalizando tarefa.");
		} catch (final NoResultException nre) {
			log.info("Nope. Cadastrando...");
			
			rafael = new User();
			rafael.setUsername("rafael");
			rafael.setPassword("rafael");
			rafael.setEmail("rafael@ajuda.ai");
			rafael.setFirstname("Rafael");
			rafael.setLastname("Lins");
			
			entityManager.persist(rafael);
			log.info("'rafael' foi cadastrado: ID {}", rafael.getId());
		}
		
		try {
			log.info("Verificando se há a AMA no sistema");
			
			entityManager.createQuery("FROM Institution WHERE slug = 'ama'").getSingleResult();
			
			log.info("Já tem. Finalizando tarefa.");
		} catch (final NoResultException nre) {
			log.info("Nope. Cadastrando...");
			
			final Institution ama = new Institution();
			ama.setName("AMA/PI - Associação de Amigos do Autista do Piauí");
			ama.setCreation(new CreationInfo());
			ama.getCreation().setCreator(rafael);
			ama.getCreation().setTime(new Date());
			ama.setDescription("## O QUE É A AMA/PI\n\nEntidade sem fins lucrativos, reconhecida de utilidade pública municipal - Lei nº 2.964/00, e utilidade pública estadual pela Lei nº 5.201/08/01, inscrita no Conselho Municipal dos Direitos da Criança e do Adolescente de Teresina (CMDCAT) nº 094/02.\n\n## COMO SURGIU A IDEIA\n\nSurgiu da necessidade dos pais em encontrar apoio e suporte técnico para educação e tratamento de seus filhos autistas. Foi fundada em 29 de Janeiro de 2000, por pais e amigos dos autistas residentes em Teresina-PI, que superando a desinformação diante do quadro de Autismo, reuniram-se para transformar questionamentos em ação somando forças para obter serviços estruturados nas áreas de saúde, educação especial, trabalho e assistência social.\n\n[Visite nosso site!](http://amigosautistas.blogspot.com.br)");
			ama.setPaymentService("moip");
			ama.setSlug("ama");
			ama.setLogo("https://ajuda.ai/res/img/logo-ama.jpg");
			ama.setBanner("http://4.bp.blogspot.com/-vCjZ1ZNuRJw/VmxGLPHls5I/AAAAAAAABWc/yBSGgUqnsmQ/s1600-r/banner_AMA1.jpg");
			ama.setAttributes(new HashMap<>());
			ama.getAttributes().put("cause_desc", "Atendimento Educacional Especializado; Acompanhamento de alunos incluídos na rede regular de ensino; Acompanhamento e orientação para as famílias (assistente social, pedagogos, psicólogos e etc); Palestras para famílias nas áreas da educação e saúde; Educação continuada para a equipe de profissionais; Defesa dos direitos constitucionais das pessoas com autismo; Assessoria pedagógica à escolas, faculdades, universidades; Atendimento em saúde, fisioterapia, fonoaudiologia e outros e muito mais");
			ama.getAttributes().put("contact", "A AMA possui equipes especializadas para a realização de cursos, oficinas e palestras. Interessados ligar para: **(86) 3216-3385** / **(86) 3221-4542**.\n\nResponsáveis por INFORMAÇÕES: Rozália Soares, Fátima Pinho, Poliana Cibelly e Luciana Luz");
			ama.getAttributes().put("website", "http://amigosautistas.blogspot.com.br");
			ama.getAttributes().put("facebook", "amapiaui");
			ama.getAttributes().put("moip_email", "rafael.lins777@gmail.com");
			ama.getAttributes().put("nationalId", "05.311.137/0001-80");
			
			entityManager.persist(ama);
			log.info("AMA foi cadastrada: ID {} = /{}", ama.getId(), ama.getSlug());
		}
		
		try {
			log.info("Verificando se há a APIPA no sistema");
			
			entityManager.createQuery("FROM Institution WHERE slug = 'apipa'").getSingleResult();
			
			log.info("Já tem. Finalizando tarefa.");
		} catch (final NoResultException nre) {
			log.info("Nope. Cadastrando...");
			
			final Institution apipa = new Institution();
			apipa.setName("APIPA® - ASSOCIAÇÃO PIAUIENSE DE PROTEÇÃO E AMOR AOS ANIMAIS");
			apipa.setCreation(new CreationInfo());
			apipa.getCreation().setCreator(rafael);
			apipa.getCreation().setTime(new Date());
			apipa.setDescription("## Quem somos\n\nA Associação Piauiense de Proteção e Amor aos Animais – APIPA, constituída em 10 de dezembro de 2007 e inscrita no CNPJ sob o Nº 10.216.609/0001-56, é uma pessoa jurídica de direito privado e sem fins lucrativos.\n\nUtilidade Pública Municipal: LEI 3.914, de 14 de setembro de 2009.\n\nUtilidade Pública Estadual: LEI 5.971, de 24 de fevereiro de 2010.\n\n## Finalidade\n\nPromover campanhas educativas visando a conscientização da sociedade quanto aos direitos dos animais. Colaborar com as entidades e órgãos oficiais competentes no sentido de aprimorar a legislação e anteprojetos, contribuindo para ampliação dos Direitos Universais dos Animais em harmonia com os seres humanos e com a natureza. Assistir, defender e proteger, por todos os meios legais, todos os animais.\n\n[Visite nosso site!](http://www.apipa10.org)");
			apipa.setPaymentService("moip");
			apipa.setSlug("apipa");
			apipa.setLogo("https://ajuda.ai/res/img/logo-ama.jpg");
			apipa.setBanner("http://www.apipa10.org/images/uniterevolution/slidehome2/images/apipa/slide-show/slide-show7c.jpg");
			apipa.setAttributes(new HashMap<>());
			apipa.getAttributes().put("cause_desc", "asdasd");
			apipa.getAttributes().put("contact", "Aqui, nós estamos abertos ao diálogo. Use o [nosso formulário de contato](http://www.apipa10.org/apipa-forms/contato.php) para nos enviar a sua mensagem.\n\nNós teremos prazer em dialogar com você!\n\n[ABRIR FORMULÁRIO DE CONTATO](http://www.apipa10.org/apipa-forms/contato.php)\n\nE-mail: **(apipa10@apipa10.org)**\n\nTelefone: **(86) 98846-8020**");
			apipa.getAttributes().put("website", "http://www.apipa10.org");
			apipa.getAttributes().put("facebook", "apipa.piaui");
			apipa.getAttributes().put("moip_email", "rafael.lins777@gmail.com");
			apipa.getAttributes().put("nationalId", "10.216.609/0001-56");
			
			entityManager.persist(apipa);
			log.info("APIPA foi cadastrada: ID {} = /{}", apipa.getId(), apipa.getSlug());
		}
	}
}
