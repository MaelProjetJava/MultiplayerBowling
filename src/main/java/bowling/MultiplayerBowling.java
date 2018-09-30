/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowling;

import bowling.Frame;
import bowling.MultiPlayerGame;
import bowling.SinglePlayerGame;

/**
 *
 * @author malet
 */
public class MultiplayerBowling implements MultiPlayerGame {
	
	private SinglePlayerGame games[];
	private String playersNames[];
	private int currentPlayer;

	@Override
	public String startNewGame(String[] playerName)
					throws IllegalArgumentException {
		if (playerName == null || playerName.length == 0) {
			throw new IllegalArgumentException(
					"There need to be at least 1 player.");
		}
		
		currentPlayer = 0;
		games = new SinglePlayerGame[playerName.length];
		
		for (int i = 0; i < games.length; i++)
			games[i] = new SinglePlayerGame();
		
		playersNames = playerName.clone();
		
		return getNextPlayerStr();
	}

	@Override
	public String lancer(int nombreDeQuillesAbattues)
						throws IllegalStateException {
		if (games == null || playersNames == null) {
			throw new IllegalStateException(
					"The game has not been started yet !");
		} else if (gameFinished()) {
			throw new IllegalStateException(
						"The game is finished !");
		}
		
		games[currentPlayer].lancer(nombreDeQuillesAbattues);
		
		if (currentPlayerFrame().isFinished())
			nextPlayer();
		
		return getNextPlayerStr();
	}

	@Override
	@SuppressWarnings("empty-statement")
	public int scoreFor(String playerName) throws IllegalArgumentException {
		if (playersNames == null) {
			throw new IllegalStateException(
					"The game has not been started yet !");
		}

		int i = -1;
		while (++i < playersNames.length &&
					!playersNames[i].equals(playerName));
		
		if (i == playersNames.length)
			throw new IllegalArgumentException("This player doesn't exist !");
		
		return games[i].score();
	}
	
	
	private String getNextPlayerStr() {
		if (gameFinished())
			return "Partie terminée";
		
		StringBuilder str_builder = new StringBuilder();
		
		Frame currentPlayerFrame = currentPlayerFrame();
		if (currentPlayerFrame.isFinished() &&
				currentPlayerFrame.getFrameNumber() != 10)
			currentPlayerFrame = currentPlayerFrame.next();
		
		str_builder.append("Prochain tir: joueur ");
		str_builder.append(playersNames[currentPlayer]);
		str_builder.append(", tour n° ");
		str_builder.append(currentPlayerFrame.getFrameNumber());
		str_builder.append(", boule n° ");
		str_builder.append(currentPlayerFrame.getBallsThrown() + 1);
		
		System.out.println(str_builder.toString());
		
		return str_builder.toString();
	}
	
	private int playerCount() {
		return playersNames.length;
	}
	
	private void nextPlayer() {
		currentPlayer = (currentPlayer + 1) % playerCount();
		System.out.println("Next player !");
	}
	
	private Frame currentPlayerFrame() {
		return games[currentPlayer].getCurrentFrame();
	}
	
	private boolean gameFinished() {
		Frame frame = games[playerCount() - 1].getCurrentFrame();
		return frame.getFrameNumber() == 10 && frame.isFinished();
	}
}
