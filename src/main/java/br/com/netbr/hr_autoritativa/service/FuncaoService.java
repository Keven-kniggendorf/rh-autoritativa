package br.com.netbr.hr_autoritativa.service;

import br.com.netbr.hr_autoritativa.domain.Funcao;
import br.com.netbr.hr_autoritativa.events.BeforeDeleteFuncao;
import br.com.netbr.hr_autoritativa.model.FuncaoDTO;
import br.com.netbr.hr_autoritativa.repos.FuncaoRepository;
import br.com.netbr.hr_autoritativa.util.CustomCollectors;
import br.com.netbr.hr_autoritativa.util.NotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class FuncaoService {

    private final FuncaoRepository funcaoRepository;
    private final ApplicationEventPublisher publisher;

    public FuncaoService(final FuncaoRepository funcaoRepository,
            final ApplicationEventPublisher publisher) {
        this.funcaoRepository = funcaoRepository;
        this.publisher = publisher;
    }

    public List<FuncaoDTO> findAll() {
        final List<Funcao> funcaos = funcaoRepository.findAll(Sort.by("id"));
        return funcaos.stream()
                .map(funcao -> mapToDTO(funcao, new FuncaoDTO()))
                .toList();
    }

    public FuncaoDTO get(final Long id) {
        return funcaoRepository.findById(id)
                .map(funcao -> mapToDTO(funcao, new FuncaoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final FuncaoDTO funcaoDTO) {
        final Funcao funcao = new Funcao();
        mapToEntity(funcaoDTO, funcao);
        return funcaoRepository.save(funcao).getId();
    }

    public void update(final Long id, final FuncaoDTO funcaoDTO) {
        final Funcao funcao = funcaoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(funcaoDTO, funcao);
        funcaoRepository.save(funcao);
    }

    public void delete(final Long id) {
        final Funcao funcao = funcaoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteFuncao(id));
        funcaoRepository.delete(funcao);
    }

    private FuncaoDTO mapToDTO(final Funcao funcao, final FuncaoDTO funcaoDTO) {
        funcaoDTO.setId(funcao.getId());
        funcaoDTO.setNomeFuncao(funcao.getNomeFuncao());
        funcaoDTO.setDepartamento(funcao.getDepartamento());
        return funcaoDTO;
    }

    private Funcao mapToEntity(final FuncaoDTO funcaoDTO, final Funcao funcao) {
        funcao.setNomeFuncao(funcaoDTO.getNomeFuncao());
        funcao.setDepartamento(funcaoDTO.getDepartamento());
        return funcao;
    }

    public Map<Long, String> getFuncaoValues() {
        return funcaoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Funcao::getId, Funcao::getNomeFuncao));
    }

}
