package Model;
import java.util.ArrayList;

import DataAccess.PlayerDataAccess;
import DataObject.PlayerDataObject;
import DomainObject.PlayerDomainObject;
import restService.Message;


public class PlayerModel {
	
	public static PlayerDomainObject GetPlayerById(Message message, int id) {
		PlayerDataObject playerData = PlayerDataAccess.getPlayerById(id);
		PlayerDomainObject playerDomain = new PlayerDomainObject(playerData);
		return playerDomain;
	}

	public static ArrayList<PlayerDomainObject> GetAllPlayers(Message messasge) {
		ArrayList<PlayerDataObject> playerDataList = PlayerDataAccess.getAllPlayers();
		ArrayList<PlayerDomainObject> playerDomainList = PlayerDomainObject.MapList(playerDataList);
		return playerDomainList;
	}



	public static boolean ValidatePlayerById(int id) {
		if (PlayerDataAccess.getPlayerById(id) == null)
			return false;
		return true;
	}
	
	public static PlayerDomainObject RegisterPlayer(Message message, PlayerDomainObject domainPlayerToCreate) {
		String user = domainPlayerToCreate.userName;
		String pass = domainPlayerToCreate.password;

		ArrayList<PlayerDomainObject> players = GetAllPlayers(message);

		for (int i=0; i < players.size(); i++) {
			if (players.get(i).userName.equals(user)) {
				message.addErrorMessage("The username is already taken.");
				return null;
			}
		}

		if (!(user.length() > 6 && user.length() < 18 && user.matches("[a-zA-Z0-9]+"))) {
			message.addErrorMessage("The username is invalid.");
			return null;
		}

		if (!(pass.length() > 6 && pass.length() < 18 && pass.matches("[a-zA-Z0-9]+"))) {
			message.addErrorMessage("The password is invalid.");
			return null;
		}

		PlayerDataObject playerData = new PlayerDataObject(0, user, pass);
		PlayerDataObject newPlayer = PlayerDataAccess.createPlayer(playerData);
		PlayerDomainObject newPlayerDomain = GetPlayerById(message, newPlayer.id);
		
		return newPlayerDomain;
	}


}
