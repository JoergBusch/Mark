package marc.queries;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import marc.utils.Secret;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.raw.DDragonAPI;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.Skin;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.StaticChampion;

public class Lore {


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
	public Lore run() {

		Random rand = new Random();

		List<Entry<Integer, StaticChampion>> champions = api.getChampions().entrySet().stream().parallel().toList();

		Entry<Integer, StaticChampion> entry = champions.get(rand.nextInt(champions.size()));
		StaticChampion champion = entry.getValue();

		List<Skin> skins = champion.getSkins();
		Skin random = skins.get(rand.nextInt(1,skins.size()));

		this.cname = champion.getName();	
		this.story = champion.getLore();
		this.title = champion.getTitle();
		this.sname = random.getName();
		this.loose = rand.nextInt(0,5);
		
		if(rand.nextInt(10000) == 187) {
			this.loose = 9999;
		}

		this.url  = "https://cdn.communitydragon.org/latest/champion/" + entry.getKey() + "/splash-art/centered/skin/" + random.getNum();

		return this;

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
