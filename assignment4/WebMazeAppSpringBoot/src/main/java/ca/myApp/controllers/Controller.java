package ca.myApp.controllers;

/**
 * Controller to make get and post requests for spring boot application
 **/

import ca.myApp.model.MazeGame;
import ca.myApp.model.MoveDirection;
import ca.myApp.restapi.ApiBoardWrapper;
import ca.myApp.restapi.ApiGameWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    public static List<ApiGameWrapper> boards = new ArrayList<>();
    public static List<MazeGame> mazes = new ArrayList<>();

    @GetMapping("api/about")
    public String getName(){
        return "Arsh Mann";
    }

    @GetMapping("api/games")
    public List<ApiGameWrapper> getBoards(){
        return boards;
    }

    @PostMapping("api/games")
    public ResponseEntity<ApiGameWrapper> addMazeBoard(@RequestBody ApiGameWrapper board) {
        int boardIndex = boards.size()+1;
        board.setIsGameLost(false);
        board.setIsGameWon(false);
        board.setNumCheeseGoal(5);
        board.setNumCheeseFound(0);
        board.setGameNumber(boardIndex);

        MazeGame mazeGame = new MazeGame();
        mazes.add(mazeGame);
        boards.add(board);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    @GetMapping("api/games/{id}")
    public ApiGameWrapper getGame(@PathVariable int id){
        for(ApiGameWrapper board : boards){
            if(board.gameNumber==id)
                return board;
        }
        throw new IllegalArgumentException();
    }

    @GetMapping("api/games/{id}/board")
    public ApiBoardWrapper getBoard(@PathVariable int id){
        if(id-1 < boards.size()){
            ApiGameWrapper board = boards.get(id-1);
            ApiBoardWrapper maze = ApiBoardWrapper.makeFromGame(mazes.get(id-1));
            return maze;
        }
        throw new IllegalArgumentException();
    }

    @PostMapping("api/games/{id}/moves")
    public ResponseEntity<Object> moveItems(@PathVariable int id, @RequestBody String direction){
        List<String> validMoves = new ArrayList<>();
        validMoves.add("MOVE_UP");
        validMoves.add("MOVE_DOWN");
        validMoves.add("MOVE_LEFT");
        validMoves.add("MOVE_RIGHT");

        if ( !(id-1 < boards.size() && id-1>=0) )
            throw new IllegalArgumentException(); // 404
        if(!validMoves.contains(direction) && !direction.equals("MOVE_CATS"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400


        MazeGame mazeGame = mazes.get(id-1);
        ApiGameWrapper board = boards.get(id-1);
        if(direction.equals("MOVE_CATS")){
            mazeGame.doCatMoves();
        } else { // check for valid mouse move
            if(mazeGame.isValidPlayerMove(getMove(direction))){
                mazeGame.recordPlayerMove(getMove(direction));
                if(mazeGame.hasUserWon())
                    board.isGameWon = true;
            }
        }

        if(mazeGame.hasUserLost()){
            board.setIsGameLost(true);
            mazeGame.doCatMoves();
        }
        board.numCheeseFound = mazeGame.getNumberCheeseCollected();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // 202
    }
// POST /api/games/1/cheatstate
    @PostMapping("api/games/{id}/cheatstate")
    public ResponseEntity<Object> cheatState(@PathVariable int id, @RequestBody String option){
        if(! (id-1 < boards.size() && id-1 >= 0) )
            throw new IllegalArgumentException(); // 400

        ApiGameWrapper board = boards.get(id-1);
        MazeGame mazeGame = mazes.get(id-1);
        if(option.equals("1_CHEESE")){
            mazeGame.setNumberCheeseToCollect(1);
            board.setNumCheeseGoal(1);
        }
        else if(option.equals("SHOW_ALL")){
            mazeGame.makeVisible();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // 202
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND,
            reason = "Request Id not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler(){
    }

    private MoveDirection getMove(String direction) {
        switch (direction) {
            case "MOVE_UP" -> {
                return MoveDirection.MOVE_UP;
            }
            case "MOVE_RIGHT" -> {
                return MoveDirection.MOVE_RIGHT;
            }
            case "MOVE_LEFT" -> {
                return MoveDirection.MOVE_LEFT;
            }
            case "MOVE_DOWN" -> {
                return MoveDirection.MOVE_DOWN;
            }
        }
        throw new RuntimeException();
    }

}
