package Model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.Point;


import DataAccess.BoardDataAccess;
import DataAccess.DictionaryDataAccess;
import DataAccess.GameDataAccess;
import DataAccess.GameTypeDataAccess;
import DataAccess.PlayerDataAccess;
import DataObject.BoardDataObject;
import DataObject.GameDataObject;
import DataObject.PlayerDataObject;
import DomainObject.BoardDomainObject;
import DomainObject.GameDomainObject;
import DomainObject.PlayerDomainObject;
import DomainObject.SpaceDomainObject;
import restService.Message;


public class GameModel {
	

	public static ArrayList<GameDomainObject> GetAllGameSummaries(Message message) {
		ArrayList<GameDataObject> gameDataList = GameDataAccess.getAllGames();
		ArrayList<GameDomainObject> gameDomainList = GameDomainObject.MapList(gameDataList);
		return gameDomainList;
	}

	public static GameDomainObject GetGameDetailsByGameIdandPlayerId(Message message, int gameId, int playerId) {
		//Return game details
		// if the game is setup - return the board and the current players words, and current score
		// if the game is complete - return both words, and the scores
	

		//Get the Game Details
		GameDataObject gameData = GameDataAccess.getGameById(gameId);
		GameDomainObject gameDomain = new GameDomainObject(gameData);

		boolean isPlayer1 = false;
		boolean isPlayer2 = false;
		
		if (gameDomain.player1Id == playerId) {
			isPlayer1 = true;
		} else if (gameDomain.player2Id == playerId) {
			isPlayer2 = true;
		} else {
            message.addErrorMessage("Player " + playerId + " is not part of this game");
            return null;
		}


		BoardDomainObject boardDomain = BoardModel.GetBoardDetailsByGameId(message, gameDomain.id);
		gameDomain.board = boardDomain;
        //if (boardDomain != null) {
        //    if (isPlayer1) {
        //        gameDomain.board.p2Tiles = "";
        //    } else if (isPlayer2) {
        //        gameDomain.board.p1Tiles = "";
        //    }
        //}

    	return gameDomain;
	}


	public static GameDomainObject GetGameById(Message message, int id) {
		GameDataObject gameData = GameDataAccess.getGameById(id);
		GameDomainObject gameDomain = new GameDomainObject(gameData);
		return gameDomain;
	}


	public static boolean ValidateGameById(int id) {
		if (GameDataAccess.getGameById(id) == null)
			return false;
		return true;
	}

	public static GameDomainObject CreateGame(Message message, GameDomainObject domainGameToCreate) {
		if (PlayerModel.ValidatePlayerById(domainGameToCreate.player1Id) == false) {
			message.addErrorMessage("Player1Id does not exist.");
		}

		if (PlayerModel.ValidatePlayerById(domainGameToCreate.player2Id) == false) {
			message.addErrorMessage("Player2Id does not exist.");
		}

		if (ValidateGameById(domainGameToCreate.gameTypeId) == false) {
			message.addErrorMessage("GameTypeId does not exist.");
		}
		
		GameDataObject gamedata = new GameDataObject(domainGameToCreate.id, domainGameToCreate.gameTypeId, domainGameToCreate.player1Id, -1, "Playing", domainGameToCreate.player1Id, domainGameToCreate.player2Id, 0, 0);
		GameDataAccess.createGame(gamedata);
		GameDomainObject domainObject = new GameDomainObject(gamedata);
		
        return domainObject;
	}

