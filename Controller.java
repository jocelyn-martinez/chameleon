package com.mcfall.chameleon;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    Map<String, Game> games = new HashMap<String, Game>();

    private static record NewGameRequest(String hostPlayerName, int numberOfPlayers) {
    }

    private static record JoinGameRequest(String playerName) {
    }

    private static record JoinGameResponse(int numberOfPlayers, List<String> currentPlayers) {
    }

    private static record NextOrCurrentResponse(String clue, int number) {

    }

    @PostMapping(value="game")
    public ResponseEntity<String> newGame(
        @RequestBody NewGameRequest request
        ) {
        Game game = new Game(request.numberOfPlayers);
        game.addPlayers(request.hostPlayerName);
        games.put(game.getCode(), game);
        return new ResponseEntity<String>(game.getCode(), HttpStatus.OK);
    }

    @GetMapping(value="{gameCode}/players")
    public ResponseEntity<List<String>> getPlayers(@PathVariable String gameCode) {
        Game game = games.get(gameCode);
        return new ResponseEntity<List<String>>(game.getPlayers(), HttpStatus.OK);
    }

    @PostMapping(value="game/{gameCode}/players")
    public ResponseEntity<JoinGameResponse> addplayer(@PathVariable String gameCode, @RequestBody JoinGameRequest request) {
        Game game = games.get(gameCode);
        game.addPlayers(request.playerName);
        int number = game.getNumberofPlayers();
        List<String> players = game.getPlayers();
        JoinGameResponse response = new JoinGameResponse(number, players);
        return new ResponseEntity<JoinGameResponse>(response, HttpStatus.OK);
    }
    @DeleteMapping(value="{gameCode}/players/{name}")
    public void removePlayer(@PathVariable String gameCode, @PathVariable String name) {
        Game game = games.get(gameCode);
        game.removePlayer(name);
    }

    @PostMapping(value="game/{gameCode}/round/{name}") 
    //Previous URL: value="{gameCode}/{name}/next"
    public ResponseEntity<NextOrCurrentResponse> getNext(@PathVariable String gameCode, @PathVariable String name) {
        Game game = games.get(gameCode);
        game.getNext();
        String clue = clueOrChameleon(game, name);
        int round = game.round;
        NextOrCurrentResponse r = new NextOrCurrentResponse(clue, round);
        return new ResponseEntity<NextOrCurrentResponse>(r, HttpStatus.OK);
    }

    public String clueOrChameleon(Game game, String player) {
        if(player.equals(game.getChameleon())) return "chameleon";
        return game.getCurrent();
    }

    @GetMapping(value="game/{gameCode}/round/{name}")
    public ResponseEntity<NextOrCurrentResponse> getCurrent(@PathVariable String gameCode, @PathVariable String name) {
        Game game = games.get(gameCode);
        String clue = clueOrChameleon(game, name);
        int round = game.round;
        NextOrCurrentResponse r = new NextOrCurrentResponse(clue, round);
        return new ResponseEntity<NextOrCurrentResponse>(r, HttpStatus.OK);
    }

}
