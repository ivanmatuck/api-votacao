package com.ivanmatuck.apivotacao.api;

import com.ivanmatuck.apivotacao.dto.VoteRequestDto;
import com.ivanmatuck.apivotacao.dto.VoteResponseDto;
import com.ivanmatuck.apivotacao.dto.VotacaoRequestDto;
import com.ivanmatuck.apivotacao.dto.VotacaoResponseDto;
import com.ivanmatuck.apivotacao.service.VotacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Api(value = "votacao")
@RequestMapping(value = "api/v1/votacao", produces = "application/json")
public class VotacaoController {

    private final VotacaoService votacaoService;

    @Autowired
    public VotacaoController(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    @ApiOperation(value="get all votacaos", response = VotacaoResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Votacaos found.")
    })
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(this.votacaoService.listVotacaos());
    }

    @ApiOperation(value="get one votacao", response = VotacaoResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Votacao found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return ResponseEntity.ok(this.votacaoService.getVotacao(id));
    }

    @ApiOperation(value="create one votacao", response = VotacaoResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Votacao successfully created.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody VotacaoRequestDto votacao) throws URISyntaxException {
        VotacaoResponseDto response = this.votacaoService.createVotacao(votacao);
        return ResponseEntity.created(new URI(response.getId())).body(response);
    }

    @ApiOperation(value="add a vote in a votacao", response = VotacaoResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Vote successfully added.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody VoteRequestDto vote) throws URISyntaxException {
        VoteResponseDto response = this.votacaoService.addVote(vote);
        return ResponseEntity.created(new URI(response.toString())).body(response);
    }

    @ApiOperation(value="get the votacao result", response = VotacaoResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Votacao result found.")
    })
    @GetMapping("/result/{id}")
    public ResponseEntity<?> getVotacaoResult(@PathVariable String id){
        return ResponseEntity.ok(this.votacaoService.getVotacaoResult(id));
    }
}