	public static GameDomainObject PlayWord(Message message, int gameId, int playerId, ArrayList<SpaceDomainObject> spaces ) {
		
		GameDataObject gameData = GameDataAccess.getGameById(gameId);
		GameDomainObject gameDomain = new GameDomainObject(gameData);
	


		if(ValidateGameById(gameId) == false){
			message.addErrorMessage("The gameId does not exist.");
			
		}

		
		
		if( (gameDomain.gameStatus) != ("Playing")){
			message.addErrorMessage("The game is not in the Playing status.");
			
		}
		if(PlayerModel.ValidatePlayerById(playerId) == false){
			message.addErrorMessage("The playerId does not exist.");

		}
		
		

		if(playerId != gameDomain.player1Id){
			if(playerId != gameDomain.player2Id){
				message.addErrorMessage("The playerId is invalid for the game.");
			}
		}
		BoardDataObject boardData = BoardDataAccess.getBoardByGameId(gameId);
		
		
		if(gameData.currentTurnPlayer == playerId){
			String tiles = "";
			if(gameData.currentTurnPlayer == gameDomain.player1Id){
				tiles = boardData.p1Tiles;
			}
			if(gameData.currentTurnPlayer == gameDomain.player2Id){
				tiles = boardData.p2Tiles;
			}
			Map<Character, Integer> charCount = new HashMap<>();
			for(SpaceDomainObject i : spaces){
				if(i.isMyLetter){
				String tempString = i.letter;
				char ch = tempString.charAt(0);
				if(tiles.indexOf(ch) == -1){
					message.addErrorMessage("Player played an invalid tile.");
				}else if(charCount.containsKey(ch)){
					message.addErrorMessage("Player played an invalid tile.");
				}else{
					charCount.put(ch, 1);
				}
				}
			}
			
		}else{
			message.addErrorMessage("It is not this players turn.");
		}
		
		if(message.getErrorMessage().size() == 0){
			UpdateBoard(message, gameId, spaces);
		}
		if(gameDomain.player1Id == playerId){
				
			gameData.currentTurnPlayer++;
		}
		if(gameDomain.player2Id == playerId){
			
			gameData.currentTurnPlayer--;
		}
		
		UpdatePlayerTiles(message, gameId, playerId, spaces);
		ScoreWord(message, gameId, playerId, spaces);
		return gameDomain;

    }


    public static boolean ValidateWordPlacement(Message message, int gameId, ArrayList<SpaceDomainObject> spaces) {
	GameDataObject game = GameDataAccess.getGameById(gameId);
		BoardDataObject board = BoardDataAccess.getBoardByGameId(gameId);

		boolean isCenter = false;
		boolean sameRow = true;
		boolean sameColumn = true;

		if ((game.currentTurnPlayer == game.player1Id) && (board.board ==  "               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " +
		"               " )) {
		for (int i = 0; i < spaces.size(); i++) {
			SpaceDomainObject space = spaces.get(i);
			if ((space.column == 8) && (space.row == 8)) {
				isCenter = true;
			}
		}

		if (isCenter == false) {
			message.addErrorMessage("The first word must cover the center square.");
			return false;
		}
	}

	SpaceDomainObject firstTile = spaces.get(0);
		
	for (int i = 1; i < spaces.size(); i++) {
		SpaceDomainObject space = spaces.get(i);
		if (space.column != firstTile.column) {
			sameColumn = false;
		}

		if (space.row != firstTile.row) {
			sameRow = false;
		}
	}

		System.out.println(sameColumn);
		System.out.println(sameRow);
		if ((sameColumn == true) || (sameRow == true)) {
			return true;
			
		} else {
			message.addErrorMessage("All letters played must be in the same row or column.");
			return false;
		}
    } 


    public static void UpdateBoard(Message message, int gameId, ArrayList<SpaceDomainObject> spaces) {
		BoardDataObject boardData = BoardDataAccess.getBoardByGameId(gameId);
		
		for(SpaceDomainObject i : spaces){
			int indexToChange = (i.row-1)*15 + (i.column-1);
			if(boardData.board.charAt(indexToChange) == ' '){
			boardData.board = boardData.board.substring(0, indexToChange) + i.letter + boardData.board.substring((indexToChange+1), boardData.board.length());
			
			}
		}
		
    }

