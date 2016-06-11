package ajuda.ai.website.task;

import java.util.Date;

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
import ajuda.ai.model.institution.Institution;

/**
 * Ao inicializar, cadastra a AMA. Temporário. Apenas para testes.
 * 
 * @author Rafael Lins
 *
 */
@Singleton
@Startup
public class TempCadastrarAMA {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private Logger log;
	
	@Transactional
	@PostConstruct
	public void execute() {
		log.info("Verificando se há a AMA no sistema");
		Institution ama;
		
		try {
			ama = (Institution) entityManager.createQuery("FROM Institution WHERE slug = 'ama'").getSingleResult();
		} catch (final NoResultException nre) {
			ama = null;
		}
		
		if (ama == null) {
			log.info("Nope. Cadastrando...");
			ama = new Institution();
			ama.setName("AMA/PI - Associação de Amigos do Autista do Piauí");
			ama.setCreation(new CreationInfo());
			ama.getCreation().setCreator("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
			ama.getCreation().setTime(new Date());
			ama.setDescription("## O QUE É A AMA/PI\n\nEntidade sem fins lucrativos, reconhecida de utilidade pública municipal - Lei nº 2.964/00, e utilidade pública estadual pela Lei nº 5.201/08/01, inscrita no Conselho Municipal dos Direitos da Criança e do Adolescente de Teresina (CMDCAT) nº 094/02.\n\n## COMO SURGIU A IDEIA\n\nSurgiu da necessidade dos pais em encontrar apoio e suporte técnico para educação e tratamento de seus filhos autistas. Foi fundada em 29 de Janeiro de 2000, por pais e amigos dos autistas residentes em Teresina-PI, que superando a desinformação diante do quadro de Autismo, reuniram-se para transformar questionamentos em ação somando forças para obter serviços estruturados nas áreas de saúde, educação especial, trabalho e assistência social.\n\n[Visite nosso site!](http://amigosautistas.blogspot.com.br)");
			ama.setPaymentService(PaymentServiceEnum.PAG_SEGURO);
			ama.setPaymentServiceData("{\"email\":\"rafael.lins777@gmail.com\",\"token\":\"9FCFC504381E4033A3F01466ACC13203\"}");
			ama.setSlug("ama");
			
			entityManager.persist(ama);
			log.info("AMA foi cadastrada: ID {} = /{}", ama.getId(), ama.getSlug());
		}
		else {
			log.info("Já tem. Finalizando tarefa.");
		}
	}
}
