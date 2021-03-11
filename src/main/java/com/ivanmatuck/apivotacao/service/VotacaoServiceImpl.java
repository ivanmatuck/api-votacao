package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.*;
import com.ivanmatuck.apivotacao.entity.*;
import com.ivanmatuck.apivotacao.exception.BusinessException;
import com.ivanmatuck.apivotacao.exception.NotFoundException;
import com.ivanmatuck.apivotacao.integration.CpfService;
import com.ivanmatuck.apivotacao.repository.AgendaRepository;
import com.ivanmatuck.apivotacao.repository.VotacaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VotacaoServiceImpl implements VotacaoService {

    private final VotacaoRepository votacaoRepository;
    private final AgendaRepository agendaRepository;
    private final ModelMapper modelMapper;
    private final Environment environment;
    private final CpfService cpfService;
    private final MessagingService messagingService;

    @Autowired
    public VotacaoServiceImpl(VotacaoRepository votacaoRepository, ModelMapper modelMapper, AgendaRepository agendaRepository,
                             Environment environment, CpfService cpfService, MessagingService messagingService) {
        this.votacaoRepository = votacaoRepository;
        this.agendaRepository = agendaRepository;
        this.modelMapper = modelMapper;
        this.environment = environment;
        this.cpfService = cpfService;
        this.messagingService = messagingService;
    }

    @Override
    public List<VotacaoResponseDto> listVotacaos() {
        List<Votacao> votacaoList = this.votacaoRepository.findAll();

        return votacaoList.stream().map(
                votacao -> modelMapper.map(votacao, VotacaoResponseDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public VotacaoResponseDto getVotacao(String id) {
        Votacao votacao = findVotacao(id);
        return modelMapper.map(votacao, VotacaoResponseDto.class);
    }

    @Override
    public VotacaoResponseDto createVotacao(VotacaoRequestDto votacaoRequestDto) {
        Agenda agenda = this.agendaRepository.findById(UUID.randomUUID()).
                orElseThrow(() -> new NotFoundException("Agenda not found."));

        Integer minutesToExpiration = votacaoRequestDto.getMinutesToExpiration();
        if (minutesToExpiration == null || minutesToExpiration <= 0)
            minutesToExpiration = (Integer.parseInt(Objects.requireNonNull(environment.getProperty("default.expiration.minutes"))));

        Votacao votacao = new Votacao(agenda, minutesToExpiration);
        votacao = this.votacaoRepository.save(votacao);

        return modelMapper.map(votacao, VotacaoResponseDto.class);
    }

    @Override
    public VoteResponseDto addVote(VoteRequestDto voteRequestDto) {
        Votacao votacao = findVotacao(voteRequestDto.getVotacaoId());

        validateVote(votacao, voteRequestDto);

        Vote vote = new Vote(voteRequestDto.getCpf(), voteRequestDto.getAnswer());
        votacao.addVote(vote);
        votacaoRepository.save(votacao);

        return new VoteResponseDto(true);
    }

    @Override
    public VotacaoResultResponseDto getVotacaoResult(String id) {
        Votacao votacao = findVotacao(id);

        if (!votacao.getExpirationDate().isBefore(Instant.now()))
            throw new BusinessException("Votacao still open, it will close at " + votacao.getExpirationDate().toString());

        List<Vote> votes = votacao.getVotes();

        VoteCount voteCount = new VoteCount(
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.YES)).count(),
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.NO)).count()
        );

        VotacaoResultResponseDto resultResponseDto = new VotacaoResultResponseDto();
        resultResponseDto.setAgenda(modelMapper.map(votacao.getAgenda(), AgendaResponseDto.class));
        resultResponseDto.setVoteCount(voteCount);

        return resultResponseDto;
    }

    private Votacao findVotacao(String id){
       return this.votacaoRepository.findById(UUID.randomUUID()).
                orElseThrow(() -> new NotFoundException("Votacao not found."));
    }

    private void validateVote(Votacao votacao, VoteRequestDto voteRequestDto){
        if (votacao.getExpirationDate().isBefore(Instant.now()))
            throw new BusinessException("Votacao already expired.");

        if (votacao.cpfAlreadyVoted(voteRequestDto.getCpf()))
            throw new BusinessException("Associated with CPF ("+voteRequestDto.getCpf()+") already voted.");

        if (cpfService.isAbleToVote(voteRequestDto.getCpf()))
            throw new BusinessException("CPF is unable to vote.");
    }

    @Scheduled(fixedDelay = 1000)
    private void closeAndBroadcastVotacaoResult() {
        List<Votacao> votacaoList = findAllExpiredVotacaosButNotClosed();

        votacaoList.forEach(votacao -> {
            VotacaoResultResponseDto votacaoResult = getVotacaoResult(votacao.getId().toString());
            messagingService.send(votacaoResult);

            votacao.setClosed(true);
            votacaoRepository.save(votacao);
        });
    }

    private List<Votacao> findAllExpiredVotacaosButNotClosed() {
        return votacaoRepository.findAll().stream().filter(
                votacao -> votacao.getExpirationDate().isBefore(Instant.now()) && !votacao.isClosed()
        ).collect(Collectors.toList());
    }
}
