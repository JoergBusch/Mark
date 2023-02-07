package marc.queries;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import marc.utils.Epoch;
import marc.utils.Secret;
import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.calling.DataCall;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.constants.types.lol.GameQueueType;
import no.stelar7.api.r4j.basic.constants.types.lol.MatchlistMatchType;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.MatchListBuilder;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.StaticChampion;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;


public class Match {

	public final R4J r4j = new R4J(Secret.CREDS);
	public DateTimeFormatter formatter;
	public DecimalFormat decimals;
	public Map<Integer, StaticChampion> championData;
	
	public Match() {

		DataCall.setCacheProvider(new FileSystemCacheProvider());

		this.decimals = new DecimalFormat("###.##");
		this.formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		this.championData  = r4j.getDDragonAPI().getChampions();
	}

	public String getRecent(String self) {

		Summoner selfS;
		MatchParticipant selfP;
		StaticChampion champion;
		LOLMatch match;

		double rate;

		selfS = Summoner.byName(LeagueShard.EUW1, self);

		String result = "";
		String msg = "";

		msg = msg.concat("`" + selfS.getName() + "\n");
		msg = msg.concat(formatter.format(Epoch.secondsToZoned(Epoch.weekAgo())) + " - " + formatter.format(Epoch.secondsToZoned(Epoch.now())) + "\n");
		msg = msg.concat("======================\n");


		int wins  = 0;	

		List<String> refs = new MatchListBuilder(LeagueShard.EUW1,
				selfS.getPUUID(),
				GameQueueType.RANKED_FLEX_SR,
				MatchlistMatchType.RANKED,
				0, 
				100 ,
				Epoch.weekAgo(),
				Epoch.now()).withPuuid(selfS.getPUUID()).get();


		for (String current : refs){

			match = LOLMatch.get(LeagueShard.EUW1, current);
			selfP  = match.getParticipants().stream().filter(p -> p.getPuuid().equals(selfS.getPUUID())).findFirst().get();
			champion = championData.get(selfP.getChampionId());

			if(isWin(match, selfS.getPUUID())) {
				result = "W";
				wins++;
			}else {
				result = "L";
			}

			msg = msg.concat(formatter.format(match.getMatchCreationAsDate()) + ", " + result + ", " + champion.getName() + "\n");
		}

		rate = 0;
		if(!refs.isEmpty()) {
			rate = 100 * wins/refs.size();	
		}

		msg = msg.concat("======================");
		msg = msg.concat("\n" + refs.size() + " Games, " + wins + " Wins, " + decimals.format(rate) + "% winrate.`\n\n");

		if(rate >= 50) {
			msg = msg.concat("Gute Arbeit. Viel Spa\u00DF in der Kluft :slight_smile:");
		}else {
			msg = msg.concat("Mach dir nichts draus! Nächste Match wird's bestimmt was :slight_smile:");
		}

		return msg;
	}

	public String sortByWinrate(List<String> summoners) {

		String msg = "`";
		List<Pair<String, Double>> model  = new ArrayList<Pair<String, Double>>();


		for(String summoner: summoners) {
			msg = msg.concat(summoner + " ");
			Pair<String, Double> temp = getRecentWinrate(summoner, GameQueueType.RANKED_FLEX_SR, 100);
			model.add(temp);
		}

		msg += "\n======================\n";
		Collections.sort(model, Comparator.comparing(p -> p.getSecond()));
		Collections.reverse(model);


		for(Pair<String, Double> pair: model) {
			msg = msg.concat(pair.getFirst());
		}

		msg = msg.concat("======================`\n\n");
		msg = msg.concat("Gute Arbeit Beschwörer ihr spielt gut! :slight_smile::");

		return msg;
	}

	public Pair<String, Double> getRecentWinrate(String self, GameQueueType queue, int amountOfRecentGames) {

		Summoner selfS = Summoner.byName(LeagueShard.EUW1, self);

		int wins  = 0;
		int games = 0;
		String msg;

		List<String> refs = new LinkedList<String>();
		List<String> temp;

		for(int i = 0; i < amountOfRecentGames; i = i + 100) {

			temp = new MatchListBuilder(LeagueShard.EUW1, selfS.getPUUID(), queue, MatchlistMatchType.RANKED, i, 100 , Epoch.weekAgo(), Epoch.now()).withPuuid(selfS.getPUUID()).get();
			refs.addAll(temp);

		}

		for (String current : refs){

			LOLMatch match = LOLMatch.get(LeagueShard.EUW1, current);	
			if(isWin(match, selfS.getPUUID())) {wins++;}
			games++;
		}

		double rate = 0;
		if(!refs.isEmpty()) {
			rate = 100 * wins/refs.size();	
		}

		msg = String.format("%02d", wins) + " Wins, " + String.format("%02d", games - wins) + " Losses, " + rate + "% Winrate " + self + "\n";
		
		return new Pair<String, Double>(msg, rate);
	}

	public String getTotal(String self) {

		long startTime = 0, endTime = 0;
		double rate;

		String msg = "`";

		Summoner selfS = Summoner.byName(LeagueShard.EUW1, self);

		msg = msg.concat(selfS.getName() + "\n");

		int wins  = 0;	

		List<String> refs = new LinkedList<String>();
		List<String> temp;

		for(int i : Arrays.asList(0,100)) {
			temp = new MatchListBuilder(LeagueShard.EUW1, selfS.getPUUID(), GameQueueType.RANKED_FLEX_SR, MatchlistMatchType.RANKED, i, 100 , null, null).withPuuid(selfS.getPUUID()).get();
			refs.addAll(temp);
		}
		
		for (String current : refs){

			LOLMatch match = LOLMatch.get(LeagueShard.EUW1, current);
			if(isWin(match, selfS.getPUUID())) {
				wins++;
			}

		}

		if(!refs.isEmpty()) {
		startTime = LOLMatch.get(LeagueShard.EUW1, refs.get(0)).getGameCreation();
		endTime = LOLMatch.get(LeagueShard.EUW1, refs.get(refs.size() - 1)).getGameCreation();
		}
		
		msg = msg.concat(formatter.format(Epoch.secondsToZoned(startTime / 1000l)) + " - " + formatter.format(Epoch.secondsToZoned(endTime / 1000l)) + "\n");
		msg = msg.concat("======================\n");
		rate = 0;

		if(!refs.isEmpty()) {
			rate = 100 * wins/refs.size();	
		}
		msg = msg.concat(refs.size() + " Games, " + wins + " Wins, " + decimals.format(rate) + "% Winrate.`\n\n");

		if(rate >= 50) {
			msg = msg.concat("Gute Arbeit. Viel Spaß in der Kluft :slight_smile:");
		}else {
			msg = msg.concat("Mach dir nichts draus! Nächste Match wird's bestimmt was :slight_smile:");
		}

		return msg;
	}

	private boolean isWin(LOLMatch match, String PUUID) {
		MatchParticipant self  = match.getParticipants().stream().filter(p -> p.getPuuid().equals(PUUID)).findFirst().get();
		return self.didWin();
	}
}
