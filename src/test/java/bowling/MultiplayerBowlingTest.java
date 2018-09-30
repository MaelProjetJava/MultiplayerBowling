/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowling;

import bowling.MultiplayerBowling;
import bowling.MultiPlayerGame;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dark
 */
public class MultiplayerBowlingTest {
	
	private MultiPlayerGame game;
	private static final String playersNames[] = {
		"John",
		"Jeff",
		"Marilyn"
	};
	
	@Before
	public void setUp() {
		game = new MultiplayerBowling();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void exceptionIfNamesNull() throws Exception {
		game.startNewGame(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void exceptionIfNamesEmpty() throws Exception {
		String playersNames[] = {};
		game.startNewGame(playersNames);
	}
	
	@Test
	public void correctFirstPlayer() throws Exception {
		String str = game.startNewGame(playersNames);
		assertPlayer("John", 1, 1, str);
	}
	
	@Test
	public void scoreCorrectlyInitialized() throws Exception {
		game.startNewGame(playersNames);
		
		for (String playerName : playersNames)
			assertEquals(0, game.scoreFor(playerName));
	}
	
	@Test(expected = IllegalStateException.class)
	public void allNameInvalidWhenGameNotStarted() throws Exception {
		game.scoreFor(playersNames[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nameNotInGameInvalid() throws Exception {
		game.startNewGame(playersNames);
		game.scoreFor("Johnny");
	}

	@Test(expected = IllegalStateException.class)
	public void testLancerPartieNonDemarrer() throws Exception {
		game.lancer(4);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testLancerPartieTerminee() throws Exception {
		game.startNewGame(playersNames);
		
		int i = 0;
		try {
			for (i = 0; i < playersNames.length * 2 * 10; i++)
				game.lancer(3);
		} catch(Exception e) {
			fail("Exception thrown before end of game, i = " + i);
		}
		
		game.lancer(1);
	}
	
	@Test
	public void testTypicalGame() throws Exception {
		String str = game.startNewGame(playersNames);
		assertPlayer("John", 1, 1, str);
		
		doRoundType1(1);
		doRoundType2(2);
		doRoundType1(3);
		doRoundType3(4);
		doRoundType3(5);
		doRoundType2(6);
		doRoundType1(7);
		doRoundType2(8);
		doRoundType2(9);
		finalRound();
		
		assertEquals(300, game.scoreFor("John"));
		assertEquals(51, game.scoreFor("Jeff"));
		assertEquals(87, game.scoreFor("Marilyn"));
	}
	
	private void doRoundType1(int roundNumber) throws Exception {
		String str = rollStrike();
		assertPlayer("Jeff", roundNumber, 1, str);
		
		str = rollSpare("Jeff", roundNumber);
		assertPlayer("Marilyn", roundNumber, 1, str);
		
		str = rollTwo(3, "Marilyn", roundNumber);
		if (roundNumber != 10)
			assertPlayer("John", roundNumber + 1, 1, str);
		else
			assertEquals("Partie terminée", str);
	}
	
	private void doRoundType2(int roundNumber) throws Exception {
		String str = rollStrike();
		assertPlayer("Jeff", roundNumber, 1, str);
		
		str = rollTwo(0, "Jeff", roundNumber);
		assertPlayer("Marilyn", roundNumber, 1, str);
		
		str = rollTwo(4, "Marilyn", roundNumber);
		if (roundNumber != 10)
			assertPlayer("John", roundNumber + 1, 1, str);
		else
			assertEquals("Partie terminée", str);
	}
	
	private void doRoundType3(int roundNumber) throws Exception {
		String str = rollStrike();
		assertPlayer("Jeff", roundNumber, 1, str);
		
		str = rollTwo(2, "Jeff", roundNumber);
		assertPlayer("Marilyn", roundNumber, 1, str);
		
		str = rollSpare("Marilyn", roundNumber);
		if (roundNumber != 10)
			assertPlayer("John", roundNumber + 1, 1, str);
		else
			assertEquals("Partie terminée", str);
	}
	
	private void finalRound() throws Exception {
		int roundNumber = 10;
		
		String str = rollStrike();
		assertPlayer("John", roundNumber, 2, str);
		
		str = rollStrike();
		assertPlayer("John", roundNumber, 3, str);
		
		str = rollStrike();
		assertPlayer("Jeff", roundNumber, 1, str);
		
		str = rollSpare("Jeff", roundNumber);
		assertPlayer("Jeff", roundNumber, 3, str);
		
		str = game.lancer(1);
		assertPlayer("Marilyn", roundNumber, 1, str);
		
		str = rollTwo(3, "Marilyn", roundNumber);
		if (roundNumber != 10)
			assertPlayer("John", roundNumber + 1, 1, str);
		else
			assertEquals("Partie terminée", str);
	}
	
	private String rollTwo(int pins, String name, int roundNumber) throws Exception {
		String str = game.lancer(pins);
		assertPlayer(name, roundNumber, 2, str);
		return game.lancer(pins);
	}
	
	private String rollSpare(String name, int roundNumber) throws Exception {
		String str = game.lancer(7);
		assertPlayer(name, roundNumber, 2, str);
		return game.lancer(3);
	}

	private String rollStrike() throws Exception {
		return game.lancer(10);
	}
	
	private void assertPlayer(String name, int round, int ball, String str){
		String correctStr = "Prochain tir: joueur " + name +
			", tour n° " + round + ", boule n° " + ball;
		assertEquals(correctStr, str);
	}
}
