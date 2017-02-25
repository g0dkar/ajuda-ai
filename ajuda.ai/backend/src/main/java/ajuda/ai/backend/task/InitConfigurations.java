package ajuda.ai.backend.task;

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

import ajuda.ai.model.billing.PaymentServiceEnum;
import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.model.extra.Page;
import ajuda.ai.model.institution.Institution;

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
		try {
			log.info("Verificando se há a AMA no sistema");
			entityManager.createQuery("FROM Institution WHERE slug = 'ama'").getSingleResult();
			log.info("Já tem. Finalizando tarefa.");
		} catch (final NoResultException nre) {
			log.info("Nope. Cadastrando...");
			final Institution ama = new Institution();
			ama.setName("AMA/PI - Associação de Amigos do Autista do Piauí");
			ama.setCreation(new CreationInfo());
			ama.getCreation().setCreator("00aa71f3-78b0-4355-88c8-0baee33cab86");
			ama.getCreation().setTime(new Date());
			ama.setDescription("## O QUE É A AMA/PI\n\nEntidade sem fins lucrativos, reconhecida de utilidade pública municipal - Lei nº 2.964/00, e utilidade pública estadual pela Lei nº 5.201/08/01, inscrita no Conselho Municipal dos Direitos da Criança e do Adolescente de Teresina (CMDCAT) nº 094/02.\n\n## COMO SURGIU A IDEIA\n\nSurgiu da necessidade dos pais em encontrar apoio e suporte técnico para educação e tratamento de seus filhos autistas. Foi fundada em 29 de Janeiro de 2000, por pais e amigos dos autistas residentes em Teresina-PI, que superando a desinformação diante do quadro de Autismo, reuniram-se para transformar questionamentos em ação somando forças para obter serviços estruturados nas áreas de saúde, educação especial, trabalho e assistência social.\n\n[Visite nosso site!](http://amigosautistas.blogspot.com.br)");
			ama.setPaymentService(PaymentServiceEnum.MOIP);
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
			entityManager.createQuery("FROM Page WHERE slug = 'ajude'").getSingleResult();
		} catch (final NoResultException nre) {
			final Page page = new Page();
			page.setSlug("ajude");
			page.setContent("Em desenvolvimento");
			page.setTitle("Ajude o Ajuda.Ai");
			page.setSubtitle("Bom, ainda estamos fazendo essa parte");
			page.setHeaderLine1("Obrigado por querer <strong>fazer a diferença</strong>");
			page.setHeaderLine2("Por enquanto, ainda estamos trabalhando!");
			page.setCreation(new CreationInfo());
			page.getCreation().setCreator("00aa71f3-78b0-4355-88c8-0baee33cab86");
			page.getCreation().setTime(new Date());
			page.setPublished(true);
			entityManager.persist(page);
		}
	}
}
