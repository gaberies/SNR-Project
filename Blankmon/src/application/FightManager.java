package application;

import java.util.ArrayList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FightManager {

	private ArrayList<PICKNAMEmon> mPlayerTeam;
	private ArrayList<PICKNAMEmon> mEnemyTeam;

	public FightManager(ArrayList<PICKNAMEmon> playerTeam, ArrayList<PICKNAMEmon> enemyTeam) {
		mPlayerTeam = playerTeam;
		mEnemyTeam = enemyTeam;
	}

	public String attackEnemy(int indexOfPlayer, int indexOfEnemy, int indexOfMove) {
		checkNulls(indexOfPlayer, indexOfEnemy, indexOfMove);
		PICKNAMEmon playerPICKNAMEmon = mPlayerTeam.get(indexOfPlayer);
		PICKNAMEmon enemyPICKNAMEmon = mEnemyTeam.get(indexOfEnemy);
		Move playerPICKNAMEmonMove = mPlayerTeam.get(indexOfPlayer).getMoves().getMove(indexOfMove);
		playerPICKNAMEmonMove.activateMove(playerPICKNAMEmon, enemyPICKNAMEmon);
		return "Player attacked Enemy Pokemon";
	}

	public String attackPlayer(int indexOfPlayer, int indexOfEnemy, int indexOfMove) {
		checkNulls(indexOfPlayer, indexOfEnemy, indexOfMove);
		PICKNAMEmon playerPICKNAMEmon = mPlayerTeam.get(indexOfPlayer);
		PICKNAMEmon enemyPICKNAMEmon = mEnemyTeam.get(indexOfEnemy);
		Move enemyPICKNAMEmonMove = mEnemyTeam.get(indexOfEnemy).getMoves().getMove(indexOfMove);
		enemyPICKNAMEmonMove.activateMove(enemyPICKNAMEmon, playerPICKNAMEmon);
		return "Enemy attacked player Pokemon";
	}

	public void itemUse(boolean isPlayer, int indexOfTeam, Enum<?> item) {
		throw new NotImplementedException();
	}

	public ArrayList<PICKNAMEmon> getPlayerTeam() {
		return mPlayerTeam;
	}

	public ArrayList<PICKNAMEmon> getEnemyTeam() {
		return mEnemyTeam;
	}

	private void checkNulls(int indexOfPlayer, int indexOfEnemy, int indexOfMove) {
		if (mPlayerTeam.get(indexOfPlayer) == null) {
			throw new NullPointerException("Player Pokemon was null, String or Result Object?");
		}
		if (mEnemyTeam.get(indexOfEnemy) == null) {
			throw new NullPointerException("Enemy Pokemon was null, String or Result Object?");
		}
		if (mPlayerTeam.get(indexOfEnemy).getMoves() == null) {
			throw new NullPointerException("Player Pokemon Move was null, String or Result Object?");
		}
		if (mPlayerTeam.get(indexOfPlayer).getMoves().getMove(indexOfMove) == null) {
			throw new NullPointerException("PlayerTeam Pokemomn Move was null, String or Result Object?");
		}
	}

}