    public static void UpdatePlayerTiles(Message message, int gameId, int playerId, ArrayList<SpaceDomainObject> spaces) {
		GameDataObject gameData = GameDataAccess.getGameById(gameId);
		GameDomainObject gameDomain = new GameDomainObject(gameData);
		BoardDataObject boardData = BoardDataAccess.getBoardByGameId(gameId);
			String tiles = "";
			if(gameDomain.player1Id == playerId){
				tiles = boardData.p1Tiles;
			}
			if(gameDomain.player2Id == playerId){
				tiles = boardData.p2Tiles;
			}
			int count = 0;
			for(SpaceDomainObject i : spaces){
				if(i.isMyLetter){
					for(int x=0; x < tiles.length(); x++){
						char temp = tiles.charAt(x);
						String temp1 = "" + temp;
						if(i.letter.equals(temp1)){
							tiles = tiles.substring(0,x) + tiles.substring(x+1);
							count++;
						}
					}
				}
			}
			//fill the player tiles
			if(boardData.letterBag.length() < count){
				tiles = boardData.letterBag + tiles;
			}else{
			int index = 0;
				while(index < count){

					tiles = boardData.letterBag.substring(index,index+1) + tiles;
					index++;
				}
			}
			
			if(gameDomain.player1Id == playerId){
				boardData.p1Tiles = orderString(tiles);
				System.out.println(boardData.p1Tiles);
			}
			if(gameDomain.player2Id == playerId){
				boardData.p2Tiles = orderString(tiles);
				System.out.println(boardData.p1Tiles);

    }
}
	private static String orderString(String unorderedString) {

            List<String> letters = Arrays.asList(unorderedString.split(""));
            Collections.sort(letters);
            String ordered = "";
            for (String letter : letters) {
            ordered += letter;
            }
            return ordered;
    }

