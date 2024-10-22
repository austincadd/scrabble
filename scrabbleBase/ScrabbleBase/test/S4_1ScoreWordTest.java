import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Controller.GameController;
import Controller.PlayerController;
import DataAccess.BoardDataAccess;
import DataAccess.DictionaryDataAccess;
import DataAccess.GameDataAccess;
import DataAccess.GameTypeDataAccess;
import DataAccess.PlayerDataAccess;
import DomainObject.SpaceDomainObject;
import Model.GameModel;
import restService.Message;
import restService.Request.CreateGameRequest;
import restService.Request.PlayWordRequest;
import restService.Request.RegisterPlayerRequest;
import restService.Response.GameResponse;
import restService.Response.PlayerResponse;

public class S4_1ScoreWordTest {
 
    @Before 
    public void Setup() {

    	GameTypeDataAccess GameTypeDataAccess = new GameTypeDataAccess();
		PlayerDataAccess playerDataAccess = new PlayerDataAccess();
		BoardDataAccess boardDataAccess = new BoardDataAccess();
		GameDataAccess gameDataAccess = new GameDataAccess();
        DictionaryDataAccess dictionaryDataAccess = new DictionaryDataAccess();

    }	


   
	/*
     * "Score Word"
     * "------Call PlayWord game, player, and word.  - The word is not validated in this sprint."
	 */
	@Test
	public void Test_ScoreWord_One() {

        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(4, 8, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(5, 8, "P", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(6, 8, "P", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(7, 8, "L", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(8, 8, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);


        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 20 points", response.getPlayer1Score() == 20);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

	}

	/*
     * "Score Word - Triple Word Score"
     * "Call ScoreWord with a word to be scored.  Test Triple Word Score"
	 */
	@Test
	public void Test_ScoreWord_TripleWordScore() {

        //Test 1: TW Score - 1,1
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(1, 1, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(2, 1, "T", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 1, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 9 points", response.getPlayer1Score() == 9);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

        //Test 2: TW Score - 8,1
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(7, 1, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(8, 1, "X", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(9, 1, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 39 points", response.getPlayer1Score() == 39);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

        //Test 3: TW Score - 8,15
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(8, 15, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(9, 15, "P", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(10, 15, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

        assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 54 points", response.getPlayer1Score() == 54);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);
 	}

	/*
     * "Score Word - Double Word Score"
     * "Call ScoreWord with a word to be scored.  Test Double Word Score"
	 */
	@Test
	public void Test_ScoreWord_DoubleWordScore() {

        //Test 1: DW Score - 4,4
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(4, 4, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(5, 4, "T", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(6, 4, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 6 points", response.getPlayer1Score() == 6);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

        //Test 2: DW Score - 14,2
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(14, 2, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(14, 2, "X", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(14, 2, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 26 points", response.getPlayer1Score() == 26);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

        //Test 3: DW Score - 5,11
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(5, 11, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(5, 12, "P", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(5, 13, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

        assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 36 points", response.getPlayer1Score() == 36);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);
 	}


	/*
     * "Score Word - Triple Letter Score"
     * "Call ScoreWord with a word to be scored.  Test Triple Letter Score"
	 */
	@Test
	public void Test_ScoreWord_TripleLetterScore() {

        //Test 1: TL Score - 6,2
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(6, 1, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(6, 2, "T", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(6, 3, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 5 points", response.getPlayer1Score() == 5);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

        //Test 2: TL Score - 14,10
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(14, 9, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(14, 10, "X", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(14, 11, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 31 points", response.getPlayer1Score() == 31);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);
 	}



	/*
     * "Score Word - Double Letter Score"
     * "Call ScoreWord with a word to be scored.  Test Double Letter Score"
	 */
	@Test
	public void Test_ScoreWord_DoubleLetterScore() {

        //Test 1: DL Score - 1,12
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(1, 12, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(2, 12, "T", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 12, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 4 points", response.getPlayer1Score() == 4);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

        //Test 2: DL Score - 15,12
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(15, 11, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(15, 12, "X", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(15, 13, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 22 points", response.getPlayer1Score() == 22);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);
 	}


	/*
     * "Score Word - Multiple Premium Squares"
     * "Call ScoreWord with a word to be scored.  Test Multiple Premium Squares"
	 */
	@Test
	public void Test_ScoreWord_MultiplePremiumsSquares_1() {

        //Test 1: DL Score - 1,12
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(8, 12, "C", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(9, 12, "R", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(10, 12, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(11, 12, "Z", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(12, 12, "Y", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 44 points", response.getPlayer1Score() == 44);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

 	}



	/*
     * "Score Word - Multiple Premium Squares"
     * "Call ScoreWord with a word to be scored.  Test Multiple Premium Squares"
	 */
	@Test
	public void Test_ScoreWord_MultiplePremiumsSquares_2() {

        //Test 1: DL Score - 1,12
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(1, 15, "F", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(2, 15, "O", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 15, "U", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(4, 15, "R", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 24 points", response.getPlayer1Score() == 24);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

 	}

	/*
     * "Score Word - Multiple Premium Squares"
     * "Call ScoreWord with a word to be scored.  Test Multiple Premium Squares"
	 */
	@Test
	public void Test_ScoreWord_MultiplePremiumsSquares_3() {

        //Test 1: DL Score - 1,12
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(8, 12, "B", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(9, 12, "R", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(10, 12, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(11, 12, "I", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(12, 12, "N", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 20 points", response.getPlayer1Score() == 20);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

 	}


    /*
     * "Score Word - Already used Premium Squares"
     * "Call ScoreWord with a word to be scored.  Test already used Premium Squares"
	 */
	@Test
	public void Test_ScoreWord_MultiplePremiumsSquares_SomeAlreadyUsed_1() {

  
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(8, 4, "B", false)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(9, 4, "R", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(10, 4, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(11, 4, "I", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(12, 4, "N", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 14 points", response.getPlayer1Score() == 14);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

 	}

    /*
     * "Score Word - Already used Premium Squares"
     * "Call ScoreWord with a word to be scored.  Test already used Premium Squares"
	 */
	@Test
	public void Test_ScoreWord_MultiplePremiumsSquares_SomeAlreadyUsed_2() {

  
        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 3, "S", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 4, "E", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 5, "V", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 6, "E", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 7, "N", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 8, "T", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(3, 9, "Y", false)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 28 points", response.getPlayer1Score() == 28);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

 	}



/////////////////////////////////////

    /*
     * "Score Word"
     * "------Call PlayWord game, player, and word.  - The word is not validated in this sprint."
	 */
	@Test
	public void Test_ScoreWord_AllLetters_SeveralSmallTests() {



        Message message = new Message();
		ArrayList<SpaceDomainObject> spaces = new ArrayList<SpaceDomainObject>();

        //Test 1: THE : Points: 6

        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "T", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "H", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        GameResponse response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 6 points", response.getPlayer1Score() == 6);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);


        //Test 1: FIVE : Points: 10
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "F", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "I", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "V", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "E", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

		assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 16 points", response.getPlayer1Score() == 16);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);


        //Test 1: BOXING : Points: 16
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "B", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "O", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "X", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "I", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "N", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "G", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

        assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 32 points", response.getPlayer1Score() == 32);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);
        
        //Test 1: WIZARDS : Points: 20
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "W", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "I", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "Z", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "A", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "R", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "D", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "S", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

        assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 52 points", response.getPlayer1Score() == 52);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);


        //Test 1: JUMP : Points: 15
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "J", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "U", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "M", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "P", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

        assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 77 points", response.getPlayer1Score() == 67);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);


        //Test 1: QUICKLY : Points: 25
        spaces = new ArrayList<SpaceDomainObject>();
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "Q", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "U", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "I", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "C", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "K", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "L", true)));
        spaces.add(new SpaceDomainObject(new PlayWordRequest.Space(0, 0, "Y", true)));
        GameModel.ScoreWord(message, 2, 1, spaces);

        response = GameController.getGameByGameIdAndPlayerId(message, 2, 1);

        assertTrue("The service should be successful", message.getErrorMessage().size() == 0 );
        assertTrue("Player1 Score should update to 92 points", response.getPlayer1Score() == 92);
        assertTrue("Player2 Score should still be 0 points", response.getPlayer2Score() == 0);

    }


        
}
        