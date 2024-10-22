package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import DataAccess.BoardDataAccess;
import DataObject.BoardDataObject;
import DomainObject.BoardDomainObject;
import restService.Message;

public class BoardModel {
	
	public static BoardDomainObject GetBoardDetailsById(Message message, int boardId) {
		BoardDataObject boardData = BoardDataAccess.getBoardById(boardId);
		BoardDomainObject boardDomain = new BoardDomainObject(boardData);

		return boardDomain;
	}

	public static BoardDomainObject GetBoardDetailsByGameId(Message message, int gameId) {
		BoardDataObject boardData = BoardDataAccess.getBoardByGameId(gameId);
        if (boardData == null) 
          return null;

		BoardDomainObject boardDomain = new BoardDomainObject(boardData);

		return boardDomain;
	}

	public static BoardDomainObject CreateBoard(Message message, int gameId, int gameTypeId) {
		String letterbagString = "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
		String p1Tiles = "";
		String p2Tiles = "";
		String board = "";

		for (int i = 0; i < 225; i ++) {
			board += " ";
		}


		List<Character> characters = new ArrayList<>();
		for (char c: letterbagString.toCharArray()) {
			characters.add(c);
		}

		Collections.shuffle(characters);
		StringBuilder shuffledString = new StringBuilder();
		for (char c: characters) {
			shuffledString.append(c);
		}

		letterbagString = shuffledString.toString();

		for (int i = 0; i < 7; i++) {
			p1Tiles += letterbagString.charAt(i);
		}
		
		for (int i = 7; i < 14; i++) {
			p2Tiles += letterbagString.charAt(i);
		}
		letterbagString.substring(14);

		BoardDataObject boarddata = new BoardDataObject(gameTypeId, gameId, p1Tiles, p2Tiles, board, letterbagString);
		BoardDataAccess.createBoard(boarddata);
		BoardDomainObject boardObject = new BoardDomainObject(boarddata);
        return boardObject;
        return null;
	}


	public static Boolean ValidateWordForBoard(Message message, int gameId, String word) {
		//This needs to be implemented.
		return false;
	}

}