    public static void ScoreWord (Message message, int gameId, int playerId, ArrayList<SpaceDomainObject> spaces) {
		GameDataObject game = GameDataAccess.getGameById(gameId);
		int score = 0;
		int scoreToAdd = 0;
		int wordScore = 0;
		int doubleWordFlag = 0;
		int tripleWordFlag = 0;
		
		List<Point> doubleLetterCoords = new ArrayList<>();
		List<Point> tripleLetterCoords = new ArrayList<>();
		List<Point> doubleWordCoords = new ArrayList<>();
		List<Point> tripleWordCoords = new ArrayList<>();

		// I know this isnt efficient
		tripleLetterCoords.add(new Point(6,2)); tripleLetterCoords.add(new Point(10,2)); tripleLetterCoords.add(new Point(2,6)); tripleLetterCoords.add(new Point(6,6)); tripleLetterCoords.add(new Point(10,6)); tripleLetterCoords.add(new Point(14,6)); tripleLetterCoords.add(new Point(2,10)); tripleLetterCoords.add(new Point(6, 10)); tripleLetterCoords.add(new Point(10,10)); tripleLetterCoords.add(new Point(14,10)); tripleLetterCoords.add(new Point(6,14)); tripleLetterCoords.add(new Point(10,14));
		doubleLetterCoords.add(new Point(4,1)); doubleLetterCoords.add(new Point(12,1)); doubleLetterCoords.add(new Point(7,3)); doubleLetterCoords.add(new Point(9,3)); doubleLetterCoords.add(new Point(1,4)); doubleLetterCoords.add(new Point(8,4)); doubleLetterCoords.add(new Point(15,4)); doubleLetterCoords.add(new Point(3,7)); doubleLetterCoords.add(new Point(7,7)); doubleLetterCoords.add(new Point(9,7)); doubleLetterCoords.add(new Point(13,7)); doubleLetterCoords.add(new Point(4,8)); doubleLetterCoords.add(new Point(12,8)); doubleLetterCoords.add(new Point(3,9)); doubleLetterCoords.add(new Point(7,9)); doubleLetterCoords.add(new Point(9,9)); doubleLetterCoords.add(new Point(13,9)); doubleLetterCoords.add(new Point(1,12)); doubleLetterCoords.add(new Point(8,12)); doubleLetterCoords.add(new Point(15,12)); doubleLetterCoords.add(new Point(7,13)); doubleLetterCoords.add(new Point(9,13)); doubleLetterCoords.add(new Point(4,15)); doubleLetterCoords.add(new Point(12,15));
		tripleWordCoords.add(new Point(1,1)); tripleWordCoords.add(new Point(8,1)); tripleWordCoords.add(new Point(15,1)); tripleWordCoords.add(new Point(1,8)); tripleWordCoords.add(new Point(15,8)); tripleWordCoords.add(new Point(1,15)); tripleWordCoords.add(new Point(8,15)); tripleWordCoords.add(new Point(15,15));
		doubleWordCoords.add(new Point(2,2)); doubleWordCoords.add(new Point(14,2)); doubleWordCoords.add(new Point(3,3)); doubleWordCoords.add(new Point(13,3)); doubleWordCoords.add(new Point(4,4)); doubleWordCoords.add(new Point(12,4)); doubleWordCoords.add(new Point(5,5)); doubleWordCoords.add(new Point(11,5)); doubleWordCoords.add(new Point(8,8)); doubleWordCoords.add(new Point(5,11)); doubleWordCoords.add(new Point(11,11)); doubleWordCoords.add(new Point(4,12)); doubleWordCoords.add(new Point(12,12)); doubleWordCoords.add(new Point(3,13)); doubleWordCoords.add(new Point(13,13)); doubleWordCoords.add(new Point(2,14)); doubleWordCoords.add(new Point(14,14));   

		if (playerId == game.player1Id) {
			score = game.player1Score;
			System.out.println("player1StartScore: "+score);
		} else if (playerId == game.player2Id) {
			score = game.player2Score;
			System.out.println("player2StartScore: "+score);
		} else {
			message.addErrorMessage("Something went wrong.");
		}

		for (int i=0; i < spaces.size(); i++) {
			String letter = spaces.get(i).letter;
			
			switch(letter) {
				case "A":
				case "E":
				case "I":
				case "L":
				case "N":
				case "O":
				case "R":
				case "S":
				case "T":
				case "U":
					scoreToAdd = 1;
					break;
				case "D":
				case "G":
					scoreToAdd = 2;
					break;
				case "B":
				case "C":
				case "M":
				case "P":
					scoreToAdd = 3;
					break;
				case "F":
				case "V":
				case "W":
				case "Y":
				case "H":
					scoreToAdd = 4;
					break;
				case "K":
					scoreToAdd = 5;
					break;
				case "J":
				case "X":
					scoreToAdd = 8;
					break;
				case "Q":
				case "Z":
					scoreToAdd = 10;
					break;
			}

			int col = spaces.get(i).column;
			int row = spaces.get(i).row;
			Point m = new Point(col, row);
			
			if (doubleLetterCoords.contains(m)) {
				scoreToAdd = scoreToAdd * 2;
				doubleLetterCoords.remove(m);
				System.out.println(doubleLetterCoords);
			} else if (doubleWordCoords.contains(m)) {
				doubleWordFlag = 1;
			} else if (tripleLetterCoords.contains(m)) {
				scoreToAdd = scoreToAdd * 3;
				tripleLetterCoords.remove(m);
			} else if (tripleWordCoords.contains(m)) {
				tripleWordFlag = 1;
			}

			wordScore = wordScore + scoreToAdd;
		}

		if (doubleWordFlag == 1) {
			wordScore = wordScore * 2;
		}
		if (tripleWordFlag == 1) {
			wordScore = wordScore * 3;
		}

		score = score + wordScore;

		if (playerId == game.player1Id) {
			game.player1Score = score;
		} else if (playerId == game.player2Id) {
			game.player2Score = score;
		} else {
			message.addErrorMessage("Something went wrong.");
		}
    }

}
