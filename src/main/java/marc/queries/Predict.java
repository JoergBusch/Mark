package marc.queries;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import marc.utils.Secret;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.utils.SummonerCrawler;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.raw.DDragonAPI;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.Skin;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.StaticChampion;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

public class Predict {


	public final R4J r4J = new R4J(Secret.CREDS);
	public DDragonAPI api = r4J.getDDragonAPI();

	private String url, cname, title, story, sname;
	private int loose;

	/**
	 * Searches DragonApi for ID corresponding to given champion
	 * @param champion champion	
	 * @return 
	 * @return champion's ID
	 */
	public Predict run(String name) {
		
		Summoner selfS = Summoner.byName(LeagueShard.EUW1, name);
		selfS.getPUUID();

		// Set of Players the summoner recently played with
		SummonerCrawler crawler = new SummonerCrawler(Summoner.byName(LeagueShard.EUW1, "name"));
		crawler.crawlGames();
		Set<String> associates = (Set<String>) crawler.get().stream().map(s -> {return s.getPUUID();});
		System.out.println(associates);
		
		return this;

	}
	
	public static void main(String[] args) {
		Predict p = new Predict();
		p.run("GeorgeUBush");
	}
	

	@Override
	public String toString() {

		String loss = " Loss";
		if (this.loose > 1) {
			loss = " Losses";
		}

		return  "`" + cname + " " + title +
				"\n======================\n" + story + "`" + 
				"\n\n" + "Schau dir mal diesen Skin an, wie findest du " + sname + "? :slight_smile:" + "\n\n" + url;
	}
}